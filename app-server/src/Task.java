
public class Task {
    int id;
    String title;
    boolean done = false;

    Task(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public String toString() {
        return (done ? "[x] " : "[ ] ") + title;
    }
}