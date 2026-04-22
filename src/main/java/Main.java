import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import service.ClientMain;
import service.ServerMain;


public class Main {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8080;
    private static final int STARTUP_WAIT_ATTEMPTS = 40;
    private static final int STARTUP_WAIT_MS = 250;

    public static void main(String[] args) {
        ensureServerIsRunning();
        ClientMain.main(args);
    }

    private static void ensureServerIsRunning() {
        if (isServerReachable()) {
            return;
        }

        System.out.println("Server is not reachable. Starting embedded server...");
        Thread serverThread = new Thread(() -> ServerMain.main(new String[0]), "diguni-server-thread");
        serverThread.setDaemon(true);
        serverThread.start();

        for (int i = 0; i < STARTUP_WAIT_ATTEMPTS; i++) {
            if (isServerReachable()) {
                System.out.println("Server started successfully.");
                return;
            }
            try {
                Thread.sleep(STARTUP_WAIT_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Warning: server did not start in time. Client will continue and retry on requests.");
    }

    private static boolean isServerReachable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(HOST, PORT), 300);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}