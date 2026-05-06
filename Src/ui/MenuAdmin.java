package Src.ui;

import Src.auth.AuthService;
import Src.auth.Utilisateur;
import Src.services.*;

import java.util.Scanner;

/**
 * ╔══════════════════════════════════════════════╗
 *  MenuAdmin  —  Interface Administrateur
 *  Produits · Clients · Vendeurs · Ventes · Rapports
 * ╚══════════════════════════════════════════════╝
 */
public class MenuAdmin {

    private Utilisateur        utilisateur;
    private Scanner            scanner;
    private ProduitService     produitService;
    private ClientService      clientService;
    private VenteService       venteService;
    private RapportService     rapportService;
    private AuthService        authService;
    private UtilisateurService utilisateurService;

    public MenuAdmin(Utilisateur utilisateur, Scanner scanner, AuthService authService) {
        this.utilisateur        = utilisateur;
        this.scanner            = scanner;
        this.produitService     = new ProduitService(scanner);
        this.clientService      = new ClientService(scanner);
        this.venteService       = new VenteService(scanner, utilisateur);
        this.rapportService     = new RapportService();
        this.authService        = authService;
        this.utilisateurService = new UtilisateurService(authService, scanner);
    }

    // ═══════════════════════════════════════════════════════════════
    //  MENU PRINCIPAL ADMIN
    // ═══════════════════════════════════════════════════════════════

