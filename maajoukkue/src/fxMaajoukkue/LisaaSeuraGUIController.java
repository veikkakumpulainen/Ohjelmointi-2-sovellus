package fxMaajoukkue;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import maajoukkue.Maajoukkue;
import maajoukkue.Seura;

/**
 * @author weeik
 * @version 26.4.2024
 *
 */
public class LisaaSeuraGUIController implements ModalControllerInterface<Seura>, Initializable {

    
    @FXML private TextField editLisaaSeura;
    @FXML private Label labelVirhe;
    private Seura seuraKohdalla;
    @FXML private ComboBoxChooser<Seura> seuraKentat;
    private Maajoukkue maajoukkue = new Maajoukkue();
    private boolean tallennetaan = false;
    
    
    /**
     * @param maaj maajoukkue
     */
    public void setMaajoukkue(Maajoukkue maaj) {
        maajoukkue = maaj;
    }
    
    
    @FXML private void handleValitse() {
        valitseSeura();
    }
    
    
    /**
     * Valitaan seura listasta
     */
    private void valitseSeura() {
        seuraKohdalla = seuraKentat.getSelectedObject();
    }
    
    
    /**
     * Vahvista-nappi
     */
    @FXML private void handleVahvista() {
        if (editLisaaSeura.getText().isEmpty()){
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        tallennetaan = true;
        ModalController.closeStage(editLisaaSeura);
    }
    
    
    /**
     * Peruuta-nappi
     */
    @FXML private void handlePeruuta() {
        seuraKohdalla = null;
        ModalController.closeStage(editLisaaSeura);
    }
    
    
    /**
     * Virhe
     * @param virhe jos virheellinen syöte
     */
    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    
    /**
     * @param modalityStage taso
     * @param oletus seura
     * @param maajoukkue maajoukkue
     * @return seuran
     */
    public static Seura kysyNimi(Stage modalityStage, Seura oletus, Maajoukkue maajoukkue) {
        return ModalController.<Seura, LisaaSeuraGUIController>showModal(LisaaSeuraGUIController.class.getResource("LisaaSeuraGUIView.fxml"), "Seuran Lisäys", modalityStage, oletus, ctrl -> ctrl.setMaajoukkue(maajoukkue)); 
    }
   
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
        
    }
    
    
    /**
     * Kuuntelee, jos luodaan uusi seura
     */
    protected void alusta() {
        editLisaaSeura.setOnKeyReleased(e -> kasitteleMuutosSeuraan(editLisaaSeura));
    }
    
    
    /**
     * Kirjoitetaan uusi seura
     * @param edit muokattava kenttä
     */
    private void kasitteleMuutosSeuraan(TextField edit) {
        if (seuraKohdalla == null) return;
        String s = edit.getText();
            String virhe = null;
            virhe = seuraKohdalla.setNimi(s);
            if (virhe == null ) {
                naytaVirhe(virhe);
            } else {
                naytaVirhe(virhe);
            }
        
    }
    

    @Override
    public Seura getResult() {
        if (!tallennetaan ) return null;
        return seuraKohdalla;
    }
    

    @Override
    public void handleShown() {
        editLisaaSeura.requestFocus();
        for (int i=0; i<maajoukkue.getSeuroja(); i++) {
            Seura seura = maajoukkue.getSeura(i);
            seuraKentat.add(seura.getNimi(), seura);
        }
    }
    

    @Override
    public void setDefault(Seura oletus) {
        seuraKohdalla = oletus;
        alusta();
        editLisaaSeura.setText(seuraKohdalla.getNimi());
    }
    
}
