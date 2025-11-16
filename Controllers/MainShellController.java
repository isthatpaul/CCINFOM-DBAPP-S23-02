package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import session.UserSession;

public class MainShellController {
    @FXML private Label staffNameLabel;
    @FXML private Label roleLabel;
    @FXML private StackPane contentArea;
    @FXML private Button adminBtn;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        if (UserSession.isLoggedIn()) {
            staffNameLabel.setText(UserSession.get().username());
            roleLabel.setText("(" + UserSession.get().role() + ")");
            adminBtn.setVisible("ADMIN".equalsIgnoreCase(UserSession.get().role()));
        }
        loadDashboard();
    }

    private void setContent(String fxmlName) {
        try {
            Parent node = FXMLLoader.load(getClass().getResource("/Views/" + fxmlName));
            contentArea.getChildren().setAll(node); // setAll(Node...) OK
            if (statusLabel != null) statusLabel.setText("Loaded: " + fxmlName);
        } catch (Exception e) {
            if (statusLabel != null) statusLabel.setText("Error loading " + fxmlName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadDashboard() { setContent("DashboardView.fxml"); }
    public void loadCustomers() { setContent("CustomersView.fxml"); }
    public void loadMeters() { setContent("MetersView.fxml"); }
    public void loadAssignments() { setContent("AssignmentsView.fxml"); }
    public void loadConsumption() { setContent("ConsumptionView.fxml"); }
    public void loadBills() { setContent("BillsView.fxml"); }
    public void loadPayments() { setContent("PaymentsView.fxml"); }
    public void loadReports() { setContent("ReportsView.fxml"); }
    public void loadAdmin() { setContent("AdminView.fxml"); }

    public void logout() {
        try {
            var loader = new FXMLLoader(getClass().getResource("/Views/LoginView.fxml"));
            var root = loader.load();
            var scene = staffNameLabel.getScene();
            scene.setRoot((Parent) root);
        } catch (Exception e) {
            if (statusLabel != null) statusLabel.setText("Error logging out: " + e.getMessage());
            e.printStackTrace();
        }
        UserSession.clear();
    }
}