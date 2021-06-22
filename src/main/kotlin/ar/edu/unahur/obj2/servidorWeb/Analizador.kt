package ar.edu.unahur.obj2.servidorWeb

abstract class Analizador(){
    val modulosRespuesta = mutableMapOf <Modulo,MutableList<Respuesta>>()

    fun recibirRespuesta(pedido: Pedido){
        val moduloPedido = ServidorWeb.moduloSegunPedido(pedido)
        if(!modulosRespuesta.containsKey(moduloPedido)) {
            modulosRespuesta[moduloPedido] = mutableListOf<Respuesta>(ServidorWeb.darRespuesta(pedido))
        }
        else {
            modulosRespuesta[moduloPedido]?.add(ServidorWeb.darRespuesta(pedido))
        }
    }

}

class AnalizadorDemoras(val demoraMinima: Int) : Analizador(){
    fun cantidadRespuestasDemoradas(modulo: Modulo) = modulosRespuesta[modulo]?.count { it.respuestaDemorada(demoraMinima)}
}

class AnalizadorIPSospechosa() : Analizador(){
    //se debe poder consultar cuántos pedidos realizó una cierta IP sospechosa
    val IPsSospechosas = mutableMapOf<String,MutableList<Pedido>>()
    fun recibirPedidoConIPSospechosa(respuesta: Respuesta){
        val ipPedido = respuesta.pedido.ip
        if(!IPsSospechosas.containsKey(ipPedido)) {
            IPsSospechosas[ipPedido] = mutableListOf<Pedido>(respuesta.pedido)
        }
        else {
            IPsSospechosas[ipPedido]?.add(respuesta.pedido)
        }
    }

    fun sumarModuloIPSospechosas(modulo: Modulo,IP: String) = modulosRespuesta[modulo]!!.count { it.compararIP(IP) } //modulo: Int -> recursividad; modulo: Modulo -> SumBy
    //fun contarIPsSospechosas(IP: String,n: Int = modulosRespuesta.keys.size - 1): Int{
    //    if(n == 0){
    //        return sumarModuloIPSospechosas(0,IP)
    //    } else {
    //        return sumarModuloIPSospechosas(n,IP) + contarIPsSospechosas(IP,n - 1)
    //    }
    //}
    fun contarPedidosConIPs(IP: String) = modulosRespuesta.keys.sumBy { this.sumarModuloIPSospechosas(it,IP)!! }



    fun consultadoPorTodasLasIPsSospechosas() = modulosRespuesta.keys.map { this.sumarModuloIPSospechosas(it,) }
    fun moduloMasConsultado() = modulosRespuesta.keys.maxByOrNull { this.consultadoPorTodasLasIPsSospechosas() }
    //cuál fue el módulo más consultado por todas las IPs sospechosas


    fun ipSospechosasPorRuta(ruta: String,ip: String) =
    fun pedidosSospechososSegunRuta(ruta: String) = IPsSospechosas.keys.filter {  }.toSet()

    //el conjunto de IPs sospechosas que requirieron una cierta ruta.
}

class AnalizadorEstadisticas() : Analizador(){
    fun tiempoDeRespuestaPromedio() = tiempoTotalRespuestas() / cantidadTotalRespuestas()
    fun cantidadTotalRespuestas() = modulosRespuesta.values.size
    fun tiempoTotalRespuestas() = modulosRespuesta.values.sumBy { it.tiempo }

}

