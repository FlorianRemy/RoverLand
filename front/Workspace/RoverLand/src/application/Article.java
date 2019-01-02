package application;

import javafx.beans.property.*;

public class Article {
	private StringProperty articleImgFile;
	private StringProperty articleName;
	private StringProperty articleDescription;
	private StringProperty articlePrice;
	
	public Article(String articleImgFile, String articleName, String articleDescription, String articlePrice) {
		this.articleImgFile = new SimpleStringProperty(articleImgFile);
		this.articleName = new SimpleStringProperty(articleName);
		this.articleDescription = new SimpleStringProperty(articleDescription);
		this.articlePrice = new SimpleStringProperty(articlePrice);
	}
	
	public String toString() {
		String temp = this.articleName + ":" + this.articleDescription + ":" + this.articlePrice;
		return temp;
	}

	public StringProperty getArticleImgFile() {
		return articleImgFile;
	}

	public void setArticleImgFile(String articleImgFile) {
		this.articleImgFile.set(articleImgFile);
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
