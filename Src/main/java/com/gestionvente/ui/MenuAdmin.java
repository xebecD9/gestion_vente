package main.java.com.gestionvente.ui;

public class MenuAdmin {

    public static void afficherMenu() {
        
        Console.printTitle("=== MENU ADMINISTRATEUR ===");
        Console.printInfo("1. Gérer les produits");
        Console.printInfo("2. Gérer les clients");
        Console.printInfo("3. Gérer les vendeurs");
        Console.printInfo("3. Gérer les commandes");
        Console.printInfo("4. Gérer les utilisateurs");
        Console.printInfo("5. Se déconnecter");
        Console.printInfo("0. Quitter l'application");
        Console.printTitle("===========================");
        

        while (true) {
            Console.printInfo("Veuillez choisir une option : ");
            String choix = Console.scanner.nextLine();

            switch (choix) {
                case "1":
                    // Appeler la méthode pour gérer les produits
                    break;
                case "2":
                    // Appeler la méthode pour gérer les clients
                    break;
                case "3":
                    // Appeler la méthode pour gérer les vendeurs
                    break;
                case "4":
                    // Appeler la méthode pour gérer les commandes
                    break;
                case "5":
                    // Appeler la méthode pour gérer les utilisateurs
                    break;
                case "6":
                    // Appeler la méthode pour se déconnecter
                    break;
                case "0":
                    Console.printInfo("Merci d'avoir utilisé l'application. Au revoir !");
                    System.exit(0);
                default:
                    Console.printError("Option invalide. Veuillez réessayer.");
    }
    
    
}
}
}
