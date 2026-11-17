import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class DataClient {
    String host;
    int port;

    DataClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    List<String> ask(String command) throws IOException {
        List<String> lines = new ArrayList<>();
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))) {
            out.println(command);
            String line;
            while ((line = in.readLine()) != null && !line.equals("END"))
                lines.add(line);
        }
        return lines;
    }

    List<Task> getAll() throws IOException {
        List<Task> tasks = new ArrayList<>();
        for (String line : ask("GETALL")) {
            String[] part = line.split("\\|", 3);
            Task t = new Task(Integer.parseInt(part[0]), part[2]);
            t.done = part[1].equals("true");
            tasks.add(t);
        }
        return tasks;
    }

    void insert(String title) throws IOException {
        ask("INSERT " + title);
    }

    void setDone(int id, boolean done) throws IOException {
        ask("SETDONE " + id + " " + done);
    }

    void remove(int id) throws IOException {
        ask("REMOVE " + id);
    }
}