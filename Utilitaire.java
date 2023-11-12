package simengine;

import java.util.Random;

/**
 * *** PROJET FOURMIS ***
 *
 * Cette classe fournit un ensemble de m�thodes utilitaires pour le bon fonctionnement
 * de votre projet. 
 *
 * @author Justin Templemore-Finlayson
 * @author justin@umlteacher.eu
 * 28 d�c. 06
 */
public class Utilitaire 
{
	private static Random ranGen = new Random();
	
	/**
	 * Cette m�thode renvoi une valeur al�atoire dans l'intervalle [0..<code>poids.length-1</code>].
	 * La g�n�ration de la caleur utilise des pond�rations qui sont d�finies par le tableau poids, 
	 * re�u en param�tre. 
	 * 
	 * Exemple: Pour un tableau poids = { 1 1 10 1 }, la valeur "0" � un poids �gal � "1", 
	 * la valeur "2" � un poids �gale � "10", etc. La valeur retourn�e a donc 1 chance sur 13
	 * d'�tre "0", "1" ou "3", et 10 chances sur 13 d'�tre "2".
	 */
	public static int randomPondere (int[] poids)
	{
		int somme = 0;
		for (int i=0; i<poids.length; i++) somme += poids[i];
		int instance = ranGen.nextInt(somme);
		int cumul = 0;
		int valeur = 0;
		for (valeur=0; valeur<poids.length; valeur++)
		{
			cumul += poids[valeur];
			if (instance < cumul) break;
		}
		return valeur;
	}
}
