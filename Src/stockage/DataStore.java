package Src.stockage;

import Src.modeles.*;
import Src.ui.Console;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class DataStore {

    // ── Chemins des fichiers texte (créés dynamiquement) ─────────
    private static final String FICHIER_PRODUITS   = "data/produits.txt";
    private static final String FICHIER_CLIENTS    = "data/clients.txt";
    private static final String FICHIER_VENTES     = "data/ventes.txt";
    private static final String FICHIER_MOUVEMENTS = "data/mouvements.txt";

    /** Listes en mémoire (Collections). */
    private List<Produit>        produits   = new ArrayList<>();
    private List<Client>         clients    = new ArrayList<>();
    private List<Vente>          ventes     = new ArrayList<>();
    private List<MouvementStock> mouvements = new ArrayList<>();

    /** Instance unique du Singleton. */
    private static DataStore instance;

    /**
     * Méthode d'accès globale à l'instance unique (Pattern Singleton).
     * @return L'instance unique de DataStore.
     */
    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    /**
     * Constructeur privé pour empêcher l'instanciation externe (Pattern Singleton).
     * Au démarrage, il charge toutes les données depuis les fichiers s'ils existent.
     */
    private DataStore() {
        chargerProduits();
        chargerClients();
        chargerVentes();
        chargerMouvements();
    }
    
    /**
     * Méthode utilitaire interne pour s'assurer que le dossier "data" existe
     * avant de tenter d'écrire dans un fichier.
     * @param cheminFichier Chemin complet du fichier à écrire.
     */
    private void verifierDossier(String cheminFichier) {
        File fichier = new File(cheminFichier);
        File dossier = fichier.getParentFile();
        if (dossier != null && !dossier.exists()) {
            dossier.mkdirs(); // Crée le dossier dynamiquement
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  GESTION DES PRODUITS (Équipements)
    // ═══════════════════════════════════════════════════════════════
    
    public List<Produit> getProduits() { return produits; }

    /**
     * Ajoute un produit à la mémoire et sauvegarde le fichier.
     * @param p Le produit à ajouter
     */
    public void ajouterProduit(Produit p) {
        produits.add(p);
        sauvegarderProduits();
    }

    /**
     * Supprime un produit via son ID (Lambda expression).
     * @param id L'identifiant du produit
     */
    public void supprimerProduit(int id) {
        // removeIf permet de supprimer avec une condition concise (Java 8+)
        produits.removeIf(p -> p.getId() == id);
        sauvegarderProduits();
    }

    /**
     * Recherche un produit par son identifiant.
     */
    public Produit trouverProduit(int id) {
        for (Produit p : produits) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    /**
     * Génère dynamiquement le prochain ID disponible (Auto-incrément).
     */
    public int prochainIdProduit() {
        int max = 0;
        for (Produit p : produits) {
            if (p.getId() > max) max = p.getId();
        }
        return max + 1;
    }

    /**
     * Charge les équipements depuis le fichier texte.
     */
    private void chargerProduits() {
        File f = new File(FICHIER_PRODUITS);
        if (!f.exists()) return; // Si le fichier n'existe pas, on arrête la lecture sans erreur
        
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String l = ligne.trim();
                // On ignore les lignes vides et les commentaires (commençant par #)
                if (l.isEmpty() || l.startsWith("#")) continue;
                
                String[] p = l.split(";"); // Les attributs sont séparés par un point-virgule
                if (p.length >= 8) {
                    produits.add(new Produit(
                        Integer.parseInt(p[0].trim()),  // ID
                        p[1].trim(),                    // Nom
                        p[2].trim(),                    // Ref
                        p[3].trim(),                    // Categorie
                        p[4].trim(),                    // Certification
                        Double.parseDouble(p[5].trim()),// Prix
                        Integer.parseInt(p[6].trim()),  // Quantité
                        Integer.parseInt(p[7].trim())   // Seuil
                    ));
                }
            }
        } catch (IOException e) {
            Console.afficherErreur("Erreur chargement équipements : " + e.getMessage());
        }
    }

    /**
     * Écrit tous les produits en mémoire dans le fichier texte.
     */
    public void sauvegarderProduits() {
        verifierDossier(FICHIER_PRODUITS);
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHIER_PRODUITS))) {
            // Écriture de l'en-tête (utile pour relire le fichier manuellement)
            pw.println("# Fichier des équipements aérospatiaux");
            pw.println("# Format : id;nom;referenceFabricant;categorie;certification;prix;quantiteStock;seuilAlerte");
            for (Produit p : produits) pw.println(p.toString());
        } catch (IOException e) {
            Console.afficherErreur("Erreur sauvegarde équipements : " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  GESTION DES CLIENTS (Compagnies)
    // ═══════════════════════════════════════════════════════════════
    
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
                if (l.isEmpty() || l.startsWith("#")) continue;
                
                String[] p = l.split(";");
                if (p.length >= 5) {
                    clients.add(new Client(
                        Integer.parseInt(p[0].trim()),
                        p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim()
                    ));
                }
            }
        } catch (IOException e) {
            Console.afficherErreur("Erreur chargement compagnies : " + e.getMessage());
        }
    }

    public void sauvegarderClients() {
        verifierDossier(FICHIER_CLIENTS);
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHIER_CLIENTS))) {
            pw.println("# Fichier des compagnies clientes");
            pw.println("# Format : id;nomCompagnie;contactPrincipal;email;adresse");
            for (Client c : clients) pw.println(c.toString());
        } catch (IOException e) {
            Console.afficherErreur("Erreur sauvegarde compagnies : " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  GESTION DES VENTES
    // ═══════════════════════════════════════════════════════════════
    
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
    
    /**
     * Charge les ventes. C'est plus complexe car une vente contient des Lignes de Vente.
     */
    private void chargerVentes() {
        File f = new File(FICHIER_VENTES);
        if (!f.exists()) return;
        
        // Formatteur pour convertir la chaîne texte en objet Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String l = ligne.trim();
                if (l.isEmpty() || l.startsWith("#")) continue;
                
                String[] p = l.split(";");
                if (p.length >= 7) {
                    // Extraction des attributs simples
                    int id = Integer.parseInt(p[0].trim());
                    int clientId = Integer.parseInt(p[1].trim());
                    String vendeurId = p[2].trim();
                    LocalDateTime dateVente = LocalDateTime.parse(p[3].trim(), formatter);
                    boolean annulee = Boolean.parseBoolean(p[5].trim());
                    String lignesStr = p[6].trim(); // Chaîne qui contient les ID produits et quantités
                    
                    // Reconstitution de la vente
                    Client client = trouverClient(clientId);
                    if (client != null) {
                        Vente vente = new Vente(id, client, vendeurId);
                        vente.setDate(dateVente);
                        vente.setAnnulee(annulee);
                        
                        // Traitement des lignes de vente (format "prodId:qte,prodId:qte")
                        if (!lignesStr.isEmpty()) {
                            String[] strLignes = lignesStr.split(","); // Séparation par virgule
                            for (String strLv : strLignes) {
                                String[] lvParts = strLv.split(":"); // Séparation par deux-points
                                if (lvParts.length == 2) {
                                    int prodId = Integer.parseInt(lvParts[0]);
                                    int qte = Integer.parseInt(lvParts[1]);
                                    
                                    Produit prod = trouverProduit(prodId);
                                    if (prod != null) {
                                        // Ajout de la ligne reconstituée à la vente
                                        vente.ajouterLigne(new LigneVente(prod, qte));
                                    }
                                }
                            }
                        }
                        ventes.add(vente);
                    }
                }
            }
        } catch (Exception e) {
            Console.afficherErreur("Erreur chargement ventes : " + e.getMessage());
        }
    }

    public void sauvegarderVentes() {
        verifierDossier(FICHIER_VENTES);
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHIER_VENTES))) {
            pw.println("# Historique des ventes aérospatiales");
            pw.println("# Format : id;clientId;vendeurId;date;total;annulee;lignes(prodId:qte,...)");
            for (Vente v : ventes) {
                pw.println(v.getId() + ";" + v.getClient().getId() + ";"
                    + v.getVendeurId() + ";" + v.getDateFormatee()
                    + ";" + v.getTotalTTC() + ";" + v.isAnnulee() + ";" + v.serialiserLignes());
            }
        } catch (IOException e) {
            Console.afficherErreur("Erreur sauvegarde ventes : " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  GESTION DES MOUVEMENTS DE STOCK
    // ═══════════════════════════════════════════════════════════════
    
    public List<MouvementStock> getMouvements() { return mouvements; }

    public void ajouterMouvement(MouvementStock m) {
        mouvements.add(m);
        sauvegarderMouvements();
    }

    public int prochainIdMouvement() {
        int max = 0;
        for (MouvementStock m : mouvements) if (m.getId() > max) max = m.getId();
        return max + 1;
    }

    private void chargerMouvements() {
        File f = new File(FICHIER_MOUVEMENTS);
        if (!f.exists()) return;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String l = ligne.trim();
                if (l.isEmpty() || l.startsWith("#")) continue;
                
                String[] p = l.split(";");
                if (p.length >= 7) {
                    MouvementStock m = new MouvementStock(
                        Integer.parseInt(p[0].trim()),
                        Integer.parseInt(p[2].trim()),
                        Integer.parseInt(p[3].trim()),
                        MouvementStock.TypeMouvement.valueOf(p[4].trim()), // Conversion de chaîne vers l'énumération TypeMouvement
                        MouvementStock.MotifMouvement.valueOf(p[5].trim()),// Conversion de chaîne vers l'énumération MotifMouvement
                        p[6].trim()
                    );
                    m.setDate(LocalDateTime.parse(p[1].trim(), formatter));
                    mouvements.add(m);
                }
            }
        } catch (Exception e) {
            Console.afficherErreur("Erreur chargement mouvements : " + e.getMessage());
        }
    }

    public void sauvegarderMouvements() {
        verifierDossier(FICHIER_MOUVEMENTS);
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHIER_MOUVEMENTS))) {
            pw.println("# Journal d'audit des mouvements de stocks");
            pw.println("# Format : id;date;produitId;quantite;type;motif;utilisateurId");
            for (MouvementStock m : mouvements) {
                pw.println(m.toString());
            }
        } catch (IOException e) {
            Console.afficherErreur("Erreur sauvegarde mouvements : " + e.getMessage());
        }
    }
}
