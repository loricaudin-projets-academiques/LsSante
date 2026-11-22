public class LsSante {

    public static ListeGraphe carte;
    public static FenetrePrincipale fenetre;
    public static FenetreGraphe fenetreGraphe;

    public static void main(String[] args){
        fenetre = new FenetrePrincipale();
        
        carte = new ListeGraphe();
        fenetre.setVisible(true);
    }
}
