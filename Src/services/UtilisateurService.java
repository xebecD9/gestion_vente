package Src.services;

import Src.auth.*;
import Src.ui.Console;
import java.util.Scanner;
public class UtilisateurService {
    private AuthService authService;
    private Scanner scanner;

    public UtilisateurService(AuthService authService, Scanner scanner) {
        this.authService = authService;
        this.scanner = scanner;
    }
    /** Lister les vendeurs. */
    public void listerVendeurs() {
       Console.afficherTitre("Liste des vendeurs");
       boolean trouve = false;
       for (Utilisateur u : authService.getTousUtilisateurs()) {
              if (u.getRole().equalsIgnoreCase("VENDEUR")) {
                Console.afficherInfo(u.getId() + " - " + u.getNom() + " (" + u.getIdentifiant() + ")");
                trouve = true;
              }
         }
       if (!trouve) {
           Console.afficherInfo("Aucun vendeur trouvé.");
       }
    }
    /** Rechercher un vendeur. */
    public void rechercherVendeur() {
        Console.afficherTitre("Rechercher un vendeur");
        Console.demanderSaisie("Nom ou identifiant du vendeur");
        String critere = scanner.nextLine().trim().toLowerCase();
        boolean trouve = false;
        for (Utilisateur u : authService.getTousUtilisateurs()) {
            if (u.getRole().equalsIgnoreCase("VENDEUR") &&
                (u.getNom().toLowerCase().contains(critere) || u.getIdentifiant().toLowerCase().contains(critere))) {
                Console.afficherInfo(u.getId() + " - " + u.getNom() + " (" + u.getIdentifiant() + ")");
                trouve = true;
            }
        }
        if (!trouve) {
            Console.afficherInfo("Aucun vendeur trouvé pour le critère : " + critere);
        }
    } 
    /** Ajouter un vendeur. */
    public void ajouterVendeur() {
        Console.afficherTitre("Ajouter un vendeur");

        Console.demanderSaisie("Nom");
        String nom = scanner.nextLine().trim();

        Console.demanderSaisie("Identifiant");
        String identifiant = scanner.nextLine().trim();

        Console.demanderSaisie("Mot de passe");
        String motDePasse = scanner.nextLine().trim();

        int id = authService.prochainId();
        Vendeur v = new Vendeur(id, identifiant, motDePasse, nom);
        authService.ajouterUtilisateur(v);
        Console.afficherSucces("Vendeur ajouté avec l'ID : " + id);
    }
    /** Modifier un vendeur. */
    public void modifierVendeur(){
        Console.afficherTitre("Modifier un vendeur");
        listerVendeurs();
        
        Console.demanderSaisie("ID du vendeur a modifier");
        int id; 
        try{
            id = Integer.parseInt(scanner.nextLine().trim());
        }
        catch(NumberFormatException e){
            Console.afficherErreur("ID invalide.");
            return;
        }

        Utilisateur u = authService.trouverUtilisateur(id);
        if (u == null || !u.getRole().equalsIgnoreCase("VENDEUR")) {
            Console.afficherErreur("Vendeur introuvable.");
            return;
        }
        Console.demanderSaisie("Nouveau nom [" + u.getNom() + "]");
        String nom = scanner.nextLine().trim();
        if(!nom.isEmpty()) u.setNom(nom);

        Console.demanderSaisie("Nouveau identifiant [" + u.getIdentifiant() + "]");
        String identifiant = scanner.nextLine().trim();
        if(!identifiant.isEmpty()) u.setIdentifiant(identifiant);
        Console.demanderSaisie("Nouveau mot de passe [********]");
        String motDePasse = scanner.nextLine().trim();
        if(!motDePasse.isEmpty()) u.setMotDePasse(motDePasse);

    } 
    /** Supprimer un vendeur. */
    public void supprimerVendeur(){
        Console.afficherTitre("Supprimer un vendeur");
        listerVendeurs();

        Console.demanderSaisie("ID du vendeurs  supprimer");
        int id;
        try{
            id = Integer.parseInt(scanner.nextLine().trim());

        }
        catch(NumberFormatException e){
            Console.afficherErreur("ID invalide.");
            return;
        }
        Utilisateur u = authService.trouverUtilisateur(id);
        if (u == null || !u.getRole().equalsIgnoreCase("VENDEUR")) {
            Console.afficherErreur("Vendeur introuvable.");
            return;
        }
        authService.supprimerUtilisateur(u);
        Console.afficherSucces("Vendeur supprimé.");    
    }  
    /** Reinitialiser le mot de passe. */
    public void reinitialisermotdepasse(){
        Console.afficherTitre("Reinitialiser le mot de passe");
        listerVendeurs();

        Console.demanderSaisie("ID  vendeur ");
          int id;
        try{
            id = Integer.parseInt(scanner.nextLine().trim());

        }
        catch(NumberFormatException e){
            Console.afficherErreur("ID invalide.");
            return;
        }
        Utilisateur u = authService.trouverUtilisateur(id);
        if (u == null || !u.getRole().equalsIgnoreCase("VENDEUR")) {
            Console.afficherErreur("Vendeur introuvable.");
            return;
        }
        Console.demanderSaisie("Nouveau mot de passe ");
        String pwd = scanner.nextLine().trim();
        u.setMotDePasse(AuthService.hasher(pwd));
        authService.sauvegarderUtilisateurs();
        Console.afficherSucces("Mot de passe reinitialisee pour:" + u.getNom());
    }
}
