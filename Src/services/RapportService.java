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

    private DataStore store;

    public RapportService() {
        this.store = DataStore.getInstance();
    }

    // ── Ventes du jour ───────────────────────────────────────────
    public void rapportJour() {
        Console.afficherTitre("Ventes du jour");
        LocalDateTime debut = LocalDateTime.now().withHour(0).withMinute(0);
        afficherRapportPeriode(debut, LocalDateTime.now(), "AUJOURD'HUI");
    }

    // ── Ventes de la semaine ─────────────────────────────────────
    public void rapportSemaine() {
        Console.afficherTitre("Ventes de la semaine");
        LocalDateTime debut = LocalDateTime.now().minusDays(7);
        afficherRapportPeriode(debut, LocalDateTime.now(), "CETTE SEMAINE");
    }

    // ── Ventes du mois ───────────────────────────────────────────
    public void rapportMois() {
        Console.afficherTitre("Ventes du mois");
        LocalDateTime debut = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        afficherRapportPeriode(debut, LocalDateTime.now(), "CE MOIS");
    }

    // ── Rapport par période ──────────────────────────────────────
    private void afficherRapportPeriode(LocalDateTime debut, LocalDateTime fin, String label) {
        List<Vente> ventes = store.getVentes();
        double totalCA  = 0;
        int    nbVentes = 0;

        for (Vente v : ventes) {
            if (!v.isAnnulee()
                    && v.getDate().isAfter(debut)
                    && v.getDate().isBefore(fin)) {
                totalCA += v.getTotalTTC();
                nbVentes++;
                System.out.printf("  Vente #%-4d | %-20s | %10.2f F%n",
                    v.getId(), v.getClient().getNom(), v.getTotalTTC());
            }
        }

        Console.separateur();
        System.out.printf(Console.GRAS + "  %s — %d vente(s) — CA : %.2f F%n" + Console.RESET,
            label, nbVentes, totalCA);
    }

    // ── Chiffre d'affaires total ─────────────────────────────────
    public void chiffreAffairesTotal() {
        Console.afficherTitre("Chiffre d'affaires total");
        double total = 0;
        int    nb    = 0;
        for (Vente v : store.getVentes()) {
            if (!v.isAnnulee()) { total += v.getTotalTTC(); nb++; }
        }
        System.out.printf(Console.GRAS + "  Total : %.2f F sur %d vente(s)%n" + Console.RESET, total, nb);
    }

    // ── Top produits ─────────────────────────────────────────────
    public void topProduits() {
        Console.afficherTitre("Produits les plus vendus");
        Map<String, Integer> compteur = new HashMap<>();

        for (Vente v : store.getVentes()) {
            if (!v.isAnnulee()) {
                for (LigneVente lv : v.getLignes()) {
                    String nom = lv.getProduit().getNom();
                    compteur.put(nom, compteur.getOrDefault(nom, 0) + lv.getQuantite());
                }
            }
        }

        if (compteur.isEmpty()) { Console.afficherInfo("Aucune donnée."); return; }

        compteur.entrySet().stream()
            .sorted((a, b) -> b.getValue() - a.getValue())
            .limit(5)
            .forEach(e -> System.out.printf("  %-25s : %d unité(s)%n", e.getKey(), e.getValue()));
    }

    // ── Top vendeurs ─────────────────────────────────────────────
    public void topVendeurs() {
        Console.afficherTitre("Vendeurs les plus performants");
        Map<String, Double> ca = new HashMap<>();

        for (Vente v : store.getVentes()) {
            if (!v.isAnnulee()) {
                String vid = v.getVendeurId();
                ca.put(vid, ca.getOrDefault(vid, 0.0) + v.getTotalTTC());
            }
        }

        if (ca.isEmpty()) { Console.afficherInfo("Aucune donnée."); return; }

        ca.entrySet().stream()
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .forEach(e -> System.out.printf("  %-20s : %.2f F%n", e.getKey(), e.getValue()));
    }

    // ── Export rapport .txt ──────────────────────────────────────
    public void exporterRapport() {
        String date     = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        String nomFichier = "data/rapport_" + date + ".txt";

        try (PrintWriter pw = new PrintWriter(new FileWriter(nomFichier))) {
            pw.println("=== RAPPORT DE VENTES ===");
            pw.println("Date : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            pw.println();

            double total = 0;
            int nb = 0;
            for (Vente v : store.getVentes()) {
                if (!v.isAnnulee()) {
                    pw.printf("Vente #%d | %s | Client: %s | Total: %.2f F%n",
                        v.getId(), v.getDateFormatee(), v.getClient().getNom(), v.getTotalTTC());
                    total += v.getTotalTTC();
                    nb++;
                }
            }

            pw.println();
            pw.printf("TOTAL : %.2f F | %d vente(s)%n", total, nb);
            Console.afficherSucces("Rapport exporté : " + nomFichier);

        } catch (IOException e) {
            Console.afficherErreur("Erreur export : " + e.getMessage());
        }
    }
}
