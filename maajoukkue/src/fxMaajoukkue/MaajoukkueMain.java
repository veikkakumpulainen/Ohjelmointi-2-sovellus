package fxMaajoukkue;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import maajoukkue.Maajoukkue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author weeik
 * @version 15.1.2024
 *
 */
public class MaajoukkueMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("MaajoukkueGUIView.fxml"));
            final Pane root = ldr.load();
            final MaajoukkueGUIController maajoukkueCtrl = (MaajoukkueGUIController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("maajoukkue.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Maajoukkue");
            
            Maajoukkue maajoukkue = new Maajoukkue();
            maajoukkueCtrl.setMaajoukkue(maajoukkue);
            
            if (!maajoukkueCtrl.avaa() ) Platform.exit();
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei kaytossa
     */
    public static void main(String[] args) {
        launch(args);
    }
}