package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime
import kotlin.properties.Delegates

// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc
enum class CodigoHttp(val codigo: Int) {
    OK(200),
    NOT_IMPLEMENTED(501),
    NOT_FOUND(404),
}

class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime) {
    val protocolo = url.takeWhile { it.isLetter() }
    val ruta = "/" + url.split("://").last().substringAfter("/")
    val extension = url.takeLastWhile { it.isLetter() }
}

open class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido)

object ServidorWeb{
    var modulos = mutableListOf<Modulo>()
    var demoraMinima: Int? = null //(en milisegundos)
    //val pedidos = mutableListOf<Pedido>()

    fun validacionProtocolo(pedido: Pedido) =
        if (pedido.protocolo == "http") CodigoHttp.OK
        else CodigoHttp.NOT_IMPLEMENTED

    fun recibirPedidoModulo(pedido: Pedido): Respuesta {
        if (validacionProtocolo(pedido) == CodigoHttp.NOT_IMPLEMENTED) {
            val RespuestaNOT_IMPLEMENTED = Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido)
            return RespuestaNOT_IMPLEMENTED
        }
        else {
            val modulo = moduloSegunPedido(pedido)

            if (modulo != null){
                val RespuestaOK = Respuesta(CodigoHttp.OK, modulo.body, modulo.tiempoRespuesta, pedido)
                return RespuestaOK
            }
            else{
                val RespuestaNOT_FOUND = Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido)
                return RespuestaNOT_FOUND
            }
        }
    }

    private fun moduloSegunPedido(nuevoPedido: Pedido) =  modulos.find { it.puedeResponder(nuevoPedido) }

    var analizadores = mutableListOf<Analizador>()

    //Idea:
    //fun enviarRespuestaAAnalizar(moduloAsignado: Modulo,respuestaAAnalizar: Respuesta) =
    //    analizadores.forEach(recibirRespuesta(moduloAsignado,respuestaAAnalizar))
}


