package maajoukkue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * pitää yllä varsinaista seurarekisteriä, eli osaa lisätä ja poistaa seuran
 * lukee ja kirjoittaa seurat tiedostoon
 * osaa etsiä ja lajitella
 * @author vesal
 * @version 8.3.2021
 */
public class Seurat implements Iterable<Seura>{

    private ArrayList<Seura> alkiot = new ArrayList<Seura>();
    private String tiedNimi;
    private boolean muutettu = false;
    
    /**
     * Alustaminen
     */
    public Seurat() {
        // ei tarvii tehdä mitään
    }
    
    
    /**
     * @example
     * <pre name="test">
     * Seurat seurat = new Seurat();
     * Seura puijo = new Seura();
     * Seura jami = new Seura();
     * seurat.lisaa(puijo); seurat.getLkm() === 1;
     * seurat.lisaa(jami); seurat.getLkm() === 2;
     * </pre>
     * @param seura lisättävä seura
     */
    public void lisaa(Seura seura) {
        alkiot.add(seura);
        muutettu = true;
    }
    
    
    /**
     * @return alkioiden määrän
     */
    public int getLkm() {
        return alkiot.size();
    }

    
    
    /**
     * Haetaan hiihtäjän seura
     * @param tunnusnro jäsenen tunnusnumero jolle seura haetaan
     * @return tietorakenne jossa viiteet löydetteyihin seuroihin
     */
    public Seura annaSeura(int tunnusnro) {
        Seura hiihtajanSeura = new Seura();
        for (Seura seura: alkiot) // iteraattori
            if (seura.getTunnusNro() == tunnusnro) hiihtajanSeura = seura;
        return hiihtajanSeura;
    }
    
    
    /**
     * Tallentaa seuran tiedostoon
     * @param hakemisto mihin hakemistoon
     * @throws SailoException jos tallentaminen epäonnistuu
     * 
     */
    public void tallenna(String hakemisto) throws SailoException {
        if (!muutettu) return;
        File ftied = new File(hakemisto + "/seurat.dat");
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftied, false))) {
            for (Seura seura: this) {
                fo.println(seura.toString());
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
     * Seurat seurat = new Seurat();
     * Seura puijo = new Seura();
     * Seura jami = new Seura();
     * puijo.setNimi("testi1");
     * 
     * 
     * String hakemisto = "testisuomi";
     * File dir = new File(hakemisto);
     * dir.mkdir();
     * File ftied = new File(hakemisto+"/seurat.dat");
     * try {
     * ftied.createNewFile();
     * ftied.createNewFile() === false;
     * } catch (IOException e){
     *   System.out.println("Ei saa luettua");
     * }
     * seurat.lueTiedostosta("eioleolemassa"); #THROWS SailoException
     * 
     * seurat.lisaa(puijo);
     * seurat.lisaa(jami);
     * seurat.tallenna(hakemisto);
     * 
     * Seurat seurat2 = new Seurat();
     * seurat2.lueTiedostosta(hakemisto);
     * seurat.annaSeura(0).getNimi().equals(puijo.getNimi());
     * ftied.delete() === true;
     * dir.delete() === true;
     * </pre>
     * @param hakemisto maajoukkue, josta luetaan
     * @throws SailoException jos jokin menee pieleen
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedNimi = hakemisto + "/seurat.dat";
        File ftied = new File(tiedNimi);
        
        try (Scanner fi = new Scanner(new FileInputStream(ftied))){
            while (fi.hasNext()) {
                String s = fi.nextLine();
                if (s == null || "".equals(s) || s.charAt(0) == ';') continue;
                Seura seura = new Seura();
                seura.parse(s);
                lisaa(seura);
            }
            muutettu = false;
        } catch (FileNotFoundException e) {
            throw new SailoException("Ei saa luettua tiedostoa " + tiedNimi);
        }
        
    }
    
    
    /**
     * Testiohjelma harrastuksille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        //
    }


    @Override
    public Iterator<Seura> iterator() {
        return alkiot.iterator();
    }


    /**
     * @param i kenen
     * @return seuran
     */
    public Seura getSeura(int i) {
        return alkiot.get(i);
    }
}
