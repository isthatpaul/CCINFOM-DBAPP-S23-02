package Controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import Model.Staff;
import Model.StaffCRUD;
import session.UserSession;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final StaffCRUD staffCRUD = new StaffCRUD();

    @FXML
    public void onLogin() {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Enter credentials.");
            return;
        }

        Staff staff = staffCRUD.getRecordByUsername(user);
        // TODO: use password hashing instead of plain comparison
        if (staff == null || !pass.equals(staff.passwordHash())) {
            errorLabel.setText("Invalid username or password.");
            return;
        }

        UserSession.set(staff);
        openMainShell();
    }

    private void openMainShell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MainShell.fxml"));
            Scene scene = new Scene(loader.load());
            if (getClass().getResource("/Styles/styles.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/Styles/styles.css").toExternalForm());
            }
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dashboard");
        } catch (Exception e) {
            errorLabel.setText("Failed to load main shell.");
            e.printStackTrace();
        }
    }
}