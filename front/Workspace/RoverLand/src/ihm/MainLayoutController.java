package ihm;

import application.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class MainLayoutController {
	@FXML
	private TableView<Article> articleList;
	@FXML
	private TableColumn<Article, String> articleTitle;
	@FXML
	private TableColumn<Article, String> articleDescription;
	@FXML
	private TableColumn<Article, String> articlePrice;
	@FXML
	private ImageView addToCartButton;
	@FXML
	private ImageView cartButton;
	
	private Main mainApp;
	
	private ClientApp clientApp;
	
	private Site site;
	
	@FXML
    private void initialize() {
		articleTitle.setCellValueFactory(cellData -> cellData.getValue().getArticleName());
		articleDescription.setCellValueFactory(cellData -> cellData.getValue().getArticleDescription());
		articlePrice.setCellValueFactory(cellData -> cellData.getValue().getArticlePrice());
		
		addToCartButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Article selectedArticle = articleList.getSelectionModel().getSelectedItem();
				if(selectedArticle != null) {
					clientApp.addToCart(site.getUser().getId(), selectedArticle.getArticleID());
					//site.getHmCart().get(site.getUser().getId()).getArticleList().add(selectedArticle);
				} 
			}
		});
		
		cartButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				mainApp.showCartLayout();
			}
		});
	}
	
	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        articleList.setItems(site.getArticleList());
    }
	
	public void setClientApp(ClientApp clientApp) {
		this.clientApp = clientApp;
	}
	
	public void setSite(Site site) {
		this.site = site;
	}
}
