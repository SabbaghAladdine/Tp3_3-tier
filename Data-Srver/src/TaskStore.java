import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class TaskStore {
    static final String FILE = "todo.txt";
    List<Task> tasks = new ArrayList<>();
    int nextId = 1;

    TaskStore() {
        try {
            for (String line : Files.readAllLines(Paths.get(FILE))) {
                String[] part = line.split("\\|", 3);
                Task t = new Task(Integer.parseInt(part[0]), part[2]);
                t.done = part[1].equals("true");
                tasks.add(t);
                nextId = Math.max(nextId, t.id + 1);
            }
        } catch (IOException e) {
            // premier lancement : pas de fichier, la liste reste vide
        }
    }

    void save() {
        List<String> lines = new ArrayList<>();
        for (Task t : tasks)
            lines.add(t.id + "|" + t.done + "|" + t.title);
        try {
            Files.write(Paths.get(FILE), lines);
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }
    int insert(String title) {
        Task t = new Task(nextId++, title);
        tasks.add(t);
        save();
        return t.id;
    }

    boolean setDone(int id, boolean done) {
        for (Task t : tasks)
            if (t.id == id) {
                t.done = done;
                save();
                return true;
            }
        return false;
    }

    boolean remove(int id) {
        boolean removed = tasks.removeIf(t -> t.id == id);
        if (removed) save();
        return removed;
    }
}