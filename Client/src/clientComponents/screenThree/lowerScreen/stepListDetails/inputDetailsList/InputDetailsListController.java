package clientComponents.screenThree.lowerScreen.stepListDetails.inputDetailsList;


import clientComponents.screenThree.lowerScreen.stepListDetails.StepListDetailsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenThree.lowerScreen.stepListDetails.inputDetailsList.inputBody.InputBodyController;
import utilWebApp.DTOInputDetailsWeb;
import utilsDesktopApp.DTOInputDetailsJavaFX;

import java.io.IOException;
import java.util.List;

public class InputDetailsListController {

    private clientComponents.screenThree.lowerScreen.stepListDetails.StepListDetailsController maimStepListDetailsController;
    //private SystemEngineInterface systemEngine;
    @FXML
    private HBox inputDetailsComponent;
    @FXML
    private InputBodyController inputDetailsComponentController;
    @FXML
    private Accordion inputListAccordion;

    @FXML
    public void initialize() {
        if (inputDetailsComponentController != null) {
            inputDetailsComponentController.setMainController(this);
        }
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        //this.systemEngine = systemEngine;
        //inputDetailsComponentController.setSystemEngine(systemEngine);
    }

    public void setMainController(StepListDetailsController maimStepListDetailsController) {
        this.maimStepListDetailsController = maimStepListDetailsController;
    }


    public void updateInputsStepDetails(List<DTOInputDetailsWeb> dtoInputsDetails) {
        inputListAccordion.getPanes().clear();

        for (DTOInputDetailsWeb input : dtoInputsDetails) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("inputBody/inputBody.fxml"));
                HBox contentPane = fxmlLoader.load();

                InputBodyController controller = fxmlLoader.getController();
                controller.setMainController(this);
                //controller.setSystemEngine(systemEngine);

                TitledPane titledPane = new TitledPane(input.getFinalName(), contentPane);

                // Store the controller in the TitledPane's properties
                controller.setInputData(input);

                inputListAccordion.getPanes().add(titledPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}