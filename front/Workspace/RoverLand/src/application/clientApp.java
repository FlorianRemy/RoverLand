package application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientApp {
	
	public ClientApp() {}
	
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

		String articlesString = articleListRd.substring(articleListRd.indexOf("[") + 1, articleListRd.indexOf("]"));
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
					newArticle.setArticleName(tempTab[1]);
				}
				else if(tempTab[0].equals("\"description\"")) {
					newArticle.setArticleDescription(tempTab[1]);
				}
				else if(tempTab[0].equals("\"prix\"")) {
					newArticle.setArticlePrice(tempTab[1] + " €");
				}
			}
			articleList.add(newArticle);
		}
		
		System.out.println(articleList);
		return articleList;
	}
	
	public void addToCart(int id_user, int id_article){
		String url = "http://[::1]:8000/addToCart";
		String urlParameters = "{"+"\"id_article\":"+String.valueOf(id_article)+","+"\"id_user\":"+String.valueOf(id_user)+"}";
	
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

		String articlesString = articleListFromCartRd.substring(articleListFromCartRd.indexOf("[") + 1, articleListFromCartRd.indexOf("]"));
		articlesString = articlesString.replace("},","}//");
		articlesString = articlesString.replace("{","");
		articlesString = articlesString.replace("}","");
		String[] articlesArray = articlesString.split("//");
		
		System.out.println(Arrays.toString(articlesArray));
		
		for(String article:articlesArray) {
			String[] articleFields = article.split(",");
			Article newArticle = new Article();
			
			for(String articleField : articleFields) {
				String[] tempTab = articleField.split(":");
				
				if(tempTab[0].equals("\"id\"")) {
					newArticle.setArticleID(Integer.valueOf(tempTab[1]));
				}
				else if(tempTab[0].equals("\"nom\"")) {
					newArticle.setArticleName(tempTab[1]);
				}
				else if(tempTab[0].equals("\"description\"")) {
					newArticle.setArticleDescription(tempTab[1]);
				}
				else if(tempTab[0].equals("\"prix\"")) {
					newArticle.setArticlePrice(tempTab[1] + " €");
				}
			}
			articleList.add(newArticle);
		}
		
		return articleList;
	}
	
	public void deleteAnnouncement(int id_user, int id_article) {
		String url = "http://[::1]:8000/deleteAnnouncement";
		String urlParameters = "{"+"\"id_article\":"+String.valueOf(id_article)+","+"\"id_user\":"+String.valueOf(id_user)+"}";
		
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
		
		cartAmount = cartAmount.substring(cartAmount.indexOf("\"") + 1, cartAmount.indexOf("\"")) + " €";
		return cartAmount;
	}
	
	
	// HTTP GET request
	public String sendGet(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		//con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		//System.out.println(response.toString());
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

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		System.out.println(response.toString());
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

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		System.out.println(response.toString());
		return response.toString();
	}
		
}
