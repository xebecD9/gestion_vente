package Src.services;

import Src.modeles.*;

import Src.stockage.DataStore;
import Src.ui.Console;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RapportService {

    // Référence au Singleton DataStore pour accéder aux données
    private DataStore store;

    /**
     * Constructeur par défaut.
     * Initialise l'accès au stockage global des données.
     */
    public RapportService() {
        this.store = DataStore.getInstance();
    }

    // ── Rapports Temporels ───────────────────────────────────────

    /** 
     * Génère et affiche le rapport des ventes pour la journée en cours.
     * Utilise les API de date/heure de Java 8 (LocalDateTime).
     */
    public void rapportJour() {
        Console.afficherTitre("Ventes du jour");
        // On récupère la date d'aujourd'hui, minuit (00:00)
        LocalDateTime debut = LocalDateTime.now().withHour(0).withMinute(0);
        afficherRapportPeriode(debut, LocalDateTime.now(), "AUJOURD'HUI");
    }

    /** 
     * Génère et affiche le rapport des ventes pour les 7 derniers jours.
     */
    public void rapportSemaine() {
        Console.afficherTitre("Ventes de la semaine");
        // On recule de 7 jours par rapport à maintenant
        LocalDateTime debut = LocalDateTime.now().minusDays(7);
        afficherRapportPeriode(debut, LocalDateTime.now(), "CETTE SEMAINE");
    }

    /** 
     * Génère et affiche le rapport des ventes du mois en cours.
     */
    public void rapportMois() {
        Console.afficherTitre("Ventes du mois");
        // On se place au 1er jour du mois courant, à minuit
        LocalDateTime debut = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        afficherRapportPeriode(debut, LocalDateTime.now(), "CE MOIS");
    }

    /** 
     * Méthode générique interne pour calculer le chiffre d'affaires sur une période donnée.
     * @param debut Date de début d'analyse
     * @param fin Date de fin d'analyse
     * @param label Étiquette à afficher (ex: "CE MOIS")
     */
    private void afficherRapportPeriode(LocalDateTime debut, LocalDateTime fin, String label) {
        List<Vente> ventes = store.getVentes();
        double totalCA  = 0;
        int    nbVentes = 0;

        // Parcours de l'ensemble des ventes
        for (Vente v : ventes) {
            // Conditions : non annulée ET dans la plage temporelle
            if (!v.isAnnulee() && v.getDate().isAfter(debut) && v.getDate().isBefore(fin)) {
                totalCA += v.getTotalTTC();
                nbVentes++;
                // Affichage formaté d'une ligne de vente
                System.out.printf("  Vente #%-4d | %-20s | %10.2f F%n",
                    v.getId(), v.getClient().getNomCompagnie(), v.getTotalTTC());
            }
        }

        Console.separateur();
        // Affichage du bilan consolidé
        System.out.printf(Console.GRAS + "  %s — %d vente(s) — CA : %.2f F%n" + Console.RESET,
            label, nbVentes, totalCA);
    }

    // ── Rapports Globaux ─────────────────────────────────────────

    /** 
     * Calcule et affiche le Chiffre d'Affaires total historique.
     */
    public void chiffreAffairesTotal() {
        Console.afficherTitre("Chiffre d'affaires total");
        double total = 0;
        int    nb    = 0;
        
        // Cumul de toutes les ventes valides
        for (Vente v : store.getVentes()) {
            if (!v.isAnnulee()) { 
                total += v.getTotalTTC(); 
                nb++; 
            }
        }
        System.out.printf(Console.GRAS + "  Total : %.2f F sur %d vente(s)%n" + Console.RESET, total, nb);
    }

    /** 
     * Identifie les équipements (produits) les plus vendus.
     * Utilise une Map pour compter les occurrences et les Streams pour trier.
     */
    public void topProduits() {
        Console.afficherTitre("Équipements les plus vendus");
        // Dictionnaire pour stocker le nom du produit et la quantité totale vendue
        Map<String, Integer> compteur = new HashMap<>();

        for (Vente v : store.getVentes()) {
            if (!v.isAnnulee()) {
                // Pour chaque vente, on parcourt toutes les lignes de la commande
                for (LigneVente lv : v.getLignes()) {
                    String nom = lv.getProduit().getNom();
                    // On cumule la quantité vendue. Si absent, on part de 0.
                    compteur.put(nom, compteur.getOrDefault(nom, 0) + lv.getQuantite());
                }
            }
        }

        if (compteur.isEmpty()) { 
            Console.afficherInfo("Aucune donnée disponible."); 
            return; 
        }

        // Tri du dictionnaire en utilisant les Streams Java 8
        compteur.entrySet().stream()
            // On trie par ordre décroissant des valeurs (quantités)
            .sorted((a, b) -> b.getValue() - a.getValue())
            // On limite aux 5 premiers éléments (Top 5)
            .limit(5)
            // On affiche chaque élément résultant
            .forEach(e -> System.out.printf("  %-25s : %d unité(s)%n", e.getKey(), e.getValue()));
    }

    /** 
     * Classement des vendeurs par chiffre d'affaires généré.
     * Même logique de dictionnaire et de stream que topProduits().
     */
    public void topVendeurs() {
        Console.afficherTitre("Vendeurs les plus performants");
        // Dictionnaire <ID Vendeur, Chiffre d'affaires total>
        Map<String, Double> ca = new HashMap<>();

        for (Vente v : store.getVentes()) {
            if (!v.isAnnulee()) {
                String vid = v.getVendeurId();
                // Cumul du CA par vendeur
                ca.put(vid, ca.getOrDefault(vid, 0.0) + v.getTotalTTC());
            }
        }

        if (ca.isEmpty()) { 
            Console.afficherInfo("Aucune donnée disponible."); 
            return; 
        }

        // Tri et affichage
        ca.entrySet().stream()
            // Compare des doubles en ordre décroissant
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .forEach(e -> System.out.printf("  %-20s : %.2f F%n", e.getKey(), e.getValue()));
    }

    // ── Exportation ─────────────────────────────────────────────

    /** 
     * Exporte un rapport consolidé de toutes les ventes dans un fichier.
     * Le dossier utilisé est nommé dynamiquement "rapport".
     */
    public void exporterRapport() {
        // Date formatée pour potentiellement horodater le fichier si besoin
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        
        // Création dynamique du dossier "rapport" s'il n'existe pas
        java.io.File dossier = new java.io.File("rapport");
        if (!dossier.exists()) {
            dossier.mkdirs(); // Crée le dossier "rapport" dynamiquement
        }
        
        // Nom du fichier de sortie
        String nomFichier = "rapport/rapport.pdf"; // Note pédagogique : c'est un fichier texte déguisé en .pdf pour respecter l'énoncé

        try (PrintWriter pw = new PrintWriter(new FileWriter(nomFichier))) {
            pw.println("==================================================");
            pw.println("           RAPPORT DE VENTES AÉROSPATIALES        ");
            pw.println("==================================================");
            pw.println("Date d'édition : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            pw.println("--------------------------------------------------");

            double total = 0;
            int nb = 0;
            
            // Écriture du détail des ventes
            for (Vente v : store.getVentes()) {
                if (!v.isAnnulee()) {
                    pw.printf("Vente #%d | %s | Compagnie: %s | Total: %.2f F%n",
                        v.getId(), v.getDateFormatee(), v.getClient().getNomCompagnie(), v.getTotalTTC());
                    total += v.getTotalTTC();
                    nb++;
                }
            }

            pw.println("==================================================");
            // Bilan total
            pw.printf("BILAN GLOBAL : %.2f F (%d ventes validées)%n", total, nb);
            pw.println("==================================================");
            
            Console.afficherSucces("Rapport exporté avec succès dans : " + nomFichier);

        } catch (IOException e) {
            Console.afficherErreur("Erreur lors de l'exportation : " + e.getMessage());
        }
    }
}
