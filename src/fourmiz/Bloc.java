package fourmiz;

import java.awt.*;
import java.util.Random;

/**
 * Un bloc du terrain. Chaque bloc connaît sa couleur de fond, le nombre de
 * fourmis présentes, la quantité de nourriture, et s'il est franchissable.
 */
abstract class Bloc{

	private static final Color COULEUR_NOURRITURE = new Color(0x7CB342);
	private static final Color COULEUR_FOURMI = new Color(0x3E2723);

	Color couleur;
	public int fourmis,nourriture;
	boolean possibility;

	public Bloc(){
		fourmis=0;
	}

	public void dessiner(Graphics g)
	{
		Rectangle clip = g.getClipBounds();
		if (clip == null || clip.width <= 0 || clip.height <= 0) return;
		int w = clip.width, h = clip.height;

		Graphics2D g2 = (g instanceof Graphics2D) ? (Graphics2D) g : null;
		if (g2 != null) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}

		// Fond du bloc (vert si de la nourriture est présente, sans écraser la couleur propre du bloc)
		Color fond = (nourriture != 0) ? COULEUR_NOURRITURE : couleur;
		if (fond != null) {
			g.setColor(fond);
			g.fillRect(0, 0, w, h);
		}

		// Quantité de nourriture, centrée dans le bloc
		if (nourriture != 0)
		{
			String texte = String.valueOf(nourriture);
			FontMetrics fm = g.getFontMetrics();
			g.setColor(Color.WHITE);
			g.drawString(texte, (w - fm.stringWidth(texte)) / 2,
					(h + fm.getAscent() - fm.getDescent()) / 2);
		}

		// Fourmis : positions pseudo-aléatoires mais stables entre deux
		// rafraîchissements (graine fixe), pour éviter le scintillement
		if (fourmis > 0){
			g.setColor(COULEUR_FOURMI);
			Random posGen = new Random(fourmis * 31L + 7);
			int corpsW = Math.max(4, w / 8);
			int corpsH = Math.max(2, h / 12);
			for (int i=0;i<fourmis;i++){
				int xf = posGen.nextInt(Math.max(1, w - corpsW));
				int yf = posGen.nextInt(Math.max(1, h - corpsH));
				g.fillOval(xf, yf, corpsW, corpsH);
			}
		}
	}

	public abstract void setfourmis(int fourm);
	public abstract void setnourriture(int x);

	public boolean getpossibility(){
		return possibility;
	}

	/**
	 * Enregistre le passage d'une fourmi : entrée (true) ou sortie (false) du bloc.
	 * Le compteur ne descend jamais en dessous de zéro.
	 */
	public  void mvtfourmi(boolean mvt){
		if (mvt) fourmis++; // Fourmi entre dans le bloc
		else if (fourmis > 0) fourmis--; // Fourmi sort du bloc
	}
}
