package Src.auth;

import Src.ui.Console;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════════════════╗
 * ║ CLASSE AuthService                                                                      ║
 * ║ Rôle : Gère l'authentification (connexion/déconnexion), le hachage des mots de passe, ║
 * ║ et la persistance des utilisateurs dans un fichier texte.                             ║
 * ╚═══════════════════════════════════════════════════════════════════════════════════════╝
 */
public class AuthService {
    
    // Constante définissant le chemin vers le fichier texte des utilisateurs
    private static final String FICHIER_UTILISATEURS = "data/utilisateurs.txt";
    // Nombre maximum de tentatives de connexion autorisées
    private static final int    MAX_TENTATIVES       = 3;

    // Liste en mémoire contenant tous les utilisateurs chargés
    private List<Utilisateur> utilisateurs;
    // Référence vers l'utilisateur actuellement connecté
    private Utilisateur       utilisateurConnecte;

    /**
     * Constructeur par défaut.
     * Initialise la liste et charge les données depuis le fichier.
     */
    public AuthService() {
        this.utilisateurs        = new ArrayList<>();
        this.utilisateurConnecte = null;
        // Chargement initial des utilisateurs
        chargerUtilisateurs();
    }

    // ── Hashage MD5 ──────────────────────────────────────────────
    /**
     * Hache un mot de passe en utilisant l'algorithme MD5.
     * (Remarque pédagogique : MD5 n'est plus considéré comme sécurisé pour la production, 
     * mais il illustre bien le principe du hachage asymétrique).
     * 
     * @param motDePasse Le mot de passe en clair.
     * @return Le condensat (hash) sous forme de chaîne hexadécimale.
     */
    public static String hasher(String motDePasse) {
        try {
            // Instanciation de l'algorithme de hachage MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Conversion du texte en tableau d'octets et application du hachage
            byte[] messageDigest = md.digest(motDePasse.getBytes("UTF-8"));
            
            // Conversion du tableau d'octets en un nombre entier positif (BigInteger)
            BigInteger no = new BigInteger(1, messageDigest);
            
            // Conversion du nombre en chaîne hexadécimale (base 16)
            String hash = no.toString(16);
            
            // Ajout des zéros initiaux si le hash fait moins de 32 caractères
            while (hash.length() < 32) hash = "0" + hash;
            return hash;
        } catch (Exception e) {
            // Capture toute exception liée au hachage (ex: algorithme introuvable)
            throw new RuntimeException("Erreur de hashage : " + e.getMessage());
        }
    }

    // ── Login ────────────────────────────────────────────────────
    /**
     * Tente de connecter un utilisateur avec son identifiant et son mot de passe.
     * Limite le nombre de tentatives en cas d'erreur.
     * 
     * @param identifiant Le nom d'utilisateur (login)
     * @param motDePasse Le mot de passe en clair
     * @return L'objet Utilisateur si connecté avec succès, sinon null.
     */
    public Utilisateur login(String identifiant, String motDePasse) {
        int tentatives = 0;
        
        // Boucle qui permet jusqu'à MAX_TENTATIVES essais
        while (tentatives < MAX_TENTATIVES) {
            // On hache le mot de passe fourni pour le comparer à celui stocké
            String hash = hasher(motDePasse);
            
            // Parcours de la liste des utilisateurs pour trouver une correspondance
            for (Utilisateur u : utilisateurs) {
                // Vérification de l'identifiant et du hash du mot de passe
                if (u.getIdentifiant().equals(identifiant) && u.getMotDePasse().equals(hash)) {
                    // Connexion réussie, on garde l'utilisateur en mémoire
                    utilisateurConnecte = u;
                    return u;
                }
            
            }
            
            // Si aucune correspondance n'est trouvée, on incrémente le compteur
            tentatives++;
            if (tentatives < MAX_TENTATIVES) {
                Console.afficherErreur("Identifiants incorrects. Tentative "
                        + tentatives + "/" + MAX_TENTATIVES);
            }
        }
        
        // Si la boucle se termine, l'utilisateur a épuisé ses tentatives
        Console.afficherErreur("Compte bloqué après " + MAX_TENTATIVES + " tentatives.");
        return null;
    }

