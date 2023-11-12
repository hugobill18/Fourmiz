package fourmiz;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FiltreSimple extends FileFilter{
	   //Description et extension acceptée par le filtre
	   private String description;
	   private String extension;
	   //Constructeur à partir de la description et de l'extension acceptée
	   public FiltreSimple(String description, String extension){
	      if(description == null || extension ==null){
	         throw new NullPointerException("La description (ou extension) ne peut être null.");
	      }
	      this.description = description;
	      this.extension = extension;
	   }
	   //Implémentation de FileFilter
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