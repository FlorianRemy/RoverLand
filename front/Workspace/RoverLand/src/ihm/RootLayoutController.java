package ihm;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class RootLayoutController {
	@FXML
	private MenuItem menuItemMainPage;
	@FXML
	private MenuItem menuItemPreviousPage;
	
	private Main mainApp;
	
	@FXML
    private void initialize() {
		//mainApp.showCartLayout();
	}
	
	@FXML
	private void handleInit() {
		//mainApp.showCartLayout();
    }
	
	@FXML
    private void handlePreviousPage() {
		
    }
	
	@FXML
    private void handleExit() {
		Platform.exit();
		System.exit(0);
	}
	
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}
}
