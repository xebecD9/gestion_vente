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

    /** Créer une vente. */
    public void creerVente() {
        Console.afficherTitre("Nouvelle vente");

        Console.demanderSaisie("ID de la compagnie cliente");
        int clientId;
        try { clientId = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); return; }

        Client client = store.trouverClient(clientId);
        if (client == null) { Console.afficherErreur("Compagnie introuvable."); return; }

        int id = store.prochainIdVente();
        Vente vente = new Vente(id, client, utilisateurConnecte.getIdentifiant());

        boolean continuer = true;
        while (continuer) {
            Console.demanderSaisie("ID équipement (0 pour terminer)");
            int produitId;
            try { produitId = Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); continue; }

            if (produitId == 0) { continuer = false; continue; }

            Produit p = store.trouverProduit(produitId);
            if (p == null) { Console.afficherErreur("Équipement introuvable."); continue; }

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
            Console.afficherInfo("Vente annulée — aucun équipement.");
            return;
        }

        afficherRecu(vente);
        Console.demanderSaisie("Confirmer la vente ? (O/N)");
        String rep = scanner.nextLine().trim();

        if (rep.equalsIgnoreCase("O")) {
            store.ajouterVente(vente);
            
            for (LigneVente lv : vente.getLignes()) {
                MouvementStock m = new MouvementStock(
                    store.prochainIdMouvement(), lv.getProduit().getId(), lv.getQuantite(),
                    MouvementStock.TypeMouvement.SORTIE, MouvementStock.MotifMouvement.VENTE,
                    utilisateurConnecte.getIdentifiant()
                );
                store.ajouterMouvement(m);
            }
            
            store.sauvegarderProduits();

            if (utilisateurConnecte instanceof Vendeur) {
                ((Vendeur) utilisateurConnecte).ajouterVente(vente.getTotalTTC());
            }

            Console.afficherSucces("Vente #" + id + " enregistrée avec succès !");
            
            genererFacture(vente);
            
        } else {
            for (LigneVente lv : vente.getLignes()) {
                lv.getProduit().setQuantiteStock(
                    lv.getProduit().getQuantiteStock() + lv.getQuantite()
                );
            }
            Console.afficherInfo("Vente annulée.");
        }
    }

    /** Annuler une vente. */
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

        for (LigneVente lv : vente.getLignes()) {
            lv.getProduit().setQuantiteStock(
                lv.getProduit().getQuantiteStock() + lv.getQuantite()
            );
            
            MouvementStock m = new MouvementStock(
                store.prochainIdMouvement(), lv.getProduit().getId(), lv.getQuantite(),
                MouvementStock.TypeMouvement.ENTREE, MouvementStock.MotifMouvement.ANNULATION_VENTE,
                utilisateurConnecte.getIdentifiant()
            );
            store.ajouterMouvement(m);
        }

        store.sauvegarderVentes();
        store.sauvegarderProduits();
        Console.afficherSucces("Vente #" + id + " annulée. Stocks restaurés.");
    }

    /** Afficher reçu ASCII. */
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
            "Compagnie : " + v.getClient().getNomCompagnie());
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
    
    /** Générer Facture. */
    private void genererFacture(Vente v) {
        java.io.File dossier = new java.io.File("rapports");
        if (!dossier.exists()) dossier.mkdirs();
        
        String nomFichier = "rapports/facture_" + v.getId() + ".txt";
        
        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(nomFichier))) {
            pw.println("==================================================");
            pw.println("              FACTURE DE VENTE                    ");
            pw.println("==================================================");
            pw.println("Facture N°   : " + v.getId());
            pw.println("Date         : " + v.getDateFormatee());
            pw.println("Vendeur      : " + v.getVendeurId());
            pw.println("--------------------------------------------------");
            pw.println("CLIENT");
            pw.println("Nom          : " + v.getClient().getNomCompagnie());
            pw.println("Contact      : " + v.getClient().getContactPrincipal());
            pw.println("Adresse      : " + v.getClient().getAdresse());
            pw.println("==================================================");
            pw.println(String.format("%-25s %-5s %15s", "Article", "Qte", "Montant HT"));
            pw.println("--------------------------------------------------");
            
            for (LigneVente lv : v.getLignes()) {
                String nomP = lv.getProduit().getNom();
                if (nomP.length() > 24) nomP = nomP.substring(0, 21) + "...";
                pw.println(String.format("%-25s x%-4d %15.2f F", 
                    nomP, lv.getQuantite(), lv.getSousTotal()));
            }
            
            pw.println("--------------------------------------------------");
            pw.println(String.format("%-31s %15.2f F", "Sous-total HT :", v.getSousTotal()));
            pw.println(String.format("%-31s %15.2f F", "TVA (19%) :", v.getMontantTVA()));
            pw.println("==================================================");
            pw.println(String.format("%-31s %15.2f F", "TOTAL TTC :", v.getTotalTTC()));
            pw.println("==================================================");
            pw.println("Merci pour votre confiance.");
            
            Console.afficherSucces("Facture générée : " + nomFichier);
        } catch (java.io.IOException e) {
            Console.afficherErreur("Erreur génération facture : " + e.getMessage());
        }
    }
}
