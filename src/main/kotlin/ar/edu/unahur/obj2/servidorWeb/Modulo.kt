package ar.edu.unahur.obj2.servidorWeb

import kotlin.properties.Delegates

enum class Extensiones(val valor: String) {
    HTML("html"),
    JPG("jpg"),
    PNG("png"),
    GIF("gif"),
    DOCX("docx"),
    ODT("odt"),
    MP4("mp4"),
}

abstract class Modulo(){
    open var extensiones: MutableList<String> = mutableListOf()
    var body by Delegates.notNull<String>()  //"Un texto fijo"
    var tiempoRespuesta by Delegates.notNull<Int>()//"Un numero, tambien fijo"
    //abstract fun cantidadDeRespuestasDemoradas()?

    fun puedeResponder(pedido: Pedido) = pedido.protocolo == "http" && extensiones.contains(pedido.extension) //arreglar para usar con String
    //el pedido tiene que tener el protocolo "HTTP" y cumplir con alguna de las extensiones permitidas.
}

object ModuloImagen: Modulo() {
    override var extensiones = mutableListOf(Extensiones.JPG.valor,Extensiones.PNG.valor,Extensiones.GIF.valor)
}

object ModuloTexto: Modulo() {
    override var extensiones = mutableListOf(Extensiones.DOCX.valor,Extensiones.ODT.valor,Extensiones.HTML.valor)
}

object ModuloVideo: Modulo() {
    override var extensiones = mutableListOf(Extensiones.MP4.valor)
}

object SinModulo: Modulo()
