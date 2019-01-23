package ihm;
	
import java.io.IOException;

import application.Site;
import application.App;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	/**primaryStage : conteneur principal*/
	private Stage primaryStage;
	/**rootLayout : layout principal contenant les onglets principaux*/
	private BorderPane rootLayout;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("RoverLand");
			initRootLayout();
			showIdLayout();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showIdLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("IDLayout.fxml"));
			AnchorPane Overview = (AnchorPane) loader.load();
			rootLayout.setCenter(Overview);
			IDLayoutController controller = loader.getController();
			controller.setSite(App.getSite());
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showMainLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("MainLayout.fxml"));
			AnchorPane Overview = (AnchorPane) loader.load();
			rootLayout.setCenter(Overview);
			MainLayoutController controller = loader.getController();
			controller.setSite(App.getSite());
			controller.setClientApp(App.getClientApp());
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showCartLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("CartLayout.fxml"));
			AnchorPane Overview = (AnchorPane) loader.load();
			rootLayout.setCenter(Overview);
			CartLayoutController controller = loader.getController();
			controller.setSite(App.getSite());
			controller.setClientApp(App.getClientApp());
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		App testApp = new App();
		App.main(args);
		launch(args);
	}
}
