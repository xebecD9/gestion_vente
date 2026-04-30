package Src.modeles;

public class Produit {

    private int    id;
    private String nom;
    private String categorie;
    private double prix;
    private int    quantiteStock;

    public Produit(int id, String nom, String categorie, double prix, int quantiteStock) {
        this.id            = id;
        this.nom           = nom;
        this.categorie     = categorie;
        this.prix          = prix;
        this.quantiteStock = quantiteStock;
    }

    public int    getId()            { return id; }
    public String getNom()           { return nom; }
    public String getCategorie()     { return categorie; }
    public double getPrix()          { return prix; }
    public int    getQuantiteStock() { return quantiteStock; }

    public void setNom(String nom)               { this.nom = nom; }
    public void setCategorie(String categorie)   { this.categorie = categorie; }
    public void setPrix(double prix)             { this.prix = prix; }
    public void setQuantiteStock(int quantite)   { this.quantiteStock = quantite; }

    public boolean estStockBas() { return quantiteStock < 5; }

    @Override
    public String toString() {
        return id + "|" + nom + "|" + categorie + "|" + prix + "|" + quantiteStock;
    }
}
