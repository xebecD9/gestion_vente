package Src.ui;

import java.util.Scanner;
import Src.auth.*;
public class MenuPrincipal {
    private AuthService authService;
    private Scanner scanner;

    //constructeur
    public MenuPrincipal() {
        this.authService = new AuthService();
        this.scanner = new Scanner(System.in);
    }
    public void demarrer(){
        Console.afficherBanniere();
        Console.askinput("Identifiant");
        String identifiant = scanner.nextLine().trim();
        Console.askinput("password");
        String password = scanner.nextLine().trim();
        Console.separateur();

        Utilisateur u = authService.login(identifiant, password);

        if(u ==null){
            Console.printError("Connexion impossible");
            scanner.close();
            return;
        }
        Console.printSuccess("Bienvenue" + u.getName() + "!");
        Console.printInfo("Role" + u.getRole());
        Console.separateur();


        if(u.getRole().equals("admin")){
            MenuAdmin.afficherMenu();
        }
        else if(u.getRole().equals("vendeur")){
            MenuVendeur.afficherMenu();

        }
        else{
            Console.printError("Role " + u.getRole() + "non reconnu");
        }
        authService.logout();
        Console.separateur();
        Console.printInfo("Deconnexion effectuee");
        scanner.close();
    }
    
}
