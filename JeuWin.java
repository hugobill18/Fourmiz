package fourmiz;


import java.awt.*;
import simengine.JeuPanel;
import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;

import javax.swing.filechooser.FileFilter;

/**
 * Une simple interface de jeu, � am�liorer.
 *
 * @author Justin Templemore-Finlayson
 * @author justin@umlteacher.eu
 * D�cembre 2006
 */
public class JeuWin implements ActionListener,ChangeListener
{
	JeuPanel jp;
	Monde m;
	JButton start,stop,step, charge;
	JPanel command;
	JSlider cursor;
	JCheckBox grille;
	public JeuWin(){
		JFrame.setDefaultLookAndFeelDecorated(true);	// Etiliser decorations Swing
		JFrame win = new JFrame("Fourmiz");				// Cr�er la fen�tre avec titre
		m = new Monde("terrain.dat"); 					// Cr�er un objet de type monde qui impl�mente terrain de Jeu
		jp = new JeuPanel(m);							// Cr�er le jeu
		
		start=new JButton("Demarrer");
		start.addActionListener(this);
		stop=new JButton("Arreter");
		stop.addActionListener(this);
		step=new JButton("Pas � pas");
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
		
		win.getContentPane().add(jp,BorderLayout.CENTER);
		win.getContentPane().add(command,BorderLayout.SOUTH);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// Quitter l'appui lors d'un appui sur X
		win.setSize(800,600);								// Etablir les dimensions initiales
		win.setLocationRelativeTo(null);					// Centrer la fen�tre
		win.setVisible(true);								// Afficher la fen�tre
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(start)) { // Clic sur Demarrer
			jp.start();
		}
		if (e.getSource().equals(stop)) { // Clic sur Arreter
			jp.stop();
		}
		if (e.getSource().equals(step)) { // Clic sur Pas � pas
			jp.stop();
			jp.step();
		}
		if (e.getSource().equals(charge)) { // Clic sur le boutton charger
			jp.stop();
		    JFileChooser chooser = new JFileChooser("../fourmiz");
		    // Note: source for ExampleFileFilter can be found in FileChooserDemo,
		    // under the demo/jfc directory in the JDK.
		    FileFilter dat = new FiltreSimple("Fichiers dat",".dat");
		    chooser.addChoosableFileFilter(dat);
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
		if (e.getSource().equals(cursor)) { // D�placement du curseur
			jp.setStepDelay(500 - cursor.getValue()); // Sens normal : du +lent au +rapide
		}
		
	}
	public static void main(String[] args)  
	{
		new JeuWin();
		
	}
	


}