package Src.auth;

public abstract class Utilisateur {

    private int    id;
    private String identifiant;
    private String motDePasse;
    private String nom;
    private String role;

    public Utilisateur(int id, String identifiant,String motDePasse, String nom, String role) {
        this.id          = id;
        this.identifiant = identifiant;
        this.motDePasse  = motDePasse;
        this.nom         = nom;
        this.role        = role;
    }
    //getters 
    public int    getId()          { return id; }
    public String getIdentifiant() { return identifiant; }
    public String getMotDePasse()  { return motDePasse; }
    public String getNom()         { return nom; }
    public String getRole()        { return role; }
    //setters
    public void setNom(String nom)               { this.nom = nom; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public void setIdentifiant(String identifiant) { this.identifiant = identifiant; }
    
    // tostring qui permet d'afficher l'utilisateur sous forme de chaine de caracteres
    // et qui est  utilisé pour enregistrer les utilisateurs dans un fichier texte  
    @Override
    public String toString() {
        return id + ";" + identifiant + ";" + motDePasse + ";" + nom + ";" + role;
    }
}
