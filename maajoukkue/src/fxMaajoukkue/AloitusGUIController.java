package fxMaajoukkue;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author weeik
 * @version 13.2.2024
 *
 */
public class AloitusGUIController implements ModalControllerInterface<String> {

    @FXML private TextField textVastaus;
    private String vastaus = null;
    
    
    @FXML private void handleOK() {
        vastaus = textVastaus.getText();
        ModalController.closeStage(textVastaus);
    }
    
    
    @FXML private void handleCancel() {
        ModalController.closeStage(textVastaus);
    }

    
    @Override
    public String getResult() {
        return vastaus;
    }
    
    
    @Override
    public void setDefault(String oletus) {
        textVastaus.setText(oletus);
    }

    
    @Override
    public void handleShown() {
        textVastaus.requestFocus();
    }


    
    /**
     * @param modalityStage mille modaalisia
     * @param oletus mitä nimeä käytetään oletuksena
     * @return null jos cancel, muuuten kirjoitettu nimi
     */
    public static String kysyNimi(Stage modalityStage, String oletus) {
        return ModalController.showModal(
                AloitusGUIController.class.getResource("AloitusGUIView.fxml"),
                "Suomi",
                modalityStage, oletus);
    }

    
}
