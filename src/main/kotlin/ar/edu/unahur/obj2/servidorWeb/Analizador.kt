package ar.edu.unahur.obj2.servidorWeb

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