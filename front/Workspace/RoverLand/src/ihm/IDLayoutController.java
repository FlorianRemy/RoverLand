package ihm;

import java.awt.TextField;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;

public class IDLayoutController {
	@FXML
	private TextField userId;
	@FXML
	private PasswordField userPassword;
	@FXML
	private Button valid;
	
	private Main mainApp;
	
	@FXML
    private void initialize() {
		valid.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				mainApp.showMainCartLayout();
			}
		});
	}
	
	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
