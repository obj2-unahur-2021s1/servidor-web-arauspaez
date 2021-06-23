package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDate

abstract class Analizador {
    val modulosRespuesta = mutableMapOf <Modulo,MutableList<Respuesta>>()

    fun recibirRespuesta(pedido: Pedido){
        val moduloPedido = pedido.moduloSegunPedido()
        if(!modulosRespuesta.containsKey(moduloPedido)) {
            modulosRespuesta[moduloPedido] = mutableListOf(ServidorWeb.darRespuesta(pedido))
        }
        else {
            modulosRespuesta[moduloPedido]?.add(ServidorWeb.darRespuesta(pedido))
        }
    }
    fun cantidadRespuestas() = modulosRespuesta.values.size
}

class AnalizadorDemoras(val demoraMinima: Int) : Analizador(){
    fun cantidadRespuestasDemoradas(modulo: Modulo) = modulosRespuesta[modulo]?.count { it.respuestaDemorada(demoraMinima)}
}

class AnalizadorIPSospechosa : Analizador() {
    //se debe poder consultar cuántos pedidos realizó una cierta IP sospechosa
    var IPsSospechosas = mutableSetOf<String>()

    //cuántos pedidos realizó una cierta IP sospechosa
    fun contarPedidosEnUnModulo(IP: String,modulo: Modulo) =
        modulosRespuesta[modulo]?.count { it.compararIP(IP) }

    fun totalPedidosSospechosos(IP: String) =
        modulosRespuesta.keys.sumBy { this.contarPedidosEnUnModulo(IP,it)!! }

    //MODULO MAS CONSULTADO

    fun cantidadDeConsultasEnUnModulo(modulo: Modulo): Int {
        var resultado = 0
        for (IP in IPsSospechosas) {
            resultado += this.contarPedidosEnUnModulo(IP,modulo)!!
        }
        return resultado
    }
    fun moduloMasConsultado() = modulosRespuesta.keys.maxByOrNull { this.cantidadDeConsultasEnUnModulo(it) }

    //cuántos pedidos realizó una cierta IP sospechosa sin mirar MODULOSRESPUESTA
    fun contarPedidosDeUnaIPEnModulo(modulo: Modulo, IP: String) =
        modulosRespuesta[modulo]?.count { it.compararIP(IP)} //ACTUAL

    //el conjunto de IPs sospechosas que requirieron una cierta ruta.

    //fun ipSospechosasPorRuta(ruta: String,ip: String) = 1
    fun rutaDeRespuestaEnModulo(ruta: String,modulo: Modulo) = modulosRespuesta[modulo]!!.filter { it.compararRuta(ruta) }
    fun rutasTodosLosModulos(ruta: String) = modulosRespuesta.keys.flatMap { this.rutaDeRespuestaEnModulo(ruta,it) }


    //fun conjuntoIPsSospechosas(ruta: String) = IPsSospechosas.filter { this.rutasTodosLosModulos(ruta).any { it.pedido.ip == it } }  }.toSet()

    //fun modulosRespuesta.values compararRuta(it)
    //fun conjuntoIPsSospechosas20(ruta: String) = IPsSospechosas.forEach { (it) }.toSet()


    //FALTA ESTO Y ESTADISTICAS

    //1.Para cada modulo, filtrar las respuestas que coinciden con la ruta = devuelve una lista
    //2.filtrar las IPsSospechosas segun (1)

}

class AnalizadorEstadisticas : Analizador(){
    fun tiempoRespuestasModulo(modulo: Modulo) = modulosRespuesta[modulo]!!.sumBy { it.tiempo }
    fun tiempoRespuestasTodosLosModulos() = modulosRespuesta.keys.sumBy { this.tiempoRespuestasModulo(it) }
    fun tiempoDeRespuestaPromedio() = tiempoRespuestasTodosLosModulos() / cantidadRespuestas()

    //cantPedidos
    fun pedidosEnModulo(hora1: LocalDate, hora2: LocalDate, modulo: Modulo) = modulosRespuesta[modulo]!!.count { it.fueAtendidoEntreHoras(hora1,hora2) }
    fun totalPedidos(hora1: LocalDate,hora2: LocalDate) = modulosRespuesta.keys.sumBy { this.pedidosEnModulo(hora1,hora2,it) }

    //cantidad de respuestas cuyo body incluye un determinado String (p.ej. cuántas respuestas dicen "hola", lo que incluye "hola amigos" y "ayer me dijeron hola 4 veces"),
    //porcentaje de pedidos con respuesta exitosa.
}

