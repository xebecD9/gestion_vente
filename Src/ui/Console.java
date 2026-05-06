package Src.ui;


public class Console {

    public static void nettoyerEcran() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println(); 
        }
    }

    // ── Réinitialisation ─────────────────────────────────────────
    public static final String RESET  = "\033[0m";

    // ── Styles ────────────────────────────────────────────────────
    public static final String GRAS        = "\033[1m";
    public static final String ITALIQUE    = "\033[3m";
    public static final String SOULIGNE    = "\033[4m";
    public static final String INVERSE     = "\033[7m";
    public static final String ESTOMPE     = "\033[2m";

    // ── Couleurs texte ────────────────────────────────────────────
    public static final String ROUGE    = "\033[91m";   
    public static final String VERT     = "\033[92m";  
    public static final String JAUNE    = "\033[93m";   
    public static final String BLEU     = "\033[94m";   
    public static final String MAGENTA  = "\033[95m";   
    public static final String CYAN     = "\033[96m";   
    public static final String BLANC    = "\033[97m";   
    public static final String GRIS     = "\033[90m";   

    // ── Couleurs sombres ──────────────────────────────────────────
    public static final String ROUGE_S   = "\033[31m";
    public static final String VERT_S    = "\033[32m";
    public static final String JAUNE_S   = "\033[33m";
    public static final String BLEU_S    = "\033[34m";
    public static final String MAGENTA_S = "\033[35m";
    public static final String CYAN_S    = "\033[36m";

    // ── Arrière-plans ─────────────────────────────────────────────
    public static final String BG_ROUGE   = "\033[41m";
    public static final String BG_VERT    = "\033[42m";
    public static final String BG_JAUNE   = "\033[43m";
    public static final String BG_BLEU    = "\033[44m";
    public static final String BG_MAGENTA = "\033[45m";
    public static final String BG_CYAN    = "\033[46m";
    public static final String BG_GRIS    = "\033[100m";

  
    private static final int LARGEUR = 50;

   
    public static void afficherBanniere() {
        String bord  = CYAN + GRAS;
        String texte = BLANC + GRAS;
        String sous  = GRIS + ITALIQUE;

        System.out.println();
        System.out.println(bord + "  ╔══════════════════════════════════════════════╗" + RESET);
        System.out.println(bord + "  ║" + texte + "             AERO SPACE MANAGEMENT SYSTEM  "         + bord + "║" + RESET);
        System.out.println(bord + "  ║" + sous  + "                        D . I . K . W      "             + bord + "║" + RESET);
        System.out.println(bord + "  ║" + GRIS  + "         votre satisfaction,notre priorite "         + bord + "║" + RESET);
        System.out.println(bord + "  ╚══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

   
    public static void afficherTitre(String titre) {
        System.out.println();
        String ligne = construireLigneTitre(titre.toUpperCase(), '─');
        System.out.println(CYAN + GRAS + ligne + RESET);
    }

   
    public static void afficherSousTitre(String titre) {
        System.out.println();
        System.out.println(BLEU + GRAS + "  ▸ " + titre + RESET);
        System.out.println(BLEU + ESTOMPE + "  " + "─".repeat(LARGEUR - 2) + RESET);
    }


    public static void separateur() {
        System.out.println(GRIS + "  " + "─".repeat(LARGEUR - 2) + RESET);
    }

   
    public static void separateurEpais() {
        System.out.println(BLEU + GRAS + "  " + "═".repeat(LARGEUR - 2) + RESET);
    }

    /** Message de succès (vert). */
    public static void afficherSucces(String msg) {
        System.out.println(VERT + GRAS + "  ✔ " + RESET + VERT + msg + RESET);
    }

    /** Message d'erreur (rouge). */
    public static void afficherErreur(String msg) {
        System.out.println(ROUGE + GRAS + "  ✘ " + RESET + ROUGE + msg + RESET);
    }

    /** Message d'information (jaune). */
    public static void afficherInfo(String msg) {
        System.out.println(JAUNE + "  ℹ  " + RESET + ESTOMPE + msg + RESET);
    }

    /** Message d'avertissement (magenta). */
    public static void afficherAvertissement(String msg) {
        System.out.println(MAGENTA + GRAS + "  ⚠ " + RESET + MAGENTA + msg + RESET);
    }

    /** Texte neutre estompé (aide contextuelle, conseils). */
    public static void afficherAide(String msg) {
        System.out.println(GRIS + ITALIQUE + "    " + msg + RESET);
    }

    // ═══════════════════════════════════════════════════════════════
    //  OPTIONS DE MENU
    // ═══════════════════════════════════════════════════════════════

    /**
     * Affiche une option numérotée.
     * @param numero  numéro de l'option (0 = action de sortie)
     * @param icone   icône Unicode (ex : "📦", "👤", "📊")
     * @param texte   libellé de l'option
     */
    public static void afficherOption(int numero, String icone, String texte) {
        if (numero == 0) {
            // Option de sortie : style discret
            System.out.println(GRIS + "  [" + numero + "] " + RESET + ESTOMPE + icone + " " + texte + RESET);
        } else {
            System.out.println(JAUNE + GRAS + "  [" + numero + "] " + RESET + BLANC + icone + " " + texte + RESET);
        }
    }

    /**
     * Surcharge sans icône (rétrocompatibilité).
     */
    public static void afficherOption(int numero, String texte) {
        afficherOption(numero, "•", texte);
    }

    // ═══════════════════════════════════════════════════════════════
    //  SAISIE
    // ═══════════════════════════════════════════════════════════════

    /** Invite de saisie standard. */
    public static void demanderSaisie(String label) {
        System.out.print(CYAN + "  ▶ " + label + " : " + RESET);
    }

    /** Invite de saisie pour mot de passe (masqué visuellement). */
    public static void demanderMotDePasse(String label) {
        System.out.print(MAGENTA + "  🔒 " + label + " : " + RESET);
    }

    // ═══════════════════════════════════════════════════════════════
    //  BADGES DE RÔLE
    // ═══════════════════════════════════════════════════════════════

    /**
     * Affiche un badge coloré selon le rôle (ADMIN / VENDEUR / …).
     */
    public static void afficherBadgeRole(String role) {
        String badge;
        switch (role.toUpperCase()) {
            case "ADMIN":
                badge = BG_ROUGE + BLANC + GRAS + " ⚙ ADMINISTRATEUR " + RESET;
                break;
            case "VENDEUR":
                badge = BG_BLEU + BLANC + GRAS + " 🛒 VENDEUR " + RESET;
                break;
            default:
                badge = BG_GRIS + BLANC + GRAS + " ? " + role.toUpperCase() + " " + RESET;
        }
        System.out.println("  " + badge);
    }

    // ═══════════════════════════════════════════════════════════════
    //  TABLEAUX & DONNÉES
    // ═══════════════════════════════════════════════════════════════

    /**
     * Affiche une ligne de tableau formatée.
     * Exemple : afficherLigneTableau("ID", "12") → │ ID            │ 12           │
     */
    public static void afficherLigneTableau(String cle, String valeur) {
        String k = pad(cle,   16);
        String v = pad(valeur, 26);
        System.out.println(GRIS + "  │ " + RESET + BLANC + k + GRIS + " │ " + RESET + ESTOMPE + v + GRIS + " │" + RESET);
    }

    /** En-tête de tableau. */
    public static void afficherEnTeteTableau(String col1, String col2) {
        String k = pad(col1.toUpperCase(), 16);
        String v = pad(col2.toUpperCase(), 26);
        System.out.println(BLEU + GRAS + "  ┌──────────────────┬────────────────────────────┐" + RESET);
        System.out.println(BLEU + GRAS + "  │ " + RESET + CYAN + GRAS + k + BLEU + GRAS + " │ " + RESET + CYAN + GRAS + v + BLEU + GRAS + " │" + RESET);
        System.out.println(BLEU + GRAS + "  ├──────────────────┼────────────────────────────┤" + RESET);
    }

    /** Pied de tableau. */
    public static void afficherPiedTableau() {
        System.out.println(BLEU + GRAS + "  └──────────────────┴────────────────────────────┘" + RESET);
    }

    // ═══════════════════════════════════════════════════════════════
    //  CHARGEMENT
    // ═══════════════════════════════════════════════════════════════

    /**
     * Affiche une animation de chargement simple (bloquante, démo visuelle).
     * @param message texte affiché pendant le chargement
     * @param ms      durée totale en millisecondes
     */
    public static void afficherChargement(String message, int ms) {
        String[] frames = { "⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏" };
        int steps = Math.max(1, ms / 80);
        try {
            for (int i = 0; i < steps; i++) {
                System.out.print("\r" + CYAN + "  " + frames[i % frames.length] + "  " + message + " " + RESET);
                Thread.sleep(80);
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        System.out.println("\r" + VERT + "  ✔  " + message + RESET + "          ");
    }

    // ═══════════════════════════════════════════════════════════════
    //  UTILITAIRES INTERNES
    // ═══════════════════════════════════════════════════════════════

    private static String construireLigneTitre(String titre, char remplissage) {
        int espaceTotal = LARGEUR - titre.length() - 4; // 4 = "  " + " " + " "
        int gauche = Math.max(1, espaceTotal / 2);
        int droite = Math.max(1, espaceTotal - gauche);
        return "  " + String.valueOf(remplissage).repeat(gauche) + " " + titre + " " + String.valueOf(remplissage).repeat(droite);
    }

    private static String pad(String s, int largeur) {
        if (s == null) s = "";
        if (s.length() >= largeur) return s.substring(0, largeur);
        return s + " ".repeat(largeur - s.length());
    }
}
