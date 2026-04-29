package Src.auth;

import Src.ui.Console;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.security.MessageDigest;
import java.math.BigInteger;


public class AuthService {

    private static final String FICHIERS_UTILISATEURS = "data/utilisateurs.txt";
    private List<Utilisateur> utilisateurs;
    private Utilisateur utilisateurConnecte;

    public AuthService() {
        this.utilisateurs = new ArrayList<>();
        this.utilisateurConnecte = null;
        chargerUtilisateurs();
    }

    public static String hasher(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes("UTF-8"));
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hash = new StringBuilder(no.toString(16));

            while (hash.length() < 32) {
                hash.insert(0, "0");
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du hashage : " + e.getMessage());
        }
    }

    public Utilisateur login(String identifiant, String password) {
        String hashSaisi = hasher(password);

        for (Utilisateur u : utilisateurs) {
            if (u.getIdentifiant().equals(identifiant)) {
                if (u.getPassword().equals(hashSaisi)) {
                    this.utilisateurConnecte = u;
                    return u;
                } else {
                    Console.printError("Mot de passe incorrect.");
                    return null;
                }
            }
        }
        
        Console.printError("Utilisateur introuvable.");
        return null;
    }
    public void logout() {
        this.utilisateurConnecte = null;
        Console.printInfo("Déconnexion réussie.");
    }
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
    private void chargerUtilisateurs(){
        File fichier = new File(FICHIERS_UTILISATEURS);
        if (!fichier.exists()) {
            Console.printWarning("Aucun utilisateur trouvé. Le fichier " + FICHIERS_UTILISATEURS + " est manquant.");
            return;
        }
        //ici on lit le fichier et on crée les utilisateurs en fonction de leur rôle
        try(BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while((ligne = br.readLine()) !=null){
                String lignenettoye = ligne.trim();
                if(lignenettoye.isEmpty() || lignenettoye.startsWith("#")){

                }else{
            String[] parts = ligne.split("\\|");
            if(parts.length != 5){
                Console.printWarning("Ligne mal formatée dans " + FICHIERS_UTILISATEURS + " : " + lignenettoye);

            }else{
                try{
            int id = Integer.parseInt(parts[0]);
            String ident = parts[1].trim();
            String psw = parts[2].trim();
            String name = parts[3].trim();
            String role = parts[4].trim();

            if (role.equalsIgnoreCase("admin")){
                utilisateurs.add(new Admin(id, ident, psw, name));
            } else if (role.equalsIgnoreCase("vendeur")){
                utilisateurs.add(new Vendeur(id, ident, psw, name));
            } else {
                Console.printWarning("Rôle inconnu pour l'utilisateur " + ident + " : " + role);
            }

        }catch(NumberFormatException e){
            Console.printError("ID invalide sur la ligne:" + lignenettoye );
        }
    }
}
}
    Console.printSuccess(utilisateurs.size() + "utilisateurs charges ");
}catch(IOException e){
    Console.printError("Erreur de lecture : " + e.getMessage()) ;
}
}
    public boolean estconnecte(){
        return this.utilisateurConnecte != null;
    }
}    

    
