package fourmiz;

/**
 * Affichage terminal du jeu Fourmiz.
 * Remplace la fenêtre Swing (JeuWin) par un rendu ANSI dans le terminal.
 *
 * Usage:
 *   java -cp ... fourmiz.TerminalJeu [fichier.dat] [delai_ms]
 *
 * Exemples:
 *   fourmiz.TerminalJeu                     → terrain.dat, 250 ms/pas
 *   fourmiz.TerminalJeu terrain2.dat 100    → rapide
 *   fourmiz.TerminalJeu terrain.dat 0       → aussi vite que possible
 */
public class TerminalJeu {

    // ── Codes ANSI ────────────────────────────────────────────────────────────
    static final String RESET    = "\033[0m";
    static final String BOLD     = "\033[1m";
    static final String CLEAR    = "\033[H\033[2J";   // aller en (0,0) + effacer
    static final String BG_RED   = "\033[41;97m";     // Fourmiliere → fond rouge, texte blanc
    static final String BG_GRAY  = "\033[100;37m";    // Obstacle    → fond gris foncé
    static final String BG_GREEN = "\033[42;30m";     // Nourriture  → fond vert, texte noir
    static final String BG_WHITE = "\033[47;30m";     // Vide        → fond blanc, texte noir
    static final String FG_CYAN  = "\033[96m";
    static final String FG_YELLOW= "\033[93m";

    // Largeur d'une cellule (en caractères)
    static final int CELL_W = 6;

    // ── Rendu d'une frame ────────────────────────────────────────────────────

    static void render(Monde m, int step, int delayMs) {
        StringBuilder sb = new StringBuilder();
        sb.append(CLEAR);

        // En-tête
        sb.append(BOLD).append(FG_CYAN)
          .append("  ╔══════════════════════════╗\n")
          .append("  ║     F O U R M I Z        ║\n")
          .append("  ╚══════════════════════════╝\n")
          .append(RESET);
        sb.append(String.format("  Pas : %s%d%s   Fourmis : %s%d%s   Vitesse : %s%d ms%s%n",
                BOLD, step, RESET,
                FG_YELLOW, m.fourmis, RESET,
                FG_YELLOW, delayMs, RESET));
        sb.append("  Ctrl+C pour quitter\n\n");

        // En-tête colonnes
        sb.append("       ");
        for (int c = 0; c < m.getColonnes(); c++) {
            sb.append(String.format(" col%-2d ", c));
        }
        sb.append("\n");

        // Séparateur haut
        sb.append("      +");
        for (int c = 0; c < m.getColonnes(); c++) sb.append("------+");
        sb.append("\n");

        // Lignes du terrain
        for (int r = 0; r < m.getLignes(); r++) {
            sb.append(String.format(" lig%d |", r));
            for (int c = 0; c < m.getColonnes(); c++) {
                Bloc b = m.terrain[r][c];
                sb.append(cellStr(b)).append(RESET).append("|");
            }
            sb.append("\n");

            // Séparateur bas de ligne
            sb.append("      +");
            for (int c = 0; c < m.getColonnes(); c++) sb.append("------+");
            sb.append("\n");
        }

        // Légende
        sb.append("\n  ")
          .append(BG_RED).append("  F   ").append(RESET).append(" Fourmiliere  ")
          .append(BG_GRAY).append(" ### ").append(RESET).append(" Obstacle  ")
          .append(BG_GREEN).append(" nnn ").append(RESET).append(" Nourriture  ")
          .append(BG_WHITE).append(" *n  ").append(RESET).append(" Vide (*n = n fourmis)\n");

        System.out.print(sb);
        System.out.flush();
    }

    /** Contenu coloré d'une cellule (sans le RESET final). */
    static String cellStr(Bloc b) {
        if (b instanceof Fourmiliere) {
            String txt = b.fourmis > 0 ? "F+" + b.fourmis : "  F ";
            return BG_RED + String.format(" %-4s ", txt);
        }
        if (b instanceof Obstacle) {
            return BG_GRAY + " #### ";
        }
        if (b.nourriture > 0) {
            String txt = b.fourmis > 0
                    ? b.nourriture + "*" + b.fourmis
                    : String.valueOf(b.nourriture);
            return BG_GREEN + String.format(" %-4s ", txt);
        }
        // Case vide
        String txt = b.fourmis > 0 ? "*" + b.fourmis : "";
        return BG_WHITE + String.format(" %-4s ", txt);
    }

    // ── Point d'entrée ───────────────────────────────────────────────────────

    public static void main(String[] args) throws InterruptedException {
        String fichier = args.length > 0 ? args[0] : "terrain.dat";
        int delayMs    = args.length > 1 ? Integer.parseInt(args[1]) : 250;

        Monde m = new Monde(fichier);
        render(m, 0, delayMs);

        int step = 0;
        while (true) {
            if (delayMs > 0) Thread.sleep(delayMs);
            m.step();
            step++;
            render(m, step, delayMs);
        }
    }
}
