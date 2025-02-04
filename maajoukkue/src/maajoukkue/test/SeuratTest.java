package maajoukkue.test;
// Generated by ComTest BEGIN
import java.io.IOException;
import java.io.File;
import static org.junit.Assert.*;
import org.junit.*;
import maajoukkue.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2024.05.10 12:18:17 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class SeuratTest {



  // Generated by ComTest BEGIN
  /** testLisaa35 */
  @Test
  public void testLisaa35() {    // Seurat: 35
    Seurat seurat = new Seurat(); 
    Seura puijo = new Seura(); 
    Seura jami = new Seura(); 
    seurat.lisaa(puijo); assertEquals("From: Seurat line: 39", 1, seurat.getLkm()); 
    seurat.lisaa(jami); assertEquals("From: Seurat line: 40", 2, seurat.getLkm()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testLueTiedostosta97 
   * @throws SailoException when error
   */
  @Test
  public void testLueTiedostosta97() throws SailoException {    // Seurat: 97
    Seurat seurat = new Seurat(); 
    Seura puijo = new Seura(); 
    Seura jami = new Seura(); 
    puijo.setNimi("testi1"); 
    String hakemisto = "testisuomi"; 
    File dir = new File(hakemisto); 
    dir.mkdir(); 
    File ftied = new File(hakemisto+"/seurat.dat"); 
    try {
    ftied.createNewFile(); 
    assertEquals("From: Seurat line: 115", false, ftied.createNewFile()); 
    } catch (IOException e){
    System.out.println("Ei saa luettua"); 
    }
    try {
    seurat.lueTiedostosta("eioleolemassa"); 
    fail("Seurat: 119 Did not throw SailoException");
    } catch(SailoException _e_){ _e_.getMessage(); }
    seurat.lisaa(puijo); 
    seurat.lisaa(jami); 
    seurat.tallenna(hakemisto); 
    Seurat seurat2 = new Seurat(); 
    seurat2.lueTiedostosta(hakemisto); 
    seurat.annaSeura(0).getNimi().equals(puijo.getNimi()); 
    assertEquals("From: Seurat line: 128", true, ftied.delete()); 
    assertEquals("From: Seurat line: 129", true, dir.delete()); 
  } // Generated by ComTest END
}