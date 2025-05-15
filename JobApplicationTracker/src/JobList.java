import javafx.collections.*;

public class JobList<T> {
    private final ObservableList<T> jobs;

    public JobList(ObservableList<T> jobs) {
        this.jobs = jobs;
    }

    public void addJob(T job) {
        jobs.add(job);
    }

    public void removeJob(T job) {
        jobs.remove(job);
    }

    public ObservableList<T> getJobs() {
        return jobs;
    }
}
