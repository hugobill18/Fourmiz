package simengine;

import java.awt.Graphics;

/**
 * 
 * *** PROJET FORUMIS *** 
 * 
 * Un terrain est un espace rectangulaire constitué de blocs, dimensionné par un nombre de 
 * lignes et de colonnes (de blocs) fixes, contenant des des fourmis, des obstacles, de la
 * nourriture, une fourmillière.
 * 
 * Cette interface définit les méthodes minimales que u'ilise un objet TerrainDeJeu pour 
 * afficher et faire évoluer le terrain.
 *  
 * @author Justin Templemore-Finlayson
 * @author justin@umlteacher.eu
 * @author Décembre 2006
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
	 * Dessine un bloc du terrain ayant les coordonnées (ligne;colonne) données en paramètre.
	 * Le bloc identifié par (0;0) est en haut à gauche du terrain.
	 * Le contexte graphique donné en paramètre est un espace sur l'affichage 
	 * <strong>dédié au bloc<strong>, dans lequel il doit se dessiner.
	 */
	public abstract void dessinerBloc (int ligne, int colonne, Graphics g);
	/**
	 * Cette méthode fait évoluer le terrain par une itération de simulation.
	 * Chaque bloc du terrain doit être mis à jour (si nécessaire), ainsi que les fourmis, qui
	 * doivent chacun se déplacer et effectuer leurs diverses actions de fourmi.
	 */
	public abstract void step ();
}
