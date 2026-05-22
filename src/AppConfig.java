/*
====================================================
CONFIG
====================================================
Configuración central del sistema.

Buena práctica:
concentrar constantes globales.
====================================================
*/

public class AppConfig {

    // Ruta archivo consolidado
    public static final String RUTA_NOTAS =
            "data/marquesina_mix.txt";

    // Movimiento
    public static final int VELOCIDAD = 2;

    // 16 ms ≈ 60 FPS
    public static final int INTERVALO_FRAME = 16;
}
