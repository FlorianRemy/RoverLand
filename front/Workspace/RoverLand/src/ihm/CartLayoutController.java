package ihm;

import application.Article;
import application.ClientApp;
import application.Site;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CartLayoutController {
	@FXML
	private TableView<Article>  cartContent;
	
	@FXML
	private TableColumn<Article, String> titleArticleInCart;
	@FXML
	private TableColumn<Article, String> descriptionArticleInCart;
	@FXML
	private TableColumn<Article, String> quantityOfArticleInCart;
	@FXML
	private TableColumn<Article, String> priceOfArticleInCart;
	
	@FXML
	private ImageView delArticleButton;
	@FXML
	private TextField totalPrice;
	
	private Main mainApp;
	
	private ClientApp clientApp;
	
	private Site site;
	
	@FXML
    private void initialize() {
		titleArticleInCart.setCellValueFactory(cellData -> cellData.getValue().getArticleName());
		descriptionArticleInCart.setCellValueFactory(cellData -> cellData.getValue().getArticleDescription());
		priceOfArticleInCart.setCellValueFactory(cellData -> cellData.getValue().getArticlePrice());
		
		delArticleButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Article selectedArticle = cartContent.getSelectionModel().getSelectedItem();
				if(selectedArticle != null) {
					clientApp.deleteAnnouncement(site.getUser().getId(), selectedArticle.getArticleID());
					site.getHmCart().get(site.getUser().getId()).setArticleList(clientApp.getCart(site.getUser().getId())); 
			        cartContent.setItems(site.getHmCart().get(site.getUser().getId()).getArticleList());
					totalPrice.setText(clientApp.getCartAmount(site.getUser().getId()));
					//site.getHmCart().get(site.getUser().getId()).getArticleList().remove(selectedArticle);
				} 
			}
		});
	}
	
	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        site.getHmCart().get(site.getUser().getId()).setArticleList(clientApp.getCart(site.getUser().getId())); 
        cartContent.setItems(site.getHmCart().get(site.getUser().getId()).getArticleList());
        totalPrice.setText(clientApp.getCartAmount(site.getUser().getId()));
    }
	
	public void setClientApp(ClientApp clientApp) {
		this.clientApp = clientApp;
	}
	
	public void setSite(Site site) {
		this.site = site;
	}
}
