public class MaillonNoeud{
    private String nom;
    private String caracteristiques;
    private MaillonArete listeVoisins;
    private MaillonNoeud suivant;

    public MaillonNoeud(String nom, String caracteristiques){
        this.nom = nom;
        this.caracteristiques = caracteristiques;
        this.listeVoisins = null;
        this.suivant = null;
    }

    public String getNom() {
        return nom;
    }

    public String getCaracteristiques() {
        return caracteristiques;
    }

    public MaillonArete getListeVoisins() {
        return listeVoisins;
    }

    public MaillonNoeud getSuivant() {
        return suivant;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCaracteristiques(String caracteristiques) {
        this.caracteristiques = caracteristiques;
    }

    public void setListeVoisins(MaillonArete listeVoisins) {
        this.listeVoisins = listeVoisins;
    }

    public void setSuivant(MaillonNoeud suivant) {
        this.suivant = suivant;
    }
}
