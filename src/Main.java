import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    private static final int CANTIDAD_TRENES = 6;

        private int[] posicionesX =
                new int[CANTIDAD_TRENES];

        private int[] velocidades =
        cargarVelocidades();

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

        for (int i = 0; i < CANTIDAD_TRENES; i++) {
        posicionesX[i] = 800 + (i * 260);
        }

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

                    for (int i = 0; i < CANTIDAD_TRENES; i++) {

                        posicionesX[i] -= velocidades[i];

                        if (posicionesX[i] < -(mensaje.length() * 8) - 150) {
                            posicionesX[i] = getWidth() + (i * 260);
                        }
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

        System.out.println(
                "\n===== CICLOS DE TRANSMISIÓN ====="
        );

        for (int i = 0; i < CANTIDAD_TRENES; i++) {

            int anchoPantalla =
                    Toolkit
                            .getDefaultToolkit()
                            .getScreenSize()
                            .width;

            int anchoTexto =
                    mensaje.length() * 8;

            int distanciaTotal =
                    anchoPantalla + anchoTexto;

            int tiempo =
                    (
                            distanciaTotal
                            / velocidades[i]
                    ) * AppConfig.INTERVALO_FRAME;

            System.out.println(
                    "Carril "
                    + (i + 1)
                    + " | velocidad "
                    + velocidades[i]
                    + " | ciclo estimado: "
                    + tiempo / 1000
                    + " segundos"
            );
        }

        Timer recargaNotas =
                new Timer(10000, e -> {

                    System.out.println(
                            "\nRecargando transmisión..."
                    );

                    mensaje =
                            TextLoader.cargarTexto(
                                    AppConfig.RUTA_NOTAS
                            );
                    velocidades = cargarVelocidades();
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
   RANDOMIZACIÓN DE VELOCIDADES POR TREN
   ====================================================
   Actualiza la consolidación de <random_velocities.sh>.*/

    private int[] cargarVelocidades() {

        int[] nuevasVelocidades =
                new int[CANTIDAD_TRENES];

        try {

            java.util.List<String> lineas =
                    java.nio.file.Files.readAllLines(
                            java.nio.file.Paths.get(
                                    "data/velocidades.txt"
                            )
                    );

            for (
                    int i = 0;
                    i < CANTIDAD_TRENES
                    && i < lineas.size();
                    i++
            ) {

                nuevasVelocidades[i] =
                        Integer.parseInt(
                                lineas.get(i).trim()
                        );
            }

        } catch (Exception e) {

            System.out.println(
                    "Error cargando velocidades."
            );

            return new int[]{2,2,2,2,2,2};
        }

        return nuevasVelocidades;
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

        int altoDisponible = getHeight();
        int altoCarril = altoDisponible / CANTIDAD_TRENES;
        int tamanoFuente = Math.max(10, altoCarril - 4);

    g.setFont(
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    tamanoFuente
            )
    );

    g.setColor(Color.GREEN);

    for (int i = 0; i < CANTIDAD_TRENES; i++) {

        int y =
                (altoCarril * i)
                + altoCarril
                - 4;

        String renderTexto =
        mensaje + " ° _ ";

        g.drawString(
                renderTexto,
                posicionesX[i],
                y
        );
    }

}

    private static Rectangle calcularAreaPantallas() {

        GraphicsEnvironment entorno =
                GraphicsEnvironment
                        .getLocalGraphicsEnvironment();

        Rectangle areaTotal = new Rectangle();

        for (GraphicsDevice pantalla
                : entorno.getScreenDevices()) {

            Rectangle bounds =
                    pantalla
                            .getDefaultConfiguration()
                            .getBounds();

            areaTotal =
                    areaTotal.union(bounds);
        }

        return areaTotal;
    }

    private static JPanel crearBarraTitulo(JFrame ventana) {

        JPanel barra = new JPanel(new BorderLayout());

        barra.setBackground(Color.BLACK);
        barra.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        Color.GREEN
                )
        );

        barra.setPreferredSize(
                new Dimension(0, 24)
        );

        JLabel end = new JLabel(
                "END",
                SwingConstants.CENTER
        );

        end.setForeground(Color.GREEN);
        end.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        end.setBorder(
                BorderFactory.createEmptyBorder(
                        4,
                        20,
                        4,
                        20
                )
        );

        end.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            System.exit(0);
                        }
                    }
                }
        );

        barra.add(end, BorderLayout.CENTER);

        return barra;
    }

    private static void registrarHoverBarra(
            JPanel contenedor,
            JPanel barraTitulo
    ) {

        MouseAdapter hover = new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                barraTitulo.setVisible(true);
                contenedor.revalidate();
                contenedor.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                barraTitulo.setVisible(false);
                contenedor.revalidate();
                contenedor.repaint();
            }
        };

        contenedor.addMouseListener(hover);
        barraTitulo.addMouseListener(hover);
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

        ventana.setUndecorated(true);

        Main panel = new Main();

        JPanel barraTitulo = crearBarraTitulo(ventana);
        barraTitulo.setVisible(false);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Color.BLACK);
        contenedor.setBorder(
                BorderFactory.createLineBorder(
                        Color.GREEN,
                        1
                )
        );

        contenedor.add(barraTitulo, BorderLayout.NORTH);
        contenedor.add(panel, BorderLayout.CENTER);

        registrarHoverBarra(contenedor, barraTitulo);

        ventana.setContentPane(contenedor);

        Rectangle pantalla =
                calcularAreaPantallas();

        ventana.setBounds(
                pantalla.x,
                pantalla.y,
                pantalla.width,
                100
        );

        ventana.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        ventana.setVisible(true);
    }

}
