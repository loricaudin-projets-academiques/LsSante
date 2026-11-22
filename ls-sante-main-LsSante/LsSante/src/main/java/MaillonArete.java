public class MaillonArete{
    private int fiabilite;
    private int distance;
    private int duree;
    private String destination;
    private MaillonArete suivant;

    public MaillonArete(int fiabilite, int distance, int duree, String destination){
        this.fiabilite = fiabilite;
        this.distance = distance;
        this.duree = duree;
        this.destination = destination;
        this.suivant = null;
    }

    public int getFiabilite() {
        return fiabilite;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuree() {
        return duree;
    }

    public String getDestination() {
        return destination;
    }

    public MaillonArete getSuivant() {
        return suivant;
    }

    public void setFiabilite(int fiabilite) {
        this.fiabilite = fiabilite;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setSuivant(MaillonArete suivant) {
        this.suivant = suivant;
    }
}
