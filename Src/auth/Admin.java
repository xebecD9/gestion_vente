package Src.auth;

public class Admin extends Utilisateur {

    public Admin(int id, String identifiant, String motDePasse, String nom) {
        super(id, identifiant, motDePasse, nom, "ADMIN");
    }
}
