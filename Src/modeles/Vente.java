package Src.modeles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Vente {

    private static final double TVA = 0.19; // 19%

    private int               id;
    private Client            client;
    private String            vendeurId;
    private List<LigneVente>  lignes;
    private LocalDateTime     dateVente;
    private boolean           annulee;

    public Vente(int id, Client client, String vendeurId) {
        this.id        = id;
        this.client    = client;
        this.vendeurId = vendeurId;
        this.lignes    = new ArrayList<>();
        this.dateVente = LocalDateTime.now();
        this.annulee   = false;
    }

    public void ajouterLigne(LigneVente ligne) { lignes.add(ligne); }

    public double getSousTotal() {
        double total = 0;
        for (LigneVente l : lignes) total += l.getSousTotal();
        return total;
    }

    public double getMontantTVA()  { return getSousTotal() * TVA; }
    public double getTotalTTC()    { return getSousTotal() + getMontantTVA(); }

    public int            getId()       { return id; }
    public Client         getClient()   { return client; }
    public String         getVendeurId(){ return vendeurId; }
    public List<LigneVente> getLignes() { return lignes; }
    public LocalDateTime  getDate()     { return dateVente; }
    public boolean        isAnnulee()   { return annulee; }
    public void           annuler()     { this.annulee = true; }
    
    public void setDate(LocalDateTime date) { this.dateVente = date; }
    public void setAnnulee(boolean annulee) { this.annulee = annulee; }

    public String getDateFormatee() {
        return dateVente.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    public String serialiserLignes() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lignes.size(); i++) {
            LigneVente lv = lignes.get(i);
            sb.append(lv.getProduit().getId()).append(":").append(lv.getQuantite());
            if (i < lignes.size() - 1) sb.append(",");
        }
        return sb.toString();
    }
}
