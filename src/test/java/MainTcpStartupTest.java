import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTcpStartupTest {

    @Test
    void isServerReachableReturnsTrueWhenPortIsListening() throws Exception {
        try (ServerSocket serverSocket = bindTestSocketOn8080()) {
            Assumptions.assumeTrue(serverSocket != null, "Port 8080 is busy, skipping reachability assertion.");
            assertTrue(invokeIsServerReachable());
        }
    }

    @Test
    void ensureServerIsRunningDoesNotStartEmbeddedServerWhenPortAlreadyListening() throws Exception {
        try (ServerSocket serverSocket = bindTestSocketOn8080()) {
            Assumptions.assumeTrue(serverSocket != null, "Port 8080 is busy, skipping startup assertion.");

            PrintStream originalOut = System.out;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                System.setOut(new PrintStream(output));
                invokeEnsureServerIsRunning();
            } finally {
                System.setOut(originalOut);
            }

            String text = output.toString();
            assertFalse(text.contains("Starting embedded server"));
        }
    }

    private ServerSocket bindTestSocketOn8080() throws Exception {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("127.0.0.1", 8080));
            return serverSocket;
        } catch (BindException e) {
            return null;
        }
    }

    private boolean invokeIsServerReachable() throws Exception {
        Method method = Main.class.getDeclaredMethod("isServerReachable");
        method.setAccessible(true);
        return (boolean) method.invoke(null);
    }

    private void invokeEnsureServerIsRunning() throws Exception {
        Method method = Main.class.getDeclaredMethod("ensureServerIsRunning");
        method.setAccessible(true);
        method.invoke(null);
    }
}

