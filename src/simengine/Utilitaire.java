package simengine;

import java.util.Random;

/**
 * *** PROJET FOURMIS ***
 *
 * Cette classe fournit un ensemble de méthodes utilitaires pour le bon fonctionnement
 * de votre projet. 
 *
 * @author Justin Templemore-Finlayson
 * @author justin@umlteacher.eu
 * 28 déc. 06
 */
public class Utilitaire 
{
	private static Random ranGen = new Random();
	
	/**
	 * Cette méthode renvoi une valeur aléatoire dans l'intervalle [0..<code>poids.length-1</code>].
	 * La génération de la caleur utilise des pondérations qui sont définies par le tableau poids, 
	 * reçu en paramètre. 
	 * 
	 * Exemple: Pour un tableau poids = { 1 1 10 1 }, la valeur "0" à un poids égal à "1", 
	 * la valeur "2" à un poids égale à "10", etc. La valeur retournée a donc 1 chance sur 13
	 * d'être "0", "1" ou "3", et 10 chances sur 13 d'être "2".
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
