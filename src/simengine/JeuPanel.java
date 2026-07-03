package simengine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
	private static final Color COULEUR_FOND = new Color(0x2B2B2B);
	private static final Color COULEUR_GRILLE = new Color(0, 0, 0, 28);
	private static final Color COULEUR_BORDURE = new Color(0x4E342E);
	private static final int MARGE = 10;

	// données du terrain
	private TerrainDeJeu terrain;	// le terrain à afficher et à faire évoluer
	private int lignes;				// stockée pour raisons de performance
	private int colonnes;			// stockée pour raisons de performance

	// données d'affichage
	private boolean grilleVisible;

	// données de contrôle du Thread d'évolution
	private volatile int stepDelay;
	private volatile int stepCount = 0;
	private volatile boolean stepActive = false;
	private Thread stepThread = null;

	// notification (sur l'EDT) après chaque pas exécuté
	private Runnable stepListener = null;

	/* ********************************** INITIALISATION *****************************************/
	/**
	 * Initialise le Jeu sans Terrain. Toute action par la suite n'aura pas d'effet, tant que un
	 * Terrain n'a pas été fournie.
	 */
	public JeuPanel ()
	{
		this.grilleVisible = true;
		this.stepDelay = 100;
		setBackground(COULEUR_FOND);
	}
	/**
	 * Initialise le Jeu avec le TerrainDeJeu à afficher
	 * @param terrain
	 */
	public JeuPanel (TerrainDeJeu terrain)
	{
		this();
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
			notifierPas();
			repaint();
		}
	}

	/* ************************************* EVOLUTION **************************************/

	/*
	 * Code du Thread qui contrôle les pas de l'évolution. Un pas est exécutée toutes les
	 * <code>stepDelay</code> millisecondes.
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
				return; // Arrêt demandé : ne pas exécuter de pas supplémentaire
			}
			if (stepActive) this.stepBody();
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
			stepThread = new Thread(this, "evolution-terrain");
			stepThread.setDaemon(true);
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
			stepCount++;
			this.repaint();
			notifierPas();
		}
	}
	/**
	 * Modifie le delai entre les pas d'évolution (millisecondes)
	 * @param stepDelay
	 */
	public void setStepDelay (int stepDelay)
	{
		this.stepDelay = Math.max(0, stepDelay);
	}
	/**
	 * Récupère le delai entre les pas d'évolution (millisecondes)
	 * @return
	 */
	public int getStepDelay ()
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
	/**
	 * Indique si l'évolution automatique est en cours.
	 */
	public boolean isRunning()
	{
		return stepActive;
	}
	/**
	 * Enregistre un écouteur appelé (sur le thread d'affichage Swing) après chaque pas.
	 */
	public void setStepListener (Runnable stepListener)
	{
		this.stepListener = stepListener;
	}
	private void notifierPas ()
	{
		Runnable l = stepListener;
		if (l != null) SwingUtilities.invokeLater(l);
	}

	/* ********************************* AFFICHAGE *****************************************/

	/*
	 * Héritée de JPanel
	 */
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		int displayWidth = getWidth();
		int displayHeight = getHeight();
		if (g instanceof Graphics2D)
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (terrain == null || lignes <= 0 || colonnes <= 0)
		{
			String text = "Pas de terrain initialisé";
			g.setColor(Color.LIGHT_GRAY);
			int textW = (int)(g.getFontMetrics().getStringBounds(text,g).getWidth());
			int textH = (int)(g.getFontMetrics().getStringBounds(text,g).getHeight());
			g.drawString(text, (displayWidth-textW)/2, (displayHeight-textH)/2+textH);
		}
		else
		{
			int blockWidth = (displayWidth-MARGE) / colonnes;
			int blockHeight = (displayHeight-MARGE) / lignes;
			if (blockWidth <= 0 || blockHeight <= 0) return; // Fenêtre trop petite

			int terrainWidth = blockWidth * colonnes;
			int terrainHeight = blockHeight * lignes;

			int xStart = (displayWidth - terrainWidth) / 2;
			int yStart = (displayHeight - terrainHeight) / 2;

			// Dessiner le fond
			g.setColor(Color.WHITE);
			g.fillRect(xStart, yStart, terrainWidth, terrainHeight);

			// Déléguer l'affichage de chaque bloc à TerrainDeJeu
			for (int row=0; row<lignes; row++)
				for (int col=0; col<colonnes; col++)
				{
					Graphics graphicsForBlock = g.create (xStart+col*blockWidth, yStart+row*blockHeight, blockWidth, blockHeight);
					try {
						terrain.dessinerBloc(row,col,graphicsForBlock);
					} finally {
						graphicsForBlock.dispose();
					}
				}

			// (Facultatif) super-imposer l'affichage d'une grille
			if (grilleVisible)
			{
				g.setColor(COULEUR_GRILLE);
				for (int i = 1; i < colonnes; i++ )
					g.drawLine(xStart+i*blockWidth, yStart, xStart+i*blockWidth, yStart+terrainHeight);
				for (int i = 1; i < lignes; i++ )
					g.drawLine(xStart, yStart+i*blockHeight, xStart+terrainWidth, yStart+i*blockHeight);
			}

			// Déssiner la bordure du terrain
			g.setColor(COULEUR_BORDURE);
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
		repaint();
	}

}
