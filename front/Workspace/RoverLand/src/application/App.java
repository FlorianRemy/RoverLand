package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class App {
	private static Site site;
	//private static ObservableList<Article> testArticleList;
	private static ClientApp clientApp; 
	
	public App(){
		//testArticleList = FXCollections.observableArrayList();
		
		/* Ajout de données de test */
		/*testArticleList.add(new Article("https://classicsworld.co.uk/wp-content/uploads/2017/09/Rover-75-2.0-CDT-CLUB-01-820x547.jpg","La belle Rover 75 n°1", "Belle auto, gros moteur", "3420"));
		testArticleList.add(new Article("https://www.autoscout24.fr/assets/auto/images/model/rover/rover-75/rover-75-l-01.jpg", "La belle Rover 75 n°2 " ,"Très puissante", "100000"));
		testArticleList.add(new Article("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSY0Rf6DOhVqOgSCic9aaG8qplUcwLeqnpq4MsRetUS8CVus_FVIg","SuperClio", "C'est une clio, on sait pas ce qu'elle fout là", "5€"));
		testArticleList.add(new Article("http://lautoscope.fr/wp-content/uploads/2016/02/Rover75_malle.jpg","La belle Rover 75 n°3", "Importée du bled", "600"));
		testArticleList.add(new Article("http://boitierrouge.com/wp-content/uploads/2016/03/75-02-1024x632.jpeg","La belle Rover 75 n°4", "Très rare", "2500"));*/
		
		clientApp = new ClientApp();
	}
	
	public static void main (String[] args){
		site = new Site(clientApp.getList());
	}
	
	public static Site getSite() {
		return site;
	}

	public static void setSite(Site site) {
		App.site = site;
	}

	public static ClientApp getClientApp() {
		return clientApp;
	}

	public static void setClientApp(ClientApp clientApp) {
		App.clientApp = clientApp;
	}
	
	
}
