import java.io.*;
import java.util.*;
import java.time.*;

public class RapportService {

    private static final String FICHIER_VENTES = "ventes.txt";

    public static List<String[]> lireVentes() {
        List<String[]> ventes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FICHIER_VENTES))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                ventes.add(ligne.split(";"));
            }
        } catch (IOException e) {
            System.out.println("Erreur lecture fichier !");
        }
        return ventes;
    }

    public static double chiffreAffairesTotal() {
        double total = 0;
        for (String[] v : lireVentes()) {
            total += Integer.parseInt(v[3]) * Double.parseDouble(v[4]);
        }
        return total;
    }

    public static void ventesDuJour() {
        LocalDate today = LocalDate.now();
        double total = 0;

        for (String[] v : lireVentes()) {
            if (LocalDate.parse(v[0]).equals(today)) {
                total += Integer.parseInt(v[3]) * Double.parseDouble(v[4]);
            }
        }
        System.out.println("Ventes du jour : " + total);
    }

    public static void ventesSemaine() {
        LocalDate today = LocalDate.now();
        LocalDate debut = today.minusDays(today.getDayOfWeek().getValue() - 1);

        double total = 0;
        for (String[] v : lireVentes()) {
            LocalDate d = LocalDate.parse(v[0]);
            if (!d.isBefore(debut) && !d.isAfter(today)) {
                total += Integer.parseInt(v[3]) * Double.parseDouble(v[4]);
            }
        }
        System.out.println("Ventes semaine : " + total);
    }

    public static void ventesMois() {
        LocalDate today = LocalDate.now();
        double total = 0;

        for (String[] v : lireVentes()) {
            LocalDate d = LocalDate.parse(v[0]);
            if (d.getMonth() == today.getMonth() && d.getYear() == today.getYear()) {
                total += Integer.parseInt(v[3]) * Double.parseDouble(v[4]);
            }
        }
        System.out.println("Ventes mois : " + total);
    }

    public static void topProduit() {
        Map<String, Integer> map = new HashMap<>();
        for (String[] v : lireVentes()) {
            map.put(v[2], map.getOrDefault(v[2], 0) + Integer.parseInt(v[3]));
        }

        String top = "";
        int max = 0;
        for (String p : map.keySet()) {
            if (map.get(p) > max) {
                max = map.get(p);
                top = p;
            }
        }
        System.out.println("Top produit : " + top);
    }

    public static void topVendeur() {
        Map<String, Double> map = new HashMap<>();
        for (String[] v : lireVentes()) {
            double montant = Integer.parseInt(v[3]) * Double.parseDouble(v[4]);
            map.put(v[1], map.getOrDefault(v[1], 0.0) + montant);
        }

        String top = "";
        double max = 0;
        for (String v : map.keySet()) {
            if (map.get(v) > max) {
                max = map.get(v);
                top = v;
            }
        }
        System.out.println("Top vendeur : " + top);
    }

    public static void exporterRapport() {
        String fichier = "rapport_" + System.currentTimeMillis() + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(fichier))) {
            pw.println("===== RAPPORT =====");
            pw.println("CA total : " + chiffreAffairesTotal());
        } catch (IOException e) {
            System.out.println("Erreur export !");
        }
    }
}
