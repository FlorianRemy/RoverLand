package ihm;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

/**
 * Classe RootLayoutController : Controller du layout Menu
 * @author karim
 *
 */
public class RootLayoutController {
	@FXML
	private MenuItem menuItemMainPage;
	@FXML
	private MenuItem menuItemPreviousPage;
	
	private Main mainApp;
	
	@FXML
    private void initialize() {
		// Do nothing
	}
	
	@FXML
	private void handleIDLayout() {
		mainApp.showIdLayout();
    }
	
	@FXML
    private void handleMainLayout() {
		mainApp.showMainLayout();
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
