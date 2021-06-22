package ar.edu.unahur.obj2.servidorWeb

abstract class Analizador {
    val modulosRespuesta = mutableMapOf <Modulo,MutableList<Respuesta>>()

    fun recibirRespuesta(pedido: Pedido){
        val moduloPedido = ServidorWeb.moduloSegunPedido(pedido)
        if(!modulosRespuesta.containsKey(moduloPedido)) {
            modulosRespuesta[moduloPedido] = mutableListOf(ServidorWeb.darRespuesta(pedido))
        }
        else {
            modulosRespuesta[moduloPedido]?.add(ServidorWeb.darRespuesta(pedido))
        }
    }

}

class AnalizadorDemoras(val demoraMinima: Int) : Analizador(){
    fun cantidadRespuestasDemoradas(modulo: Modulo) = modulosRespuesta[modulo]?.count { it.respuestaDemorada(demoraMinima)}
}

class AnalizadorIPSospechosa : Analizador() {
    //se debe poder consultar cuántos pedidos realizó una cierta IP sospechosa
    val IPsSospechosas = mutableMapOf<String, MutableList<Pedido>>()
    fun recibirPedidoConIPSospechosa(respuesta: Respuesta) {
        val ipPedido = respuesta.pedido.ip
        if (!IPsSospechosas.containsKey(ipPedido)) {
            IPsSospechosas[ipPedido] = mutableListOf(respuesta.pedido)
        } else {
            IPsSospechosas[ipPedido]?.add(respuesta.pedido)
        }
    }
    //fun contarPedidosDeUnaIP(IP: String) = IPsSospechosas[IP]?.count() //VER
    //cuántos pedidos realizó una cierta IP sospechosa sin mirar MODULOSRESPUESTA
    fun contarPedidosDeUnaIPEnModulo(modulo: Modulo, IP: String) = modulosRespuesta[modulo]?.count { it.compararIP(IP)} //ACTUAL
    //cuántos pedidos realizó una cierta IP sospechosa

    fun pedidosTotalesRealizadosPorUnaIpSospechosa(ipSospechosa: String) = modulosRespuesta.keys.sumBy { this.contarPedidosDeUnaIPEnModulo(it,ipSospechosa)!! }

    fun cantidadDeConsultasIPsSospechosasEnUnModulo(modulo: Modulo): Int {
        var resultado = 0
        for (IP in IPsSospechosas) {
        resultado += this.contarPedidosDeUnaIPEnModulo(modulo, IP.toString())!!
        }
        return resultado
    }

    //fun cantidadDeConsultasIPsSospechosasEnUnModulo2(modulo: Modulo) = IPsSospechosas.keys.map { contarPedidosDeUnaIPEnModulo(modulo,it) } //VER

    fun moduloMasConsultado() = modulosRespuesta.keys.maxByOrNull { this.cantidadDeConsultasIPsSospechosasEnUnModulo(it) }
    //cuál fue el módulo más consultado por todas las IPs sospechosas

    //fun sumarModuloIPSospechosas(modulo: Modulo,IP: String) = modulosRespuesta[modulo]!!.count { it.compararIP(IP) } //modulo: Int -> recursividad; modulo: Modulo -> SumBy


    //FALTA ESTO Y ESTADISTICAS
    //fun ipSospechosasPorRuta(ruta: String,ip: String) = 1
    //fun pedidosSospechososSegunRuta(ruta: String) = IPsSospechosas.keys.filter().toSet()

    //el conjunto de IPs sospechosas que requirieron una cierta ruta.
}

class AnalizadorEstadisticas : Analizador(){
    //fun tiempoDeRespuestaPromedio() = tiempoTotalRespuestas() / cantidadTotalRespuestas()
    //fun cantidadTotalRespuestas() = modulosRespuesta.values.size
    //fun tiempoTotalRespuestas() = modulosRespuesta.values.sumBy { it.tiempo }

}

