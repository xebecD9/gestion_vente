import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DataStore {

    private static final String FICHIER_CLIENTS = "data/clients.txt";

    private final List<Client> clients          = new ArrayList<>();
    private final AtomicInteger prochainIdClient = new AtomicInteger(1);

    // ── Chargement ─────────────────────────────────────────────────
    public void chargerClients() {
        File fichier = new File(FICHIER_CLIENTS);
        if (!fichier.exists()) {
            Console.info("[DataStore] Aucun enregistrement — liste vide.");
            return;
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(fichier), StandardCharsets.UTF_8))) {
            String ligne;
            int ignores = 0;
            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim();
                if (ligne.isEmpty() || ligne.startsWith("#")) continue;
                try {
                    Client c = Client.fromCSV(ligne);
                    clients.add(c);
                    if (c.getId() >= prochainIdClient.get())
                        prochainIdClient.set(c.getId() + 1);
                } catch (Exception e) {
                    ignores++;
                    Console.avertissement("[DataStore] Ligne ignorée : " + ligne);
                }
            }
            Console.info("[DataStore] " + clients.size() + " client(s) chargé(s)."
                    + (ignores > 0 ? " (" + ignores + " ignorée(s))" : ""));
        } catch (IOException e) {
            Console.erreur("[DataStore] Erreur lecture : " + e.getMessage());
        }
    }

    // ── Sauvegarde ─────────────────────────────────────────────────
    public void sauvegarderClients() {
        new File("data").mkdirs();
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FICHIER_CLIENTS), StandardCharsets.UTF_8))) {
            bw.write("# id|nom|prenom|telephone|email|adresse");
            bw.newLine();
            for (Client c : clients) {
                bw.write(c.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            Console.erreur("[DataStore] Erreur sauvegarde : " + e.getMessage());
        }
    }

    // ── CRUD ───────────────────────────────────────────────────────
    public void ajouterClient(Client c) {
        c.setId(prochainIdClient.getAndIncrement());
        clients.add(c);
        sauvegarderClients();
    }

    public List<Client> getTousLesClients() {
        return new ArrayList<>(clients);
    }

    public Client getClientParId(int id) {
        return clients.stream()
                .filter(c -> c.getId() == id)
                .findFirst().orElse(null);
    }

    public boolean supprimerClient(int id) {
        boolean ok = clients.removeIf(c -> c.getId() == id);
        if (ok) sauvegarderClients();
        return ok;
    }

    public List<Client> rechercherParNom(String terme) {
        String t = terme.toLowerCase();
        return clients.stream()
                .filter(c -> c.getNom().toLowerCase().contains(t)
                          || c.getPrenom().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public int getNombreClients() {
        return clients.size();
    }
}