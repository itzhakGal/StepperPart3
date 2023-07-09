package adminComponents.screenTwo.lowerScreen;

import adminComponents.screenTwo.RolesManagementController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import stepper.systemEngine.SystemEngineInterface;

public class LowerManagementController {

    private RolesManagementController mainRolesManagementController;
    //private SystemEngineInterface systemEngine;
    @FXML
    private TextField roleName;

    @FXML
    private TextField roleDescription;

    @FXML
    private ListView<String> selectFlowAssignedList;
    @FXML
    private Button saveRole;

    @FXML
    void saveRoleAction(ActionEvent event) {

    }

    @FXML
    void selectFlowAssignedListActivate(ActionEvent event) {

    }

    public void setMainController(RolesManagementController rolesManagementController) {
        this.mainRolesManagementController = rolesManagementController;

    }
    public void setSystemEngine(SystemEngineInterface systemEngine) {
        //this.systemEngine = systemEngine;
    }

}