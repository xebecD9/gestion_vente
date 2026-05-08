package Src.services;

import Src.modeles.*;
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

    /** Ajouter un produit. */
    public void ajouterProduit() {
        Console.afficherTitre("Ajouter un équipement");

        Console.demanderSaisie("Nom de l'équipement");
        String nom = scanner.nextLine().trim();

        Console.demanderSaisie("Référence Fabricant");
        String referenceFabricant = scanner.nextLine().trim();
        
        Console.demanderSaisie("Catégorie ");
        String categorie = scanner.nextLine().trim();

        Console.demanderSaisie("Certification ");
        String certification = scanner.nextLine().trim();

        Console.demanderSaisie("Prix");
        double prix = 0;
        try { prix = Double.parseDouble(scanner.nextLine().trim());
         if (prix <= 0) {
        Console.afficherErreur("Le prix doit être supérieur à 0."); return;} 
        } catch (NumberFormatException e) { Console.afficherErreur("Prix invalide."); return; }

        Console.demanderSaisie("Quantité initiale en stock");
        int quantite = 0;
        try { quantite = Integer.parseInt(scanner.nextLine().trim());
        if (quantite < 0) { Console.afficherErreur("Le stock ne peut pas être négatif."); return; } } catch (NumberFormatException e) { Console.afficherErreur("Quantité invalide."); return; }
        
        Console.demanderSaisie("Seuil d'alerte de stock bas");
        int seuil = 0;
        try { seuil = Integer.parseInt(scanner.nextLine().trim());
        if (seuil < 0) {
        Console.afficherErreur("Le seuil ne peut pas être négatif.");
        return; } } catch (NumberFormatException e) { Console.afficherErreur("Seuil invalide."); return; }

        int id = store.prochainIdProduit();
        Produit p = new Produit(id, nom, referenceFabricant, categorie, certification, prix, quantite, seuil);
        store.ajouterProduit(p);
        
        MouvementStock m = new MouvementStock(
            store.prochainIdMouvement(), p.getId(), quantite,
            MouvementStock.TypeMouvement.ENTREE, MouvementStock.MotifMouvement.AJOUT_INITIAL,
            "ADMIN"
        );
        store.ajouterMouvement(m);
        
        Console.afficherSucces("Équipement ajouté avec l'ID : " + id);
    }

    /** Modifier un produit. */
    public void modifierProduit() {
        Console.afficherTitre("Modifier un équipement");
        afficherTousProduits();

        Console.demanderSaisie("ID de l'équipement à modifier");
        int id;
        try { id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); return; }

        Produit p = store.trouverProduit(id);
        if (p == null) { Console.afficherErreur("Équipement introuvable."); return; }

        Console.demanderSaisie("Nouveau nom [" + p.getNom() + "]");
        String nom = scanner.nextLine().trim();
        if (!nom.isEmpty()) p.setNom(nom);

        Console.demanderSaisie("Nouvelle Référence [" + p.getReferenceFabricant() + "]");
        String ref = scanner.nextLine().trim();
        if (!ref.isEmpty()) p.setReferenceFabricant(ref);
        
        Console.demanderSaisie("Nouvelle Catégorie [" + p.getCategorie() + "]");
        String cat = scanner.nextLine().trim();
        if (!cat.isEmpty()) p.setCategorie(cat);

        Console.demanderSaisie("Nouvelle Certification [" + p.getCertification() + "]");
        String cert = scanner.nextLine().trim();
        if (!cert.isEmpty()) p.setCertification(cert);

        Console.demanderSaisie("Nouveau prix [" + p.getPrix() + "]");
        String prixStr = scanner.nextLine().trim();
        if (!prixStr.isEmpty()) {
            try { p.setPrix(Double.parseDouble(prixStr)); }
            catch (NumberFormatException e) { Console.afficherErreur("Prix ignoré."); }
        }

        Console.demanderSaisie("Nouveau stock [" + p.getQuantiteStock() + "]");
        String stockStr = scanner.nextLine().trim();
        if (!stockStr.isEmpty()) {
            try { 
                int oldStock = p.getQuantiteStock();
                int newStock = Integer.parseInt(stockStr);
                p.setQuantiteStock(newStock); 
                
                if (oldStock != newStock) {
                    MouvementStock m = new MouvementStock(
                        store.prochainIdMouvement(), p.getId(), Math.abs(newStock - oldStock),
                        newStock > oldStock ? MouvementStock.TypeMouvement.ENTREE : MouvementStock.TypeMouvement.SORTIE, 
                        MouvementStock.MotifMouvement.CORRECTION,
                        "ADMIN"
                    );
                    store.ajouterMouvement(m);
                }
            }
            catch (NumberFormatException e) { Console.afficherErreur("Stock ignoré."); }
        }
        
        Console.demanderSaisie("Nouveau seuil d'alerte [" + p.getSeuilAlerte() + "]");
        String seuilStr = scanner.nextLine().trim();
        if (!seuilStr.isEmpty()) {
            try { p.setSeuilAlerte(Integer.parseInt(seuilStr)); }
            catch (NumberFormatException e) { Console.afficherErreur("Seuil ignoré."); }
        }

        store.sauvegarderProduits();
        Console.afficherSucces("Équipement modifié.");
    }

    /** Supprimer un produit. */
    public void supprimerProduit() {
        Console.afficherTitre("Supprimer un équipement");
        afficherTousProduits();

        Console.demanderSaisie("ID de l'équipement à supprimer");
        int id;
        try { id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); return; }

        Produit p = store.trouverProduit(id);
        if (p == null) { Console.afficherErreur("Équipement introuvable."); return; }

        store.supprimerProduit(id);
        Console.afficherSucces("Équipement supprimé.");
    }

    /** Afficher tous les produits. */
    public void afficherTousProduits() {
        Console.nettoyerEcran();
        Console.afficherTitre("Liste des équipements");
        List<Produit> liste = store.getProduits();
        if (liste.isEmpty()) { Console.afficherInfo("Aucun équipement enregistré."); return; }

        System.out.println(Console.BLEU + "  +" + "-".repeat(110) + "+" + Console.RESET);
        System.out.println(Console.BLEU + "  | " + Console.CYAN + Console.GRAS
            + String.format("%-4s | %-20s | %-15s | %-12s | %-8s | %10s | %6s | %5s | %-8s", "ID", "Nom", "Réf", "Catégorie", "Certif", "Prix (F)", "Stock", "Seuil", "Alerte")
            + Console.BLEU + " |" + Console.RESET);
        System.out.println(Console.BLEU + "  +" + "-".repeat(110) + "+" + Console.RESET);

        for (Produit p : liste) {
            String alerte = p.estStockBas() ? Console.ROUGE + Console.GRAS + "⚠ BAS  " + Console.RESET : Console.VERT + "✔ OK   " + Console.RESET;
            String stockColor = p.estStockBas() ? Console.ROUGE : Console.VERT;
            
            System.out.printf(Console.BLEU + "  | " + Console.RESET + "%-4d " + Console.BLEU + "|" + Console.RESET + " %-20s " + Console.BLEU + "|" + Console.RESET + " %-15s " + Console.BLEU + "|" + Console.RESET + " %-12s " + Console.BLEU + "|" + Console.RESET + " %-8s " + Console.BLEU + "|" + Console.RESET + " %10.2f " + Console.BLEU + "| " + stockColor + "%6d " + Console.BLEU + "| " + Console.RESET + "%5d " + Console.BLEU + "| " + Console.RESET + "%s " + Console.BLEU + "|%n" + Console.RESET,
                p.getId(), 
                p.getNom().length() > 20 ? p.getNom().substring(0, 17) + "..." : p.getNom(), 
                p.getReferenceFabricant(), 
                p.getCategorie() != null && p.getCategorie().length() > 12 ? p.getCategorie().substring(0, 9) + "..." : (p.getCategorie() == null ? "" : p.getCategorie()),
                p.getCertification(), 
                p.getPrix(), 
                p.getQuantiteStock(), 
                p.getSeuilAlerte(),
                alerte);
        }
        System.out.println(Console.BLEU + "  +" + "-".repeat(110) + "+" + Console.RESET);
        System.out.println();
    }

    /** Rechercher un produit. */
    public void rechercherProduit() {
        Console.afficherTitre("Rechercher un équipement");
        Console.demanderSaisie("Nom ou Référence");
        String terme = scanner.nextLine().trim().toLowerCase();

        List<Produit> resultats = new ArrayList<>();
        for (Produit p : store.getProduits()) {
            if (p.getNom().toLowerCase().contains(terme)
                    || p.getReferenceFabricant().toLowerCase().contains(terme)) {
                resultats.add(p);
            }
        }

        if (resultats.isEmpty()) {
            Console.afficherInfo("Aucun équipement trouvé.");
        } else {
            for (Produit p : resultats) {
                System.out.printf("[%d] %s - Réf: %s - Cert: %s - %.2f F - Stock: %d%n",
                    p.getId(), p.getNom(), p.getReferenceFabricant(), p.getCertification(), p.getPrix(), p.getQuantiteStock());
            }
        }
    }

    /** Afficher alertes stock bas. */
    public void afficherAlertesStock() {
        Console.nettoyerEcran();
        Console.afficherTitre("Alertes stock bas & Ravitaillement");
        boolean trouve = false;
        for (Produit p : store.getProduits()) {
            if (p.estStockBas()) {
                Console.afficherErreur("Stock bas : " + p.getNom()
                    + " (ID:" + p.getId() + ") — " + p.getQuantiteStock() + " unité(s) [Seuil: " + p.getSeuilAlerte() + "]");
                trouve = true;
            }
        }
        if (!trouve) {
            Console.afficherSucces("Tous les stocks sont suffisants.");
            return;
        }

        System.out.println();
        Console.demanderSaisie("Voulez-vous ravitailler un équipement ? (O/N)");
        String reponse = scanner.nextLine().trim();
        if (reponse.equalsIgnoreCase("O")) {
            Console.demanderSaisie("Saisissez l'ID de l'équipement");
            try {
                int id = Integer.parseInt(scanner.nextLine().trim());
                Produit p = store.trouverProduit(id);
                if (p != null) {
                    Console.demanderSaisie("Quantité à ajouter");
                    int qte = Integer.parseInt(scanner.nextLine().trim());
                    if (qte > 0) {
                        p.setQuantiteStock(p.getQuantiteStock() + qte);
                        
                        MouvementStock m = new MouvementStock(
                            store.prochainIdMouvement(), p.getId(), qte,
                            MouvementStock.TypeMouvement.ENTREE, 
                            MouvementStock.MotifMouvement.RAVITAILLEMENT,
                            "ADMIN"
                        );
                        store.ajouterMouvement(m);
                        
                        store.sauvegarderProduits();
                        Console.afficherSucces("Stock mis à jour : " + p.getNom() + " (Nouveau stock: " + p.getQuantiteStock() + ")");
                    } else {
                        Console.afficherErreur("La quantité doit être positive.");
                    }
                } else {
                    Console.afficherErreur("Équipement introuvable.");
                }
            } catch (NumberFormatException e) {
                Console.afficherErreur("Saisie invalide.");
            }
        }
    }
    
    /** Historique Mouvements. 
     * elle permet de connaitre les mouvements de stock
    */
    public void afficherMouvements() {
        Console.nettoyerEcran();
        Console.afficherTitre("Historique des mouvements de stock");
        
        List<MouvementStock> liste = store.getMouvements();
        if (liste.isEmpty()) { Console.afficherInfo("Aucun mouvement enregistré."); return; }
        
        System.out.println(Console.BLEU + "  +" + "-".repeat(105) + "+" + Console.RESET);
        System.out.println(Console.BLEU + "  | " + Console.CYAN + Console.GRAS
            + String.format("%-4s | %-19s | %-20s | %-6s | %-8s | %-16s | %-10s", "ID", "Date", "Produit", "Qte", "Type", "Motif", "Utilisateur")
            + Console.BLEU + " |" + Console.RESET);
        System.out.println(Console.BLEU + "  +" + "-".repeat(105) + "+" + Console.RESET);
        
        for (MouvementStock m : liste) {
            Produit p = store.trouverProduit(m.getProduitId());
            String nomProd = p != null ? p.getNom() : "ID:" + m.getProduitId();
            if (nomProd.length() > 20) nomProd = nomProd.substring(0, 17) + "...";
            
            String typeCol = m.getType() == MouvementStock.TypeMouvement.ENTREE ? 
                Console.VERT + "ENTREE  " + Console.RESET : Console.ROUGE + "SORTIE  " + Console.RESET;
                
            System.out.printf(Console.BLEU + "  | " + Console.RESET + "%-4d " + Console.BLEU + "|" + Console.RESET + " %-19s " + Console.BLEU + "|" + Console.RESET + " %-20s " + Console.BLEU + "|" + Console.RESET + " %-6d " + Console.BLEU + "| " + typeCol + Console.BLEU + "|" + Console.RESET + " %-16s " + Console.BLEU + "|" + Console.RESET + " %-10s " + Console.BLEU + "|%n" + Console.RESET,
                m.getId(), m.getDateFormatee(), nomProd, m.getQuantite(), m.getMotif().name(), m.getUtilisateurId());
        }
        
        System.out.println(Console.BLEU + "  +" + "-".repeat(105) + "+" + Console.RESET);
        System.out.println();
    }
}
