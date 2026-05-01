package com.mazenfahim.YallaBudget.Controller;

import com.mazenfahim.YallaBudget.Service.BudgetService;
import com.mazenfahim.YallaBudget.Service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for creating the first local user PIN.
 */
public class PinSetupController {
    private final UserService userService = new UserService();
    private final BudgetService budgetService = new BudgetService();

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField pinInput;

    @FXML
    private PasswordField confirmPinInput;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    @FXML
    public void onCreatePinClicked() {
        String username = usernameInput.getText();
        String pin = pinInput.getText();
        String confirmPin = confirmPinInput.getText();

        if (pin == null || pin.isBlank() || confirmPin == null || confirmPin.isBlank()) {
            showValidationError("Please enter and confirm your 4-digit PIN.");
            return;
        }

        if (!pin.equals(confirmPin)) {
            showValidationError("PIN confirmation does not match.");
            return;
        }

        try {
            userService.createUser(username, pin);
            navigateTo(budgetService.cycleExists() ? "DashboardView.fxml" : "SetupView.fxml");
        } catch (IllegalArgumentException e) {
            showValidationError(e.getMessage());
        } catch (IOException e) {
            showValidationError("Could not open the next screen.");
        }
    }

    public void showValidationError(String message) {
        errorLabel.setText(message);
    }

    private void navigateTo(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mazenfahim/YallaBudget/" + fxmlFile));
        Scene scene = new Scene(loader.load(), 900, 680);
        Stage stage = (Stage) pinInput.getScene().getWindow();
        stage.setScene(scene);
    }
}
