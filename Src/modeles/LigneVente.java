package Src.modeles;

public class LigneVente {

    private Produit produit;
    private int     quantite;
    private double  prixUnitaire;

    public LigneVente(Produit produit, int quantite) {
        this.produit      = produit;
        this.quantite     = quantite;
        this.prixUnitaire = produit.getPrix();
    }

    public Produit getProduit()      { return produit; }
    public int     getQuantite()     { return quantite; }
    public double  getPrixUnitaire() { return prixUnitaire; }
    public double  getSousTotal()    { return prixUnitaire * quantite; }

    @Override
    public String toString() {
        return produit.getNom() + " x" + quantite + " @ " + prixUnitaire + " = " + getSousTotal();
    }
}
