package fourmiz;


import java.awt.*;
import simengine.JeuPanel;
import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;

import javax.swing.filechooser.FileFilter;

/**
 * Une simple interface de jeu, à améliorer.
 *
 * @author Justin Templemore-Finlayson
 * @author justin@umlteacher.eu
 * Décembre 2006
 */
public class JeuWin implements ActionListener,ChangeListener
{
	JeuPanel jp;
	Monde m;
	JButton start,stop,step, charge;
	JPanel command;
	JSlider cursor;
        JCheckBox grille;
        JFileChooser chooser;
        public JeuWin(){
		JFrame.setDefaultLookAndFeelDecorated(true);	// Etiliser decorations Swing
		JFrame win = new JFrame("Fourmiz");				// Créer la fenêtre avec titre
		m = new Monde("terrain.dat"); 					// Créer un objet de type monde qui implémente terrain de Jeu
		jp = new JeuPanel(m);							// Créer le jeu
		
		start=new JButton("Demarrer");
		start.addActionListener(this);
		stop=new JButton("Arreter");
		stop.addActionListener(this);
		step=new JButton("Pas à pas");
		step.addActionListener(this);
		charge=new JButton("Charger");
		charge.addActionListener(this);
		grille=new JCheckBox("Afficher la grille");
		grille.setSelected(true);
		grille.addActionListener(this);
		cursor = new JSlider(JSlider.HORIZONTAL,0, 500, 250);
		cursor.setMajorTickSpacing(20);
		cursor.setMinorTickSpacing(5);
		cursor.addChangeListener(this);
		command=new JPanel();
		command.add(start);
		command.add(stop);
		command.add(step);
		command.add(cursor);
		command.add(grille);
                command.add(charge);

                chooser = new JFileChooser("../fourmiz");
                FileFilter dat = new FiltreSimple("Fichiers dat",".dat");
                chooser.addChoosableFileFilter(dat);

                win.getContentPane().add(jp,BorderLayout.CENTER);
		win.getContentPane().add(command,BorderLayout.SOUTH);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// Quitter l'appui lors d'un appui sur X
		win.setSize(800,600);								// Etablir les dimensions initiales
		win.setLocationRelativeTo(null);					// Centrer la fenêtre
                win.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(start)) { // Clic sur Demarrer
			jp.start();
		}
		if (e.getSource().equals(stop)) { // Clic sur Arreter
			jp.stop();
		}
		if (e.getSource().equals(step)) { // Clic sur Pas à pas
			jp.stop();
			jp.step();
		}
                if (e.getSource().equals(charge)) { // Clic sur le boutton charger
                        jp.stop();
                    int returnVal = chooser.showOpenDialog(null);
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                       try {
		    	 
				m.chargement(chooser.getSelectedFile().getName());
				jp.init(m);
				} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    }
		}
		if (e.getSource().equals(grille)) { // Clic sur la Checkbox
			if (grille.isSelected()) jp.setGrilleVisible(true);
			else jp.setGrilleVisible(false);
			jp.repaint();
		}
		
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(cursor)) { // Déplacement du curseur
			jp.setStepDelay(500 - cursor.getValue()); // Sens normal : du +lent au +rapide
		}
		
	}
	public static void main(String[] args)  
	{
		new JeuWin();
		
	}
	


}
