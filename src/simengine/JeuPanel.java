package simengine;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * *** PROJET FOURMIS ***
 *
 * La classe JeuPanel impl�mente le moteur de simulation du projet fourmis.
 * Elle encadre deux activit�s qui sont d�l�gu�es � un objet TerrainDeJeu:
 *   (1) Afficher les blocs du terrain
 *   (2) Faire �voluer le terrain et ses fourmis.
 *   
 * (1) Affichage
 * La classe est un JPanel qui peut s'afficher dans une interface Java/Swing. 
 * Elle calcule la position de chaque bloc du terrain, et d�l�gue l'affichage de chaque bloc
 * � TerrainDeJeu.
 * 
 * (2) Evolution
 * La classe utilise un Thread pour ex�cuter r�guli�rement une �volution du terrain, dont 
 * l'impl�mentation doit �tre fournit par TerrainDeJeu. L'�volution est 
 * consider� comme un s�quence de "steps" (pas), espac�s par un "stepDelay". 
 * L'ex�cution des pas est control� par les m�thodes start(), stop(), step() et 
 * setStepDelay(int). 
 *
 * @author Justin Templemore-Finlayson
 * @author justin@umlteacher.eu
 * D�cembre 2006
 */
public class JeuPanel extends JPanel implements Runnable
{
	// donn�es du terrain
	private TerrainDeJeu terrain;	// le terrain � afficher et � faire �voluer
	private int lignes;				// stock�e pour raisons de performance
	private int colonnes;			// stock�e pour raisons de performance
	
	// donn�es d'affichage
	private boolean grilleVisible;
	
	// donn�es de contr�le du Thread d'�volution
	private int stepDelay;
	private int stepCount = 0;
	private boolean stepActive = false;
	private Thread stepThread = null;

	/* ********************************** INITIALISATION *****************************************/
	/**
	 * Initialise le Jeu sans Terrain. Toute action par la suite n'aura pas d'effet, tant que un
	 * Terrain n'a pas �t� fournie.
	 */
	public JeuPanel ()
	{
		this.grilleVisible = true;
		this.stepDelay = 100;
	}
	/**
	 * Initialise le Jeu avec le TerrainDeJeu � afficher
	 * @param terrain
	 */
	public JeuPanel (TerrainDeJeu terrain)
	{
		this.grilleVisible = true;
		this.stepDelay = 100;
		init (terrain);
	}
	/**
	 * (Re)initialise le Jeu avec un nouveau TerrainDeJeu. Cette m�thode ne fait rien si le
	 * Jeu est en cours d'�volution. L'�volution peut �tre arret� par un appel � stop().
	 * @param terrain
	 */
	public void init (TerrainDeJeu terrain)
	{
		if (!stepActive)
		{
			this.terrain = terrain;
			this.lignes = terrain.getLignes();
			this.colonnes = terrain.getColonnes();
			stepCount = 0;
			repaint();
		}
	}

	/* ************************************* EVOLUTION **************************************/
	
	/*
	 * Code du Thread qui contr�le les pas de l'�volution. Un pas est ex�cut�e toutes les 
	 * <code>delaiDePas</code> millisecondes.
	 * @see java.lang.Runnable#run()
	 */
	public void run() 
	{
		while (stepActive)
		{
			try 
			{ 
				Thread.sleep(stepDelay); 
			} 
			catch (InterruptedException e) 
			{ 
				stepActive = false; 
			}
			this.stepBody();
		}
	}
	/**
	 * (Re)d�marre le thread d'�volution, si arret�. 
	 */
	public synchronized void start()
	{
		if (!stepActive)
		{
			stepActive = true;
			stepThread = new Thread(this);
			stepThread.start();
		}
	}
	/**
	 * Arr�te le thread d'�volution, si d�marr�.
	 */
	public synchronized void stop()
	{
		if (stepActive)
		{
			stepActive = false;
			stepThread.interrupt();
		}
	}
	/**
	 * Execute un pas de l'�volution, si l'�volution n'est as active 
	 * (en cours d'ex�cution automatique). 
	 */
	public final void step()
	{
		if (!stepActive) stepBody();
	}
	/*
	 * Effectue le v�ritable ex�cution d'un pas. 
	 */
	private synchronized final void stepBody ()
	{
		if (terrain != null) 
		{
			terrain.step();
			this.repaint();
			stepCount++;
		}
	}
	/**
	 * Modifie le delai entre les pas d'�volution (millisecondes)
	 * @param stepDelay
	 */
	public synchronized void setStepDelay (int stepDelay)
	{
		this.stepDelay = stepDelay;
	}
	/**
	 * R�cup�re le delai entre les pas d'�volution (millisecondes)
	 * @return
	 */
	public synchronized int getStepDelay ()
	{
		return stepDelay;
	}
	/**
	 * R�cup�re le nombre de pas ex�cut�es depuis la derni�re initialisation du terrain
	 * @return
	 */
	public int getStepCount() 
	{
		return stepCount;
	}
	
	/* ********************************* AFFICHAGE *****************************************/
	
	/*
	 * H�rit�e de JPanel 
	 */
	public void paintComponent (Graphics g)
	{
		int displayWidth = getWidth();
		int displayHeight = getHeight();
		if (terrain == null)
		{
			String text = "Pas de terrain initialis�";
			int textW = (int)(g.getFontMetrics().getStringBounds(text,g).getWidth());
			int textH = (int)(g.getFontMetrics().getStringBounds(text,g).getHeight());
			g.drawString(text, (displayWidth-textW)/2, (displayHeight-textH)/2+textH);
		} 
		else
		{
			int blockWidth = (displayWidth-10) / colonnes; 
			int blockHeight = (displayHeight-10) / lignes;
			
			int terrainWidth = blockWidth * colonnes;
			int terrainHeight = blockHeight * lignes;
			
			int xStart = (displayWidth - terrainWidth) / 2;
			int yStart = (displayHeight - terrainHeight) / 2;
	
			// Effacer tout
			g.clearRect(0, 0, displayWidth, displayHeight);
	
			// Dessiner le fond
			g.setColor(Color.WHITE);
			g.fillRect(xStart, yStart, terrainWidth, terrainHeight);
	
			// D�l�guer l'affichage de chaque bloc � TerrainDeJeu
			Graphics graphicsForBlock = null;
			for (int row=0; row<lignes; row++)
				for (int col=0; col<colonnes; col++)
				{
					graphicsForBlock = g.create (xStart+col*blockWidth, yStart+row*blockHeight, blockWidth, blockHeight);
					terrain.dessinerBloc(row,col,graphicsForBlock);
				}
	
			// (Facultatif) super-imposer l'affichage d'une grille
			if (grilleVisible)
			{
				g.setColor(Color.LIGHT_GRAY);
				for (int i = 1; i < colonnes; i++ )
					g.drawLine(xStart+i*blockWidth, yStart, xStart+i*blockWidth, yStart+terrainHeight);
				for (int i = 1; i < lignes; i++ )
					g.drawLine(xStart, yStart+i*blockHeight, xStart+terrainWidth, yStart+i*blockHeight);
			}
	
			// D�ssiner la bordure du terrain
			g.setColor(Color.BLACK);
			g.drawRect(xStart, yStart, terrainWidth, terrainHeight);
		}
	}

	/**
	 * Active / D�sactive l'affichage de la grille des blocs.
	 * @param grilleVisible
	 */
	public void setGrilleVisible(boolean grilleVisible)
	{
		this.grilleVisible = grilleVisible;
	}

}
