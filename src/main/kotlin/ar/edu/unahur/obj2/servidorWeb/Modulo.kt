package ar.edu.unahur.obj2.servidorWeb

import kotlin.properties.Delegates

enum class Extensiones(val valor: String) {
    HTML("html"),
    JPG("jpg"),
    PNG("png"),
    GIF("gif"),
    DOCX("docx"),
    ODT("odt"),
    MP4("mp4")
}

abstract class Modulo(){
    open var extensiones: MutableList<Extensiones> = mutableListOf<Extensiones>()
    var body by Delegates.notNull<String>()  //"Un texto fijo"
    var tiempoRespuesta by Delegates.notNull<Int>()//"Un numero, tambien fijo"
    //abstract fun cantidadDeRespuestasDemoradas()?

    fun puedeResponder(pedido: Pedido) = pedido.protocolo == "http" && extensiones.contains(pedido.extension) //arreglar para usar con String
    //el pedido tiene que tener el protocolo "HTTP" y cumplir con alguna de las extensiones permitidas.
}

class ModuloImagen() : Modulo() {
    override var extensiones = mutableListOf<Extensiones>(Extensiones.JPG,Extensiones.PNG,Extensiones.GIF)
}

class ModuloTexto() : Modulo() {
    override var extensiones = mutableListOf<Extensiones>(Extensiones.DOCX,Extensiones.ODT,Extensiones.HTML)
}

class ModuloVideo() : Modulo() {
    override var extensiones = mutableListOf<Extensiones>(Extensiones.MP4)
}

object ModuloSinExtensiones: Modulo()

object ModuloSinProtocolo: Modulo()