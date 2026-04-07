package fourmiz;

import java.awt.*;
import java.util.Random;

abstract class Bloc{
	Color couleur;
	public int fourmis,nourriture;
	boolean possibility;
	private static Random ranGen = new Random();
	public Bloc(){
		fourmis=0;
	}
	public void dessiner(Graphics g)
	{
		if (g.getClipBounds().width>=0){
			if (nourriture!=0) couleur=Color.GREEN;
			g.setColor(couleur);
			g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
			if(nourriture!= 0)
			{
				g.setColor(Color.BLACK);
				g.drawString(""+nourriture, g.getClipBounds().width/2-10, g.getClipBounds().height/2);
			}
			if (fourmis!=0){
				g.setColor(Color.BLACK);
				int xf,yf;
				//Image im;
				//im = getImage("antv.gif");
				for (int i=0;i<fourmis;i++){
					xf = ranGen.nextInt(g.getClipBounds().width);
					//System.out.println(xf);
					yf = ranGen.nextInt(g.getClipBounds().height);
					g.fillOval(xf, yf, 4, 2);
					//g.drawString(".", xf, yf);
				}
			}
		}
		
	}
	public abstract void setfourmis(int fourm);
	public abstract void setnourriture(int x);
	public boolean getpossibility(){
		return possibility;
	}
	
	public  void mvtfourmi(boolean mvt){
		if (mvt==false) fourmis--; // Fourmi sort du bloc
		else fourmis++; // Fourmi entre dans le bloc
	}
}
