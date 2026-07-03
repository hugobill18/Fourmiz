package fourmiz;

import java.awt.Color;

/**
 * Un bloc vide, franchissable, pouvant contenir de la nourriture.
 */
public class Vide extends Bloc{

	public Vide(){
		possibility=true;
		nourriture=0;
		couleur=new Color(0xF7F3E8); // Sable clair
	}

	public void setfourmis(int fourm) {
		fourmis=fourm;
	}

	public void setnourriture (int x)
	{
		nourriture=x;
	}
}
