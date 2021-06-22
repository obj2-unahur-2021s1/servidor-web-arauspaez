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

open class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido){
    fun respuestaDemorada(demoraMinima: Int) = tiempo > demoraMinima
    fun compararIP(ip: String) = pedido.ip == ip
}

object ServidorWeb{
    var modulos = mutableListOf<Modulo>()
    //val pedidos = mutableListOf<Pedido>()

    fun validacionProtocolo(pedido: Pedido) =
        if (pedido.protocolo == "http") CodigoHttp.OK
        else CodigoHttp.NOT_IMPLEMENTED

    fun darRespuesta(pedido: Pedido): Respuesta {
        if (validacionProtocolo(pedido) == CodigoHttp.NOT_IMPLEMENTED) {
            val RespuestaNOT_IMPLEMENTED = Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido)
            return RespuestaNOT_IMPLEMENTED
        }
        else {
            val modulo = moduloSegunPedido(pedido)

            if (modulo != ModuloSinExtensiones){
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

    fun moduloSegunPedido(pedido: Pedido) : Modulo {
        var moduloElegido = modulos.find { it.puedeResponder(pedido) }
        if (validacionProtocolo(pedido) == CodigoHttp.NOT_IMPLEMENTED){
            moduloElegido = ModuloSinProtocolo
        }
        else if(moduloElegido == null) {
            moduloElegido = ModuloSinExtensiones
        }
        return moduloElegido
    }
    //Analizadores
    var analizadores = mutableListOf<Analizador>()

    //Idea:
    fun enviarRespuestaAAnalizar(pedido: Pedido) { analizadores.forEach { it.recibirRespuesta(pedido) } }
}

