import java.util.ArrayList;
import java.util.List;

public class Client {

    private int    id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String adresse;
    private List<Integer> historiqueVenteIds;

    public Client(int id, String nom, String prenom,
                  String telephone, String email, String adresse) {
        this.id                 = id;
        this.nom                = nom;
        this.prenom             = prenom;
        this.telephone          = telephone;
        this.email              = email;
        this.adresse            = adresse;
        this.historiqueVenteIds = new ArrayList<>();
    }

    public Client(String nom, String prenom,
                  String telephone, String email, String adresse) {
        this(0, nom, prenom, telephone, email, adresse);
    }

    // ── Getters / Setters ──────────────────────────────────────────
    public int    getId()                     { return id; }
    public void   setId(int id)               { this.id = id; }
    public String getNom()                    { return nom; }
    public void   setNom(String nom)          { this.nom = nom; }
    public String getPrenom()                 { return prenom; }
    public void   setPrenom(String prenom)    { this.prenom = prenom; }
    public String getTelephone()              { return telephone; }
    public void   setTelephone(String tel)    { this.telephone = tel; }
    public String getEmail()                  { return email; }
    public void   setEmail(String email)      { this.email = email; }
    public String getAdresse()                { return adresse; }
    public void   setAdresse(String adresse)  { this.adresse = adresse; }
    public List<Integer> getHistoriqueVenteIds() { return historiqueVenteIds; }
    public void ajouterVente(int venteId)     { historiqueVenteIds.add(venteId); }

    // ── Utilitaires ────────────────────────────────────────────────
    public String getNomComplet() {
        return nom.toUpperCase() + " " + prenom;
    }

    public String toCSV() {
        return id + "|" + nom + "|" + prenom + "|"
                + telephone + "|" + email + "|" + adresse;
    }

    public static Client fromCSV(String ligne) {
        String[] p = ligne.split("\\|", -1);
        if (p.length < 6)
            throw new IllegalArgumentException("Ligne CSV invalide : " + ligne);
        return new Client(
                Integer.parseInt(p[0].trim()),
                p[1].trim(), p[2].trim(),
                p[3].trim(), p[4].trim(), p[5].trim()
        );
    }

    @Override
    public String toString() {
        return String.format(
            "Client{id=%d, nom='%s', prenom='%s', tel='%s', email='%s', adresse='%s'}",
            id, nom, prenom, telephone, email, adresse
        );
    }
}