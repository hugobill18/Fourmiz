package fourmiz;
import java.awt.Graphics;
import java.io.*;

import simengine.*;

public class Monde implements TerrainDeJeu{
	int lignes,colonnes,fourmis;
	Bloc terrain[][];
	Fourmi fourmi[];
	public Monde(String chemin){
		try {

			this.chargement(chemin);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	
	/**
	 * Chargement du fichier contenant le terrain
	 *
	 */
	public void chargement(String chemin) throws Exception{
		//Ouverture du fichier
		BufferedReader in = new BufferedReader(new FileReader(chemin));
		StreamTokenizer st = new StreamTokenizer(in);
		int tokenType = 0;
		int i = 0;
		tokenType = st.nextToken ();
		//Definition lignes, colonnes, nombre de fourmis
		while ( tokenType == StreamTokenizer.TT_NUMBER && i<3 ) {
			if (i == 0) lignes = (int)st.nval;
				if (i==1) {
					colonnes =(int)st.nval;
					terrain=new Bloc[lignes][colonnes];
				}
				if (i==2) fourmis = (int)st.nval;
				tokenType = st.nextToken ();
				i++;
			}
		//Creation du monde
		int x_fourm,y_fourm; //Variables permettant de retenir la position de la fourmiliere
		x_fourm = 0;
		y_fourm = 0;
			for (int j=0;j<lignes;j++){
				for (int k=0;k<colonnes;k++){
					if (tokenType == StreamTokenizer.TT_NUMBER)
					{
						if ((int)st.nval==-1) {terrain[j][k]=new Fourmiliere();x_fourm = j; y_fourm = k;}
						if ((int)st.nval==-2) terrain[j][k]=new Obstacle();
						if ((int)st.nval>=0) {terrain[j][k]=new Vide(); terrain[j][k].setnourriture((int)st.nval);}
						tokenType = st.nextToken ();
					}
				}
			}
			//Fermeture du fichier
			in.close();
	// Création des fourmis à la position (x_fourm,y_fourm)
			terrain[x_fourm][y_fourm].setfourmis(fourmis);
			fourmi=new Fourmi[fourmis];
			for (i=0;i<fourmis;i++) fourmi[i]=new Fourmi(x_fourm,y_fourm);
	}
	
	
	public void dessinerBloc(int ligne, int colonne, Graphics g) {
		terrain[ligne][colonne].dessiner(g);

	}

	public int getColonnes() {
		return colonnes;
	}

	public int getLignes() {
		return lignes;
	}

	public void step() {
		int xtmp,ytmp;
		for (int i=0;i<fourmis;i++) {
			terrain[fourmi[i].getx()][fourmi[i].gety()].mvtfourmi(false);
			xtmp=fourmi[i].getx();
			ytmp=fourmi[i].gety();
			fourmi[i].deplacerrand();
			while(fourmi[i].getx()<0||fourmi[i].getx()>lignes-1||fourmi[i].gety()<0||fourmi[i].gety()>colonnes-1||terrain[fourmi[i].getx()][fourmi[i].gety()].getpossibility()==false){
				fourmi[i].setx(xtmp);
				fourmi[i].sety(ytmp);
				fourmi[i].deplacerrand();
			}
			terrain[fourmi[i].getx()][fourmi[i].gety()].mvtfourmi(true);
			

		}

	}

}
