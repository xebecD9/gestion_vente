package Src.services;

import Src.modeles.Produit;
import Src.stockage.DataStore;
import Src.ui.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProduitService {

    private DataStore store;
    private Scanner   scanner;

    public ProduitService(Scanner scanner) {
        this.store   = DataStore.getInstance();
        this.scanner = scanner;
    }

    // ── Ajouter ──────────────────────────────────────────────────
    public void ajouterProduit() {
        Console.afficherTitre("Ajouter un produit");

        Console.demanderSaisie("Nom");
        String nom = scanner.nextLine().trim();

        Console.demanderSaisie("Catégorie");
        String categorie = scanner.nextLine().trim();

        Console.demanderSaisie("Prix");
        double prix = 0;
        try { prix = Double.parseDouble(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("Prix invalide."); return; }

        Console.demanderSaisie("Quantité en stock");
        int quantite = 0;
        try { quantite = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("Quantité invalide."); return; }

        int id = store.prochainIdProduit();
        Produit p = new Produit(id, nom, categorie, prix, quantite);
        store.ajouterProduit(p);
        Console.afficherSucces("Produit ajouté avec l'ID : " + id);
    }

    // ── Modifier ─────────────────────────────────────────────────
    public void modifierProduit() {
        Console.afficherTitre("Modifier un produit");
        afficherTousProduits();

        Console.demanderSaisie("ID du produit à modifier");
        int id;
        try { id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); return; }

        Produit p = store.trouverProduit(id);
        if (p == null) { Console.afficherErreur("Produit introuvable."); return; }

        Console.demanderSaisie("Nouveau nom [" + p.getNom() + "]");
        String nom = scanner.nextLine().trim();
        if (!nom.isEmpty()) p.setNom(nom);

        Console.demanderSaisie("Nouvelle catégorie [" + p.getCategorie() + "]");
        String cat = scanner.nextLine().trim();
        if (!cat.isEmpty()) p.setCategorie(cat);

        Console.demanderSaisie("Nouveau prix [" + p.getPrix() + "]");
        String prixStr = scanner.nextLine().trim();
        if (!prixStr.isEmpty()) {
            try { p.setPrix(Double.parseDouble(prixStr)); }
            catch (NumberFormatException e) { Console.afficherErreur("Prix ignoré."); }
        }

        Console.demanderSaisie("Nouveau stock [" + p.getQuantiteStock() + "]");
        String stockStr = scanner.nextLine().trim();
        if (!stockStr.isEmpty()) {
            try { p.setQuantiteStock(Integer.parseInt(stockStr)); }
            catch (NumberFormatException e) { Console.afficherErreur("Stock ignoré."); }
        }

        store.sauvegarderProduits();
        Console.afficherSucces("Produit modifié.");
    }

    // ── Supprimer ────────────────────────────────────────────────
    public void supprimerProduit() {
        Console.afficherTitre("Supprimer un produit");
        afficherTousProduits();

        Console.demanderSaisie("ID du produit à supprimer");
        int id;
        try { id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); return; }

        Produit p = store.trouverProduit(id);
        if (p == null) { Console.afficherErreur("Produit introuvable."); return; }

        store.supprimerProduit(id);
        Console.afficherSucces("Produit supprimé.");
    }

    // ── Afficher tous ────────────────────────────────────────────
    public void afficherTousProduits() {
        Console.afficherTitre("Liste des produits");
        List<Produit> liste = store.getProduits();
        if (liste.isEmpty()) { Console.afficherInfo("Aucun produit enregistré."); return; }

        System.out.println(Console.GRAS
            + String.format("%-5s %-20s %-15s %10s %8s %s", "ID", "Nom", "Catégorie", "Prix", "Stock", "Alerte")
            + Console.RESET);
        Console.separateur();

        for (Produit p : liste) {
            String alerte = p.estStockBas() ? Console.ROUGE + "⚠ BAS" + Console.RESET : "";
            System.out.printf("%-5d %-20s %-15s %10.2f %8d %s%n",
                p.getId(), p.getNom(), p.getCategorie(), p.getPrix(), p.getQuantiteStock(), alerte);
        }
        Console.separateur();
    }

    // ── Recherche ────────────────────────────────────────────────
    public void rechercherProduit() {
        Console.afficherTitre("Rechercher un produit");
        Console.demanderSaisie("Nom ou catégorie");
        String terme = scanner.nextLine().trim().toLowerCase();

        List<Produit> resultats = new ArrayList<>();
        for (Produit p : store.getProduits()) {
            if (p.getNom().toLowerCase().contains(terme)
                    || p.getCategorie().toLowerCase().contains(terme)) {
                resultats.add(p);
            }
        }

        if (resultats.isEmpty()) {
            Console.afficherInfo("Aucun produit trouvé.");
        } else {
            for (Produit p : resultats) {
                System.out.printf("[%d] %s - %s - %.2f F - Stock: %d%n",
                    p.getId(), p.getNom(), p.getCategorie(), p.getPrix(), p.getQuantiteStock());
            }
        }
    }

    // ── Alertes stock bas ────────────────────────────────────────
    public void afficherAlertesStock() {
        Console.afficherTitre("Alertes stock bas");
        boolean trouve = false;
        for (Produit p : store.getProduits()) {
            if (p.estStockBas()) {
                Console.afficherErreur("Stock bas : " + p.getNom()
                    + " (ID:" + p.getId() + ") — " + p.getQuantiteStock() + " unité(s)");
                trouve = true;
            }
        }
        if (!trouve) Console.afficherSucces("Tous les stocks sont suffisants.");
    }
}
