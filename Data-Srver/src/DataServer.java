import java.io.*;
import java.net.*;

public class DataServer {
    static final int PORT = 6060;

    public static void main(String[] args) throws IOException {
        TaskStore store = new TaskStore();
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Data server ready on port " + PORT);
        while (true) {
            try (Socket socket = server.accept();
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                handle(store, in.readLine(), out);
            } catch (Exception e) {
                System.out.println("Request failed: " + e);
            }
        }
    }

    static void handle(TaskStore store, String line, PrintWriter out) {
        if (line == null) return;
        String[] part = line.split(" ", 2);
        String arg = part.length > 1 ? part[1] : "";
        switch (part[0]) {
            case "GETALL" -> {
                for (Task t : store.tasks)
                    out.println(t.id + "|" + t.done + "|" + t.title);
                out.println("END");
            }
            case "INSERT" -> out.println("OK " + store.insert(arg));
            case "SETDONE" -> {
                String[] a = arg.split(" ");
                int id = Integer.parseInt(a[0]);
                boolean done = Boolean.parseBoolean(a[1]);
                out.println(store.setDone(id, done) ? "OK" : "NOTFOUND");
            }
            case "REMOVE" -> {
                int id = Integer.parseInt(arg);
                out.println(store.remove(id) ? "OK" : "NOTFOUND");
            }
            default -> out.println("Unknown command.");
        }
    }
}