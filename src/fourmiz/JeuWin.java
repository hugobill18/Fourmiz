package fourmiz;


import java.awt.*;
import java.io.File;
import java.util.Hashtable;

import simengine.JeuPanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

import java.awt.event.*;

import javax.swing.filechooser.FileFilter;

/**
 * Interface graphique de la simulation Fourmiz.
 *
 * @author Justin Templemore-Finlayson
 * @author justin@umlteacher.eu
 * Décembre 2006
 */
public class JeuWin implements ActionListener,ChangeListener
{
	private static final int DELAI_MAX = 500; // ms ; le curseur va du plus lent au plus rapide
	private static final String TERRAIN_DEFAUT = "terrain.dat";

	JeuPanel jp;
	Monde m;
	JButton start,stop,step, charge;
	JPanel command;
	JSlider cursor;
	JCheckBox grille;
	JFileChooser chooser;
	private JLabel statut;
	private String nomTerrain = TERRAIN_DEFAUT;

	public JeuWin(){
		JFrame win = new JFrame("Fourmiz — Simulation de fourmis");
		m = new Monde(TERRAIN_DEFAUT);				// Créer un objet de type monde qui implémente terrain de Jeu
		jp = new JeuPanel(m);						// Créer le jeu

		start=new JButton("▶ Démarrer");
		start.setToolTipText("Lancer l'évolution automatique");
		start.addActionListener(this);
		stop=new JButton("⏸ Arrêter");
		stop.setToolTipText("Suspendre l'évolution");
		stop.setEnabled(false);
		stop.addActionListener(this);
		step=new JButton("⏭ Pas à pas");
		step.setToolTipText("Exécuter un seul pas de simulation");
		step.addActionListener(this);
		charge=new JButton("📂 Charger…");
		charge.setToolTipText("Charger un fichier de terrain (.dat)");
		charge.addActionListener(this);

		grille=new JCheckBox("Grille");
		grille.setSelected(true);
		grille.setToolTipText("Afficher ou masquer la grille des blocs");
		grille.addActionListener(this);

		cursor = new JSlider(JSlider.HORIZONTAL,0, DELAI_MAX, DELAI_MAX/2);
		cursor.setMajorTickSpacing(100);
		cursor.setMinorTickSpacing(25);
		cursor.setPaintTicks(true);
		Hashtable<Integer, JLabel> etiquettes = new Hashtable<>();
		etiquettes.put(0, new JLabel("Lent"));
		etiquettes.put(DELAI_MAX, new JLabel("Rapide"));
		cursor.setLabelTable(etiquettes);
		cursor.setPaintLabels(true);
		cursor.setToolTipText("Vitesse de la simulation");
		cursor.addChangeListener(this);
		jp.setStepDelay(DELAI_MAX - cursor.getValue());

		JPanel boutons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
		boutons.add(start);
		boutons.add(stop);
		boutons.add(step);
		boutons.add(charge);

		JPanel vitesse = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
		vitesse.add(new JLabel("Vitesse :"));
		vitesse.add(cursor);
		vitesse.add(grille);

		command=new JPanel(new BorderLayout());
		command.setBorder(new EmptyBorder(4, 8, 4, 8));
		command.add(boutons, BorderLayout.WEST);
		command.add(vitesse, BorderLayout.EAST);

		statut = new JLabel();
		statut.setBorder(new EmptyBorder(2, 12, 4, 12));
		jp.setStepListener(this::majStatut);
		majStatut();

		JPanel sud = new JPanel(new BorderLayout());
		sud.add(command, BorderLayout.NORTH);
		sud.add(statut, BorderLayout.SOUTH);

		chooser = new JFileChooser(".");
		FileFilter dat = new FiltreSimple("Fichiers terrain (*.dat)",".dat");
		chooser.addChoosableFileFilter(dat);
		chooser.setFileFilter(dat);

		win.getContentPane().add(jp,BorderLayout.CENTER);
		win.getContentPane().add(sud,BorderLayout.SOUTH);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// Quitter l'appli lors d'un appui sur X
		win.setSize(800,600);								// Etablir les dimensions initiales
		win.setMinimumSize(new Dimension(560, 420));
		win.setLocationRelativeTo(null);					// Centrer la fenêtre
		win.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(start)) { // Clic sur Démarrer
			jp.start();
		}
		if (e.getSource().equals(stop)) { // Clic sur Arrêter
			jp.stop();
		}
		if (e.getSource().equals(step)) { // Clic sur Pas à pas
			jp.stop();
			jp.step();
		}
		if (e.getSource().equals(charge)) { // Clic sur le bouton Charger
			jp.stop();
			int returnVal = chooser.showOpenDialog(charge.getTopLevelAncestor());
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				chargerTerrain(chooser.getSelectedFile());
			}
		}
		if (e.getSource().equals(grille)) { // Clic sur la Checkbox
			jp.setGrilleVisible(grille.isSelected());
		}
		majStatut();
	}

	/**
	 * Charge un nouveau terrain sans corrompre le monde courant : le fichier est
	 * d'abord chargé dans un nouveau Monde, qui ne remplace l'ancien qu'en cas de succès.
	 */
	private void chargerTerrain(File fichier) {
		try {
			Monde nouveau = new Monde(fichier.getAbsolutePath());
			if (nouveau.getLignes() <= 0 || nouveau.getColonnes() <= 0 || nouveau.fourmi == null) {
				throw new IllegalStateException("Terrain vide ou incomplet");
			}
			m = nouveau;
			jp.init(m);
			nomTerrain = fichier.getName();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(charge.getTopLevelAncestor(),
					"Impossible de charger « " + fichier.getName() + " » :\n" + ex.getMessage(),
					"Erreur de chargement", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void majStatut() {
		boolean enCours = jp.isRunning();
		start.setEnabled(!enCours);
		stop.setEnabled(enCours);
		statut.setText("Terrain : " + nomTerrain
				+ "   •   Fourmis : " + (m != null && m.fourmi != null ? m.fourmi.length : 0)
				+ "   •   Pas : " + jp.getStepCount()
				+ (enCours ? "   •   En cours…" : "   •   En pause"));
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(cursor)) { // Déplacement du curseur
			jp.setStepDelay(DELAI_MAX - cursor.getValue()); // Sens normal : du +lent au +rapide
		}
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> {
			try {
				for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (Exception e) {
				// Look and feel par défaut si Nimbus est indisponible
			}
			new JeuWin();
		});
	}

}
