package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDate
import kotlin.properties.Delegates

// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc
enum class CodigoHttp(val codigo: Int) {
    OK(200),
    NOT_IMPLEMENTED(501),
    NOT_FOUND(404),
}

class Pedido(val ip: String, val url: String, val fechaHora: LocalDate) {
    val protocolo = url.takeWhile { it.isLetter() }
    val ruta = "/" + url.split("://").last().substringAfter("/")
    val extension = url.takeLastWhile { it.isLetter() }

    fun moduloSegunPedido(): Modulo {
        var moduloElegido = ServidorWeb.modulos.find { it.puedeResponder(this) }
        if (ServidorWeb.validacionProtocolo(this) == CodigoHttp.NOT_IMPLEMENTED.codigo){
            moduloElegido = SinModulo
        }
        else if(moduloElegido == null) {
            moduloElegido = SinModulo
        }
        return moduloElegido
    }

}

open class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido){
    fun respuestaDemorada(demoraMinima: Int) = tiempo > demoraMinima
    fun compararIP(ip: String) = pedido.ip == ip
    fun compararRuta(ruta: String) = pedido.ruta == ruta //Para AnalizadorIPsospechosas
    fun fueAtendidoEntreHoras(hora1: LocalDate,hora2: LocalDate) = pedido.fechaHora in (hora1..hora2)
}

object ServidorWeb{
    var modulos = mutableListOf<Modulo>()
    //val pedidos = mutableListOf<Pedido>()

    fun validacionProtocolo(pedido: Pedido) =
        if (pedido.protocolo == "http") CodigoHttp.OK.codigo
        else CodigoHttp.NOT_IMPLEMENTED.codigo


    fun darRespuesta(pedido: Pedido): Respuesta {
        if (validacionProtocolo(pedido) == CodigoHttp.NOT_IMPLEMENTED.codigo) {
            val RespuestaNOT_IMPLEMENTED = Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido)
            return RespuestaNOT_IMPLEMENTED
        }
        else {
            val modulo = pedido.moduloSegunPedido()

            if (modulo != SinModulo){
                val RespuestaOK = Respuesta(CodigoHttp.OK, modulo.body, modulo.tiempoRespuesta, pedido)
                return RespuestaOK
            }
            else{
                val RespuestaNOT_FOUND = Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido)
                return RespuestaNOT_FOUND
                //Para ModuloSinExtensiones
            }
        }
    }

    //Analizadores
    var analizadores = mutableListOf<Analizador>()

    //Idea:
    fun enviarRespuestaAAnalizar(pedido: Pedido) { analizadores.forEach { it.recibirRespuesta(pedido) } }
}

