package TU;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.runners.MethodSorters;

import application.Article;
import application.Site;
import application.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@FixMethodOrder(MethodSorters.DEFAULT)
class TUSoft {
	static Site RoverLand;
	static ObservableList<Article> testArticleList;
	
	static Article article1;
	static Article article2;
	static Article article3;
	
	static User buyer;
	
	@BeforeAll
	@DisplayName("Initialization of tests variables")
	static void setUpBeforeClass() throws Exception {
		
		testArticleList = FXCollections.observableArrayList();
		
		article1 = new Article("La belle Rover 75 n°1", "Belle auto, gros moteur", "3420");
		article2 = new Article("La belle Rover 75 n°2 " ,"Très puissante", "100000");
		article3 = new Article("SuperClio", "C'est une clio, on sait pas ce qu'elle fout là", "5€");
		
		testArticleList.add(article1);
		testArticleList.add(article2);
		testArticleList.add(article3);
		
		buyer = new User(1234);
		
		RoverLand = new Site(testArticleList);
		RoverLand.setUser(buyer);
	}
	
	@org.junit.jupiter.api.Test
	@DisplayName("Add a new cart to the hashmap of the app")
	public void test1() {
		int targetNumberOfCartsAfter = RoverLand.getHmCart().size() + 1;
		RoverLand.addNewCart(RoverLand.getUser().getId());
		assertEquals(targetNumberOfCartsAfter,RoverLand.getHmCart().size());
	}

	@org.junit.jupiter.api.RepeatedTest(10)
	@DisplayName("Add a new article to the cart of the current user")
	public void test2() {
		int targetNumberOfArticlesInCart = RoverLand.getHmCart().get(RoverLand.getUser().getId()).getArticleList().size() + 1;
		RoverLand.getHmCart().get(RoverLand.getUser().getId()).getArticleList().add(new Article("titleTest", "descTest", "priceTest"));
		assertEquals(targetNumberOfArticlesInCart,RoverLand.getHmCart().get(RoverLand.getUser().getId()).getArticleList().size());
	}
	
	@org.junit.jupiter.api.Test
	@DisplayName("Delete an article from the cart of the current user")
	public void test3() {
		RoverLand.getHmCart().get(RoverLand.getUser().getId()).getArticleList().add(article1);
		int targetNumberOfArticlesInCart = RoverLand.getHmCart().get(RoverLand.getUser().getId()).getArticleList().size() - 1;
		System.out.println("List : "+RoverLand.getHmCart().get(RoverLand.getUser().getId()).getArticleList());
		RoverLand.getHmCart().get(RoverLand.getUser().getId()).delArticle(article1);
		assertEquals(targetNumberOfArticlesInCart,RoverLand.getHmCart().get(RoverLand.getUser().getId()).getArticleList().size());
		assertTrue(!RoverLand.getHmCart().get(RoverLand.getUser().getId()).getArticleList().contains(article1));
	}
}

