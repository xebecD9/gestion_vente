package Src.auth;

public class Vendeur extends Utilisateur {

    private double chiffreAffaires;

    public Vendeur(int id, String identifiant, String motDePasse, String nom) {
        super(id, identifiant, motDePasse, nom, "VENDEUR");
        this.chiffreAffaires = 0.0;
    }

    public double getChiffreAffaires()      { return chiffreAffaires; }
    public void ajouterVente(double montant) { this.chiffreAffaires += montant; }
}
