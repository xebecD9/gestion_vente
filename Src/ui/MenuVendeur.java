package Src.ui;

import Src.auth.Utilisateur;
import Src.services.ProduitService;
import Src.services.ClientService;
import Src.services.VenteService;

import java.util.Scanner;

public class MenuVendeur {

    private Utilisateur    utilisateur;
    private Scanner        scanner;
    private ProduitService produitService;
    private ClientService  clientService;
    private VenteService   venteService;

    public MenuVendeur(Utilisateur utilisateur, Scanner scanner) {
        this.utilisateur    = utilisateur;
        this.scanner        = scanner;
        this.produitService = new ProduitService(scanner);
        this.clientService  = new ClientService(scanner);
        this.venteService   = new VenteService(scanner, utilisateur);
    }

    public void afficher() {
        boolean actif = true;
        while (actif) {
            Console.nettoyerEcran();
            Console.afficherTitre("Menu Vendeur — " + utilisateur.getNom());

            Console.afficherOption(1, "Créer une vente");
            Console.afficherOption(2, "Annuler une vente");
            Console.afficherOption(3, "Voir les équipements");
            Console.afficherOption(4, "Rechercher un équipement");
            Console.afficherOption(5, "Voir les compagnies clientes");
            Console.afficherOption(6, "Rechercher une compagnie");
            Console.afficherOption(0, "Se déconnecter");
            System.out.println();

            Console.demanderSaisie("Votre choix");
            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1": venteService.creerVente();               break;
                case "2": venteService.annulerVente();             break;
                case "3": produitService.afficherTousProduits();   break;
                case "4": produitService.rechercherProduit();      break;
                case "5": clientService.afficherTousClients();     break;
                case "6": clientService.rechercherClient();        break;
                case "0": actif = false;                           break;
                default:  Console.afficherErreur("Choix invalide.");
            }
        }
    }
}
