package fourmiz;
import simengine.Utilitaire;

/**
 * Une fourmi, repérée par sa position (x, y) sur le terrain.
 * Elle se déplace d'une case vers l'une des 8 directions voisines,
 * tirée au sort selon un tableau de pondérations.
 */
public class Fourmi {

	/** Déplacements (dx, dy) des 8 directions : NO, N, NE, E, SE, S, SO, O. */
	private static final int[][] DIRECTIONS = {
		{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}
	};

	private static final int POIDS_INITIAL = 12;

	int x,y; // Positionnement de la fourmi
	boolean nourriture; // Transporte ou non de la nourriture
	int[] poids = {12,12,12,12,12,12,12,12};

	public Fourmi(int x,int y)
	{
		this.x=x;
		this.y=y;
		nourriture=false;
	}

	/** Déplace la fourmi d'une case dans une direction aléatoire pondérée. */
	public void deplacerrand()
	{
		int rand=Utilitaire.randomPondere(poids);
		setxy(rand);
		setpoids(rand);
	}

	private void setxy(int rand)
	{
		x += DIRECTIONS[rand][0];
		y += DIRECTIONS[rand][1];
	}

	/** Réinitialise les pondérations après un déplacement dans la direction donnée. */
	private void setpoids(int direction)
	{
		for (int i = 0; i < poids.length; i++) poids[i] = POIDS_INITIAL;
	}

	public void setx(int x){
		this.x=x;
	}
	public void sety(int y){
		this.y=y;
	}
	public int getx(){
		return x;
	}
	public int gety(){
		return y;
	}
}
