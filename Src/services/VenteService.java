package Src.services;


import Src.modeles.*;
import Src.auth.*;  
import Src.stockage.DataStore;
import Src.ui.Console;

import java.util.Scanner;

public class VenteService {

    private DataStore  store;
    private Scanner    scanner;
    private Utilisateur utilisateurConnecte;

    public VenteService(Scanner scanner, Utilisateur utilisateur) {
        this.store               = DataStore.getInstance();
        this.scanner             = scanner;
        this.utilisateurConnecte = utilisateur;
    }

    // ── Créer une vente ──────────────────────────────────────────
    public void creerVente() {
        Console.afficherTitre("Nouvelle vente");

        // Sélection client
        Console.demanderSaisie("ID du client");
        int clientId;
        try { clientId = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); return; }

        Client client = store.trouverClient(clientId);
        if (client == null) { Console.afficherErreur("Client introuvable."); return; }

        int id = store.prochainIdVente();
        Vente vente = new Vente(id, client, utilisateurConnecte.getIdentifiant());

        // Ajout des produits
        boolean continuer = true;
        while (continuer) {
            Console.demanderSaisie("ID produit (0 pour terminer)");
            int produitId;
            try { produitId = Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); continue; }

            if (produitId == 0) { continuer = false; continue; }

            Produit p = store.trouverProduit(produitId);
            if (p == null) { Console.afficherErreur("Produit introuvable."); continue; }

            Console.demanderSaisie("Quantité");
            int qte;
            try { qte = Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { Console.afficherErreur("Quantité invalide."); continue; }

            if (qte > p.getQuantiteStock()) {
                Console.afficherErreur("Stock insuffisant. Disponible : " + p.getQuantiteStock());
                continue;
            }

            vente.ajouterLigne(new LigneVente(p, qte));
            p.setQuantiteStock(p.getQuantiteStock() - qte);
            Console.afficherSucces(p.getNom() + " x" + qte + " ajouté.");
        }

        if (vente.getLignes().isEmpty()) {
            Console.afficherInfo("Vente annulée — aucun produit.");
            return;
        }

        // Confirmation
        afficherRecu(vente);
        Console.demanderSaisie("Confirmer la vente ? (O/N)");
        String rep = scanner.nextLine().trim();

        if (rep.equalsIgnoreCase("O")) {
            store.ajouterVente(vente);
            store.sauvegarderProduits();

            // Mise à jour chiffre d'affaires du vendeur
            if (utilisateurConnecte instanceof Vendeur) {
                ((Vendeur) utilisateurConnecte).ajouterVente(vente.getTotalTTC());
            }

            Console.afficherSucces("Vente #" + id + " enregistrée avec succès !");
        } else {
            // Remettre les stocks
            for (LigneVente lv : vente.getLignes()) {
                lv.getProduit().setQuantiteStock(
                    lv.getProduit().getQuantiteStock() + lv.getQuantite()
                );
            }
            Console.afficherInfo("Vente annulée.");
        }
    }

    // ── Annuler une vente ────────────────────────────────────────
    public void annulerVente() {
        Console.afficherTitre("Annuler une vente");
        Console.demanderSaisie("ID de la vente à annuler");
        int id;
        try { id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); return; }

        Vente vente = null;
        for (Vente v : store.getVentes()) {
            if (v.getId() == id) { vente = v; break; }
        }

        if (vente == null)       { Console.afficherErreur("Vente introuvable."); return; }
        if (vente.isAnnulee())   { Console.afficherInfo("Vente déjà annulée."); return; }

        vente.annuler();

        // Restituer le stock
        for (LigneVente lv : vente.getLignes()) {
            lv.getProduit().setQuantiteStock(
                lv.getProduit().getQuantiteStock() + lv.getQuantite()
            );
        }

        store.sauvegarderVentes();
        store.sauvegarderProduits();
        Console.afficherSucces("Vente #" + id + " annulée. Stocks restaurés.");
    }

    // ── Afficher reçu ASCII ──────────────────────────────────────
    public void afficherRecu(Vente v) {
        System.out.println();
        System.out.println(Console.CYAN + "╔══════════════════════════════════════════╗");
        System.out.println("║              REÇU DE VENTE               ║");
        System.out.println("╠══════════════════════════════════════════╣" + Console.RESET);
        System.out.printf(Console.CYAN + "║" + Console.RESET
            + " %-40s " + Console.CYAN + "║%n" + Console.RESET,
            "Vente #" + v.getId() + " — " + v.getDateFormatee());
        System.out.printf(Console.CYAN + "║" + Console.RESET
            + " %-40s " + Console.CYAN + "║%n" + Console.RESET,
            "Client : " + v.getClient().getNom());
        System.out.printf(Console.CYAN + "║" + Console.RESET
            + " %-40s " + Console.CYAN + "║%n" + Console.RESET,
            "Vendeur : " + v.getVendeurId());
        System.out.println(Console.CYAN + "╠══════════════════════════════════════════╣" + Console.RESET);

        for (LigneVente lv : v.getLignes()) {
            System.out.printf(Console.CYAN + "║" + Console.RESET
                + " %-25s x%-3d %8.2f F " + Console.CYAN + "║%n" + Console.RESET,
                lv.getProduit().getNom(), lv.getQuantite(), lv.getSousTotal());
        }

        System.out.println(Console.CYAN + "╠══════════════════════════════════════════╣" + Console.RESET);
        System.out.printf(Console.CYAN + "║" + Console.RESET
            + " %-29s %10.2f F " + Console.CYAN + "║%n" + Console.RESET,
            "Sous-total HT :", v.getSousTotal());
        System.out.printf(Console.CYAN + "║" + Console.RESET
            + " %-29s %10.2f F " + Console.CYAN + "║%n" + Console.RESET,
            "TVA (19%) :", v.getMontantTVA());
        System.out.printf(Console.CYAN + Console.GRAS + "║" + Console.RESET
            + Console.GRAS + " %-29s %10.2f F " + Console.CYAN + "║%n" + Console.RESET,
            "TOTAL TTC :", v.getTotalTTC());
        System.out.println(Console.CYAN + "╚══════════════════════════════════════════╝" + Console.RESET);
        System.out.println();
    }
}
