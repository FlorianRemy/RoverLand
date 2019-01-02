import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestReporter;

import application.Article;
import application.Site;
import application.User;
import javafx.collections.ObservableList;

class TestsSoft {
	Site RoverLand;
	ObservableList<Article> testArticleList;
	
	Article article1;
	Article article2;
	Article article3;
	
	User buyer;
	
	@BeforeAll
	public void initAll() {
		article1 = new Article("https://classicsworld.co.uk/wp-content/uploads/2017/09/Rover-75-2.0-CDT-CLUB-01-820x547.jpg","La belle Rover 75 n°1", "Belle auto, gros moteur", "3420");
		article2 = new Article("https://www.autoscout24.fr/assets/auto/images/model/rover/rover-75/rover-75-l-01.jpg", "La belle Rover 75 n°2 " ,"Très puissante", "100000");
		article3 = new Article("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSY0Rf6DOhVqOgSCic9aaG8qplUcwLeqnpq4MsRetUS8CVus_FVIg","SuperClio", "C'est une clio, on sait pas ce qu'elle fout là", "5€");
		
		testArticleList.add(article1);
		testArticleList.add(article2);
		testArticleList.add(article3);
		
		buyer = new User(1234);
		
		RoverLand = new Site(testArticleList);
	}
	
	@RepeatedTest(10)
	@DisplayName("Add a new cart to the hashmap of the app")
	public void testAddNewCart() {
		int targetNumberOfCartsAfter = RoverLand.getHmCart().size() + 1;
		RoverLand.addNewCart(RoverLand.getUser().getId());
		assertEquals(targetNumberOfCartsAfter,RoverLand.getHmCart().size());
	}
	
	@Test
	@DisplayName("Add a new article to the cart of the current user")
	public void testAddToCart() {
		int targetNumberOfArticlesInCart = RoverLand.getHmCart().get(RoverLand.getUser().getId()).getArticleList().size() + 1;
		RoverLand.addArticle(new Article("linkTest","titleTest", "descTest", "priceTest"));
		assertEquals(targetNumberOfArticlesInCart,RoverLand.getHmCart().get(RoverLand.getUser().getId()).getArticleList().size());
	}
	
	@Test
	@DisplayName("Delete an article from the cart of the current user")
	public void testDelFromCart() {
		int targetNumberOfArticlesInCart = RoverLand.getHmCart().get(RoverLand.getUser().getId()).getArticleList().size() + 1;
		RoverLand.getHmCart().get(RoverLand.getUser().getId()).delArticle(article1);
		assertEquals(targetNumberOfArticlesInCart,RoverLand.getHmCart().get(RoverLand.getUser().getId()).getArticleList().size());
		assertTrue(!RoverLand.getHmCart().get(RoverLand.getUser().getId()).getArticleList().contains(article1));
	}

}
