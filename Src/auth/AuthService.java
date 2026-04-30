package Src.auth;

import Src.ui.Console;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class AuthService {

    private static final String FICHIER_UTILISATEURS = "data/utilisateurs.txt";
    private static final int    MAX_TENTATIVES       = 3;

    private List<Utilisateur> utilisateurs;
    private Utilisateur       utilisateurConnecte;

    public AuthService() {
        this.utilisateurs        = new ArrayList<>();
        this.utilisateurConnecte = null;
        chargerUtilisateurs();
    }

    // ── Hashage MD5 ──────────────────────────────────────────────
    public static String hasher(String motDePasse) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(motDePasse.getBytes("UTF-8"));
            BigInteger no = new BigInteger(1, messageDigest);
            String hash = no.toString(16);
            while (hash.length() < 32) hash = "0" + hash;
            return hash;
        } catch (Exception e) {
            throw new RuntimeException("Erreur de hashage : " + e.getMessage());
        }
    }

    // ── Login ────────────────────────────────────────────────────
    public Utilisateur login(String identifiant, String motDePasse) {
        int tentatives = 0;
        while (tentatives < MAX_TENTATIVES) {
            String hash = hasher(motDePasse);
            for (Utilisateur u : utilisateurs) {
                if (u.getIdentifiant().equals(identifiant)
                        && u.getMotDePasse().equals(hash)) {
                    utilisateurConnecte = u;
                    return u;
                }
            }
            tentatives++;
            if (tentatives < MAX_TENTATIVES) {
                Console.afficherErreur("Identifiants incorrects. Tentative "
                        + tentatives + "/" + MAX_TENTATIVES);
            }
        }
        Console.afficherErreur("Compte bloqué après " + MAX_TENTATIVES + " tentatives.");
        return null;
    }

    // ── Logout ───────────────────────────────────────────────────
    public void logout() {
        utilisateurConnecte = null;
        Console.afficherInfo("Session terminée.");
    }

    public Utilisateur getUtilisateurConnecte() { return utilisateurConnecte; }
    public boolean     estConnecte()            { return utilisateurConnecte != null; }

    // ── Chargement fichier ───────────────────────────────────────
    private void chargerUtilisateurs() {
        File fichier = new File(FICHIER_UTILISATEURS);
        if (!fichier.exists()) {
            Console.afficherErreur("Fichier introuvable : " + FICHIER_UTILISATEURS);
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String ligneNettoye = ligne.trim();
                if (ligneNettoye.isEmpty() || ligneNettoye.startsWith("#")) {
                    // ignorer
                } else {
                    String[] parts = ligneNettoye.split("\\|");
                    if (parts.length != 5) {
                        Console.afficherInfo("Ligne ignorée : " + ligneNettoye);
                    } else {
                        try {
                            int    id    = Integer.parseInt(parts[0].trim());
                            String ident = parts[1].trim();
                            String mdp   = parts[2].trim();
                            String nom   = parts[3].trim();
                            String role  = parts[4].trim();
                            if (role.equalsIgnoreCase("ADMIN")) {
                                utilisateurs.add(new Admin(id, ident, mdp, nom));
                            } else if (role.equalsIgnoreCase("VENDEUR")) {
                                utilisateurs.add(new Vendeur(id, ident, mdp, nom));
                            } else {
                                Console.afficherInfo("Rôle inconnu : " + role);
                            }
                        } catch (NumberFormatException e) {
                            Console.afficherErreur("ID invalide : " + ligneNettoye);
                        }
                    }
                }
            }
            Console.afficherSucces(utilisateurs.size() + " utilisateur(s) chargé(s).");
        } catch (IOException e) {
            Console.afficherErreur("Erreur lecture : " + e.getMessage());
        }
    }
    public List<Utilisateur> getTousUtilisateurs() { return utilisateurs; }
    public void AjouterUtilisateur(Utilisateur u) {
        utilisateurs.add(u);
        sauvegarderUtilisateurs();
        Console.afficherSucces("Utilisateur ajouté : " + u.getIdentifiant());
    }
    public void supprimerUtilisateur(Utilisateur u) {
        utilisateurs.remove(u);
        sauvegarderUtilisateurs();
        Console.afficherSucces("Utilisateur supprimé : " + u.getIdentifiant());
    }
    public  Utilisateur trouverUtilisateurParIdentifiant(String identifiant) {
        for (Utilisateur u : utilisateurs) {
            if (u.getIdentifiant().equals(identifiant)) {
                return u;
            }
        }
        return null;
    }
    public int prochainId() {
        int maxId = 0;
        for (Utilisateur u : utilisateurs) {
            if (u.getId() > maxId) {
                maxId = u.getId();
            }
        }
        return maxId + 1;
    }
    public void sauvegarderUtilisateurs() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FICHIER_UTILISATEURS))) {
            for (Utilisateur u : utilisateurs) {
                bw.write(u.toString());
                bw.newLine();
            }
            Console.afficherSucces("Utilisateurs sauvegardés.");
        } catch (IOException e) {
            Console.afficherErreur("Erreur écriture : " + e.getMessage());
        }
    }
    public void ajouterUtilisateur(Utilisateur u) {
        utilisateurs.add(u);
        sauvegarderUtilisateurs();
        Console.afficherSucces("Utilisateur ajouté : " + u.getIdentifiant());
     }
    public Utilisateur trouverUtilisateur(int id) {
        for (Utilisateur u : utilisateurs) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    } 

}
