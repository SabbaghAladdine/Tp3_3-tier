import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TodoService {
    static final String DOWN = "Data server unreachable.";
    DataClient data;

    TodoService(DataClient data) {
        this.data = data;
    }

    List<Task> list() {
        try {
            return data.getAll();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    String add(String title) {
        if (title.isEmpty()) return "A title is required.";
        try {
            for (Task t : data.getAll())
                if (!t.done && t.title.equalsIgnoreCase(title))
                    return "This task already exists.";
            data.insert(title);
            return "Task added.";
        } catch (IOException e) {
            return DOWN;
        }
    }

    Task find(int id) throws IOException {
        for (Task t : data.getAll())
            if (t.id == id) return t;
        return null;
    }
    String toggle(int id) {
        try {
            Task t = find(id);
            if (t == null) return "Unknown task.";
            data.setDone(id, !t.done);
            return t.done ? "Task marked as not done." : "Task marked as done.";
        } catch (IOException e) {
            return DOWN;
        }
    }

    String delete(int id) {
        try {
            if (find(id) == null) return "Unknown task.";
            data.remove(id);
            return "Task deleted.";
        } catch (IOException e) {
            return DOWN;
        }
    }

    String clearDone() {
        try {
            int count = 0;
            for (Task t : data.getAll())
                if (t.done) {
                    data.remove(t.id);
                    count++;
                }
            return count + " completed task(s) removed.";
        } catch (IOException e) {
            return DOWN;
        }
    }
}