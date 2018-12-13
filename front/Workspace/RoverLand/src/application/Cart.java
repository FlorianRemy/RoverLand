package application;

import java.util.ArrayList;

public class Cart {
	private ArrayList<Article> articleList; 
	private double totalPrice;
	
	public Cart() {
		this.articleList = new ArrayList<Article>();
	}
	
	public void addArticle(Article articleToAdd) {
		articleList.add(articleToAdd);
	}
	
	public void delArticle(Article articleToDel) {
		articleList.remove(articleToDel);
	}

	public ArrayList<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(ArrayList<Article> articleList) {
		this.articleList = articleList;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
