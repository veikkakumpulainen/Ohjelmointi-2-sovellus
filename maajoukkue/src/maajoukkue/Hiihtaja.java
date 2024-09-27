/**
 * 
 */
package maajoukkue;

import java.io.OutputStream;
import kanta.SisaltaaTarkistaja;
import java.io.PrintStream;
import java.util.Comparator;
import fi.jyu.mit.ohj2.Mjonot;

/**
 * Tietää hiihtäjän kentät(jid, nimi, ikä seuranid, mitalit, kertomus)
 * Osaa tarkistaa kentän oikeellisuuden, esim ikä-kohtaan ei muuta kun numeroita
 * Osaa muuttaa 1|Iivo Niskanen|31|... merkkijonon hiihtäjän tiedoiksi
 * Osaa antaa merkkijonona i:n kentän tiedot
 * Osaa laittaa merkkijon i:neksi kentäksi.
 * 
 * @author weeik
 * @version 19.2.2024
 *
 */
public class Hiihtaja implements Cloneable {
    
    
    private int jid = 0;
    private String nimi = "";
    private int ika = 0;
    private int seuraid = 0;
    private int kulta = 0;
    private int hopea = 0;
    private int pronssi = 0;
    private int mc1 = 0;
    private int top3 = 0;
    private String kertomus = "";
    
    private static int seuraavaNro = 1;
    
    private static SisaltaaTarkistaja tarkkis = new SisaltaaTarkistaja("0123456789");
    
    
    /**
     * @author weeik
     * @version 23.4.2024
     *
     */
    public static class Vertailija implements Comparator<Hiihtaja> {
        private int k;
        
 
        /**
         * @param k minkä mukaan halutaan lajitella
         */
        public Vertailija(int k) { 
            this.k = k; 
        } 

        
        @Override
        public int compare(Hiihtaja hiihtaja1, Hiihtaja hiihtaja2) {
            return hiihtaja1.getAvain(k).compareToIgnoreCase(hiihtaja2.getAvain(k));
        }
        

    }
    
    
    /** 
     * Antaa k:n kentän sisällön merkkijonona 
     * @param k monenenko kentän sisältö palautetaan 
     * @return kentän sisältö merkkijonona 
     */ 
    public String getAvain(int k) { 
        switch ( k ) { 
        case 0: return "" + nimi;
        case 1: return "" + ika;
        case 2: return "" + (100 - ika);
        default: return "" + nimi;
        } 
    }
    
    /**
     * Tekee kloonin hiihtäjästä
     */
    @Override
    public Hiihtaja clone() throws CloneNotSupportedException {
        Hiihtaja uusi;
        uusi = (Hiihtaja)super.clone();
        return uusi;
    }
    
    
    /**
     * Apumetodi, jolla saadaan testiarvot hiihtäjälle
     */
    public void tayta() {
        nimi = "Iivo Niskanen";
        ika = 31;
        seuraid = 1;
        kulta = 4;
        hopea = 2;
        pronssi = 3;
        mc1 = 8;
        top3 = 21;
        kertomus = "Perinteisen taitaja, Vieremalta...";
    }
    
    
    /**
     * Palauttaa hiihtäjän jäsen id:n
     * @return hiihtäjän jäsen id
     */
    public int getJid() {
        return jid;
    }
    
    /**
     * Asettaa jäsenid:n ja varmistaa, että seuraava numero on
     * aina suurempi kuin tähän mennessä
     * @param nr asetettava jäsenid
     */
    public void setJid(int nr) {
        jid = nr;
        if (jid >= seuraavaNro) seuraavaNro = jid + 1;
    }
    
    
    /**
     * Antaa hiihtäjälle seuraavan jäsenid:n
     * @return hiihtäjän jäsen id:n
     * @example
     * <pre name="test">
     * Hiihtaja klaebo = new Hiihtaja();
     * klaebo.getJid() === 0;
     * klaebo.rekisteroi();
     * klaebo.getJid() === 1;
     * Hiihtaja pellegrino = new Hiihtaja();
     * pellegrino.rekisteroi();
     * pellegrino.getJid() === 2;
     * </pre>
     */
    public int rekisteroi() {
        this.jid = seuraavaNro;
        seuraavaNro++;
        return this.jid;
    }
    
