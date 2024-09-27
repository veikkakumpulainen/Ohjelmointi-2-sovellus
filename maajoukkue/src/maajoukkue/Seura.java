package maajoukkue;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * tietää seurakentän
 * osaa muuttaa 1|Puijon hiihtoseura| -merkkijonon seuran tiedoiksi
 * osaa antaa merkkijonona i:n kentän tiedot
 * osaa laittaa merkkijonon i:neksi kentäksi
 * 
 * @author weeik
 * @version 6.3.2024
 *
 */
public class Seura {
    
    private String seuranNimi = "";
    private int tunnusNro;
    
    private static int seuraavaNro = 1;
    
    
    /**
     * Alustetaan Seura. 
     */
    public Seura() {
        // Vielä ei tarvita mitään
    }


    /**
     * Tekee seurasta kloonin
     */
    @Override
    public Seura clone() throws CloneNotSupportedException {
        Seura uusi;
        uusi = (Seura)super.clone();
        return uusi;
    }
   
    
    /**
     * Antaa seuralle seuraavan rekisterinumeron
     * @return seuran tunnusNro
     * @example
     * <pre name="test">
     *   Seura seura1 = new Seura();
     *   seura1.getTunnusNro() === 0;
     *   seura1.rekisteroi();
     *   Seura seura2 = new Seura();
     *   seura2.rekisteroi();
     *   int n1 = seura1.getTunnusNro();
     *   int n2 = seura2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>

     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
    
    
    /**
     * @return seuran tunnusNro:n
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    
    /**
     * Asettaa tunnusnumeron ja varmistaa, että seuraava numero
     * on aina suurempi kuin tähän mennessä suurin
     * @param nr asetettava tunnusnumero
     */
    public void setTunnusNro(int nr) {
        tunnusNro = nr;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }
    
    
    /**
     * @example
     * <pre name="test">
     * Seura puijo = new Seura();
     * puijo.parse("0    |     Puijon hiihtoseura");
     * puijo.toString() === "0|Puijon hiihtoseura";
     * </pre>
     */
    @Override
    public String toString() {
        return tunnusNro + "|" + seuranNimi;
    }
    
    
    /**
     * Selvittää seuran nimen ja kelle se kuuluu riviltä
     * @param rivi josta tiedot selvitetään
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        seuranNimi = Mjonot.erota(sb, '|', seuranNimi);
    }
    
    /**
     * Tulostetaan seuran tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(seuranNimi);
    }

    
    
    /**
     * Tulostetaan henkilön tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    

        
    /**
     * Testiohjelma Seuralle
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        //
    }


    /**
     * @return seuran nimen
     */
    public String getNimi() {
        return this.seuranNimi;
    }


    /**
     * @param s nimi
     * @return n
     */
    public String setNimi(String s) {
        this.seuranNimi = s;
        return null;
    }

}
