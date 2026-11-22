import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


/* Tâche 1.2 */
public class ZoneGraphique extends JPanel implements MouseListener, MouseMotionListener {
    
    public ZoneGraphique() {
        super();
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    int tailleSommet = 50;
    List<Object[]> listeCoordonnes = null;
    LinkedList<MaillonNoeud> listNomsSommet = (LinkedList<MaillonNoeud>) LsSante.carte.getListeNoeuds();
    
    boolean placementANEPasChanger = false;
    MaillonNoeud sommetADeplacer = null;

    int longueurMax = -1;
    int hauteurMax = -1;
    
    /** 
     * @return Milieu des coordonnées de chaque sommet
     */
    private List<Object[]> obtenirCoords(){
        if(placementANEPasChanger){ // Eviter de casser l'affichage avec le repaint après le déplacement d'un noeud
            return listeCoordonnes;
        }
        List<Object[]> listeCoords = new LinkedList<>();
        Object[] tab;
        int x = 40 , y = 20;
        longueurMax = LsSante.fenetreGraphe.getSize().width - 300;
        hauteurMax = LsSante.fenetreGraphe.getSize().height - 200;
        for(int i = 0; i < listNomsSommet.size(); i++){
            if(x >= longueurMax - 30){
                y = y + getSize().height / 6;
                x = 40;
            }
            tab = new Object[3];

            tab[0] = listNomsSommet.get(i);
            tab[1] = x + tailleSommet;
            tab[2] = y + tailleSommet;

            listeCoords.add(tab);

            x= x + getSize().width / 7;
        }
        return listeCoords;
    }

    
    /** 
     * @param maillonRecherche
     * @return int[]
     */
    private int[] rechercherCoords(MaillonNoeud maillonRecherche){
        int[] tab = new int[2];
        for(int i = 0; i < listeCoordonnes.size(); i++){
            if(maillonRecherche.equals(listeCoordonnes.get(i)[0])){
                tab[0] = (int)(listeCoordonnes.get(i)[1]);
                tab[1] = (int)(listeCoordonnes.get(i)[2]);
            }
        }
        return tab;
    }

    int xClique = -1;
    int yClique = -1;

    
    /** 
     * @param x
     * @param y
     */
    public void deplacerSommet(int x, int y){
        if(sommetADeplacer != null){
            for(int i = 0; i < listeCoordonnes.size(); i++){
                if(listeCoordonnes.get(i)[0].equals(sommetADeplacer)){
                    int xAComparer = (int) listeCoordonnes.get(i)[1];
                    int yAComparer = (int) listeCoordonnes.get(i)[2];
                    int calculX = x - xClique;
                    int calculY = y - yClique;
    
                    int nouvX = xAComparer + calculX;
                    int nouvY = yAComparer + calculY;
    
                    if(nouvX <= longueurMax - 10 && nouvX >= 30 && nouvY <= hauteurMax - 10 && nouvY >= 30){
                        listeCoordonnes.set(i, new Object[]{listeCoordonnes.get(i)[0], nouvX, nouvY});
                        xClique = x;
                        yClique = y;
                    }

                    
                    
                }
            }
        }
        
    }

    public MaillonNoeud selectionnerSommetADeplacer(int x, int y){
        for(int i = 0; i < listeCoordonnes.size(); i++){
            int xAComparer = (int) listeCoordonnes.get(i)[1];
            int yAComparer = (int) listeCoordonnes.get(i)[2];
            if(xAComparer - 25 <= x && xAComparer + 25 >= x && yAComparer - 25 <= y && yAComparer + 25 >= y){
                return (MaillonNoeud) listeCoordonnes.get(i)[0];
            }
        }
        return null;
    }
    
    /** 
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Partie 1
        listeCoordonnes = obtenirCoords();
        
        Object[] tab;
        
        // Partie 2
        for(int i = 0; i < listNomsSommet.size(); i++){
            LinkedList<MaillonArete> listeVoisins = (LinkedList<MaillonArete>) LsSante.carte.voisinsDirects((MaillonNoeud)listNomsSommet.get(i), "Toutes");
            tab = new Object[3];
            tab[0] = listNomsSommet.get(i);
            tab[1] = (int)rechercherCoords(listNomsSommet.get(i))[0];
            tab[2] = (int)rechercherCoords(listNomsSommet.get(i))[1];
            
            for(int j = 0; j < listeVoisins.size() ; j++){
                Object[] tabRetour = null;
                String carac = listeVoisins.get(j).getFiabilite()+ ";"+ listeVoisins.get(j).getDistance()+";"+ listeVoisins.get(j).getDuree();
                
                for(int k = 0; k < listeCoordonnes.size() && (tabRetour == null || !tabRetour[0].equals(LsSante.carte.rechercherNoeud(listeVoisins.get(j).getDestination()))); k++){
                    tabRetour = new Object[3];
                    tabRetour[0] = listeCoordonnes.get(k)[0];
                    tabRetour[1] = listeCoordonnes.get(k)[1];
                    tabRetour[2] = listeCoordonnes.get(k)[2];
                }
                
                if(tabRetour[0].equals(LsSante.carte.rechercherNoeud(listeVoisins.get(j).getDestination()))){
                    dessinerArete(g, carac,(int) tab[1],(int) tab[2],(int) tabRetour[1],(int) tabRetour[2]);
                }
            }
            
        }
        
        // Partie 3
        
        for(int i = 0; i < listeCoordonnes.size(); i++){
            MaillonNoeud m = (MaillonNoeud)listeCoordonnes.get(i)[0];
            String s = m.getNom();
            dessinerSommet(g, s, (int)listeCoordonnes.get(i)[1], (int)listeCoordonnes.get(i)[2]);
        }
        
        g.dispose();
    }

    
    /** 
     * @param g
     * @param nomSom
     * @param coordonneX
     * @param coordonneY
     */
    public void dessinerSommet(Graphics g, String nomSom, int coordonneX, int coordonneY){
     
        g.setColor(Color.black);
        g.drawOval(coordonneX - 25, coordonneY - 25, 50, 50);
        g.setColor(Color.white);
        g.fillOval(coordonneX - 24, coordonneY - 24, 48, 48);
        g.setColor(Color.red);
        g.setFont(new Font("Arial",Font.PLAIN,14));
        g.drawString(nomSom, coordonneX - 10, coordonneY + 5);  
    }
    
    
    /** 
     * @param g
     * @param caracteristiques
     * @param xAller
     * @param yAller
     * @param xRetour
     * @param yRetour
     */
    public void dessinerArete(Graphics g, String caracteristiques, int xAller, int yAller, int xRetour, int yRetour){
        g.setColor(Color.BLACK);
        g.drawLine(xAller, yAller, xRetour, yRetour);
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        xClique = e.getX();
        yClique = e.getY();
        sommetADeplacer = selectionnerSommetADeplacer(xClique, yClique);
        placementANEPasChanger = true;
    }

    @Override
    public void mouseReleased(MouseEvent e){
        sommetADeplacer = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(placementANEPasChanger){
            deplacerSommet(e.getX(), e.getY());
            repaint();
        }
    }

    // Méthodes inutiles
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}