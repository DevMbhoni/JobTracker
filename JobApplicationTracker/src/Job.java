import javafx.beans.property.*;

import java.time.*;

public class Job {
    StringProperty companyName = new SimpleStringProperty();
    StringProperty positionTitle = new SimpleStringProperty();
    ObjectProperty<LocalDate> applicationDate = new SimpleObjectProperty<LocalDate>();
    StringProperty status = new SimpleStringProperty();
    BooleanProperty isRemote = new SimpleBooleanProperty();

    public Job(String companyName, String positionTitle, LocalDate applicationDate, String status, boolean isRemote) {
        this.companyName.set(companyName);
        this.positionTitle.set(positionTitle);
        this.applicationDate.set(applicationDate);
        this.status.set(status);
        this.isRemote.set(isRemote);
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public StringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public String getPositionTitle() {
        return positionTitle.get();
    }

    public StringProperty positionTitleProperty() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle.set(positionTitle);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public LocalDate getApplicationDate() {
        return applicationDate.get();
    }

    public ObjectProperty<LocalDate> applicationDateProperty() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate.set(applicationDate);
    }

    public boolean IsRemote() {
        return isRemote.get();
    }

    public BooleanProperty isRemoteProperty() {
        return isRemote;
    }

    public void setIsRemote(boolean isRemote) {
        this.isRemote.set(isRemote);
    }
}
