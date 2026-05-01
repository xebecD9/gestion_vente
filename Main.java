import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner       sc    = new Scanner(System.in);
        DataStore     store = new DataStore();
        ClientService svc   = new ClientService(store, sc);

        store.chargerClients();

        String choix;
        do {
            afficherMenu();
            choix = sc.nextLine().trim();

            switch (choix) {
                case "1" -> svc.ajouterClient();
                case "2" -> svc.modifierClient();
                case "3" -> svc.supprimerClient();
                case "4" -> svc.afficherTousLesClients();
                case "5" -> svc.rechercherClient();
                case "0" -> Console.succes("Au revoir !");
                default  -> Console.avertissement("Option invalide, réessayez.");
            }

        } while (!choix.equals("0"));

        sc.close();
    }

    private static void afficherMenu() {
        System.out.println(Console.BOLD + Console.CYAN);
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║     GESTION DES CLIENTS              ║");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.println("  ║  1. Ajouter un client                ║");
        System.out.println("  ║  2. Modifier un client               ║");
        System.out.println("  ║  3. Supprimer un client              ║");
        System.out.println("  ║  4. Afficher tous les clients        ║");
        System.out.println("  ║  5. Rechercher un client             ║");
        System.out.println("  ║  0. Quitter                          ║");
        System.out.println("  ╚══════════════════════════════════════╝" + Console.RESET);
        System.out.print("  Votre choix : ");
    }
}