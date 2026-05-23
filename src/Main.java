import javax.swing.*;
import java.awt.*;

/*
====================================================
MARQUESINA
====================================================
Motor visual principal.

Responsabilidades:
- crear ventana,
- administrar animación,
- coordinar recarga textual,
- renderizar transmisión.

POO:
Main extiende JPanel.
Esto significa que Main ES un panel gráfico.

Herencia:
Permite reutilizar comportamiento gráfico de Swing.
====================================================
*/

public class Main extends JPanel {

    /*
    ====================================================
    CONFIGURACIÓN GENERAL
    ====================================================
    Config concentra rutas y parámetros importantes.

    Centralizar configuración evita:
    - números mágicos,
    - rutas repetidas,
    - mantenimiento caótico.
    ====================================================
    */

    private String mensaje =
            TextLoader.cargarTexto(AppConfig.RUTA_NOTAS);

    // Posición horizontal inicial
    private int x = 800;

    /*
    ====================================================
    CONSTRUCTOR PRINCIPAL
    ====================================================
    Inicializa:
    - fondo,
    - timers,
    - velocidad,
    - recarga.

    El constructor representa el momento de nacimiento
    del objeto.

    Mundo Humano:
    Un sistema también necesita memoria inicial,
    ritmo y límites antes de comenzar a moverse.
    ====================================================
    */

    public Main() {

        setBackground(Color.BLACK);

        // Reduce parpadeo visual
        setDoubleBuffered(true);

        iniciarRecargaTexto();
        iniciarAnimacion();
        
        // ====================================
        // WATCHER DEL DIRECTORIO
        // ====================================

        FileWatcher watcher =
            new FileWatcher(this);

        
        new Thread(
            () -> watcher.observarDirectorio()
    ).start();
    }
/*
    ====================================================
    ANIMACIÓN PRINCIPAL
    ====================================================
    Timer de Swing:
    ejecuta código repetidamente.

    IMPORTANTE:
    Swing posee su propio hilo gráfico.
    Por eso Swing Timer es más seguro para UI.
    ====================================================
    */

    private void iniciarAnimacion() {

        Timer timer = new Timer(
                AppConfig.INTERVALO_FRAME,
                e -> {

                    x -= AppConfig.VELOCIDAD;

                    if (x < -(mensaje.length() * 14) - 300) {

                        x = getWidth();
                    }

                    repaint();
                }
        );

        timer.start();
    }
    /*
    ====================================================
    RECARGA DE TRANSMISIÓN
    ====================================================
    Recarga el archivo consolidado.

    Actualmente:
    - Bash prepara transmisión.
    - Java consume transmisión.

    Separar procesamiento y render simplifica el sistema.
    ====================================================
    */

    private void iniciarRecargaTexto() {

        int tiempoRecarga =
                RenderUtils.calcularTiempoRecarga(mensaje);

        System.out.println(
                "Tiempo estimado ciclo: "
                + tiempoRecarga / 1000
                + " segundos"
        );

        Timer recargaNotas =
                new Timer(tiempoRecarga, e -> {

                    System.out.println(
                            "Recargando transmisión..."
                    );

                    mensaje =
                            TextLoader.cargarTexto(
                                    AppConfig.RUTA_NOTAS
                            );
                });

        recargaNotas.start();
    }

   /*
   ====================================================
   ACTUALIZACIÓN DE TRANSMISIÓN
   ====================================================
   Actualiza la consolidación de <marquesina_mix.txt>.*/

    public void actualizarMensaje() {

        mensaje =
                TextLoader.cargarTexto(
                        AppConfig.RUTA_NOTAS
                );

        System.out.println(
                "Transmisión actualizada."
        );
    }
/*
    ====================================================
    RENDER
    ====================================================
    paintComponent es invocado constantemente por Swing.

    Aquí solamente debe existir lógica visual.

    Regla importante:
    Nunca mezclar carga de archivos,
    networking o procesos pesados aquí.
    ====================================================
    */

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        RenderUtils.activarAntialias(g2d);

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        g.setColor(Color.GREEN);

        g.drawString(mensaje, x, 50);
    }
/*
    ====================================================
    MAIN
    ====================================================
    Punto de entrada del programa.

    Toda aplicación Java comienza aquí.
    ====================================================
    */

    public static void main(String[] args) {

        JFrame ventana = new JFrame("marquee v1");

        Main panel = new Main();

        ventana.add(panel);

        ventana.setSize(
                Toolkit
                        .getDefaultToolkit()
                        .getScreenSize()
                        .width,
                100
        );

        ventana.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        ventana.setLocationRelativeTo(null);

        ventana.setVisible(true);
    }
}