    /**
     * Tulostetaan henkilön tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Tulostetaan henkilön tiedot
     * @param out tietovirta, johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d",  jid) + " " + nimi + " " + ika + " " + seuraid);
        out.println(" " + String.format("%02d ", kulta) + String.format("%02d ", hopea) + String.format("%02d ", pronssi)
        + String.format("%02d ", mc1) + String.format("%02d ", top3));
        out.println(" " + kertomus);
    }
    
    
    /**
     * @example
     * <pre name="test">
     * Hiihtaja iivo = new Hiihtaja();
     * iivo.parse("0   |   Iivo Niskanen  |31|1   |4|2    |3|8|21  |Perinteisen taitaja, Vieremalta...");
     * iivo.toString() === "0|Iivo Niskanen|31|1|4|2|3|8|21|Perinteisen taitaja, Vieremalta...";
     * </pre>
     */
    @Override
    public String toString() {
        return "" +
                getJid() + "|" +
                nimi + "|" +
                ika + "|" +
                seuraid + "|" +
                kulta + "|" +
                hopea + "|" +
                pronssi + "|" +
                mc1 + "|" +
                top3 + "|" +
                kertomus;
    }
    
    
    /**
     * Osaa erottaa hiihtäjän tiedot riviltä
     * @param rivi yhden hiihtäjän tiedot
     */
    public void parse(String rivi) {
          
        StringBuilder sb = new StringBuilder(rivi);
        
        setJid(Mjonot.erota(sb, '|', getJid()));
        nimi = Mjonot.erota(sb, '|', nimi);
        ika = Mjonot.erota(sb, '|', ika);
        seuraid = Mjonot.erota(sb, '|', seuraid);
        kulta = Mjonot.erota(sb, '|', kulta);
        hopea = Mjonot.erota(sb, '|', hopea);
        pronssi = Mjonot.erota(sb, '|', pronssi);
        mc1 = Mjonot.erota(sb, '|', mc1);
        top3 = Mjonot.erota(sb, '|', top3);
        kertomus = Mjonot.erota(sb, '|', kertomus);
    }
    

    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        //
    }


    /**
     * @return hiihtäjän nimi
     */
    public String getNimi() {
        return this.nimi;
    }

    
    /**
     * @return hiihtäjän ikä
     */
    public int getIka() {
        return this.ika;
    }
    
    
    /**
     * @return hiihtäjän seuraid
     */
    public int getSeuraid() {
        return this.seuraid;
    }
    
    
    /**
     * @return hiihtäjän kultamitalit
     */
    public int getKulta() {
        return this.kulta;
    }
    
    
    /**
     * @return hiihtäjän hopeamitalit
     */
    public int getHopea() {
        return this.hopea;
    }
    
    
    /**
     * @return hiihtäjän pronssimitalit
     */
    public int getPronssi() {
        return this.pronssi;
    }
    
    
    /**
     * @return hiihtäjän maailmancup voitot
     */
    public int getMc1() {
        return this.mc1;
    }
    
    
    /**
     * @return hiihtäjän top3 maailmancupissa
     */
    public int getTop3() {
        return this.top3;
    }
    
    
    
    /**
     * @return hiihtäjän kertomus
     */
    public String getKertomus() {
        return this.kertomus;
    }
    
    
    /**
     * @param s uusi nimi
     * @return null jos ok, muuten virhe
     */
    public String setNimi(String s) {
        this.nimi = s;
        return null;
    }
    
    
    /**
     * @param s uusi ikä
     * @return jos pieleen
     */
    public String setIka(String s) {
        String virhe = tarkkis.tarkista(s);
        if (virhe != null) return virhe;
        int a = Mjonot.erotaInt(s, 0);
        this.ika = a;
        return null;
    }
    
    
    /**
     * @param s uusi seura
     * @return jos pieleen
     */
    public String setSeura(String s) {
        String virhe = tarkkis.tarkista(s);
        if (virhe != null) return virhe;
        int a = Mjonot.erotaInt(s, 0);
        this.seuraid = a;
        return null;
    }
    
    
    /**
     * @param s seuran id, joka laitetaan
     * @return ei mitään
     */
    public int setSeura(int s) {
        this.seuraid = s;
        return 0;
    }
    
    
    
    /**
     * @param s uusi kulta
     * @return jos pieleen
     */
    public String setKulta(String s) {
        String virhe = tarkkis.tarkista(s);
        if (virhe != null) return virhe;
        int a = Mjonot.erotaInt(s, 0);
        this.kulta = a;
        return null;
    }
    
    
    /**
     * @param s uusi hopea
     * @return jos pieleen
     */
    public String setHopea(String s) {
        String virhe = tarkkis.tarkista(s);
        if (virhe != null) return virhe;
        int a = Mjonot.erotaInt(s, 0);
        this.hopea = a;
        return null;
    }
    
    
    /**
     * @param s uusi pronssi
     * @return jos pieleen
     */
    public String setPronssi(String s) {
        String virhe = tarkkis.tarkista(s);
        if (virhe != null) return virhe;
        int a = Mjonot.erotaInt(s, 0);
        this.pronssi = a;
        return null;
    }
    
    
    /**
     * @param s uusi mc1
     * @return jos pieleen
     */
    public String setMc1(String s) {
        String virhe = tarkkis.tarkista(s);
        if (virhe != null) return virhe;
        int a = Mjonot.erotaInt(s, 0);
        this.mc1 = a;
        return null;
    }
    
    
    /**
     * @param s uusi top3
     * @return jos pieleen
     */
    public String setTop3(String s) {
        String virhe = tarkkis.tarkista(s);
        if (virhe != null) return virhe;
        int a = Mjonot.erotaInt(s, 0);
        this.top3 = a;
        return null;
    }
    
    
    /**
     * @param s uusi kertomus
     * @return null jos ok, muuten virhe
     */
    public String setKertomus(String s) {
        this.kertomus = s;
        return null;
    }
}