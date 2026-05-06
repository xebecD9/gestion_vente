package Src.modeles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MouvementStock {
    
    public enum TypeMouvement { ENTREE, SORTIE }
    public enum MotifMouvement { VENTE, ANNULATION_VENTE, RAVITAILLEMENT, AJOUT_INITIAL, CORRECTION }

    private int id;
    private LocalDateTime date;
    private int produitId;
    private int quantite;
    private TypeMouvement type;
    private MotifMouvement motif;
    private String utilisateurId;

    public MouvementStock(int id, int produitId, int quantite, TypeMouvement type, MotifMouvement motif, String utilisateurId) {
        this.id = id;
        this.date = LocalDateTime.now();
        this.produitId = produitId;
        this.quantite = quantite;
        this.type = type;
        this.motif = motif;
        this.utilisateurId = utilisateurId;
    }

    public int getId() { return id; }
    public LocalDateTime getDate() { return date; }
    public int getProduitId() { return produitId; }
    public int getQuantite() { return quantite; }
    public TypeMouvement getType() { return type; }
    public MotifMouvement getMotif() { return motif; }
    public String getUtilisateurId() { return utilisateurId; }

    public void setDate(LocalDateTime date) { this.date = date; }

    public String getDateFormatee() {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    @Override
    public String toString() {
        return id + ";" + getDateFormatee() + ";" + produitId + ";" + quantite + ";" + type.name() + ";" + motif.name() + ";" + utilisateurId;
    }
}
