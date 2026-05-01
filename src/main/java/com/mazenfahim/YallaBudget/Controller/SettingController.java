package com.mazenfahim.YallaBudget.Controller;

import com.mazenfahim.YallaBudget.Service.BudgetService;
import com.mazenfahim.YallaBudget.Service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller for application settings such as changing PIN and resetting cycle data.
 */
public class SettingController {
    private final UserService userService = new UserService();
    private final BudgetService budgetService = new BudgetService();

    @FXML
    private PasswordField oldPinInput;

    @FXML
    private PasswordField newPinInput;

    @FXML
    private PasswordField confirmNewPinInput;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    @FXML
    public void onChangePinClicked() {
        String oldPin = oldPinInput.getText();
        String newPin = newPinInput.getText();
        String confirmPin = confirmNewPinInput.getText();

        if (newPin == null || !newPin.equals(confirmPin)) {
            showValidationError("New PIN confirmation does not match.");
            return;
        }

        try {
            userService.changePin(oldPin, newPin);
            oldPinInput.clear();
            newPinInput.clear();
            confirmNewPinInput.clear();
            showValidationError("PIN updated successfully.");
        } catch (IllegalArgumentException e) {
            showValidationError(e.getMessage());
        }
    }

    @FXML
    public void onResetCycleClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Current Cycle");
        alert.setHeaderText("This will permanently delete the current cycle and its transactions.");
        alert.setContentText("Do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                budgetService.resetCurrentCycle();
                navigateTo("SetupView.fxml");
            } catch (IOException e) {
                showValidationError("Cycle reset, but setup screen could not be opened.");
            }
        }
    }

    @FXML
    public void onBackClicked() {
        try {
            navigateTo("DashboardView.fxml");
        } catch (IOException e) {
            showValidationError("Could not return to dashboard.");
        }
    }

    public void showValidationError(String message) {
        errorLabel.setText(message);
    }

    private void navigateTo(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mazenfahim/YallaBudget/" + fxmlFile));
        Scene scene = new Scene(loader.load(), 900, 680);
        Stage stage = (Stage) oldPinInput.getScene().getWindow();
        stage.setScene(scene);
    }
}
