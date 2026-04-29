package Src.auth;

public class Vendeur extends Utilisateur {
    double totalVentes;

    public Vendeur(int id, String identifiant, String password, String name) {
        super(id, identifiant, password, name, "Vendeur");
        this.totalVentes = 0.0;
        
    }
    public double getTotalVentes() {
        return totalVentes;
    }
    public void ajouterVente(double montant) {
        if (montant > 0) {
        this.totalVentes += montant;
    }
}
}