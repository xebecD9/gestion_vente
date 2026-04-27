package main.java.com.gestionvente.ui;

import java.util.Scanner;

public class Console {
    // Couleurs de ma console
    public static final String SUCESS = "\033[42m";
    public static final String ERROR = "\033[41m";
    public static final String WARNING = "\033[43m";
    public static final String RESET = "\033[0m";
    public static final String TITLE = "\033[34m";
    public static final String INFO = "\033[36m";

    public static Scanner scanner = new Scanner(System.in);

    // Méthodes d'affichage simple 
    public static void printSuccess(String message) {
        System.out.println(SUCESS + message + RESET);
    }

    public static void printError(String message) {
        System.out.println(ERROR + message + RESET);
    }

    public static void printWarning(String message) {
        System.out.println(WARNING + message + RESET);
    }

    public static void printTitle(String message) {
        System.out.println(TITLE + message + RESET);
    }

    public static void printInfo(String message) {
        System.out.println(INFO + message + RESET);
    }

    public static void effacerecran() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void afficherBanniere() {
        System.out.println(TITLE + " ======================================================================" + RESET);
        System.out.println(TITLE + "   BIENVENUE DANS LE GESTIONNAIRE VENTE DE D.I.K.W" + RESET);
        System.out.println(TITLE + " ======================================================================" + RESET);
        System.out.println();
    }

    public static void afficherTableau(String[] entetes, String[][] lignes) {
        
        for (String entete : entetes) {
            System.out.print(String.format("%-20s", entete));
        }
        System.out.println("\n" + "-".repeat(entetes.length * 20));

        // Affichage des données
        for (String[] ligne : lignes) {
            for (String cellule : ligne) {
                System.out.print(String.format("%-20s", cellule));
            }
            System.out.println();
        }
    }

    public static String liresaisie(String message) {
        System.out.print(message + " : ");
        return scanner.nextLine();
    }

    public static int lireEntier(String message) {
        while (true) {
            try {
                return Integer.parseInt(liresaisie(message));
            } catch (NumberFormatException e) {
                printError("Erreur : veuillez saisir un nombre entier valide.");
            }
        }
    }

    public static String lireTextePur(String message) {
        while (true) {
            String saisie = liresaisie(message);
            // Vérifie si la saisie contient au moins une lettre et aucun chiffre
            if (saisie.matches("^[a-zA-ZÀ-ÿ\\s-]+$") && !saisie.trim().isEmpty()) {
                return saisie;
            } else {
                printError("Erreur : veuillez n'utiliser que des lettres.");
            }
        }
    }
} 
