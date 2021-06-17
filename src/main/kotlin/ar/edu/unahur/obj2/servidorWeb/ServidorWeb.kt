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

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido)

class ServidorWeb{
  var modulos = mutableListOf<Modulo>()
  var demoraMinima: Int? = null //(en milisegundos)
  /*Una respuesta cuyo tiempo de respuesta supere la demora mínima se considera demorada*/

  fun recibirPedido(nuevoPedido: Pedido): Respuesta{
    if(moduloSegunPedido(nuevoPedido) != null){
      val completaRespuesta = Respuesta(CodigoHttp.OK,moduloSegunPedido(nuevoPedido)!!.body,moduloSegunPedido(nuevoPedido)!!.tiempoRespuesta,nuevoPedido)
      return completaRespuesta
    }else{
      val completaRespuesta = Respuesta(CodigoHttp.NOT_IMPLEMENTED,"",10,nuevoPedido)
      return completaRespuesta
    }
    //agregar la complejidad para analizadores (**)
  }
  private fun moduloSegunPedido(nuevoPedido: Pedido) = modulos.find { it.puedeResponder(nuevoPedido) }

  var analizadores = mutableListOf<Analizador>()
}


abstract class Modulo(){
  open var extensiones = mutableListOf<String>()
  abstract var body: String //"Un texto fijo"
  abstract var tiempoRespuesta: Int //"Un numero, tambien fijo"
  //abstract fun cantidadDeRespuestasDemoradas()?

  fun puedeResponder(pedido: Pedido) = pedido.protocolo == "HTTP" && this.extensiones.contains(pedido.extension) //arreglar para usar con String
  //el pedido tiene que tener el protocolo "HTTP" y cumplir con alguna de las extensiones permitidas.
}

class ModuloImagen() : Modulo() {
  override var extensiones = mutableListOf<String>("jpg","png","gif")
  override lateinit var body: String
  override var tiempoRespuesta by Delegates.notNull<Int>()
}

class ModuloTexto() : Modulo() {
  override var extensiones = mutableListOf<String>("docx","odt")
  override lateinit var body: String
  override var tiempoRespuesta by Delegates.notNull<Int>()
}

class ModuloVideo() : Modulo() {
  override var extensiones = mutableListOf<String>("mp3")
  override lateinit var body: String
  override var tiempoRespuesta by Delegates.notNull<Int>()
}

abstract class Analizador(){
  //son objetos que registran y/o analizan distintos aspectos del tráfico
  //abstract fun pedidosRealizados()
  //abstract fun moduloMasConsultado()
}

class AnalizadorIPSospechosa() : Analizador(){
  var pedidosRealizados = mutableListOf<Pedido>()
  //se debe poder consultar cuántos pedidos realizó una cierta IP sospechosa -> pedidosRealizados.size
  fun moduloMasConsultado(){}
  fun ipSospechosasPorRuta(ruta: String){}
}

class AnalizadorEstadisticas() : Analizador()

class AnalizadorDemoras() : Analizador()
