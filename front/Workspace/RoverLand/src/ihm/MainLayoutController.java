package ihm;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class MainLayoutController {
	@FXML
	private TableView articleList;
	@FXML
	private TableColumn articleTitle;
	@FXML
	private TableColumn articleDescription;
	@FXML
	private TableColumn articlePrice;
	@FXML
	private ImageView addToCartButton;
	@FXML
	private ImageView cartButton;
	
	private Main mainApp;
	
	@FXML
    private void initialize() {
		cartButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				mainApp.showCartLayout();
			}
		});
	}
	
	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