    public void afficher() {
        boolean actif = true;

        while (actif) {
            Console.nettoyerEcran();
            Console.afficherTitre("Menu Administrateur — " + utilisateur.getNom());

            Console.afficherSousTitre("Gestion");
            Console.afficherOption(1, "📦", "Gestion des équipements");
            Console.afficherOption(2, "🏢", "Gestion des compagnies clientes");
            Console.afficherOption(3, "🛒", "Gestion des vendeurs");
            Console.afficherOption(4, "🧾", "Gestion des ventes");

            Console.afficherSousTitre("Analytique");
            Console.afficherOption(5, "📊", "Rapports & Statistiques");

            System.out.println();
            Console.separateur();
            Console.afficherOption(0, "🚪", "Se déconnecter");
            System.out.println();

            Console.demanderSaisie("Votre choix");
            String choix = scanner.nextLine().trim();
            System.out.println();

            switch (choix) {
                case "1": menuProduits(); break;
                case "2": menuClients();  break;
                case "3": menuVendeurs(); break;
                case "4": menuVentes();   break;
                case "5": menuRapports(); break;
                case "0":
                    Console.afficherInfo("Déconnexion en cours…");
                    actif = false;
                    break;
                default:
                    Console.afficherErreur("Choix invalide : « " + choix + " »");
                    Console.afficherAide("Saisissez un numéro entre 0 et 5.");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  SOUS-MENU : PRODUITS
    // ═══════════════════════════════════════════════════════════════

    private void menuProduits() {
        boolean actif = true;

        while (actif) {
            Console.afficherTitre("Gestion des équipements");

            Console.afficherSousTitre("CRUD");
            Console.afficherOption(1, "➕", "Ajouter un équipement");
            Console.afficherOption(2, "✏️ ", "Modifier un équipement");
            Console.afficherOption(3, "🗑️ ", "Supprimer un équipement");

            Console.afficherSousTitre("Consultation");
            Console.afficherOption(4, "📋", "Afficher tous les équipements");
            Console.afficherOption(5, "🔍", "Rechercher un équipement");
            Console.afficherOption(6, "⚠️ ", "Alertes stock bas");
            Console.afficherOption(7, "📜", "Historique des mouvements de stock");

            System.out.println();
            Console.separateur();
            Console.afficherOption(0, "↩️ ", "Retour au menu principal");
            System.out.println();

            Console.demanderSaisie("Votre choix");
            String choix = scanner.nextLine().trim();
            System.out.println();

            switch (choix) {
                case "1": produitService.ajouterProduit();       break;
                case "2": produitService.modifierProduit();      break;
                case "3": produitService.supprimerProduit();     break;
                case "4": produitService.afficherTousProduits(); break;
                case "5": produitService.rechercherProduit();    break;
                case "6": produitService.afficherAlertesStock(); break;
                case "7": produitService.afficherMouvements();   break;
                case "0": actif = false;                         break;
                default:
                    Console.afficherErreur("Choix invalide : « " + choix + " »");
                    Console.afficherAide("Saisissez un numéro entre 0 et 7.");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  SOUS-MENU : VENDEURS   ← bug break corrigé ici
    // ═══════════════════════════════════════════════════════════════

    private void menuVendeurs() {
        boolean actif = true;

        while (actif) {
            Console.afficherTitre("Gestion des vendeurs");

            Console.afficherSousTitre("CRUD");
            Console.afficherOption(1, "➕", "Ajouter un vendeur");
            Console.afficherOption(2, "✏️ ", "Modifier un vendeur");
            Console.afficherOption(3, "🗑️ ", "Supprimer un vendeur");

            Console.afficherSousTitre("Consultation");
            Console.afficherOption(4, "📋", "Afficher tous les vendeurs");
            Console.afficherOption(5, "🔍", "Rechercher un vendeur");

            Console.afficherSousTitre("Sécurité");
            Console.afficherOption(6, "🔑", "Réinitialiser un mot de passe");

            System.out.println();
            Console.separateur();
            Console.afficherOption(0, "↩️ ", "Retour au menu principal");
            System.out.println();

            Console.demanderSaisie("Votre choix");
            String choix = scanner.nextLine().trim();
            System.out.println();

            switch (choix) {
                case "1": utilisateurService.ajouterVendeur();          break;  
                case "2": utilisateurService.modifierVendeur();         break;  
                case "3": utilisateurService.supprimerVendeur();        break;  
                case "4": utilisateurService.listerVendeurs();          break;  
                case "5": utilisateurService.rechercherVendeur();       break;  
                case "6": utilisateurService.reinitialisermotdepasse(); break;  
                case "0": actif = false;                                break;
                default:
                    Console.afficherErreur("Choix invalide : « " + choix + " »");
                    Console.afficherAide("Saisissez un numéro entre 0 et 6.");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  SOUS-MENU : CLIENTS
    // ═══════════════════════════════════════════════════════════════

    private void menuClients() {
        boolean actif = true;

        while (actif) {
            Console.afficherTitre("Gestion des compagnies clientes");

            Console.afficherSousTitre("CRUD");
            Console.afficherOption(1, "➕", "Ajouter une compagnie");
            Console.afficherOption(2, "✏️ ", "Modifier une compagnie");
            Console.afficherOption(3, "🗑️ ", "Supprimer une compagnie");

            Console.afficherSousTitre("Consultation");
            Console.afficherOption(4, "📋", "Afficher toutes les compagnies");
            Console.afficherOption(5, "🔍", "Rechercher une compagnie");
            Console.afficherOption(6, "📜", "Historique des achats");

            System.out.println();
            Console.separateur();
            Console.afficherOption(0, "↩️ ", "Retour au menu principal");
            System.out.println();

            Console.demanderSaisie("Votre choix");
            String choix = scanner.nextLine().trim();
            System.out.println();

            switch (choix) {
                case "1": clientService.ajouterClient();           break;
                case "2": clientService.modifierClient();          break;
                case "3": clientService.supprimerClient();         break;
                case "4": clientService.afficherTousClients();     break;
                case "5": clientService.rechercherClient();        break;
                case "6": clientService.afficherHistoriqueClient(); break;
                case "0": actif = false;                           break;
                default:
                    Console.afficherErreur("Choix invalide : « " + choix + " »");
                    Console.afficherAide("Saisissez un numéro entre 0 et 6.");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  SOUS-MENU : VENTES
    // ═══════════════════════════════════════════════════════════════

    private void menuVentes() {
        boolean actif = true;

        while (actif) {
            Console.afficherTitre("Gestion des ventes");

            Console.afficherOption(1, "🧾", "Créer une vente");
            Console.afficherOption(2, "🚫", "Annuler une vente");

            System.out.println();
            Console.separateur();
            Console.afficherOption(0, "↩️ ", "Retour au menu principal");
            System.out.println();

            Console.demanderSaisie("Votre choix");
            String choix = scanner.nextLine().trim();
            System.out.println();

            switch (choix) {
                case "1": venteService.creerVente();   break;
                case "2": venteService.annulerVente(); break;
                case "0": actif = false;               break;
                default:
                    Console.afficherErreur("Choix invalide : « " + choix + " »");
                    Console.afficherAide("Saisissez 1, 2 ou 0.");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  SOUS-MENU : RAPPORTS
    // ═══════════════════════════════════════════════════════════════

    private void menuRapports() {
        boolean actif = true;

        while (actif) {
            Console.afficherTitre("Rapports & Statistiques");

            Console.afficherSousTitre("Par période");
            Console.afficherOption(1, "📅", "Ventes du jour");
            Console.afficherOption(2, "📆", "Ventes de la semaine");
            Console.afficherOption(3, "🗓️ ", "Ventes du mois");
            Console.afficherOption(4, "💰", "Chiffre d'affaires total");

            Console.afficherSousTitre("Classements");
            Console.afficherOption(5, "🏆", "Top produits");
            Console.afficherOption(6, "🥇", "Top vendeurs");

            Console.afficherSousTitre("Export");
            Console.afficherOption(7, "💾", "Exporter rapport .txt");

            System.out.println();
            Console.separateur();
            Console.afficherOption(0, "↩️ ", "Retour au menu principal");
            System.out.println();

            Console.demanderSaisie("Votre choix");
            String choix = scanner.nextLine().trim();
            System.out.println();

            switch (choix) {
                case "1": rapportService.rapportJour();           break;
                case "2": rapportService.rapportSemaine();        break;
                case "3": rapportService.rapportMois();           break;
                case "4": rapportService.chiffreAffairesTotal();  break;
                case "5": rapportService.topProduits();           break;
                case "6": rapportService.topVendeurs();           break;
                case "7": rapportService.exporterRapport();       break;
                case "0": actif = false;                          break;
                default:
                    Console.afficherErreur("Choix invalide : « " + choix + " »");
                    Console.afficherAide("Saisissez un numéro entre 0 et 7.");
            }
        }
    }
}
