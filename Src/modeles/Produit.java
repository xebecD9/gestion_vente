package Src.modeles;

public class Produit {

    private int    id;
    private String nom;
    private String referenceFabricant;
    private String categorie;
    private String certification;
    private double prix;
    private int    quantiteStock;
    private int    seuilAlerte;

    public Produit(int id, String nom, String referenceFabricant, String categorie, String certification, double prix, int quantiteStock, int seuilAlerte) {
        this.id                 = id;
        this.nom                = nom;
        this.referenceFabricant = referenceFabricant;
        this.categorie          = categorie;
        this.certification      = certification;
        this.prix               = prix;
        this.quantiteStock      = quantiteStock;
        this.seuilAlerte        = seuilAlerte;
    }

    public int    getId()                 { return id; }
    public String getNom()                { return nom; }
    public String getReferenceFabricant() { return referenceFabricant; }
    public String getCategorie()          { return categorie; }
    public String getCertification()      { return certification; }
    public double getPrix()               { return prix; }
    public int    getQuantiteStock()      { return quantiteStock; }
    public int    getSeuilAlerte()        { return seuilAlerte; }

    public void setNom(String nom)                               { this.nom = nom; }
    public void setReferenceFabricant(String referenceFabricant) { this.referenceFabricant = referenceFabricant; }
    public void setCategorie(String categorie)                   { this.categorie = categorie; }
    public void setCertification(String certification)           { this.certification = certification; }
    public void setPrix(double prix)                             { this.prix = prix; }
    public void setQuantiteStock(int quantite)                   { this.quantiteStock = quantite; }
    public void setSeuilAlerte(int seuil)                        { this.seuilAlerte = seuil; }

    public boolean estStockBas() { return quantiteStock <= seuilAlerte; }

    @Override
    public String toString() {
        return id + ";" + nom + ";" + referenceFabricant + ";" + categorie + ";" + certification + ";" + prix + ";" + quantiteStock + ";" + seuilAlerte;
    }
}
