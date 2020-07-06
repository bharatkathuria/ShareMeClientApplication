package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {



    @Override
    public void start(Stage stage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginGUI.fxml"));
        Parent root = loader.load();

        stage.setResizable(false);
        stage.setScene(new Scene(root,420,400));
        stage.setTitle("ShareMeClient -LogIn");

        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
