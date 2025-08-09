package fourmiz;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FiltreSimple extends FileFilter{
	   //Description et extension acceptÃ©e par le filtre
	   private String description;
	   private String extension;
	   //Constructeur Ã  partir de la description et de l'extension acceptÃ©e
	   public FiltreSimple(String description, String extension){
	      if(description == null || extension ==null){
	         throw new NullPointerException("La description (ou extension) ne peut Ãªtre null.");
	      }
	      this.description = description;
	      this.extension = extension;
	   }
	   //ImplÃ©mentation de FileFilter
	   public boolean accept(File file){
	      if(file.isDirectory()) { 
	         return true; 
	      } 
	      String nomFichier = file.getName().toLowerCase(); 

	      return nomFichier.endsWith(extension);
	   }
	      public String getDescription(){
	      return description;
	   }
	}