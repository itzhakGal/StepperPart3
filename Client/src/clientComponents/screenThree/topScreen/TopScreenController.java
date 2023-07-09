package clientComponents.screenThree.topScreen;

import clientComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import clientComponents.screenTwo.screenTwoDetails.flowExecuteDetails.FlowExecuteDetailsController;
import clientComponents.screenTwo.screenTwoDetails.stepListDetails.StepListDetailsController;
import com.google.gson.Gson;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import stepper.flow.execution.FlowExecutionResult;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenThree.topScreen.continuation.ContinuationController;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utils.DTOFullDetailsPastRun;
import utils.DTOStepFlowPast;
import utilsDesktopApp.DTOListContinuationFlowName;
import utilsDesktopApp.DTOListFlowsDetails;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class TopScreenController implements Initializable {

    private clientComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController mainFlowExecutionHistoryController;
    //private SystemEngineInterface systemEngine;
    @FXML
    private TableView<clientComponents.screenThree.topScreen.ExecutionData> tableFlowExecution;
    @FXML
    private TableColumn<clientComponents.screenThree.topScreen.ExecutionData, String> flowNameColumn;
    @FXML
    private TableColumn<clientComponents.screenThree.topScreen.ExecutionData, String> startDateColumn;
    @FXML
    private TableColumn<clientComponents.screenThree.topScreen.ExecutionData, String> resultExecutionColumn;

    @FXML private ComboBox<String> resultComboBox;
    @FXML private VBox continuationComponent;
    @FXML private ContinuationController continuationComponentController;
    @FXML
    private Label historyFlowsLabel;
    @FXML
    private Button rerunFlowButton;
    private SimpleStringProperty chosenFlowIdProperty;
    private SimpleStringProperty chosenFlowNameProperty;

    private SimpleBooleanProperty selectedNameTableView;
    private List<DTOFullDetailsPastRunWeb> flowsExecutedList ;
    private SimpleBooleanProperty rerunFlowButtonProperty;
    public TopScreenController()
    {
        chosenFlowIdProperty = new SimpleStringProperty("");
        chosenFlowNameProperty = new SimpleStringProperty("");
        selectedNameTableView = new SimpleBooleanProperty(false);
        rerunFlowButtonProperty = new SimpleBooleanProperty(false);
    }
    @FXML
    private void handleComboBoxAction(ActionEvent event) {
        String selectedFilter = resultComboBox.getValue();
        filterTableDataByResult(selectedFilter);
    }

    @FXML
    private void rerunFlowButtonAction(ActionEvent event) {
        rerunFlowButtonProperty.set(true);
        mainFlowExecutionHistoryController.getMainBodyController().updateExecuteFlowButton(chosenFlowNameProperty.getValue());

        String finalUrl = HttpUrl
                .parse(Constants.FLOW_EXECUTION_TASK)
                .newBuilder()
                .addQueryParameter("flowId", chosenFlowIdProperty.getValue())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new okhttp3.Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        DTOFullDetailsPastRunWeb fullDetailsPastRun = new Gson().fromJson(responseData, DTOFullDetailsPastRunWeb.class);

                        Platform.runLater(() -> {
                            mainFlowExecutionHistoryController.getMainBodyController().getFlowExecutionScreenComponentController().getFreeInputDetailsComponentController().updateFreeInputForRerunExecution(fullDetailsPastRun.getInputs(), fullDetailsPastRun.getFlowName());
                            mainFlowExecutionHistoryController.getMainBodyController().openFlowExecutionTab();
                            rerunFlowButtonProperty.set(false);
                            rerunFlowButton.setDisable(true);

                        });
                    }}finally {
                    response.close();
                }
            }
        });

        /*DTOFullDetailsPastRun fullDetailsPastRun = systemEngine.getFlowExecutedDataDTO((UUID.fromString(chosenFlowIdProperty.getValue())));
        mainFlowExecutionHistoryController.getMainBodyController().getFlowExecutionScreenComponentController().getFreeInputDetailsComponentController().updateFreeInputForRerunExecution(fullDetailsPastRun.getInputs(), fullDetailsPastRun.getFlowName());
        mainFlowExecutionHistoryController.getMainBodyController().openFlowExecutionTab();
        rerunFlowButtonProperty.set(false);
        rerunFlowButton.setDisable(true);*/
    }
    public void init() {
        tableFlowExecution.setOnMouseClicked(event -> {
            clientComponents.screenThree.topScreen.ExecutionData selectedFlow = tableFlowExecution.getSelectionModel().getSelectedItem();
            if (selectedFlow != null) {
                chosenFlowNameProperty.set(selectedFlow.getFlowName());
                chosenFlowIdProperty.set(selectedFlow.getFlowId().toString());
                rerunFlowButton.setDisable(false);
                updateContinuationDetails(selectedFlow.getFlowName());
            }
        });

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (continuationComponentController != null) {
            continuationComponentController.setMainController(this);
        }

        flowNameColumn.setCellValueFactory(new PropertyValueFactory<clientComponents.screenThree.topScreen.ExecutionData, String>("flowName"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<clientComponents.screenThree.topScreen.ExecutionData, String>("startDate"));
        resultExecutionColumn.setCellValueFactory(new PropertyValueFactory<clientComponents.screenThree.topScreen.ExecutionData, String>("resultExecutions"));
        setRightToLeftAlignment(flowNameColumn);
        setRightToLeftAlignment(startDateColumn);
        setRightToLeftAlignment(resultExecutionColumn);
        initialComboBox();
        addAnimation();
    }

     private void addAnimation() {
        tableFlowExecution.setRowFactory(tableView -> {
            TableRow<clientComponents.screenThree.topScreen.ExecutionData> row = new TableRow<>();
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), row);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), row);
            translateTransition.setFromY(100);
            translateTransition.setToY(0);
            row.setOnMouseClicked(event -> {
                fadeTransition.play();
                translateTransition.play();
            });
            return row;
        });
    }

    private void initialComboBox() {
        resultComboBox.getItems().addAll("All", FlowExecutionResult.SUCCESS.toString()
                , FlowExecutionResult.WARNING.toString(), FlowExecutionResult.FAILURE.toString());
        resultComboBox.setValue("All"); // Set the default filter option
    }
    private <T> void setRightToLeftAlignment(TableColumn<clientComponents.screenThree.topScreen.ExecutionData, T> column) {
        column.setCellFactory(new Callback<TableColumn<clientComponents.screenThree.topScreen.ExecutionData, T>, TableCell<clientComponents.screenThree.topScreen.ExecutionData, T>>() {
            @Override
            public TableCell<clientComponents.screenThree.topScreen.ExecutionData, T> call(TableColumn<clientComponents.screenThree.topScreen.ExecutionData, T> param) {
                TableCell<clientComponents.screenThree.topScreen.ExecutionData, T> cell = new TableCell<clientComponents.screenThree.topScreen.ExecutionData, T>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setAlignment(Pos.CENTER);
                        } else {
                            setText(item.toString());
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
                return cell;
            }
        });
    }

    public void setMainController(clientComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController mainFlowExecutionHistoryController) {
        this.mainFlowExecutionHistoryController = mainFlowExecutionHistoryController;
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        //this.systemEngine = systemEngine;
        //continuationComponentController.setSystemEngine(systemEngine);
    }


    public SimpleStringProperty getChosenFlowIdProperty() {
        return chosenFlowIdProperty;
    }

    public SimpleStringProperty getChosenFlowNameProperty() {
        return chosenFlowNameProperty;
    }

    public void setTableView(List<DTOFullDetailsPastRunWeb> flowsExecutedList) {
        chosenFlowIdProperty.set("");
        setDetailsFlowExecutionHistory(flowsExecutedList);
    }

    public void setDetailsFlowExecutionHistory(List<DTOFullDetailsPastRunWeb> flowsExecutedList) {
        ObservableList<clientComponents.screenThree.topScreen.ExecutionData> data = tableFlowExecution.getItems();
        data.clear();

        for (DTOFullDetailsPastRunWeb flowDetails : flowsExecutedList) {
            String flowResult;
            String activationDate = flowDetails.getActivationDate();
            if (flowDetails.getFinalResult() != null)
                 flowResult = flowDetails.getFinalResult().getDescription();
            else
                flowResult = "The flow is still in progress";

            UUID flowId = flowDetails.getUniqueId();
            clientComponents.screenThree.topScreen.ExecutionData executionData = new clientComponents.screenThree.topScreen.ExecutionData(flowDetails.getFlowName(), flowResult, activationDate, flowId);
            tableFlowExecution.getItems().add(executionData);
        }
        this.flowsExecutedList = flowsExecutedList;
    }

    public void filterTableDataByResult(String selectedFilter) {
        setDetailsFlowExecutionHistory(flowsExecutedList);
        if (!selectedFilter.equals("All")) {
            ObservableList<clientComponents.screenThree.topScreen.ExecutionData> filteredList = FXCollections.observableArrayList();
            for (clientComponents.screenThree.topScreen.ExecutionData flow : tableFlowExecution.getItems()) {
                if (flow.getResultExecutions().equals(selectedFilter)) {
                    filteredList.add(flow);
                }
            }
            tableFlowExecution.setItems(filteredList);
        }
    }

    public void updateContinuationDetails(String flowName) {

        String finalUrl = HttpUrl
                .parse(Constants.LIST_CONTINUATION_FLOW_NAME)
                .newBuilder()
                .addQueryParameter("flowName", flowName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    DTOListContinuationFlowName listContinuationFlowName = new Gson().fromJson(response.body().string(), DTOListContinuationFlowName.class);
                    Platform.runLater(() -> {
                        if(!listContinuationFlowName.getListContinuationFlowName().isEmpty()) {
                            continuationComponent.setVisible(true);
                            continuationComponentController.getFlowNameContinuationListView().getItems().clear();
                            continuationComponentController.updateContinuationDetails(listContinuationFlowName);
                        }
                    });
                }
            }
        });

        /*DTOListContinuationFlowName listContinuationFlowName = systemEngine.setListContinuationFlowName(flowName);
        if(!listContinuationFlowName.getListContinuationFlowName().isEmpty()) {
            continuationComponent.setVisible(true);
            continuationComponentController.getFlowNameContinuationListView().getItems().clear();
            continuationComponentController.updateContinuationDetails(listContinuationFlowName);
        }*/
    }

    public FlowExecutionHistoryController getMainFlowExecutionHistoryController() {
        return mainFlowExecutionHistoryController;
    }

    public ContinuationController getContinuationComponentController() {
        return continuationComponentController;
    }

    public void clearDetails() {
        ObservableList<ExecutionData> data = tableFlowExecution.getItems();
        data.clear(); // Clear the items in the tableFlowExecution
        continuationComponentController.getFlowNameContinuationListView().getItems().clear(); // Clear the items in the flowNameContinuationListView
    }

    public SimpleBooleanProperty getRerunFlowButtonProperty()
    {
        return rerunFlowButtonProperty;
    }

    public void initListener() {

        mainFlowExecutionHistoryController.getMainBodyController().isExecutionsHistoryButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        rerunFlowButton.setDisable(true);
                    }
                });
        continuationComponentController.initListener();
    }

    public Button getRerunFlowButton() {
        return rerunFlowButton;
    }
}