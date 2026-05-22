import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/*
====================================================
TEXT LOADER
====================================================
Responsabilidad única:
leer transmisión textual.

POO:
Clase utilitaria.

No necesita crear objetos.
Por eso usa métodos static.
====================================================
*/

public class TextLoader {

    /*
    ====================================================
    CARGA TEXTO DESDE ARCHIVO
    ====================================================
    Lee archivo consolidado generado por Bash.

    Mundo Humano:
    Toda transmisión depende de memoria persistente.
    El archivo funciona como una huella temporal
    entre procesos distintos.
    ====================================================
    */
   public static String cargarTexto(String rutaArchivo) {

        try {

            File archivo = new File(rutaArchivo);

            return Files.readString(archivo.toPath());

        } catch (IOException e) {

            System.out.println(
                    "Error leyendo transmisión."
            );

            return "No se pudo cargar la transmisión.";
        }
    }
}