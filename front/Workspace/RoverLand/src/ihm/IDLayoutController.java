package ihm;

import application.Site;
import application.User;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;

public class IDLayoutController {
	@FXML
	private TextField userId;
	@FXML
	private PasswordField userPassword;
	@FXML
	private Button valid;
	
	private Main mainApp;
	
	private Site site;
	
	@FXML
    private void initialize() {

		valid.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				
				final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
				
				if(!(userId.getText().equals("") || userPassword.getText().equals(""))) {
					
					userId.pseudoClassStateChanged(errorClass, false);
					userPassword.pseudoClassStateChanged(errorClass, false);
					
					if(isNumeric(userId.getText())) {
						site.setUser(new User(Integer.valueOf(userId.getText())));
						site.addNewCart(Integer.valueOf(userId.getText()));
						mainApp.showMainLayout();
					}
				}
				else {
					if(userId.getText().trim().isEmpty()) {
						userId.pseudoClassStateChanged(errorClass, true);
					}
					else {
						userId.pseudoClassStateChanged(errorClass, false);
					}
					
					if(userPassword.getText().trim().isEmpty()) {
						userPassword.pseudoClassStateChanged(errorClass, true);
					}
					else {
						userPassword.pseudoClassStateChanged(errorClass, false);
					}
				}
				
			}
		});
	}
	
	public static boolean isNumeric(String strNum) {
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
	
	public void setSite(Site site) {
		this.site = site;
	}
}
