package Src.auth;

public class Admin extends Utilisateur {

    public Admin(int id, String identifiant, String password, String name) {
        super(id, identifiant, password, name,"admin");
    }
}