package simengine;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * *** PROJET FOURMIS ***
 *
 * La classe JeuPanel implémente le moteur de simulation du projet fourmis.
 * Elle encadre deux activités qui sont délèguées à un objet TerrainDeJeu:
 *   (1) Afficher les blocs du terrain
 *   (2) Faire évoluer le terrain et ses fourmis.
 *   
 * (1) Affichage
 * La classe est un JPanel qui peut s'afficher dans une interface Java/Swing. 
 * Elle calcule la position de chaque bloc du terrain, et délègue l'affichage de chaque bloc
 * à TerrainDeJeu.
 * 
 * (2) Evolution
 * La classe utilise un Thread pour exécuter régulièrement une évolution du terrain, dont 
 * l'implémentation doit être fournit par TerrainDeJeu. L'évolution est 
 * consideré comme un séquence de "steps" (pas), espacés par un "stepDelay". 
 * L'exécution des pas est controlé par les méthodes start(), stop(), step() et 
 * setStepDelay(int). 
 *
 * @author Justin Templemore-Finlayson
 * @author justin@umlteacher.eu
 * Décembre 2006
 */
public class JeuPanel extends JPanel implements Runnable
{
	// données du terrain
	private TerrainDeJeu terrain;	// le terrain à afficher et à faire évoluer
	private int lignes;				// stockée pour raisons de performance
	private int colonnes;			// stockée pour raisons de performance
	
	// données d'affichage
	private boolean grilleVisible;
	
	// données de contrôle du Thread d'évolution
	private int stepDelay;
	private int stepCount = 0;
	private boolean stepActive = false;
	private Thread stepThread = null;

	/* ********************************** INITIALISATION *****************************************/
	/**
	 * Initialise le Jeu sans Terrain. Toute action par la suite n'aura pas d'effet, tant que un
	 * Terrain n'a pas été fournie.
	 */
	public JeuPanel ()
	{
		this.grilleVisible = true;
		this.stepDelay = 100;
	}
	/**
	 * Initialise le Jeu avec le TerrainDeJeu à afficher
	 * @param terrain
	 */
	public JeuPanel (TerrainDeJeu terrain)
	{
		this.grilleVisible = true;
		this.stepDelay = 100;
		init (terrain);
	}
	/**
	 * (Re)initialise le Jeu avec un nouveau TerrainDeJeu. Cette méthode ne fait rien si le
	 * Jeu est en cours d'évolution. L'évolution peut être arreté par un appel à stop().
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
	 * Code du Thread qui contrôle les pas de l'évolution. Un pas est exécutée toutes les 
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
	 * (Re)démarre le thread d'évolution, si arreté. 
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
	 * Arrête le thread d'évolution, si démarré.
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
	 * Execute un pas de l'évolution, si l'évolution n'est as active 
	 * (en cours d'exécution automatique). 
	 */
	public final void step()
	{
		if (!stepActive) stepBody();
	}
	/*
	 * Effectue le véritable exécution d'un pas. 
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
	 * Modifie le delai entre les pas d'évolution (millisecondes)
	 * @param stepDelay
	 */
	public synchronized void setStepDelay (int stepDelay)
	{
		this.stepDelay = stepDelay;
	}
	/**
	 * Récupère le delai entre les pas d'évolution (millisecondes)
	 * @return
	 */
	public synchronized int getStepDelay ()
	{
		return stepDelay;
	}
	/**
	 * Récupère le nombre de pas exécutées depuis la dernière initialisation du terrain
	 * @return
	 */
	public int getStepCount() 
	{
		return stepCount;
	}
	
	/* ********************************* AFFICHAGE *****************************************/
	
	/*
	 * Héritée de JPanel 
	 */
	public void paintComponent (Graphics g)
	{
		int displayWidth = getWidth();
		int displayHeight = getHeight();
		if (terrain == null)
		{
			String text = "Pas de terrain initialisé";
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
	
			// Déléguer l'affichage de chaque bloc à TerrainDeJeu
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
	
			// Déssiner la bordure du terrain
			g.setColor(Color.BLACK);
			g.drawRect(xStart, yStart, terrainWidth, terrainHeight);
		}
	}

	/**
	 * Active / Désactive l'affichage de la grille des blocs.
	 * @param grilleVisible
	 */
	public void setGrilleVisible(boolean grilleVisible)
	{
		this.grilleVisible = grilleVisible;
	}

}
