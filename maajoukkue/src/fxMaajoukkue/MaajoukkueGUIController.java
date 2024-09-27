    package fxMaajoukkue;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import maajoukkue.Hiihtaja;
import maajoukkue.Maajoukkue;
import maajoukkue.SailoException;
import maajoukkue.Seura;

/**
 * @author weeik
 * @version 15.1.2024
 *
 */
public class MaajoukkueGUIController implements Initializable{
    
    @FXML private ListChooser<Hiihtaja> chooserHiihtajat;
    @FXML private TextField editNimi;
    @FXML private TextField editIka;
    @FXML private TextField editSeura;
    @FXML private TextField editKulta;
    @FXML private TextField editHopea;
    @FXML private TextField editPronssi;
    @FXML private TextField editMc1;
    @FXML private TextField editTop3;
    @FXML private TextArea editKertomus;
    @FXML private TextField textHaku;
    
    @FXML private ComboBoxChooser<String> cbKentat;
   
    
    @FXML private void handleHaku() {
        hae(0);
    }
    
    @FXML private void handleSeura() {
        muokkaaSeuraa();
    }
    
    
    @FXML private void handleLisaa() {
        uusiHiihtaja();
    }
    
    
    @FXML private void handlePoista() {
        poistaHiihtaja();
    }
    
    
    @FXML private void handleMuokkaa() {
        muokkaa();
    } 


    @FXML private void handleTallenna() {
        tallenna();
    }
    
    
    @FXML private void handleAvaa() {
        avaa();
    }
    
    
    @FXML private void handleSulje() {
        tallenna();
        Platform.exit();
    }
    
    
    @FXML private void handleTietoja() {
        ModalController.showModal(MaajoukkueGUIController.class.getResource("ApuaGUIView.fxml"), "Tietoja", null, "");
    }

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }

    
    // =================================================================================================================
    
    private Maajoukkue maajoukkue;
    private String maajoukkueenNimi = "suomi";
    private Hiihtaja hiihtajaKohdalla;
    private TextField[] edits;
        
    /**
     * Alustaa hiihtäjien listan
     */
    private void alusta() {
        chooserHiihtajat.clear();
        chooserHiihtajat.addSelectionListener(e -> naytaHiihtaja());
        
        cbKentat.clear();
        cbKentat.add("Aakkosjarjestys");
        cbKentat.add("Nuorin");
        cbKentat.add("Vanhin");
        
        edits = new TextField[] {editNimi, editIka, editSeura, editKulta, editHopea, editPronssi, editMc1, editTop3};
    }
    
    
    /**
     * Tallentaa muutokset
     */
    private void tallenna() {
        try {
            maajoukkue.tallenna(maajoukkueenNimi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }
    
    
    /*
     * Näyttää listasta valitun hiihtäjän tiedot näytöllä
     */
    private void naytaHiihtaja() {
        hiihtajaKohdalla = chooserHiihtajat.getSelectedObject();
        if (hiihtajaKohdalla == null) return;
        
        LisaaHiihtajaGUIController.naytaHiihtaja(edits, editKertomus, hiihtajaKohdalla, maajoukkue, editSeura);
        
    }

    
    /**
     * Avaa maajoukkueen
     * @return false, jos painetaan cancel
     */
    public boolean avaa() {
        String uusinimi = AloitusGUIController.kysyNimi(null, "Suomi");
        if (uusinimi == null) return false;
        lueTiedosto(uusinimi);
        return true;
    }
    
    
    /**
     * Alustaa maajoukkueen lukemalla sem valitun nimisestä tiedostosta
     * @param nimi josta maajoukkueen tiedot luetaan
     */
    public void lueTiedosto(String nimi) {
        maajoukkueenNimi = nimi;
        //setTitle("Maajoukke - " + maajoukkueenNimi);
        try {
            maajoukkue.lueTiedostosta(nimi);
            hae(0);
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
        
    }


    /**
     * Asettaa käytettävän maajoukkueen
     * @param maajoukkue jota käytetään
     */
    public void setMaajoukkue(Maajoukkue maajoukkue) {
        this.maajoukkue = maajoukkue;
        
    }
    
    
    /**
     * Hakee hiihtäjien tiedot listaan
     * @param jid hiihtäjän jäsen id
     */
    public void hae(int jid) {
        String ehto = textHaku.getText();
        int k = cbKentat.getSelectedIndex();
        
        chooserHiihtajat.clear();
        
        int index = 0;
        Collection<Hiihtaja> hiihtajat = maajoukkue.etsi(ehto, k);
        int i = 0;
        for (Hiihtaja hiihtaja: hiihtajat){
            if (hiihtaja.getJid() == jid) index = i;
            chooserHiihtajat.add(hiihtaja.getNimi(), hiihtaja);
            i++;
        }
        
        chooserHiihtajat.setSelectedIndex(index);
    }
        
    
    /**
     * Lisätään maajoukkueeseen uusi jäsen
     */
    public void uusiHiihtaja() {
        Hiihtaja uusi = new Hiihtaja();
        uusi.rekisteroi();
        try {
            maajoukkue.lisaa(uusi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
        }
        hae(uusi.getJid());
    }
    
        
    /**
     * Seuran muokkaaminen
     */
    public void muokkaaSeuraa() {
        if (hiihtajaKohdalla == null) return;
        try {
            Seura uusi = new Seura();
            uusi = LisaaSeuraGUIController.kysyNimi(null, uusi, maajoukkue);
            if (uusi == null) return;
            if (uusi.getTunnusNro() == 0) {
                uusi.rekisteroi();
                maajoukkue.lisaa(uusi);
            }
            hiihtajaKohdalla.setSeura(uusi.getTunnusNro());
            maajoukkue.muutoksia();
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia lisäämisessä " + e.getMessage());
        }
        
        hae(hiihtajaKohdalla.getJid());
    }
    
    
    /**
     * Hiihtäjän muokkaus
     */
    private void muokkaa() {
        hiihtajaKohdalla = chooserHiihtajat.getSelectedObject();
        if (hiihtajaKohdalla == null) return;
        try {
            Hiihtaja hiihtaja = hiihtajaKohdalla.clone();
            hiihtaja = LisaaHiihtajaGUIController.kysyHiihtaja(null, hiihtaja, maajoukkue);
            if (hiihtaja == null) return;
            maajoukkue.korvaaTaiLisaa(hiihtaja);
            hae(hiihtajaKohdalla.getJid());
        } catch (CloneNotSupportedException e) {
            // 
        } catch (SailoException e) {
            //
        }

    }
    

    /**
     * Hiihtäjän poisto
     */
    private void poistaHiihtaja() {
        Hiihtaja hiihtaja = hiihtajaKohdalla;
        if (hiihtaja == null) return;
        if (!Dialogs.showQuestionDialog("Poisto", "Poistetaanko hiihtäjä: " + hiihtaja.getNimi(), "Kyllä", "Ei"))
            return;
        maajoukkue.poista(hiihtaja);
        int index = chooserHiihtajat.getSelectedIndex();
        hae(0);
        chooserHiihtajat.setSelectedIndex(index);
    }
    
}