    // ── Logout ───────────────────────────────────────────────────
    /**
     * Déconnecte l'utilisateur actuellement actif.
     */
    public void logout() {
        // La déconnexion consiste simplement à remettre cette variable à null
        utilisateurConnecte = null;
        Console.afficherInfo("Session terminée.");
    }

    // ── Accesseurs (Getters) ─────────────────────────────────────
    public Utilisateur getUtilisateurConnecte() { return utilisateurConnecte; }
    public boolean     estConnecte()            { return utilisateurConnecte != null; }
    public List<Utilisateur> getTousUtilisateurs() { return utilisateurs; }

    // ── Chargement fichier ───────────────────────────────────────
    /**
     * Vérifie si une chaîne est déjà un hash MD5 valide.
     * Un hash MD5 est composé exactement de 32 caractères hexadécimaux (0-9, a-f).
     * Cela permet de distinguer un mot de passe en clair d'un mot de passe déjà haché.
     *
     * @param valeur La chaîne à tester
     * @return true si c'est un hash MD5, false si c'est du texte clair
     */
    private static boolean estDejaHache(String valeur) {
        // Un hash MD5 = exactement 32 caractères hexadécimaux minuscules
        return valeur != null && valeur.matches("[a-f0-9]{32}");
    }

    /**charge les utilisateurs depuis le fichier utilisateurs.txt */
    private void chargerUtilisateurs() {
        File fichier = new File(FICHIER_UTILISATEURS);
        
        // Création dynamique : Si le fichier n'existe pas, on initialise avec un admin par défaut
        if (!fichier.exists()) {
            Console.afficherInfo("Aucun fichier de données trouvé.");
            Console.afficherAide("Création dynamique d'un compte Administrateur par défaut (admin/admin)...");
            // Ajout du compte admin par défaut, l'ID est 1
            Utilisateur adminDefaut = new Admin(1, "admin", hasher("admin"), "GARVIS");
            ajouterUtilisateur(adminDefaut);
            return; // Fin du chargement puisqu'on vient de le créer
        }
        
        // Ce drapeau indique si au moins un mot de passe en clair a été détecté et haché
        boolean modificationNecessaire = false;

        // Lecture du fichier ligne par ligne
        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                // Suppression des espaces en début et fin de ligne
                String ligneNettoye = ligne.trim();
                
                // Ignorer les lignes vides ou les commentaires (qui commencent par #)
                if (ligneNettoye.isEmpty() || ligneNettoye.startsWith("#")) {
                    continue;
                }
                
                // Séparation des données par le délimiteur point-virgule ';'
                String[] parts = ligneNettoye.split(";");
                
                // Vérification du nombre de champs (doit correspondre à la structure d'un utilisateur)
                if (parts.length != 5) {
                    Console.afficherInfo("Ligne ignorée (format incorrect) : " + ligneNettoye);
                } else {
                    try {
                        // Extraction et parsing des informations
                        int    id    = Integer.parseInt(parts[0].trim());
                        String ident = parts[1].trim();
                        String mdp   = parts[2].trim();
                        String nom   = parts[3].trim();
                        String role  = parts[4].trim();

                        // ── Auto-hachage ─────────────────────────────────────────
                        // Si le mot de passe n'est PAS déjà un hash MD5 valide,
                        // on le hache maintenant et on marque le fichier à réécrire.
                        if (!estDejaHache(mdp)) {
                            mdp = hasher(mdp);            // Conversion en hash MD5
                            modificationNecessaire = true; // Le fichier devra être sauvegardé
                        }
                        // ─────────────────────────────────────────────────────────
                        
                        // Instanciation de la classe enfant appropriée selon le rôle
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
        } catch (IOException e) {
            Console.afficherErreur("Erreur de lecture du fichier utilisateurs : " + e.getMessage());
        }

        // Si des mots de passe en clair ont été détectés et hachés,
        // on réécrit le fichier avec les hashes pour sécuriser les données.
        if (modificationNecessaire) {
            sauvegarderUtilisateurs();
            Console.afficherSucces("Fichier utilisateurs mis à jour : mots de passe maintenant hachés en MD5.");
        }
    }

    // ── Gestion (CRUD) des utilisateurs ─────────────────────────
    
    /**
     * Supprime un utilisateur de la liste et sauvegarde le changement.
     * 
     * @param u L'utilisateur à supprimer
     */
    public void supprimerUtilisateur(Utilisateur u) {
        // Retrait de la liste mémoire
        utilisateurs.remove(u);
        // Sauvegarde de la liste mise à jour dans le fichier texte
        sauvegarderUtilisateurs();
        Console.afficherSucces("Utilisateur supprimé : " + u.getIdentifiant());
    }

    /**
     * Recherche un utilisateur à partir de son identifiant de connexion et son nom.
     * 
     * @param identifiant L'identifiant (login) recherché
     * @return L'objet Utilisateur ou null si introuvable
     */
    public Utilisateur trouverUtilisateurParIdentifiant(String identifiant)  {
        // Parcours classique d'une liste
        for (Utilisateur u : utilisateurs) {
            if (u.getIdentifiant().equals(identifiant)) {
                return u;
            }
        }
        return null; // Renvoie null si la boucle se termine sans trouver de correspondance
    }

    /**
     * Trouve un utilisateur via son ID.
     * 
     * @param id l'ID de l'utilisateur
     * @return L'utilisateur ou null
     */
    public Utilisateur trouverUtilisateur(int id) {
        for (Utilisateur u : utilisateurs) {
            if(u.getId() == id){
                return u;
            }
        }
        return null;
    }

    /**
     * Calcule le prochain ID disponible (auto-incrémentation).
     * 
     * @return Le prochain ID sous forme d'entier
     */
    public int prochainId() {
        int maxId = 0;
        // On cherche le plus grand ID actuel dans la liste
        for (Utilisateur u : utilisateurs) {
            if (u.getId() > maxId) {
                maxId = u.getId();
            }
        }
        // On retourne la valeur max + 1
        return maxId + 1;
    }

    /**
     * Ajoute un nouvel utilisateur et l'enregistre immédiatement dans le fichier.
     * 
     * @param u L'utilisateur à ajouter
     */
    public void ajouterUtilisateur(Utilisateur u) {
        // Ajout en mémoire
        utilisateurs.add(u);
        // Persistance sur le disque
        sauvegarderUtilisateurs();
        Console.afficherSucces("Utilisateur ajouté : " + u.getIdentifiant());
    }

    /**
     * Écrit la totalité de la liste des utilisateurs dans le fichier texte (.txt).
     * Crée le dossier parent si celui-ci n'existe pas.
     */
    public void sauvegarderUtilisateurs() {
        File fichier = new File(FICHIER_UTILISATEURS);
        
        // Création dynamique : s'assure que le dossier 'data' existe avant de créer le fichier
        File dossier = fichier.getParentFile();
        if (dossier != null && !dossier.exists()) {
            dossier.mkdirs(); // Crée toute l'arborescence de dossiers nécessaire
        }
        
        // Écriture du fichier à l'aide d'un flux (BufferedWriter)
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fichier))) {
            // Ajout d'un en-tête pour indiquer la structure du fichier texte
            bw.write("# Fichier des utilisateurs"); bw.newLine();
            bw.write("# Format : id;identifiant;motDePasse(hash);nom;role"); bw.newLine();
            
            // Parcours de la liste et écriture de chaque objet
            for (Utilisateur u : utilisateurs) {
                bw.write(u.toString()); // Utilise la méthode toString() redéfinie dans Utilisateur
                bw.newLine(); // Retour à la ligne
            }
        } catch (IOException e) {
            Console.afficherErreur("Erreur d'écriture dans le fichier utilisateurs : " + e.getMessage());
        }
    }
}
