package ar.edu.unahur.obj2.servidorWeb

import kotlin.properties.Delegates

abstract class Analizador(val demoraMinima : Int){
    var respuestasRecibidas = mutableListOf<Respuesta>()
    var modulosRecibidos = mutableSetOf<Modulo>()

    fun respuestaDemorada(respuestaAAnalizar : Respuesta) = respuestaAAnalizar.tiempo > demoraMinima

    //Idea:
    fun recibirRespuesta(pedido: Pedido){
        modulosRecibidos.add(ServidorWeb.moduloSegunPedido(pedido)!!) //Se puede poner como logica de if else
        respuestasRecibidas.add(ServidorWeb.recibirPedidoModulo(pedido))
    }

}

class AnalizadorIPSospechosa(demoraMinima: Int) : Analizador(demoraMinima){
    var pedidosRealizados = mutableListOf<Pedido>()
    //se debe poder consultar cuántos pedidos realizó una cierta IP sospechosa -> pedidosRealizados.size
    fun moduloMasConsultado(){}
    fun ipSospechosasPorRuta(ruta: String){}
}

class AnalizadorEstadisticas(demoraMinima: Int) : Analizador(demoraMinima)

class AnalizadorDemoras(demoraMinima: Int) : Analizador(demoraMinima)