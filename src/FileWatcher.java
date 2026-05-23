import java.io.IOException;
import java.nio.file.*;

public class FileWatcher {

    private Main main;

    public FileWatcher(Main main) {

        this.main = main;
}

    public void observarDirectorio() {

        Path ruta = Paths.get("data/txt");

        try {

            WatchService watcher =
                    FileSystems.getDefault()
                            .newWatchService();

            ruta.register(
                    watcher,
                    StandardWatchEventKinds.ENTRY_CREATE
            );

            while (true) {

                WatchKey key = watcher.take();

                for (WatchEvent<?> event : key.pollEvents()) {

                    System.out.println(
                            "Nuevo archivo detectado."
                    );

                    ejecutarMixer();
                }

                key.reset();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void ejecutarMixer() {

        try {

            ProcessBuilder pb =
                    new ProcessBuilder(
                            "bash",
                            "scripts/mezclar_marquesina.sh"
                    );

            pb.inheritIO();

            Process proceso = pb.start();

            proceso.waitFor();

            System.out.println(
                    "Transmisión regenerada."
            );

        } catch (IOException | InterruptedException e) {

            e.printStackTrace();
        }

        main.actualizarMensaje();

    }
}
