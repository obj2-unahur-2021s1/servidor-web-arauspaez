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

class Pedido(val ip: String, val url: String /*cambiar por tipo URL?*/, val fechaHora: LocalDateTime){
  /*De cada pedido nos interesa saber:
      la dirección IP de quien hace el pedido. Para este ejercicio se puede manejar como un String, por ejemplo: "207.46.13.5";
      la fecha y hora. Se recomienda usar para esto la clase LocalDateTime;
      la URL que se está requiriendo, por ejemplo http://pepito.com.ar/documentos/doc1.html*/
}
class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido){
  /*La respuesta a un pedido consiste de:
      tiempo que tardó en responder (en milisegundos);
      un código de respuesta;
      un body o contenido que será un String;
      una referencia al pedido que la generó.*/
}

class URL(val protocolo: String,val ruta: String,val extension: String){
  /*A una URL posteriormente va a interesar descomponerla en estos datos,
  que se describen tomando "http://pepito.com.ar/documentos/doc1.html" como ejemplo:
      el protocolo, en este caso "http";
      la ruta, en este caso "/documentos/doc1.html";
      la extensión, en este caso "html".*/
        //URL = protocolo + ruta + extension
} //💡: val extension: Extension?

object ServidorWeb{
  var modulos = mutableListOf<Modulo>()

  var demoraMinima by Delegates.notNull<Int>() //(en milisegundos)
  /*Una respuesta cuyo tiempo de respuesta supere la demora mínima se considera demorada*/

  /*fun recibirPedido(nuevoPedido: Pedido): Respuesta{
    if(modulos.any { it.puedeResponder(this) } != null){
      val nuevaRespuesta = Respuesta(CodigoHttp.OK,/*body definido por el modulo*/,/*tiempo de respuesta definido por el modulo*/,nuevoPedido)
      return nuevaRespuesta
    }else{
      val nuevaRespuesta = Respuesta(CodigoHttp.NOT_FOUND,/*body definido por el modulo*/,/*tiempo de respuesta definido por el modulo*/,nuevoPedido)
      return nuevaRespuesta
    }
    //agregar la complejidad para analizadores (**)
  }*/

  var analizadores by Delegates.notNull<Analizador>()
}


abstract class Modulo(){
  //abstract fun extensiones() 💡: interface extension?
  abstract fun retorna(): String
  abstract fun tiempo(): Int
  //abstract fun cantidadDeRespuestasDemoradas()
}

class ModuloImagen() : Modulo() {
  //override fun extensiones() trabaja con jpg, png y gif
  override fun retorna() = "?"
  override fun tiempo() = 999999
}

class ModuloTexto() : Modulo() {
  //override fun extensiones() trabaja con docx, odt
  override fun retorna() = "?"
  override fun tiempo() = 999999
}

class ModuloVideo() : Modulo() {
  //override fun extensiones() trabaja con mp4
  override fun retorna() = "?"
  override fun tiempo() = 999999
}


abstract class Analizador(){
  //abstract fun pedidosRealizados()
  //abstract fun moduloMasConsultado()
}
  //son objetos que registran y/o analizan distintos aspectos del tráfico

class AnalizadorIPSospechosa() : Analizador(){
  var pedidosRealizados = mutableListOf<Pedido>()
  //se debe poder consultar cuántos pedidos realizó una cierta IP sospechosa -> pedidosRealizados.size
  fun moduloMasConsultado(){}
  fun ipSospechosasPorRuta(ruta: String){}
}

class AnalizadorEstadisticas() : Analizador()

class AnalizadorDemoras() : Analizador()
