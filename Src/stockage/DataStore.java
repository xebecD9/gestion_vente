package Src.stockage;

import Src.modeles.Produit;
import Src.modeles.Client;
import Src.modeles.Vente;
import Src.modeles.LigneVente;
import Src.ui.Console;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {

    private static final String FICHIER_PRODUITS = "data/produits.txt";
    private static final String FICHIER_CLIENTS  = "data/clients.txt";
    private static final String FICHIER_VENTES   = "data/ventes.txt";

    // ── Listes en mémoire ────────────────────────────────────────
    private List<Produit> produits = new ArrayList<>();
    private List<Client>  clients  = new ArrayList<>();
    private List<Vente>   ventes   = new ArrayList<>();

    // ── Singleton ────────────────────────────────────────────────
    private static DataStore instance;

    public static DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    private DataStore() {
        chargerProduits();
        chargerClients();
    }

    // ════════════════════════════════════════════════════════════
    // PRODUITS
    // ════════════════════════════════════════════════════════════
    public List<Produit> getProduits() { return produits; }

    public void ajouterProduit(Produit p) {
        produits.add(p);
        sauvegarderProduits();
    }

    public void supprimerProduit(int id) {
        produits.removeIf(p -> p.getId() == id);
        sauvegarderProduits();
    }

    public Produit trouverProduit(int id) {
        for (Produit p : produits) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public int prochainIdProduit() {
        int max = 0;
        for (Produit p : produits) if (p.getId() > max) max = p.getId();
        return max + 1;
    }

    private void chargerProduits() {
        File f = new File(FICHIER_PRODUITS);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String l = ligne.trim();
                if (l.isEmpty() || l.startsWith("#")) {
                    // ignorer
                } else {
                    String[] p = l.split("\\|");
                    if (p.length == 5) {
                        produits.add(new Produit(
                            Integer.parseInt(p[0].trim()),
                            p[1].trim(),
                            p[2].trim(),
                            Double.parseDouble(p[3].trim()),
                            Integer.parseInt(p[4].trim())
                        ));
                    }
                }
            }
        } catch (IOException e) {
            Console.afficherErreur("Erreur chargement produits : " + e.getMessage());
        }
    }

    public void sauvegarderProduits() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHIER_PRODUITS))) {
            pw.println("# id|nom|categorie|prix|quantiteStock");
            for (Produit p : produits) pw.println(p.toString());
        } catch (IOException e) {
            Console.afficherErreur("Erreur sauvegarde produits : " + e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════
    // CLIENTS
    // ════════════════════════════════════════════════════════════
    public List<Client> getClients() { return clients; }

    public void ajouterClient(Client c) {
        clients.add(c);
        sauvegarderClients();
    }

    public void supprimerClient(int id) {
        clients.removeIf(c -> c.getId() == id);
        sauvegarderClients();
    }

    public Client trouverClient(int id) {
        for (Client c : clients) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public int prochainIdClient() {
        int max = 0;
        for (Client c : clients) if (c.getId() > max) max = c.getId();
        return max + 1;
    }

    private void chargerClients() {
        File f = new File(FICHIER_CLIENTS);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String l = ligne.trim();
                if (l.isEmpty() || l.startsWith("#")) {
                    // ignorer
                } else {
                    String[] p = l.split("\\|");
                    if (p.length == 5) {
                        clients.add(new Client(
                            Integer.parseInt(p[0].trim()),
                            p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim()
                        ));
                    }
                }
            }
        } catch (IOException e) {
            Console.afficherErreur("Erreur chargement clients : " + e.getMessage());
        }
    }

    public void sauvegarderClients() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHIER_CLIENTS))) {
            pw.println("# id|nom|telephone|email|adresse");
            for (Client c : clients) pw.println(c.toString());
        } catch (IOException e) {
            Console.afficherErreur("Erreur sauvegarde clients : " + e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════
    // VENTES
    // ════════════════════════════════════════════════════════════
    public List<Vente> getVentes() { return ventes; }

    public void ajouterVente(Vente v) {
        ventes.add(v);
        sauvegarderVentes();
    }

    public int prochainIdVente() {
        int max = 0;
        for (Vente v : ventes) if (v.getId() > max) max = v.getId();
        return max + 1;
    }

    public void sauvegarderVentes() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHIER_VENTES))) {
            pw.println("# id|clientId|vendeurId|date|total|annulee");
            for (Vente v : ventes) {
                pw.println(v.getId() + "|" + v.getClient().getId() + "|"
                    + v.getVendeurId() + "|" + v.getDateFormatee()
                    + "|" + v.getTotalTTC() + "|" + v.isAnnulee());
            }
        } catch (IOException e) {
            Console.afficherErreur("Erreur sauvegarde ventes : " + e.getMessage());
        }
    }
}
