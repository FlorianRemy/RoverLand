package application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Classe ClientApp : Classe permettant d'effectuer des requetes HTTP vers l'API
 * Gere l'interfacage avec l'API RUST
 * @author karim
 */
public class ClientApp {
	/** Messages associes aux codes retour propres a l'API */
	private static final String MSGEXCEPTIONBADREQUEST = "Bad Request (NOK)";
	private static final String MSGEXCEPTIONNOTFOUND = "Not Found (NOK)";
	private static final String MSGEXCEPTIONUNPROCESSABLE = "Unprocessable entity (NOK)";
	
	private ClientApp() {
		// Do nothing
	}
	
	/**
	 * getList : Methode permettant de recuperer la liste des annonces
	 */
	public static ObservableList<Article> getList() {
		ObservableList<Article> articleList;
		articleList = FXCollections.observableArrayList();
		String articleListRd = "";
		
		String url = "http://[::1]:8000/getList";
		
		try {
			articleListRd=sendRequest(url, "", "GET", false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String articlesString = articleListRd.substring(articleListRd.indexOf('[') + 1, articleListRd.indexOf(']'));
		articlesString = articlesString.replace("},","}//");
		articlesString = articlesString.replace("{","");
		articlesString = articlesString.replace("}","");
		String[] articlesArray = articlesString.split("//");
		
		for(String article:articlesArray) {
			String[] articleFields = article.split(",");
			Article newArticle = new Article();
			
			for(String articleField : articleFields) {
				String[] tempTab = articleField.split(":");
				
				if(tempTab[0].equals("\"id\"")) {
					newArticle.setArticleID(Integer.valueOf(tempTab[1]));
				}
				else if(tempTab[0].equals("\"nom\"")) {
					newArticle.setArticleName(tempTab[1].replace("\"", ""));
				}
				else if(tempTab[0].equals("\"description\"")) {
					newArticle.setArticleDescription(tempTab[1].replace("\"", ""));
				}
				else if(tempTab[0].equals("\"prix\"")) {
					newArticle.setArticlePrice(tempTab[1] + " €");
				}
			}
			articleList.add(newArticle);
		}
		return articleList;
	}
	
	/**
	 * addToCart : Methode permettant d'ajouter un element au panier
	 * @param idUser
	 * @param idArticle
	 */
	public static void addToCart(int idUser, int idArticle){
		String url = "http://[::1]:8000/addToCart";
		String urlParameters = "{"+"\"id_article\":"+idArticle+","+"\"id_user\":"+idUser+"}";
	
		try {
			sendRequest(url, urlParameters, "POST", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * getCart : Methode permettant de recuperer la liste des articles du panier
	 * @param userID
	 */
	public static ObservableList<Article> getCart(int userID) {
		String url = "http://[::1]:8000/getCart";
		String userIDParameter = String.valueOf(userID);
		String articleListFromCartRd = "";
		ObservableList<Article> articleList;
		articleList = FXCollections.observableArrayList();
		
		url = url + "/" + userIDParameter;
		
		try {
			articleListFromCartRd=sendRequest(url, "", "GET", false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String articlesString = articleListFromCartRd.substring(articleListFromCartRd.indexOf('[') + 1, articleListFromCartRd.indexOf(']'));
		articlesString = articlesString.replace("},","}//");
		articlesString = articlesString.replace("{","");
		articlesString = articlesString.replace("}","");
		String[] articlesArray = articlesString.split("//");
		
		for(String article:articlesArray) {
			String[] articleFields = article.split(",");
			Article newArticle = new Article();
			
			for(String articleField : articleFields) {
				String[] tempTab = articleField.split(":");
				
				if(tempTab[0].equals("\"id\"")) {
					newArticle.setArticleID(Integer.valueOf(tempTab[1]));
				}
				else if(tempTab[0].equals("\"nom\"")) {
					newArticle.setArticleName(tempTab[1].replace("\"", ""));
				}
				else if(tempTab[0].equals("\"description\"")) {
					newArticle.setArticleDescription(tempTab[1].replace("\"", ""));
				}
				else if(tempTab[0].equals("\"prix\"")) {
					newArticle.setArticlePrice(tempTab[1] + " €");
				}
			}
			articleList.add(newArticle);
		}
		return articleList;
	}
	
	/**
	 * deleteAnnouncement : Methode permettant de supprimer un article du panier
	 * @param idUser
	 * @param idArticle
	 */
	public static void deleteAnnouncement(int idUser, int idArticle) {
		String url = "http://[::1]:8000/deleteAnnouncement";
		String urlParameters = "{"+"\"id_article\":"+idArticle+","+"\"id_user\":"+idUser+"}";
		
		try {
			sendRequest(url, urlParameters, "DELETE", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * getCartAmount : Methode permettant de recuperer le montant du panier
	 * @param userID
	 * @return
	 */
	public static String getCartAmount(int userID) {
		String cartAmount = "";
		String url = "http://[::1]:8000/getCartAmount";
		String userIDParameter = String.valueOf(userID);
	
		url = url + "/" + userIDParameter;
		
		try {
			cartAmount=sendRequest(url, "", "GET", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		cartAmount = cartAmount.substring(cartAmount.indexOf('{') + 1, cartAmount.indexOf('}'));
		String[] cartAmountField = cartAmount.split(":");
		cartAmount = cartAmountField[1] + " €";
		return cartAmount;
	}
	
	/***
	 * Methode permettant d'envoyer des requettes HTTP
	 * @param url
	 * @param urlParameters
	 * @param requestType
	 * @param isParam
	 * @return
	 * @throws Exception
	 */
	public static String sendRequest(String url, String urlParameters, String requestType, Boolean isParam) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// Add request header
		con.setRequestMethod(requestType);

		if(isParam){
			con.setRequestProperty("Content-Type", "application/json");

			// Send request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
		}
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending "+requestType+" request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
		
		if(responseCode != 200) {
			if(responseCode == 400) {
				throw new Exception(MSGEXCEPTIONBADREQUEST);
			}
			else if(responseCode == 404) {
				throw new Exception(MSGEXCEPTIONNOTFOUND);
			}
			else if(responseCode == 422) {
				throw new Exception(MSGEXCEPTIONUNPROCESSABLE);
			}
		}

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}
}
