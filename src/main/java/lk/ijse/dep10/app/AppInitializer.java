package lk.ijse.dep10.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        AnchorPane root = new FXMLLoader(getClass().getResource("/view/MainForm.fxml")).load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Move-Copy-Delete");

    }
}
