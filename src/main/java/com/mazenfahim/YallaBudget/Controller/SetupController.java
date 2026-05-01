package com.mazenfahim.YallaBudget.Controller;

import com.mazenfahim.YallaBudget.Service.BudgetService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Controller for the allowance initialization screen.
 */
public class SetupController {
    private final BudgetService budgetService = new BudgetService();

    @FXML
    private TextField allowanceInput;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setText("");
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusDays(30));
    }

    @FXML
    public void onCreateCycleClicked() {
        try {
            double allowance = Double.parseDouble(allowanceInput.getText().trim());
            LocalDate start = startDatePicker.getValue();
            LocalDate end = endDatePicker.getValue();

            if (!validateInput(allowance, start, end)) {
                return;
            }

            budgetService.createCycle(allowance, start, end);
            showDashboard();
        } catch (NumberFormatException e) {
            showValidationError("Allowance must be a valid positive number.");
        } catch (IllegalArgumentException e) {
            showValidationError(e.getMessage());
        } catch (IOException e) {
            showValidationError("Could not open the dashboard.");
        }
    }

    /**
     * Alias kept for compatibility with older FXML or diagrams.
     */
    @FXML
    public void onStartCycleClicked() {
        onCreateCycleClicked();
    }

    public boolean validateInput(double allowance, LocalDate start, LocalDate end) {
        if (!budgetService.validateAmount(allowance)) {
            showValidationError("Allowance must be a positive number.");
            return false;
        }
        if (!budgetService.validateDateRange(start, end)) {
            showValidationError("End date must be after start date.");
            return false;
        }
        return true;
    }

    public void showDashboard() throws IOException {
        navigateTo("DashboardView.fxml");
    }

    public void showValidationError(String message) {
        errorLabel.setText(message);
    }

    private void navigateTo(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mazenfahim/YallaBudget/" + fxmlFile));
        Scene scene = new Scene(loader.load(), 900, 680);
        Stage stage = (Stage) allowanceInput.getScene().getWindow();
        stage.setScene(scene);
    }
}
