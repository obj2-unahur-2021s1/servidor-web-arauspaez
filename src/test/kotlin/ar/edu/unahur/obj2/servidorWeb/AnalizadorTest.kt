package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.maps.shouldContainAnyValuesOf
import io.kotest.matchers.maps.shouldContainKeys
import io.kotest.matchers.maps.shouldContainValue
import io.kotest.matchers.shouldBe
import java.time.LocalDate


class AnalizadorTest : DescribeSpec({

    val analizadorDemora = AnalizadorDemoras(20)
    val analizadorIPSospechosa = AnalizadorIPSospechosa()
    val analizadorEstadisticas = AnalizadorEstadisticas()

    //Modulos
    ModuloTexto.body = "texto"
    ModuloTexto.tiempoRespuesta = 10

    ModuloVideo.body = "video"
    ModuloVideo.tiempoRespuesta = 10

    ModuloImagen.body = "imagen"
    ModuloImagen.tiempoRespuesta = 30

    //PedidosConModulo
    val fecha = LocalDate.of(2001, 9,11)
    val pedidoConModuloIMAGEN = Pedido("207.46.13.6","http://pepito.com.ar/documentos/doc1.jpg",fecha)
    val pedidoConModuloVIDEO = Pedido("207.46.13.6","http://pepito.com.ar/documentos/doc1.mp4",fecha)
    val pedidoConModuloTEXTO = Pedido("207.46.13.6","http://pepito.com.ar/documentos/doc1.docx",fecha)
    val pedidoConModuloOK = Pedido("207.46.13.6","http://pepito.com.ar/documentos/doc1.jpg",fecha)
    val pedidoConModuloNOT_IMPLEMENTED = Pedido("207.46.13.7","htt://pepita.com.ar/documentos/doc1.html",fecha)
    val pedidoConModuloNOT_FOUND = Pedido("207.46.13.8","http://pepite.com.ar/documentos/doc1.pdf",fecha)

    //Agregar modulos a Servidor
    ServidorWeb.modulos = mutableListOf(ModuloTexto,ModuloImagen,ModuloVideo,SinModulo)
    //Agregar analizadores a Servidor
    ServidorWeb.analizadores = mutableListOf(analizadorDemora,analizadorIPSospechosa,analizadorEstadisticas)

    val respuestaNOT_IMPLEMENTED = ServidorWeb.darRespuesta(pedidoConModuloNOT_IMPLEMENTED)
    val respuestaOK = ServidorWeb.darRespuesta(pedidoConModuloOK)
    val respuestaNOT_FOUND = ServidorWeb.darRespuesta(pedidoConModuloNOT_FOUND)

    //TODO: REVISAR
    describe("enviarRespuestaAAnalizar desde Servidor"){
        analizadorDemora.cantidadRespuestas() shouldBe 0
        analizadorIPSospechosa.cantidadRespuestas() shouldBe 0
        analizadorEstadisticas.cantidadRespuestas() shouldBe 0
        ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloOK)
        analizadorDemora.cantidadRespuestas() shouldBe 1
        analizadorIPSospechosa.cantidadRespuestas() shouldBe 1
        analizadorEstadisticas.cantidadRespuestas() shouldBe 1
        //Hasta aca corre joya
        //analizadorDemora.modulosRespuesta.shouldContainKeys(ModuloImagen)
        //analizadorIPSospechosa.modulosRespuesta.shouldContainKeys(ModuloImagen)
        //analizadorEstadisticas.modulosRespuesta.shouldContainKeys(ModuloImagen)
        //analizadorEstadisticas.modulosRespuesta.shouldContainValue(respuestaOK)
    }

    describe("Detección de demora en respuesta"){
        it("cantidadRespuestasDemoradas para ModuloImagen"){
            ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloOK)
            ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloIMAGEN)
            ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloVIDEO)
            analizadorDemora.cantidadRespuestasDemoradas(ModuloImagen) shouldBe 2
        }
    }
    describe("IPs sospechosas"){
        ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloOK)
        ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloIMAGEN)
        ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloVIDEO)
        ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloTEXTO)
        ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloNOT_FOUND)

        it("cuántos pedidos realizó una cierta IP sospechosa"){
            //4 CON IPs Sospechosa y 1 no
            analizadorIPSospechosa.totalPedidosSospechosos("207.46.13.6") shouldBe 4
        }
        it("Modulo mas consultado por IPs sospechosas"){
            analizadorIPSospechosa.IPsSospechosas = mutableSetOf("207.46.13.6","207.46.13.7","207.46.13.8")
            analizadorIPSospechosa.moduloMasConsultado() shouldBe ModuloImagen
        }
    }

    describe("Estadísticas"){
        it("tiempo de respuesta promedio"){
            ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloOK)
            ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloIMAGEN)
            ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloVIDEO)
            ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloTEXTO)
            ServidorWeb.enviarRespuestaAAnalizar(pedidoConModuloNOT_FOUND)
            analizadorEstadisticas.tiempoDeRespuestaPromedio() shouldBe 22
        }
        //it("cantidad de pedidos entre dos momentos (fecha/hora) que fueron atendidos"){
        //    analizadorEstadisticas.totalPedidos()
        //}
    }
})