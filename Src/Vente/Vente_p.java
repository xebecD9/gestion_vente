
class LigneVente {

    private Produit produit;
    private int quantite;
    private double prixUnitaire;

    public LigneVente(Produit produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = produit.getPrix();
    }

    public double getSousTotal() {
        return prixUnitaire * quantite;
    }

    public Produit getProduit() {
        return produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }
}


// =============================
// 馃敼 CLASSE Vente
// =============================
class Vente {

    private static int compteur = 1;

    private int id;
    private Client client;
    private List<LigneVente> lignes;
    private Date date;
    private String vendeur;

    public Vente(Client client, String vendeur) {
        this.id = compteur++;
        this.client = client;
        this.vendeur = vendeur;
        this.date = new Date();
        this.lignes = new ArrayList<>();
    }

    public void ajouterLigne(LigneVente ligne) {
        lignes.add(ligne);
    }

    public double getTotalHT() {
        return lignes.stream().mapToDouble(LigneVente::getSousTotal).sum();
    }

    public double getTVA() {
        return getTotalHT() * 0.1925; // TVA Cameroun
    }

    public double getTotalTTC() {
        return getTotalHT() + getTVA();
    }

    public List<LigneVente> getLignes() {
        return lignes;
    }

    public Client getClient() {
        return client;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getVendeur() {
        return vendeur;
    }
}


/******************************************************
 *        鈿欙笍 PACKAGE : com.gesteventes.services
 ******************************************************/

package com.gesteventes.services;

import com.gesteventes.modeles.*;

import java.util.*;

// =============================
// 馃敼 CLASSE VenteService
// =============================
public class VenteService {

    private List<Vente> ventes = new ArrayList<>();

    // =============================
    // 馃敻 Cr茅er une nouvelle vente
    // =============================
    public Vente creerVente(Client client, String vendeur) {
        return new Vente(client, vendeur);
    }

    // =============================
    // 馃敻 Ajouter un produit au panier
    // =============================
    public void ajouterProduit(Vente vente, Produit produit, int quantite) {

        if (produit.getQuantiteStock() < quantite) {
            System.out.println("鉂� Stock insuffisant !");
            return;
        }

        LigneVente ligne = new LigneVente(produit, quantite);
        vente.ajouterLigne(ligne);

        // 馃攧 Mise 脿 jour du stock
        produit.setQuantiteStock(produit.getQuantiteStock() - quantite);
    }

    // =============================
    // 馃敻 Finaliser la vente
    // =============================
    public void finaliserVente(Vente vente) {
        ventes.add(vente);
        afficherRecu(vente);
    }

    // =============================
    // 馃敻 Annuler une vente
    // =============================
    public void annulerVente(Vente vente) {

        // 馃攧 Restauration du stock
        for (LigneVente ligne : vente.getLignes()) {
            Produit p = ligne.getProduit();
            p.setQuantiteStock(p.getQuantiteStock() + ligne.getQuantite());
        }

        System.out.println("鉂� Vente annul茅e !");
    }

    public List<Vente> getVentes() {
        return ventes;
    }

    // =============================
    // 馃Ь G茅n茅ration du re莽u ASCII
    // =============================
    public void afficherRecu(Vente vente) {

        System.out.println("\n===================================");
        System.out.println("         馃Ь RE脟U DE VENTE");
        System.out.println("===================================");

        System.out.println("ID Vente : " + vente.getId());
        System.out.println("Client   : " + vente.getClient().getNom());
        System.out.println("Vendeur  : " + vente.getVendeur());
        System.out.println("Date     : " + vente.getDate());

        System.out.println("-----------------------------------");

        for (LigneVente ligne : vente.getLignes()) {
            System.out.printf("%-15s x%d  = %.2f FCFA\n",
                    ligne.getProduit().getNom(),
                    ligne.getQuantite(),
                    ligne.getSousTotal());
        }

        System.out.println("-----------------------------------");
        System.out.printf("TOTAL HT : %.2f FCFA\n", vente.getTotalHT());
        System.out.printf("TVA      : %.2f FCFA\n", vente.getTVA());
        System.out.printf("TOTAL TTC: %.2f FCFA\n", vente.getTotalTTC());

        System.out.println("===================================\n");
    }
                           }
