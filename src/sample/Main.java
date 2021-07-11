package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        //Loading launcher view
        final URL location = getClass().getResource("sample.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        final Parent root = fxmlLoader.load(location.openStream());
        //Creating scene
        Scene scene = new Scene(root);
        primaryStage.setTitle("Alroseb");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        //Showing application
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    public static void ch(String n){
        stage.setTitle(n+" | Alroseb");
    }
}
