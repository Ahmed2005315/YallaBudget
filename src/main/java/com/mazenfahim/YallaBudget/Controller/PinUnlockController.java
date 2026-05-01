package com.mazenfahim.YallaBudget.Controller;

import com.mazenfahim.YallaBudget.Service.BudgetService;
import com.mazenfahim.YallaBudget.Service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for local PIN authentication.
 */
public class PinUnlockController {
    private final UserService userService = new UserService();
    private final BudgetService budgetService = new BudgetService();

    @FXML
    private PasswordField pinInput;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    @FXML
    public void onUnlockClicked() {
        if (userService.isLockedOut()) {
            showValidationError("Too many incorrect attempts. Try again in " + userService.getSecondsUntilUnlock() + " seconds.");
            return;
        }

        if (userService.authenticate(pinInput.getText())) {
            try {
                navigateTo(budgetService.cycleExists() ? "DashboardView.fxml" : "SetupView.fxml");
            } catch (IOException e) {
                showValidationError("Could not open the next screen.");
            }
            return;
        }

        int attemptsLeft = Math.max(0, 3 - userService.getFailedAttempts());
        showValidationError(attemptsLeft == 0
                ? "Too many incorrect attempts. Try again in " + userService.getSecondsUntilUnlock() + " seconds."
                : "Incorrect PIN. Attempts left: " + attemptsLeft);
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
