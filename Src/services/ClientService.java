package Src.services;

import Src.modeles.Client;
import Src.modeles.Vente;
import Src.stockage.DataStore;
import Src.ui.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientService {

    private DataStore store;
    private Scanner   scanner;

    public ClientService(Scanner scanner) {
        this.store   = DataStore.getInstance();
        this.scanner = scanner;
    }

    /** Ajouter une compagnie cliente. */
    public void ajouterClient() {
        Console.afficherTitre("Ajouter une compagnie cliente");

        Console.demanderSaisie("Nom de la compagnie");
        String nom = scanner.nextLine().trim();

        Console.demanderSaisie("Contact Principal (Téléphone)");
        String tel = scanner.nextLine().trim();

        Console.demanderSaisie("Email");
        String email = scanner.nextLine().trim();

        Console.demanderSaisie("Adresse du siège");
        String adresse = scanner.nextLine().trim();

        int id = store.prochainIdClient();
        Client c = new Client(id, nom, tel, email, adresse);
        store.ajouterClient(c);
        Console.afficherSucces("Compagnie ajoutée avec l'ID : " + id);
    }

    /** Modifier une compagnie. */
    public void modifierClient() {
        Console.afficherTitre("Modifier une compagnie");
        afficherTousClients();

        Console.demanderSaisie("ID de la compagnie à modifier");
        int id;
        try { id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); return; }

        Client c = store.trouverClient(id);
        if (c == null) { Console.afficherErreur("Compagnie introuvable."); return; }

        Console.demanderSaisie("Nouveau nom [" + c.getNomCompagnie() + "]");
        String nom = scanner.nextLine().trim();
        if (!nom.isEmpty()) c.setNomCompagnie(nom);

        Console.demanderSaisie("Nouveau contact [" + c.getContactPrincipal() + "]");
        String tel = scanner.nextLine().trim();
        if (!tel.isEmpty()) c.setContactPrincipal(tel);

        Console.demanderSaisie("Nouvel email [" + c.getEmail() + "]");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) c.setEmail(email);

        Console.demanderSaisie("Nouvelle adresse [" + c.getAdresse() + "]");
        String adresse = scanner.nextLine().trim();
        if (!adresse.isEmpty()) c.setAdresse(adresse);

        store.sauvegarderClients();
        Console.afficherSucces("Compagnie modifiée.");
    }

    /** Supprimer une compagnie. */
    public void supprimerClient() {
        Console.afficherTitre("Supprimer une compagnie");
        afficherTousClients();

        Console.demanderSaisie("ID de la compagnie à supprimer");
        int id;
        try { id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); return; }

        if (store.trouverClient(id) == null) {
            Console.afficherErreur("Compagnie introuvable."); return;
        }
        store.supprimerClient(id);
        Console.afficherSucces("Compagnie supprimée.");
    }

    /** Afficher toutes les compagnies. */
    public void afficherTousClients() {
        Console.nettoyerEcran();
        Console.afficherTitre("Liste des compagnies");
        List<Client> liste = store.getClients();
        if (liste.isEmpty()) { Console.afficherInfo("Aucune compagnie enregistrée."); return; }

        System.out.println(Console.MAGENTA + "  +" + "-".repeat(90) + "+" + Console.RESET);
        System.out.println(Console.MAGENTA + "  | " + Console.CYAN + Console.GRAS
            + String.format("%-4s | %-20s | %-15s | %-20s | %-20s", "ID", "Compagnie", "Contact", "Email", "Adresse")
            + Console.MAGENTA + " |" + Console.RESET);
        System.out.println(Console.MAGENTA + "  +" + "-".repeat(90) + "+" + Console.RESET);

        for (Client c : liste) {
            System.out.printf(Console.MAGENTA + "  | " + Console.RESET + "%-4d " + Console.MAGENTA + "|" + Console.RESET + " %-20s " + Console.MAGENTA + "|" + Console.RESET + " %-15s " + Console.MAGENTA + "|" + Console.RESET + " %-20s " + Console.MAGENTA + "|" + Console.RESET + " %-20s " + Console.MAGENTA + "|%n" + Console.RESET,
                c.getId(), 
                c.getNomCompagnie().length() > 20 ? c.getNomCompagnie().substring(0, 17) + "..." : c.getNomCompagnie(), 
                c.getContactPrincipal(), 
                c.getEmail().length() > 20 ? c.getEmail().substring(0, 17) + "..." : c.getEmail(), 
                c.getAdresse().length() > 20 ? c.getAdresse().substring(0, 17) + "..." : c.getAdresse());
        }
        System.out.println(Console.MAGENTA + "  +" + "-".repeat(90) + "+" + Console.RESET);
        System.out.println();
    }

    /** Rechercher une compagnie. */
    public void rechercherClient() {
        Console.afficherTitre("Rechercher une compagnie");
        Console.demanderSaisie("Nom ou ID");
        String terme = scanner.nextLine().trim();

        List<Client> resultats = new ArrayList<>();
        for (Client c : store.getClients()) {
            if (c.getNomCompagnie().toLowerCase().contains(terme.toLowerCase())
                    || String.valueOf(c.getId()).equals(terme)) {
                resultats.add(c);
            }
        }

        if (resultats.isEmpty()) {
            Console.afficherInfo("Aucune compagnie trouvée.");
        } else {
            for (Client c : resultats) {
                System.out.printf("[%d] %s — %s — %s%n",
                    c.getId(), c.getNomCompagnie(), c.getContactPrincipal(), c.getEmail());
            }
        }
    }
 
    /** Historique achats. */
    public void afficherHistoriqueClient() {
        Console.afficherTitre("Historique d'une compagnie");
        Console.demanderSaisie("ID de la compagnie");
        int id;
        //on essaye de convertir l'entree en entier     
        try { id = Integer.parseInt(scanner.nextLine().trim()); }
        //si la conversion echoue, on affiche une erreur
        catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); return; }
        //on cherche le client par son id
        Client c = store.trouverClient(id);
        if (c == null) { Console.afficherErreur("Compagnie introuvable."); return; }

        Console.afficherInfo("Historique de : " + c.getNomCompagnie());
        Console.separateur();

        boolean trouve = false;
        for (Vente v : store.getVentes()) {
            if (v.getClient().getId() == id && !v.isAnnulee()) {
                System.out.printf("Vente #%d — %s — Total : %.2f F%n",
                    v.getId(), v.getDateFormatee(), v.getTotalTTC());
                trouve = true;
            }
        }
        if (!trouve) Console.afficherInfo("Aucun achat pour cette compagnie.");
    }
}
