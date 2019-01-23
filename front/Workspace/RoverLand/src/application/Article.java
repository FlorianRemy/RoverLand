package application;

import javafx.beans.property.*;

public class Article {
	private int articleID;
	private StringProperty articleName;
	private StringProperty articleDescription;
	private StringProperty articlePrice;
	
	public Article() {
		this.articleName = new SimpleStringProperty("");
		this.articleDescription = new SimpleStringProperty("");
		this.articlePrice = new SimpleStringProperty("");
	}
	
	public Article(String articleName, String articleDescription, String articlePrice) {
		this.articleName = new SimpleStringProperty(articleName);
		this.articleDescription = new SimpleStringProperty(articleDescription);
		this.articlePrice = new SimpleStringProperty(articlePrice);
	}
	
	public String toString() {
		String temp = this.articleName + ":" + this.articleDescription + ":" + this.articlePrice;
		return temp;
	}
	
	public int getArticleID() {
		return articleID;
	}

	public void setArticleID(int articleID) {
		this.articleID = articleID;
	}

	public StringProperty getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName.set(articleName);
	}

	public StringProperty getArticleDescription() {
		return articleDescription;
	}

	public void setArticleDescription(String articleDescription) {
		this.articleDescription.set(articleDescription);
	}

	public StringProperty getArticlePrice() {
		return articlePrice;
	}

	public void setArticlePrice(String articlePrice) {
		this.articlePrice.set(articlePrice);
	}
}
