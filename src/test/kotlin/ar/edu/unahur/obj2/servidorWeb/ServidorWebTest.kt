package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import java.time.LocalDate


class ServidorWebTest : DescribeSpec({

  //Fechas
  val fecha = LocalDate.of(2001, 9,11)

  //PedidosSinModulo
  val pedidoNOT_IMPLEMENTED = Pedido("207.46.13.5","htt://pepito.com.ar/documentos/doc1.html",fecha)
  val pedidoOK = Pedido("207.46.13.5","http://pepito.com.ar/documentos/doc1.html",fecha)

  //PedidosConModulo
  val pedidoConModuloTEXTO = Pedido("207.46.13.6","http://pepito.com.ar/documentos/doc1.docx",fecha)
  val pedidoConModuloOK = Pedido("207.46.13.6","http://pepito.com.ar/documentos/doc1.jpg",fecha)
  val pedidoConModuloNOT_IMPLEMENTED = Pedido("207.46.13.7","htt://pepita.com.ar/documentos/doc1.html",fecha)
  val pedidoConModuloNOT_FOUND = Pedido("207.46.13.8","http://pepite.com.ar/documentos/doc1.pdf",fecha)

  //Modulos
  ModuloTexto.body = "algo"
  ModuloTexto.tiempoRespuesta = 10

  ModuloVideo.body = "algo"
  ModuloVideo.tiempoRespuesta = 10

  ModuloImagen.body = "algo"
  ModuloImagen.tiempoRespuesta = 10



  describe("Un servidor web sin modulos") {
    it("con protocolo correcto"){
      ServidorWeb.validacionProtocolo(pedidoOK) shouldBe 200
    }
    it("protocolo incorrecto"){
      ServidorWeb.validacionProtocolo(pedidoNOT_IMPLEMENTED) shouldBe 501
    }
  }

  describe("Modulo puedeResponder"){
    it("Modulo Imagen"){
      ModuloImagen.puedeResponder(pedidoConModuloOK).shouldBeTrue()
    }
    it("Modulo Video"){
      ModuloVideo.puedeResponder(pedidoConModuloTEXTO).shouldBeFalse()
    }
    it("Modulo Texto"){
      ModuloTexto.puedeResponder(pedidoConModuloTEXTO).shouldBeTrue()
    }
  }

  describe("Servidor con Modulos") {
    ServidorWeb.modulos = mutableListOf(ModuloTexto,ModuloImagen,ModuloVideo,SinModulo)

    it("Respuesta NOT_IMPLEMENTED") {
      val respuesta = ServidorWeb.darRespuesta(pedidoConModuloNOT_IMPLEMENTED)
      respuesta.codigo shouldBe CodigoHttp.NOT_IMPLEMENTED
      respuesta.body shouldBe ""
      respuesta.tiempo shouldBe 10
      respuesta.pedido shouldBe pedidoConModuloNOT_IMPLEMENTED
    }
    it("Respuesta OK") {
      val respuesta = ServidorWeb.darRespuesta(pedidoConModuloOK)
      respuesta.codigo shouldBe CodigoHttp.OK
      respuesta.body shouldBe "algo"
      respuesta.tiempo shouldBe 10
      respuesta.pedido shouldBe pedidoConModuloOK
    }
    it("Respuesta NOT_FOUND") {
      val respuesta = ServidorWeb.darRespuesta(pedidoConModuloNOT_FOUND)
      respuesta.codigo shouldBe CodigoHttp.NOT_FOUND
      respuesta.body shouldBe ""
      respuesta.tiempo shouldBe 10
      respuesta.pedido shouldBe pedidoConModuloNOT_FOUND
    }
  }

})
