package simengine;

import java.awt.Graphics;

/**
 * 
 * *** PROJET FORUMIS *** 
 * 
 * Un terrain est un espace rectangulaire constitu� de blocs, dimensionn� par un nombre de 
 * lignes et de colonnes (de blocs) fixes, contenant des des fourmis, des obstacles, de la
 * nourriture, une fourmilli�re.
 * 
 * Cette interface d�finit les m�thodes minimales que u'ilise un objet TerrainDeJeu pour 
 * afficher et faire �voluer le terrain.
 *  
 * @author Justin Templemore-Finlayson
 * @author justin@umlteacher.eu
 * @author D�cembre 2006
 */
public interface TerrainDeJeu 
{
	/**
	 * Retourne le nombre de colonnes dans le terrain.
	 */
	public abstract int getColonnes();
	/**
	 * Retourne le nombre de lignes dans le terrain.
	 */
	public abstract int getLignes();
	/**
	 * Dessine un bloc du terrain ayant les coordonn�es (ligne;colonne) donn�es en param�tre.
	 * Le bloc identifi� par (0;0) est en haut � gauche du terrain.
	 * Le contexte graphique donn� en param�tre est un espace sur l'affichage 
	 * <strong>d�di� au bloc<strong>, dans lequel il doit se dessiner.
	 */
	public abstract void dessinerBloc (int ligne, int colonne, Graphics g);
	/**
	 * Cette m�thode fait �voluer le terrain par une it�ration de simulation.
	 * Chaque bloc du terrain doit �tre mis � jour (si n�cessaire), ainsi que les fourmis, qui
	 * doivent chacun se d�placer et effectuer leurs diverses actions de fourmi.
	 */
	public abstract void step ();
}
