/**
 * 
 */
package maajoukkue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.FileInputStream;

/**
 * pitää yllä varsinaista hiihtajarekisteriä, eli osaa lisätä ja poistaa hiihtäjän
 * lukee ja kirjoittaa hiihtajien tiedostoon
 * osaa etsiä ja lajitella 
 * @author weeik
 * @version 19.2.2024
 *
 */
public class Hiihtajat implements Iterator<Hiihtaja> {
    
    private static final int MAX_JASENIA = 10;
    
    int lkm = 0;
    private Hiihtaja[] alkiot;
    private String tiedNimi;
    private boolean muutettu = false;
    
    
    /**
     * @param id jota etsitään
     * @return jos löytyy, muuten -1
     */
    public int etsiId(int id) {
        for (int i=0; i<lkm; i++)
            if (id == alkiot[i].getJid()) return i;
        return -1;
    }
    
    
    /**
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * Hiihtajat hiihtajat = new Hiihtajat();
     * Hiihtaja iivo = new Hiihtaja(), pera = new Hiihtaja();
     * hiihtajat.getLkm() === 0;
     * iivo.rekisteroi(); pera.rekisteroi();
     * hiihtajat.lisaa(iivo); hiihtajat.getLkm() === 1;
     * hiihtajat.lisaa(pera); hiihtajat.getLkm() === 2;
     * hiihtajat.poista(2); hiihtajat.getLkm() === 1;
     * hiihtajat.poista(1); hiihtajat.getLkm() === 0;
     * </pre>
     * @param id joka poistetaan
     * @return 1 jos onnistuu, 0 jos ei
     */
    public int poista(int id) {
        int ind = etsiId(id);
        if (ind < 0) return 0;
        lkm--;
        for (int i = ind; i < lkm; i++)
            alkiot[i] = alkiot[i+1];
        alkiot[lkm] = null;
        muutettu = true;
        return 1;
    }
    
    
    /**
     * @param hakuehto hakuehto
     * @param k minkä mukaan
     * @return listan
     */
    public Collection<Hiihtaja> etsi(String hakuehto, int k) {
        
        List<Hiihtaja> loytyneet = new ArrayList<Hiihtaja>();
        for (int i=0; i<lkm; i++) {
            if (alkiot[i].getNimi().toUpperCase().contains(hakuehto.toUpperCase()) ) {
                loytyneet.add(alkiot[i]);
            }
        }
        loytyneet.sort(new Hiihtaja.Vertailija(k));
        return loytyneet;
    }
    
    
    /**
     * Palauttaa maajoukkueen hiihtajien lukumäärän
     * @return hiihtäjien lukumäärä
     */
    public int getLkm() {
        return this.lkm;
    }
    
    
    /**
     * Palauttaa viitteen i:n hiihtäjään
     * @param i monennenko hiihtäjän viite halutaan
     * @return viite hiihtäjään, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Hiihtaja anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || this.lkm <= i) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    
    /**
     * Luodaan alustava taulukko
     */
    public Hiihtajat() {
        this.alkiot = new Hiihtaja[MAX_JASENIA];
    }
    
    
    /**
     * Lisää uuden hiihtäjän tietorakenteeseen ja ottaa hiihtäjän omistukseensa
     * @param hiihtaja Hiihtäjään lisättävä viite
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * Hiihtajat hiihtajat = new Hiihtajat();
     * Hiihtaja iivo = new Hiihtaja(), pera = new Hiihtaja();
     * hiihtajat.getLkm() === 0;
     * hiihtajat.lisaa(iivo); hiihtajat.getLkm() === 1;
     * hiihtajat.lisaa(pera); hiihtajat.getLkm() === 2;
     * hiihtajat.lisaa(iivo); hiihtajat.getLkm() === 3;
     * hiihtajat.anna(0) === iivo;
     * hiihtajat.anna(1) === pera;
     * hiihtajat.anna(2) === iivo;
     * hiihtajat.anna(1) == iivo === false;
     * hiihtajat.anna(1) == pera === true;
     * hiihtajat.anna(3) === iivo; #THROWS IndexOutOfBoundsException 
     * hiihtajat.lisaa(iivo); hiihtajat.getLkm() === 4;
     * hiihtajat.lisaa(iivo); hiihtajat.getLkm() === 5;
     * hiihtajat.lisaa(iivo);
     * </pre>
     */
    public void lisaa(Hiihtaja hiihtaja) throws SailoException {
        if (lkm >= alkiot.length) {
            Hiihtaja[] aputaul = Arrays.copyOf(alkiot, lkm*2+1);
            alkiot = aputaul;
        }
        this.alkiot[this.lkm] = hiihtaja;
        this.lkm++;
        muutettu = true;
    }
    
    
    /**
     * @param hiihtaja joka korvataan tai lisätään
     * @throws SailoException jos jokin menee pieleen
     */
    public void korvaaTaiLisaa(Hiihtaja hiihtaja) throws SailoException {
        int id = hiihtaja.getJid();
        for (int i=0; i < lkm; i++) {
            if (alkiot[i].getJid() == id) {
                alkiot[i] = hiihtaja;
                muutettu = true;
                return;
            }
        }
        lisaa(hiihtaja);
    }
    
    
    /**
     * Tallentaa hiihtäjän tiedostoon
     * @param hakemisto mihin hakemistoon
     * @throws SailoException jos tallentaminen epäonnistuu
     * 
     */
    public void tallenna(String hakemisto) throws SailoException {
        if (!muutettu) return;
        File ftied = new File(hakemisto + "/yleistiedot.dat");
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftied, false))) {
            for (int i=0; i<this.lkm; i++) {
                Hiihtaja hiihtaja = this.anna(i);
                fo.println(hiihtaja.toString());
            }
        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        }
        
        muutettu = false;        
    }
    
    /**
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #THROWS IOException
     * #import java.io.IOException;
     * #import java.io.File;
     * 
     * Hiihtajat hiihtajat = new Hiihtajat();
     * Hiihtaja iivo = new Hiihtaja();
     * Hiihtaja pera = new Hiihtaja();
     * iivo.tayta();
     * 
     * String hakemisto = "testisuomi";
     * File dir = new File(hakemisto);
     * File ftied = new File(hakemisto+"/yleistiedot.dat");
     * dir.mkdir();
     * try {
     * ftied.createNewFile();
     * ftied.createNewFile() === false;
     * } catch (IOException e){
     *   System.out.println("Ei saa luettua");
     * }
     * hiihtajat.lueTiedostosta("eioleolemassa"); #THROWS SailoException
     * 
     * hiihtajat.lisaa(iivo);
     * hiihtajat.lisaa(pera);
     * hiihtajat.tallenna(hakemisto);
     * 
     * 
     * Hiihtajat hiihtajat2 = new Hiihtajat();
     * hiihtajat2.lisaa(pera);
     * hiihtajat.anna(0).getNimi().equals(iivo.getNimi());
     * ftied.delete() === true;
     * dir.delete() === true;
     * 
     * 
     * </pre>
     * @param hakemisto maajoukkue, johon tallennetaan
     * @throws SailoException jos jokin menee pieleen
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedNimi = hakemisto + "/yleistiedot.dat";
        File ftied = new File(tiedNimi);
        
        try (Scanner fi = new Scanner(new FileInputStream(ftied))){
            while (fi.hasNext()) {
                String s = fi.nextLine();
                if (s == null || "".equals(s) || s.charAt(0) == ';') continue;
                Hiihtaja hiihtaja = new Hiihtaja();
                hiihtaja.parse(s);
                lisaa(hiihtaja);
            }
            muutettu = false;
        } catch (FileNotFoundException e) {
            throw new SailoException("Ei saa luettua tiedostoa " + tiedNimi);
        }
        
    }
    

    /**
     * Pitää yllä varsinaista hiihtäjärekisteriä, eli osaa lisätä ja poistaa hiihtäjän
     * Lukee ja kirjoittaa hiihtäjien tiedostoon
     * Osaa etsiä ja lajitella
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        //
    }
    
    /**
     * @author weeik
     * @version 5.4.2024
     *
     */
    public class HiihtajatIterator implements Iterator<Hiihtaja> {
        private int kohdalla = 0;

        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }
    
    
        @Override
        public Hiihtaja next() {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }
        
        
    }
    
    /**
     * @return iteraattori hiihtäjistä
     */
    public Iterator<Hiihtaja> iterator() {
        return new HiihtajatIterator();
    }


    @Override
    public boolean hasNext() {
        return false;
    }


    @Override
    public Hiihtaja next() {
        return null;
    }


    /**
     * vaihtaa muutettu arvon trueksi, jos kutsutaan
     */
    public void muutoksia() {
        muutettu = true;
        
    }

}
