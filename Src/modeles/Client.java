package Src.modeles;

public class Client {

    private int    id;
    private String nomCompagnie;
    private String contactPrincipal;
    private String email;
    private String adresse;

    public Client(int id, String nomCompagnie, String contactPrincipal, String email, String adresse) {
        this.id               = id;
        this.nomCompagnie     = nomCompagnie;
        this.contactPrincipal = contactPrincipal;
        this.email            = email;
        this.adresse          = adresse;
    }

    public int    getId()               { return id; }
    public String getNomCompagnie()     { return nomCompagnie; }
    public String getContactPrincipal() { return contactPrincipal; }
    public String getEmail()            { return email; }
    public String getAdresse()          { return adresse; }

    public void setNomCompagnie(String nom)                 { this.nomCompagnie = nom; }
    public void setContactPrincipal(String contactPrincipal){ this.contactPrincipal = contactPrincipal; }
    public void setEmail(String email)                      { this.email = email; }
    public void setAdresse(String adresse)                  { this.adresse = adresse; }

    @Override
    public String toString() {
        return id + ";" + nomCompagnie + ";" + contactPrincipal + ";" + email + ";" + adresse;
    }
}
