/**
 * 
 */
package maajoukkue;

import java.io.File;
import java.util.Collection;

/**
 * huolehtii Hiihtajat ja Seurat -luokkien välisestä yhteistyöstä ja välittää näitä tietoja pyydettäessä
 * lukee ja kirjoittaa maajoukkueen tiedostoon pyytämällä apua avustajiltaan
 * @author weeik
 * @version 20.2.2024
 *
 */
public class Maajoukkue {
    
    private Hiihtajat hiihtajat = new Hiihtajat();
    private Seurat seurat = new Seurat();
    
    
    /**
     * Alustaa joukkueen tiedot
     */
    public Maajoukkue() {
        
    }
    
    
    /**
     * @param hiihtaja joka poistetaan
     * @return poistettavan indeksin, muuten 0
     */
    public int poista(Hiihtaja hiihtaja) {
        if (hiihtaja == null) return 0;
        int ret = hiihtajat.poista(hiihtaja.getJid());
        return ret;
    }
    
    
    /**
     * @param hakuehto hakuehto
     * @param k minkä mukaan
     * @return listan
     */
    public Collection<Hiihtaja> etsi(String hakuehto, int k) {
        return hiihtajat.etsi(hakuehto, k);
    }
    
    
    /**
     * Lisää uuden hiihtäjän
     * @param hiihtaja lisättävä hiihtäjä
     * @throws SailoException jos ei mahdu
     */
    public void lisaa(Hiihtaja hiihtaja) throws SailoException {
        hiihtajat.lisaa(hiihtaja);
    }
    
    
    /**
     * @param hiihtaja hiihtäjä millä korvataan
     * @throws SailoException jos jokin menee pieleen
     */
    public void korvaaTaiLisaa(Hiihtaja hiihtaja) throws SailoException {
        hiihtajat.korvaaTaiLisaa(hiihtaja);
    }
    
    
    /**
     * Lisää uuden seuran
     * @param seura lisättävä seura
     * @throws SailoException jos tulee ongelmia
     */
    public void lisaa(Seura seura) throws SailoException {
        seurat.lisaa(seura);
    }
    
    
    /**
     * Vaihtaa muutettu arvon true, jos kutsutaan
     */
    public void muutoksia() {
        hiihtajat.muutoksia();
    }
    
    
    /**
     * @return hiihtäjien lukumäärän
     */
    public int getHiihtajia() {
        return hiihtajat.getLkm();
    }
    
    
    /**
     * @return seurojen määrän
     */
    public int getSeuroja() {
        return seurat.getLkm();
    }
    
    
    /**
     * Antaa maajoukkueen i:n hiihtäjän
     * @param i monesko hiihtäjä (alkaa 0:sta)
     * @return hiihtäjä paikasta i
     */
    public Hiihtaja annaHiihtaja(int i) {
        return hiihtajat.anna(i);
    }
    
    /**
     * @param i monesko seura
     * @return seuran
     */
    public Seura annaSeura(int i) {
        return seurat.annaSeura(i);
    }
    
    
    /**
     * Luetaan tiedostosta maajoukkueen tiedot
     * @param nimi hakemiston nimi
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        File dir = new File(nimi);
        dir.mkdir();
        hiihtajat = new Hiihtajat();
        seurat = new Seurat();
        
        hiihtajat.lueTiedostosta(nimi);
        seurat.lueTiedostosta(nimi);
    }
    
    
    /**
     * Tallentaa maajoukkueen tiedot tiedostoon
     * @param nimi hakemiston nimi
     * @throws SailoException jos ei onnistu
     */
    public void tallenna(String nimi) throws SailoException {
        String virhe = "";
        try {
            hiihtajat.tallenna(nimi);
        } catch (SailoException ex) {
            virhe = ex.getMessage();
        }
        
        try {
            seurat.tallenna(nimi);
        } catch (SailoException ex) {
            virhe += ex.getMessage();
        }
        if (!"".equals(virhe)) throw new SailoException(virhe);
    }
    

    /**
     * @param args ei kätyössä
     */
    public static void main(String[] args) {
        //
    }


    /**
     * Antaa seuran
     * @param i seuran id
     * @return seuran
     */
    public Seura getSeura(int i) {
        return seurat.getSeura(i);
    }
    

}
