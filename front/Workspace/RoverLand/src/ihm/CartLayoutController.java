package ihm;

import java.awt.TextField;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

public class CartLayoutController {
	@FXML
	private TableView cartContent;
	
	@FXML
	private TableColumn titleArticleInCart;
	@FXML
	private TableColumn descriptionArticleInCart;
	@FXML
	private TableColumn quantityOfArticleInCart;
	@FXML
	private TableColumn priceOfArticleInCart;
	
	@FXML
	private ImageView delArticleButton;
	@FXML
	private TextField totalPrice;
	
	private Main mainApp;
	
	@FXML
    private void initialize() {
		
    	
	}
	
	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
