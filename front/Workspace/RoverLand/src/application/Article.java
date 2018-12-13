package application;

public class Article {
	private String articleImgFile;
	private String articleName;
	private String articleDescription;
	private String articlePrice;
	
	public Article(String articleImgFile, String articleName, String articleDescription, String articlePrice) {
		this.articleImgFile = articleImgFile;
		this.articleName = articleName;
		this.articleDescription = articleDescription;
		this.articlePrice = articlePrice;
	}

	public String getArticleImgFile() {
		return articleImgFile;
	}

	public void setArticleImgFile(String articleImgFile) {
		this.articleImgFile = articleImgFile;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public String getArticleDescription() {
		return articleDescription;
	}

	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}

	public String getArticlePrice() {
		return articlePrice;
	}

	public void setArticlePrice(String articlePrice) {
		this.articlePrice = articlePrice;
	}
}
