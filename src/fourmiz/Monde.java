package fourmiz;
import java.awt.Graphics;
import java.io.*;

import simengine.*;

/**
 * Le monde de la simulation : un terrain rectangulaire de blocs
 * (vides, obstacles, fourmilière) parcouru par des fourmis.
 */
public class Monde implements TerrainDeJeu{

	/** Nombre maximal de tirages de direction par fourmi et par pas.
	 *  Évite une boucle infinie quand une fourmi est entourée d'obstacles. */
	private static final int MAX_TENTATIVES = 64;

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
	 * Chargement du fichier contenant le terrain.
	 * Format : lignes colonnes / nombre de fourmis / grille
	 * (-1 = fourmilière, -2 = obstacle, n >= 0 = nourriture).
	 */
	public void chargement(String chemin) throws Exception{
		try (BufferedReader in = new BufferedReader(new FileReader(chemin))) {
			StreamTokenizer st = new StreamTokenizer(in);
			int tokenType = st.nextToken();
			// Définition lignes, colonnes, nombre de fourmis
			int i = 0;
			while (tokenType == StreamTokenizer.TT_NUMBER && i < 3) {
				if (i == 0) lignes = (int) st.nval;
				if (i == 1) {
					colonnes = (int) st.nval;
					terrain = new Bloc[lignes][colonnes];
				}
				if (i == 2) fourmis = (int) st.nval;
				tokenType = st.nextToken();
				i++;
			}
			// Création du monde
			int x_fourm = 0, y_fourm = 0; // Position de la fourmilière
			for (int j = 0; j < lignes; j++) {
				for (int k = 0; k < colonnes; k++) {
					if (tokenType == StreamTokenizer.TT_NUMBER) {
						int val = (int) st.nval;
						if (val == -1) { terrain[j][k] = new Fourmiliere(); x_fourm = j; y_fourm = k; }
						else if (val == -2) terrain[j][k] = new Obstacle();
						else { terrain[j][k] = new Vide(); terrain[j][k].setnourriture(val); }
						tokenType = st.nextToken();
					}
				}
			}
			// Création des fourmis à la position (x_fourm, y_fourm)
			terrain[x_fourm][y_fourm].setfourmis(fourmis);
			fourmi = new Fourmi[fourmis];
			for (i = 0; i < fourmis; i++) fourmi[i] = new Fourmi(x_fourm, y_fourm);
		}
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
		for (Fourmi f : fourmi) {
			int xtmp = f.getx();
			int ytmp = f.gety();
			terrain[xtmp][ytmp].mvtfourmi(false);
			f.deplacerrand();
			int tentatives = 0;
			while (!positionValide(f)) {
				f.setx(xtmp);
				f.sety(ytmp);
				if (++tentatives >= MAX_TENTATIVES) break; // Fourmi bloquée : elle reste sur place
				f.deplacerrand();
			}
			terrain[f.getx()][f.gety()].mvtfourmi(true);
		}
	}

	/** Indique si la position courante de la fourmi est dans le terrain et franchissable. */
	private boolean positionValide(Fourmi f) {
		int x = f.getx(), y = f.gety();
		return x >= 0 && x < lignes && y >= 0 && y < colonnes
				&& terrain[x][y].getpossibility();
	}

}
