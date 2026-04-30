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

    // ── Ajouter ──────────────────────────────────────────────────
    public void ajouterClient() {
        Console.afficherTitre("Ajouter un client");

        Console.demanderSaisie("Nom");
        String nom = scanner.nextLine().trim();

        Console.demanderSaisie("Téléphone");
        String tel = scanner.nextLine().trim();

        Console.demanderSaisie("Email");
        String email = scanner.nextLine().trim();

        Console.demanderSaisie("Adresse");
        String adresse = scanner.nextLine().trim();

        int id = store.prochainIdClient();
        Client c = new Client(id, nom, tel, email, adresse);
        store.ajouterClient(c);
        Console.afficherSucces("Client ajouté avec l'ID : " + id);
    }

    // ── Modifier ─────────────────────────────────────────────────
    public void modifierClient() {
        Console.afficherTitre("Modifier un client");
        afficherTousClients();

        Console.demanderSaisie("ID du client à modifier");
        int id;
        try { id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); return; }

        Client c = store.trouverClient(id);
        if (c == null) { Console.afficherErreur("Client introuvable."); return; }

        Console.demanderSaisie("Nouveau nom [" + c.getNom() + "]");
        String nom = scanner.nextLine().trim();
        if (!nom.isEmpty()) c.setNom(nom);

        Console.demanderSaisie("Nouveau téléphone [" + c.getTelephone() + "]");
        String tel = scanner.nextLine().trim();
        if (!tel.isEmpty()) c.setTelephone(tel);

        Console.demanderSaisie("Nouvel email [" + c.getEmail() + "]");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) c.setEmail(email);

        Console.demanderSaisie("Nouvelle adresse [" + c.getAdresse() + "]");
        String adresse = scanner.nextLine().trim();
        if (!adresse.isEmpty()) c.setAdresse(adresse);

        store.sauvegarderClients();
        Console.afficherSucces("Client modifié.");
    }

    // ── Supprimer ────────────────────────────────────────────────
    public void supprimerClient() {
        Console.afficherTitre("Supprimer un client");
        afficherTousClients();

        Console.demanderSaisie("ID du client à supprimer");
        int id;
        try { id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); return; }

        if (store.trouverClient(id) == null) {
            Console.afficherErreur("Client introuvable."); return;
        }
        store.supprimerClient(id);
        Console.afficherSucces("Client supprimé.");
    }

    // ── Afficher tous ────────────────────────────────────────────
    public void afficherTousClients() {
        Console.afficherTitre("Liste des clients");
        List<Client> liste = store.getClients();
        if (liste.isEmpty()) { Console.afficherInfo("Aucun client enregistré."); return; }

        System.out.println(Console.GRAS
            + String.format("%-5s %-20s %-15s %-25s %-20s", "ID", "Nom", "Téléphone", "Email", "Adresse")
            + Console.RESET);
        Console.separateur();

        for (Client c : liste) {
            System.out.printf("%-5d %-20s %-15s %-25s %-20s%n",
                c.getId(), c.getNom(), c.getTelephone(), c.getEmail(), c.getAdresse());
        }
        Console.separateur();
    }

    // ── Rechercher ───────────────────────────────────────────────
    public void rechercherClient() {
        Console.afficherTitre("Rechercher un client");
        Console.demanderSaisie("Nom ou ID");
        String terme = scanner.nextLine().trim();

        List<Client> resultats = new ArrayList<>();
        for (Client c : store.getClients()) {
            if (c.getNom().toLowerCase().contains(terme.toLowerCase())
                    || String.valueOf(c.getId()).equals(terme)) {
                resultats.add(c);
            }
        }

        if (resultats.isEmpty()) {
            Console.afficherInfo("Aucun client trouvé.");
        } else {
            for (Client c : resultats) {
                System.out.printf("[%d] %s — %s — %s%n",
                    c.getId(), c.getNom(), c.getTelephone(), c.getEmail());
            }
        }
    }

    // ── Historique achats ────────────────────────────────────────
    public void afficherHistoriqueClient() {
        Console.afficherTitre("Historique d'un client");
        Console.demanderSaisie("ID du client");
        int id;
        try { id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { Console.afficherErreur("ID invalide."); return; }

        Client c = store.trouverClient(id);
        if (c == null) { Console.afficherErreur("Client introuvable."); return; }

        Console.afficherInfo("Historique de : " + c.getNom());
        Console.separateur();

        boolean trouve = false;
        for (Vente v : store.getVentes()) {
            if (v.getClient().getId() == id && !v.isAnnulee()) {
                System.out.printf("Vente #%d — %s — Total : %.2f F%n",
                    v.getId(), v.getDateFormatee(), v.getTotalTTC());
                trouve = true;
            }
        }
        if (!trouve) Console.afficherInfo("Aucun achat pour ce client.");
    }
}
