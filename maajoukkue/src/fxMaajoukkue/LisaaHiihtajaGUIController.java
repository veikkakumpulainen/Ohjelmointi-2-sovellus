package fxMaajoukkue;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import maajoukkue.Hiihtaja;
import maajoukkue.Maajoukkue;

/**
 * @author weeik
 * @version 7.2.2024
 *
 */
public class LisaaHiihtajaGUIController implements ModalControllerInterface<Hiihtaja>, Initializable {

    
    @FXML private TextField editNimi;
    @FXML private TextField editIka;
    @FXML private TextField editKulta;
    @FXML private TextField editHopea;
    @FXML private TextField editPronssi;
    @FXML private TextField editMc1;
    @FXML private TextField editTop3;
    @FXML private TextArea editKertomus;
    @FXML private Label labelVirhe;
    
    
    private Hiihtaja hiihtajaKohdalla;
    TextField[] edits;
    private Maajoukkue maajoukkue;
    
    
    /**
     * @param maaj maajoukkue
     */
    public void setMaajoukkue(Maajoukkue maaj) {
        maajoukkue = maaj;
    }
    
    
    @Override
    public Hiihtaja getResult() {
        return hiihtajaKohdalla;
    }

    @Override
    public void handleShown() {
        //editNimi.requestFocus();        
    }

    
    /**
     * Tyhjentään tekstikentät 
     */
    public void tyhjenna() {
        editNimi.setText("");
    }



    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
        
    }
    
    /**
     * Aliohjelmat mihin mennään, jos muokataan kenttiä
     */
    protected void alusta() {
        edits = new TextField[] {editNimi, editIka, editKulta, editHopea, editPronssi, editMc1, editTop3};
        editNimi.setOnKeyReleased(e -> kasitteleMuutosNimeen(editNimi));
        editIka.setOnKeyReleased(e -> kasitteleMuutosNumeroon(2, editIka));
        editKulta.setOnKeyReleased(e -> kasitteleMuutosNumeroon(4, editKulta));
        editHopea.setOnKeyReleased(e -> kasitteleMuutosNumeroon(5, editHopea));
        editPronssi.setOnKeyReleased(e -> kasitteleMuutosNumeroon(6, editPronssi));
        editMc1.setOnKeyReleased(e -> kasitteleMuutosNumeroon(7, editMc1));
        editTop3.setOnKeyReleased(e -> kasitteleMuutosNumeroon(8, editTop3));
        editKertomus.setOnKeyReleased(e -> kasitteleMuutosKertomukseen(editKertomus));
    }
    
    
    /**
     * Käsittelee muutoksen nimeen
     * @param edit kenttä jota muokataan
     */
    private void kasitteleMuutosNimeen(TextField edit) {
        if (hiihtajaKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        virhe = hiihtajaKohdalla.setNimi(s);
        if (virhe == null ) {
            naytaVirhe(virhe);
        } else {
            naytaVirhe(virhe);
        }
    }
    
    
    /**
     * Käsittelee muutoksen kertomukseen
     * @param edit kenttä jota muokataan
     */
    private void kasitteleMuutosKertomukseen(TextArea edit) {
        if (hiihtajaKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        virhe = hiihtajaKohdalla.setKertomus(s);
        if (virhe == null ) {
            naytaVirhe(virhe);
        } else {
            naytaVirhe(virhe);
        }
    }
    
    
    /**
     * Käsittelee muutoksen numerokenttään
     * @param k monesko kenttä, niistä johon syötetään vain numeroita
     * @param edit kenttä jota muokataaan
     */
    private void kasitteleMuutosNumeroon(int k, TextField edit) {
        if (hiihtajaKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        switch (k) {
            case 2: virhe = hiihtajaKohdalla.setIka(s); break;
            case 3: virhe = hiihtajaKohdalla.setSeura(s); break;
            case 4: virhe = hiihtajaKohdalla.setKulta(s); break;
            case 5: virhe = hiihtajaKohdalla.setHopea(s); break;
            case 6: virhe = hiihtajaKohdalla.setPronssi(s); break;
            case 7: virhe = hiihtajaKohdalla.setMc1(s); break;
            case 8: virhe = hiihtajaKohdalla.setTop3(s); break;
            default: return;
        }
        if (virhe == null) {
            naytaVirhe(virhe);
        } else {
            naytaVirhe(virhe);
        }
    }
    
    
    /**
     * Peruutanappi pois ikkunasta ilman muutoksia
     */
    @FXML private void handlePeruuta() {
        hiihtajaKohdalla = null;
        ModalController.closeStage(editNimi);
    }
    
    /**
     * Vahvistanappi ikkunasta pois
     */
    @FXML private void handleVahvista() {
        if (hiihtajaKohdalla != null && hiihtajaKohdalla.getNimi().trim().equals("")){
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        
        ModalController.closeStage(editNimi);
    }



    @Override
    public void setDefault(Hiihtaja oletus) {
        hiihtajaKohdalla = oletus;
        naytaHiihtaja();
    }
    
    
    /**
     * Näyttää virheen näytössä jos vääriä merkkejä
     * @param virhe jos tulee virhe
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
     * Näyttää hiihtäjän tiedot
     * @param edits kentät
     * @param kertomus kertomus
     * @param hiihtaja hiihtäjä
     * @param maajoukkue maajoukkue
     * @param editSeura seura
     */
    public static void naytaHiihtaja(TextField[] edits, TextArea kertomus, Hiihtaja hiihtaja, Maajoukkue maajoukkue, TextField editSeura) {
        if (hiihtaja == null) return;
        edits[0].setText(hiihtaja.getNimi());
        edits[1].setText(String.valueOf(hiihtaja.getIka()));

        editSeura.setText(maajoukkue.annaSeura(hiihtaja.getSeuraid()).getNimi());
        edits[3].setText(String.valueOf(hiihtaja.getKulta()));
        edits[4].setText(String.valueOf(hiihtaja.getHopea()));
        edits[5].setText(String.valueOf(hiihtaja.getPronssi()));
        edits[6].setText(String.valueOf(hiihtaja.getMc1()));
        edits[7].setText(String.valueOf(hiihtaja.getTop3()));

        kertomus.setText(hiihtaja.getKertomus());
    }
    
    
    /**
     * Näyttää hiihtääjän tiedot
     */
    public void naytaHiihtaja() {
        if (hiihtajaKohdalla == null) return;
        editNimi.setText(hiihtajaKohdalla.getNimi());
        editIka.setText(String.valueOf(hiihtajaKohdalla.getIka()));
        editKulta.setText(String.valueOf(hiihtajaKohdalla.getKulta()));
        editHopea.setText(String.valueOf(hiihtajaKohdalla.getHopea()));
        editPronssi.setText(String.valueOf(hiihtajaKohdalla.getPronssi()));
        editMc1.setText(String.valueOf(hiihtajaKohdalla.getMc1()));
        editTop3.setText(String.valueOf(hiihtajaKohdalla.getTop3()));
        editKertomus.setText(hiihtajaKohdalla.getKertomus());
    }
    
    
    /**
     * @param modalityStage taso
     * @param oletus hiihtaja jonka tiedot viedään
     * @param maajoukkue maajoukkue
     * @return tiedot tuodaan
     */
    public static Hiihtaja kysyHiihtaja(Stage modalityStage, Hiihtaja oletus, Maajoukkue maajoukkue) {
        return ModalController.<Hiihtaja,LisaaHiihtajaGUIController>showModal(LisaaHiihtajaGUIController.class.getResource("LisaaHiihtajaGUIView.fxml"), "Muokkaa", modalityStage, oletus, ctrl -> ctrl.setMaajoukkue(maajoukkue));
    }

    
}
