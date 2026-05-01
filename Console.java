import java.util.Scanner;

public class Console {

    public static final String RESET  = "\u001B[0m";
    public static final String CYAN   = "\u001B[36m";
    public static final String GREEN  = "\u001B[32m";
    public static final String RED    = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BOLD   = "\u001B[1m";

    public static void titre(String texte) {
        System.out.println("\n" + BOLD + CYAN);
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.printf ("  ║  %-36s║%n", texte);
        System.out.println("  ╚══════════════════════════════════════╝" + RESET);
    }

    public static void separateur() {
        System.out.println("  " + "─".repeat(70));
    }

    public static void succes(String msg) {
        System.out.println(GREEN + "  " + msg + RESET);
    }

    public static void erreur(String msg) {
        System.out.println(RED + "  ✘ " + msg + RESET);
    }

    public static void info(String msg) {
        System.out.println(CYAN + "  ℹ " + msg + RESET);
    }

    public static void avertissement(String msg) {
        System.out.println(YELLOW + "  ⚠ " + msg + RESET);
    }

    public static void pause(Scanner sc) {
        System.out.print("\n  Appuyez sur ENTRÉE pour continuer...");
        sc.nextLine();
    }
}