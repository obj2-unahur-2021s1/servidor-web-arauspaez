// Pueden usar este archivo para hacer pruebas rápidas,
// de la misma forma en que usaban el REPL de Wollok.

// OJO: lo que esté aquí no será tenido en cuenta
// en la corrección ni reemplaza a los tests.


val url = "http://pepito.com.ar/documentos/doc1.jpg"

print(url)

//url.subSequence(0,4)

url.takeWhile { it.isLetter() }

url.takeLastWhile { it.isLetter() }

url.split("://").last()

url.split("/").last()

url.split("://").last().substringBefore("/")

url.split("://").last().substringAfter("/")

val rutaVilla = "/" + url.split("://").last().substringAfter("/")

rutaVilla


url.substringAfter("/")



url.substringAfterLast(":/")









