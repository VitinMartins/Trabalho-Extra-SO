import java.util.*;

public class Journal {
    private List<String> log;

    public Journal() {
        this.log = new ArrayList<>();
    }

    public void record(String operation) {
        log.add(operation);
    }

    public List<String> getLog() {
        return log;
    }
}