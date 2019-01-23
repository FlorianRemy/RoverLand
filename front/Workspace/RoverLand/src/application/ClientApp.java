package application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientApp {
	
	private static final String MSGEXCEPTIONBADREQUEST = "Bad Request (NOK)";
	private static final String MSGEXCEPTIONNOTFOUND = "Not Found (NOK)";
	private static final String MSGEXCEPTIONUNPROCESSABLE = "Unprocessable entity (NOK)";
	
	public ObservableList<Article> getList() {
		ObservableList<Article> articleList;
		articleList = FXCollections.observableArrayList();
		String articleListRd = "";
		
		String url = "http://[::1]:8000/getList";
		
		try {
			articleListRd=sendGet(url);
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
	
	public void addToCart(int idUser, int idArticle){
		String url = "http://[::1]:8000/addToCart";
		String urlParameters = "{"+"\"id_article\":"+idArticle+","+"\"id_user\":"+idUser+"}";
	
		try {
			sendPost(url, urlParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ObservableList<Article> getCart(int userID) {
		String url = "http://[::1]:8000/getCart";
		String userIDParameter = String.valueOf(userID);
		String articleListFromCartRd = "";
		ObservableList<Article> articleList;
		articleList = FXCollections.observableArrayList();
		
		url = url + "/" + userIDParameter;
		
		try {
			articleListFromCartRd=sendGet(url);
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
	
	public void deleteAnnouncement(int idUser, int idArticle) {
		String url = "http://[::1]:8000/deleteAnnouncement";
		String urlParameters = "{"+"\"id_article\":"+idArticle+","+"\"id_user\":"+idUser+"}";
		
		try {
			sendDelete(url, urlParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getCartAmount(int userID) {
		String cartAmount = "";
		String url = "http://[::1]:8000/getCartAmount";
		String userIDParameter = String.valueOf(userID);
	
		url = url + "/" + userIDParameter;
		
		try {
			cartAmount=sendGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("cartAmount : "+cartAmount);
		cartAmount = cartAmount.substring(cartAmount.indexOf('{') + 1, cartAmount.indexOf('}'));
		String[] cartAmountField = cartAmount.split(":");
		cartAmount = cartAmountField[1] + " €";
		System.out.println("cartAmount : "+cartAmount);
		return cartAmount;
	}
	
	
	// HTTP GET request
	public String sendGet(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
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
	
	// HTTP POST request
	public String sendPost(String url, String urlParameters) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
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
	
	// HTTP DELETE request
	public String sendDelete(String url, String urlParameters) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add request header
		con.setRequestMethod("DELETE");
		con.setRequestProperty("Content-Type", "application/json");
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'DELETE' request to URL : " + url);
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
