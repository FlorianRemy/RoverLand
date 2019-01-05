package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cart {
	private ObservableList<Article> articleList; 
	private double totalPrice;
	
	public Cart() {
		this.articleList = FXCollections.observableArrayList();
	}
	
	public void delArticle(Article articleToDel) {
		articleList.remove(articleToDel);
	}

	public ObservableList<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(ObservableList<Article> articleList) {
		this.articleList = articleList;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
