import java.io.*;
import java.net.*;

public class AppServer {
    static final int PORT = 5050;

    public static void main(String[] args) throws IOException {
        TodoService todo = new TodoService(new DataClient("localhost", 6060));
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Application server ready on port " + PORT);
        while (true) {
            try (Socket socket = server.accept();
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                handle(todo, in.readLine(), out);
            } catch (Exception e) {
                System.out.println("Request failed: " + e);
            }
        }
    }
    static void handle(TodoService todo, String line, PrintWriter out) {
        if (line == null) return;
        String[] part = line.split(" ", 2);
        String arg = part.length > 1 ? part[1] : "";
        switch (part[0]) {
            case "ADD" -> out.println(todo.add(arg.trim()));
            case "LIST" -> {
                for (Task t : todo.list())
                    out.println(t.id + "|" + t.done + "|" + t.title);
                out.println("END");
            }
            case "TOGGLE" -> out.println(todo.toggle(Integer.parseInt(arg)));
            case "DELETE" -> out.println(todo.delete(Integer.parseInt(arg)));
            case "CLEAR" -> out.println(todo.clearDone());
            default -> out.println("Unknown command.");
        }
    }
}