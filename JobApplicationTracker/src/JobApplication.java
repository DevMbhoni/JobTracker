import javafx.application.*;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.time.LocalDate;

public class JobApplication extends Application {
    private TableView<Job> table = new TableView<>();
    private TextField companyNameField = new TextField();
    private TextField posTitleField = new TextField();
    private DatePicker appDateField = new DatePicker();
    private TextField statusField = new TextField();
    private CheckBox IsRemoteField = new CheckBox("Remote");

    private JobList<Job> controller;
    private XMLBuilder builder = new XMLBuilder();
    @Override
    public void start(Stage primarystage) throws Exception {
        ObservableList<Job> jobs = FXCollections.observableArrayList(builder.loadData());
        controller = new JobList<>(jobs);


        TableColumn<Job, String> companyNameCol = new TableColumn<>("Company Name");
        companyNameCol.setCellValueFactory(cellData -> cellData.getValue().companyNameProperty());

        TableColumn<Job, String> posTitleCol = new TableColumn<>("Title");
        posTitleCol.setCellValueFactory(cellData -> cellData.getValue().positionTitleProperty());

        TableColumn<Job, String> appDateCol = new TableColumn<>("Application Date");
        appDateCol.setCellValueFactory(cellData -> cellData.getValue().applicationDateProperty().asString());

        TableColumn<Job, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        TableColumn<Job, String> isRemoteCol = new TableColumn<>("Remote");
        isRemoteCol.setCellValueFactory(cellData -> cellData.getValue().isRemoteProperty().asString());

        table.setItems(controller.getJobs());
        table.getColumns().addAll(companyNameCol, posTitleCol, appDateCol, statusCol, isRemoteCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldJob, selectedJob) -> {
            if (oldJob != null) {
                companyNameField.textProperty().unbindBidirectional(oldJob.companyNameProperty());
                posTitleField.textProperty().unbindBidirectional(oldJob.positionTitleProperty());
                appDateField.valueProperty().unbindBidirectional(oldJob.applicationDateProperty());
                statusField.textProperty().unbindBidirectional(oldJob.statusProperty());
                IsRemoteField.selectedProperty().unbindBidirectional(oldJob.isRemoteProperty());
            }

            if (selectedJob != null) {
                companyNameField.textProperty().bindBidirectional(selectedJob.companyNameProperty());
                posTitleField.textProperty().bindBidirectional(selectedJob.positionTitleProperty());
                appDateField.valueProperty().bindBidirectional(selectedJob.applicationDateProperty());
                statusField.textProperty().bindBidirectional(selectedJob.statusProperty());
                IsRemoteField.selectedProperty().bindBidirectional(selectedJob.isRemoteProperty());
            } else {
                clearForm();
            }
        });

        // === Buttons ===
        Button newButton = new Button("New");
        Button addButton = new Button("Add");
        Button deleteButton = new Button("Delete");
        Button saveButton = new Button("Save");

        newButton.setOnAction(event -> {
            table.getSelectionModel().clearSelection(); // triggers listener to clear bindings
            clearForm(); // just in case
        });

        addButton.setOnAction(event -> {
            if (companyNameField.getText().isEmpty() || posTitleField.getText().isEmpty()) {
                showAlert("Company and Title are required.", Alert.AlertType.ERROR);
                return;
            }

            if (table.getSelectionModel().getSelectedItem() != null) {
                showAlert("Click 'New' to add a new job. You are currently editing an existing one.", Alert.AlertType.ERROR);

                return;
            }

            try {
                String companyName = companyNameField.getText();
                String title = posTitleField.getText();
                LocalDate date = appDateField.getValue();
                String status = statusField.getText();
                boolean isRemote = IsRemoteField.isSelected();

                Job newJob = new Job(companyName, title, date, status, isRemote);
                controller.addJob(newJob);
                builder.Save(controller.getJobs());

                table.getSelectionModel().clearSelection();
                clearForm();
            } catch (Exception e) {
                showAlert("Invalid input. Please check your entries.", Alert.AlertType.ERROR);
            }
        });

        deleteButton.setOnAction(event -> {
            Job selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.removeJob(selected);
                builder.Save(controller.getJobs());
                table.getSelectionModel().clearSelection();
            }
        });
        saveButton.setOnAction(event -> {
            Job selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                builder.Save(controller.getJobs());  // Since properties are bound, just save
                showAlert("Job updated successfully.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("No job selected to save.", Alert.AlertType.ERROR);
            }
        });


        controller.getJobs().addListener((ListChangeListener<Job>) change -> builder.Save(controller.getJobs()));

        // === Layout ===
        VBox form = new VBox(5,
                new Label("Company Name:"), companyNameField,
                new Label("Title:"), posTitleField,
                new Label("Application Date:"), appDateField,
                new Label("Status:"), statusField,
                new Label("Remote:"), IsRemoteField,
                new HBox(5, newButton, addButton,saveButton, deleteButton)
        );
        form.setPadding(new Insets(10));

        HBox root = new HBox(10, table, form);
        root.setPadding(new Insets(10));
        HBox.setHgrow(table, Priority.ALWAYS);

        primarystage.setTitle("Job Tracker");
        primarystage.setScene(new Scene(root, 800, 400));
        primarystage.show();
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void clearForm() {
        companyNameField.clear();
        posTitleField.clear();
        appDateField.setValue(null);
        statusField.clear();
        IsRemoteField.setSelected(false);
    }


}
