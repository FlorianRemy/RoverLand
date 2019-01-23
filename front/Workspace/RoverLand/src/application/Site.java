package application;

import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;

/**
 * Classe Site : Instance du site
 * @author karim
 */
public class Site {
	/** articleList : Liste des annonces */
	private ObservableList<Article> articleList;
	/** hmCart : Liste des paniers associes aux utilisateurs */
	private Map<Integer, Cart> hmCart;
	/* user : Reference vers l'utilisateur courant */
	private User user;
	
	public Site(ObservableList<Article> articleList) {
		this.articleList=articleList;
		hmCart = new HashMap<>();
	}
	
	public void addArticle(Article articleToAdd) {
		articleList.add(articleToAdd);
	}
	
	public void addNewCart(int idUser) {
		hmCart.put(idUser, new Cart());
	}

	public ObservableList<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(ObservableList<Article> articleList) {
		this.articleList = articleList;
	}

	public Map<Integer, Cart> getHmCart() {
		return hmCart;
	}

	public void setHmCart(Map<Integer, Cart> hmCart) {
		this.hmCart = hmCart;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
