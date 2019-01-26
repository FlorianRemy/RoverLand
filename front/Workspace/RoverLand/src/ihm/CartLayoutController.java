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

/**
 * Classe CartLayoutController : Controller de la vue Panier
 * @author karim
 */
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
	
	private Site site;
	
	@FXML
    private void initialize() {
		titleArticleInCart.setCellValueFactory(cellData -> cellData.getValue().getArticleName());
		descriptionArticleInCart.setCellValueFactory(cellData -> cellData.getValue().getArticleDescription());
		priceOfArticleInCart.setCellValueFactory(cellData -> cellData.getValue().getArticlePrice());
		
		// Evenement "suppression d'un article"
		delArticleButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Article selectedArticle = cartContent.getSelectionModel().getSelectedItem();
				if(selectedArticle != null) {
					ClientApp.deleteAnnouncement(site.getUser().getId(), selectedArticle.getArticleID());
					site.getHmCart().get(site.getUser().getId()).setArticleList(ClientApp.getCart(site.getUser().getId())); 
			        cartContent.setItems(site.getHmCart().get(site.getUser().getId()).getArticleList());
					totalPrice.setText(ClientApp.getCartAmount(site.getUser().getId()));
					//site.getHmCart().get(site.getUser().getId()).getArticleList().remove(selectedArticle);
				} 
			}
		});
	}
	
	public void setItems() {
        site.getHmCart().get(site.getUser().getId()).setArticleList(ClientApp.getCart(site.getUser().getId())); 
        cartContent.setItems(site.getHmCart().get(site.getUser().getId()).getArticleList());
        totalPrice.setText(ClientApp.getCartAmount(site.getUser().getId()));
    }
	
	public void setSite(Site site) {
		this.site = site;
	}
}
