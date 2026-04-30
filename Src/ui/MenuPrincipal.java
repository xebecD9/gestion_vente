package Src.ui;

import Src.auth.*;

import java.util.Scanner;


public class MenuPrincipal {

    private AuthService authService;
    private Scanner     scanner;

    public MenuPrincipal() {
        this.authService = new AuthService();
        this.scanner     = new Scanner(System.in);
    }

    // ═══════════════════════════════════════════════════════════════
    //  DÉMARRAGE
    // ═══════════════════════════════════════════════════════════════

    public void demarrer() {

        // ── Bannière ─────────────────────────────────────────────
        Console.afficherBanniere();
        Console.afficherAide("Veuillez vous identifier pour continuer.");
        System.out.println();

        // ── Saisie des identifiants ───────────────────────────────
        Console.demanderSaisie("Identifiant");
        String identifiant = scanner.nextLine().trim();

        Console.demanderMotDePasse("Mot de passe");
        String motDePasse = scanner.nextLine().trim();

        System.out.println();

        // ── Animation de vérification ─────────────────────────────
        Console.afficherChargement("Vérification des accès…", 600);

        Console.separateur();

        // ── Authentification ──────────────────────────────────────
        Utilisateur u = authService.login(identifiant, motDePasse);

        if (u == null) {
            System.out.println();
            Console.afficherErreur("Identifiants incorrects. Accès refusé.");
            Console.afficherAide("Contactez l'administrateur si le problème persiste.");
            Console.separateur();
            scanner.close();
            return;
        }

        // ── Accueil personnalisé ──────────────────────────────────
        System.out.println();
        Console.afficherSucces("Bienvenue, " + Console.BLANC + Console.GRAS + u.getNom() + Console.RESET + Console.VERT + " !");
        Console.afficherBadgeRole(u.getRole());
        Console.separateurEpais();

        // ── Routage selon le rôle ─────────────────────────────────
        String role = u.getRole().toUpperCase();

        switch (role) {
            case "ADMIN":
                new MenuAdmin(u, scanner, authService).afficher();
                break;
            case "VENDEUR":
                new MenuVendeur(u, scanner).afficher();
                break;
            default:
                Console.afficherErreur("Rôle non reconnu : « " + u.getRole() + " ».");
                Console.afficherAide("Veuillez contacter le support technique.");
                break;
        }

        // ── Déconnexion ───────────────────────────────────────────
        authService.logout();
        System.out.println();
        Console.separateurEpais();
        Console.afficherInfo("Session terminée · À bientôt, " + u.getNom() + " !");
        Console.separateur();
        System.out.println();

        scanner.close();
    }
}
