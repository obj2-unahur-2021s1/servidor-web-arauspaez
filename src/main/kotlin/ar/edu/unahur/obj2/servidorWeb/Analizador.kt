package ar.edu.unahur.obj2.servidorWeb

abstract class Analizador(val demoraMinima : Int){
    var respuestasRecibidas = mutableListOf<Respuesta>()
    var modulosRecibidos = mutableListOf<Modulo>()

    fun respuestaDemorada(respuestaAAnalizar : Respuesta) = respuestaAAnalizar.tiempo > demoraMinima

    //fun respuestasDemoradasSegunModulo(modulo: Modulo)

    //Idea:
    fun recibirRespuesta(moduloAsignado:Modulo,respuestaAAnalizar:Respuesta){
        respuestasRecibidas += respuestaAAnalizar
        modulosRecibidos += moduloAsignado
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