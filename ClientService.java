import java.util.*;
import java.util.stream.Collectors;

public class ClientService {

    private final DataStore ds;
    private final Scanner   sc;

    public ClientService(DataStore ds, Scanner sc) {
        this.ds = ds;
        this.sc = sc;
    }

    // ── 1. Ajouter ─────────────────────────────────────────────────
    public void ajouterClient() {
        Console.titre("NOUVEAU CLIENT");

        String nom       = saisirChamp("Nom        : ");
        String prenom    = saisirChamp("Prénom     : ");
        String telephone = saisirTelephone();
        String email     = saisirEmail();
        String adresse   = saisirChamp("Adresse    : ");

        Client c = new Client(nom, prenom, telephone, email, adresse);
        ds.ajouterClient(c);

        Console.succes("✔  Client enregistré avec l'ID : " + c.getId());
        Console.pause(sc);
    }

    // ── 2. Modifier ────────────────────────────────────────────────
    public void modifierClient() {
        Console.titre("MODIFIER UN CLIENT");

        Client c = choisirParId("ID du client à modifier : ");
        if (c == null) return;

        ficheClient(c);
        Console.info("Laissez vide pour conserver la valeur actuelle.");
        Console.separateur();

        String nom  = opt("Nouveau nom        [" + c.getNom()      + "] : ");
        String pre  = opt("Nouveau prénom     [" + c.getPrenom()   + "] : ");
        String tel  = opt("Nouveau téléphone  [" + c.getTelephone()+ "] : ");
        String mail = opt("Nouvel email       [" + c.getEmail()    + "] : ");
        String adr  = opt("Nouvelle adresse   [" + c.getAdresse()  + "] : ");

        if (!nom.isEmpty())  c.setNom(nom);
        if (!pre.isEmpty())  c.setPrenom(pre);
        if (!tel.isEmpty())  c.setTelephone(tel);
        if (!mail.isEmpty()) c.setEmail(mail);
        if (!adr.isEmpty())  c.setAdresse(adr);

        ds.sauvegarderClients();
        Console.succes("✔  Client mis à jour avec succès.");
        Console.pause(sc);
    }

    // ── 3. Supprimer ───────────────────────────────────────────────
    public void supprimerClient() {
        Console.titre("SUPPRIMER UN CLIENT");

        Client c = choisirParId("ID du client à supprimer : ");
        if (c == null) return;

        ficheClient(c);
        Console.avertissement("⚠  Cette action est irréversible !");
        System.out.print("  Confirmer la suppression ? (o/n) : ");
        String rep = sc.nextLine().trim().toLowerCase();

        if (rep.equals("o") || rep.equals("oui")) {
            ds.supprimerClient(c.getId());
            Console.succes("✔  Client supprimé avec succès.");
        } else {
            Console.info("Suppression annulée.");
        }
        Console.pause(sc);
    }

    // ── 4. Afficher tous ───────────────────────────────────────────
    public void afficherTousLesClients() {
        Console.titre("LISTE DES CLIENTS");

        List<Client> liste = ds.getTousLesClients();
        if (liste.isEmpty()) {
            Console.info("Aucun client enregistré.");
            Console.pause(sc);
            return;
        }
        tableauClients(liste);
        Console.info("Total : " + liste.size() + " client(s).");
        Console.pause(sc);
    }

    // ── 5. Rechercher ──────────────────────────────────────────────
    public void rechercherClient() {
        Console.titre("RECHERCHE CLIENT");
        System.out.print("  Entrez un ID, un nom ou un prénom : ");
        String terme = sc.nextLine().trim();

        if (terme.isEmpty()) {
            Console.avertissement("Critère de recherche vide.");
            Console.pause(sc);
            return;
        }

        List<Client> res = new ArrayList<>();
        try {
            Client c = ds.getClientParId(Integer.parseInt(terme));
            if (c != null) res.add(c);
        } catch (NumberFormatException ignored) {}

        if (res.isEmpty()) res = ds.rechercherParNom(terme);

        if (res.isEmpty()) Console.avertissement("Aucun résultat pour : \"" + terme + "\"");
        else { Console.succes(res.size() + " résultat(s) trouvé(s) :"); tableauClients(res); }

        Console.pause(sc);
    }

    // ── Fiche détaillée ────────────────────────────────────────────
    public void ficheClient(Client c) {
        Console.separateur();
        System.out.println("  ID        : " + c.getId());
        System.out.println("  Nom       : " + c.getNomComplet());
        System.out.println("  Téléphone : " + c.getTelephone());
        System.out.println("  Email     : " + c.getEmail());
        System.out.println("  Adresse   : " + c.getAdresse());
        Console.separateur();
    }

    // ── Tableau ASCII ──────────────────────────────────────────────
    private void tableauClients(List<Client> liste) {
        Console.separateur();
        System.out.println(Console.CYAN + Console.BOLD +
                String.format("  %-5s %-20s %-15s %-25s %-20s",
                        "ID", "NOM COMPLET", "TÉLÉPHONE", "EMAIL", "ADRESSE")
                + Console.RESET);
        Console.separateur();
        for (Client c : liste) {
            System.out.println(String.format(
                    "  %-5d %-20s %-15s %-25s %-20s",
                    c.getId(),
                    tronq(c.getNomComplet(), 20),
                    c.getTelephone(),
                    tronq(c.getEmail(), 25),
                    tronq(c.getAdresse(), 20)
            ));
        }
        Console.separateur();
    }

    // ── Saisies validées ───────────────────────────────────────────
    private String saisirChamp(String label) {
        String v;
        do {
            System.out.print("  " + label);
            v = sc.nextLine().trim();
            if (v.isEmpty()) Console.erreur("Ce champ est obligatoire.");
        } while (v.isEmpty());
        return v;
    }

    private String opt(String label) {
        System.out.print("  " + label);
        return sc.nextLine().trim();
    }

    private String saisirTelephone() {
        String t;
        do {
            System.out.print("  Téléphone  : ");
            t = sc.nextLine().trim();
            if (!t.matches("^[0-9+\\-\\s]{8,15}$")) {
                Console.erreur("Numéro invalide (8 à 15 chiffres).");
                t = "";
            }
        } while (t.isEmpty());
        return t;
    }

    private String saisirEmail() {
        String e;
        do {
            System.out.print("  Email      : ");
            e = sc.nextLine().trim();
            if (!e.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
                Console.erreur("Email invalide (ex: nom@domaine.com).");
                e = "";
            }
        } while (e.isEmpty());
        return e;
    }

    private Client choisirParId(String msg) {
        System.out.print("  " + msg);
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            Client c = ds.getClientParId(id);
            if (c == null) Console.erreur("Aucun client avec l'ID " + id);
            return c;
        } catch (NumberFormatException e) {
            Console.erreur("ID invalide. Entrez un nombre entier.");
            return null;
        }
    }

    private String tronq(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}