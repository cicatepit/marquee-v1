import java.awt.*;
    /*
    ====================================================
    RENDER UTILS
    ====================================================
    Métodos auxiliares de render.

    Separar utilidades evita:
    - duplicación,
    - contaminación visual,
    - clases gigantes.
    ====================================================
    */

public class RenderUtils {

    /*
    ====================================================
    SUAVIZADO TIPOGRÁFICO
    ====================================================
    Anti alias reduce bordes irregulares.
    ====================================================
    */

    public static void activarAntialias(Graphics2D g2d) {

        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
    }

    /*
    ====================================================
    CÁLCULO DE RECORRIDO
    ====================================================
    Estima duración total del desplazamiento.

    Matemáticamente:

    distancia / velocidad = tiempo

    Mundo Humano:
    Toda transmisión necesita duración.
    Incluso la información posee una velocidad
    de desaparición.
    ====================================================
    */

    public static int calcularTiempoRecarga(
            String mensaje
    ) {

        int anchoPantalla =
                Toolkit
                        .getDefaultToolkit()
                        .getScreenSize()
                        .width;

        int anchoTexto = mensaje.length() * 14;

        int distanciaTotal =
                anchoPantalla + anchoTexto;

        return (
                distanciaTotal
                / AppConfig.VELOCIDAD
        ) * AppConfig.INTERVALO_FRAME;
    }
}