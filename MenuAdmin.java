import java.util.Scanner;
public class MenuAdmin {

    static Scanner sc = new Scanner(System.in);

    public static void menu() {
        int choix;

        do {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Ventes du jour");
            System.out.println("2. Ventes de la semaine");
            System.out.println("3. Ventes du mois");
            System.out.println("4. Chiffre d'affaires total");
            System.out.println("5. Produit le plus vendu");
            System.out.println("6. Vendeur le plus performant");
            System.out.println("7. Exporter rapport");
            System.out.println("0. Quitter\n");
            System.out.println ("selectionner une option: ");

            choix = sc.nextInt();

            switch (choix) {
                case 1: RapportService.ventesDuJour(); break;
                case 2: RapportService.ventesSemaine(); break;
                case 3: RapportService.ventesMois(); break;
                case 4: System.out.println(RapportService.chiffreAffairesTotal()); break;
                case 5: RapportService.topProduit(); break;
                case 6: RapportService.topVendeur(); break;
                case 7: RapportService.exporterRapport(); break;
            }

        } while (choix != 0);
    }
}
