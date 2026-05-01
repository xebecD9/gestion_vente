import java.util.Scanner;

public class MenuVendeur {

    static Scanner sc = new Scanner(System.in);

    public static void menu() {
        int choix;

        do {
            System.out.println("\n=== MENU VENDEUR ===");
            System.out.println("1. Voir ventes du jour");
            System.out.println("0. Quitter");

            choix = sc.nextInt();

            if (choix == 1) {
                RapportService.ventesDuJour();
            }

        } while (choix != 0);
    }
}
