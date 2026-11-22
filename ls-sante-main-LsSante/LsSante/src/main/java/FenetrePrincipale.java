import java.io.File;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class FenetrePrincipale extends JFrame implements WindowListener{
    public FenetrePrincipale(){
        this.setTitle("LS Santé");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setIconImage(new ImageIcon("logo.png").getImage());
        this.setMinimumSize(new Dimension(500, 300));
        this.setLocationRelativeTo(null);
        
        try { // Changer le style de l'application en style par défaut (dépendant des systèmes d'exploitation)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { // En cas d'echec, on laisse le style de java
            // Rien n'est fait si le chargement du style n'a pas fonctionné
        }

        this.addWindowListener(this); // Action lié à la fenêtre

        this.setContentPane(this.creerPane());
    }

    /* Elements du panel */

    JPanel panel;
    JButton boutonDernierGraphe, boutonOuvrirCarte;

    /* ----------------- */

    private JPanel creerPane(){
        panel = (JPanel)this.getContentPane();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Aligner verticalement les widgets
        panel.setAlignmentX(JPanel.CENTER_ALIGNMENT); // Centrer horizontalement
        panel.setBackground(Color.white);

        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Ajouter un espacement

        ImageIcon imageIcon = new ImageIcon(new ImageIcon("logo.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));

        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(imageIcon);
        imageLabel.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(imageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        boutonDernierGraphe = new JButton("Ouvrir la dernière carte");
        if(!new File(ManipulationFichiers.getLienFichier()).isFile() || !ManipulationFichiers.fichierValide(ManipulationFichiers.getLienFichier())){ // Si le dernier graphe n'existe pas
            boutonDernierGraphe.setEnabled(false); // Désactiver le bouton
        }
        boutonDernierGraphe.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        boutonDernierGraphe.addActionListener(new EcouteurBoutons());
        boutonDernierGraphe.setBackground(new Color(130,200,255));
        boutonDernierGraphe.setForeground(new Color(48,48,48));
        panel.add(boutonDernierGraphe);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        boutonOuvrirCarte = new JButton("Ouvrir une carte");
        boutonOuvrirCarte.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        boutonOuvrirCarte.addActionListener(new EcouteurBoutons());
        boutonOuvrirCarte.setBackground(new Color(130,200,255));
        boutonOuvrirCarte.setForeground(new Color(48,48,48));
        panel.add(boutonOuvrirCarte);

        return panel;
    }

    /* Evénements */

    @Override
    public void windowClosing(WindowEvent e) {
        int reponse = JOptionPane.showConfirmDialog(this, "Voulez-vous quiter l'application ?", "LS Santé", JOptionPane.YES_NO_OPTION);
        if (reponse == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }

    /* Classes internes */

    private class EcouteurBoutons implements ActionListener{

        private static String lienOuvert = null;

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == boutonOuvrirCarte || e.getSource() == boutonDernierGraphe){
                if(e.getSource() == boutonOuvrirCarte){
                    lienOuvert = ManipulationFichiers.getLienOuvertureFichier(LsSante.fenetre);
                }
                else{
                    lienOuvert = ManipulationFichiers.getLienFichier();
                }
                if(lienOuvert != null && !lienOuvert.equals("")){
                    // Pour les PC plus lent, on met une boucle de chargement le temps d'ouvrir le graphe
                    FenetreChargement fenCharge = new FenetreChargement(LsSante.fenetre, "LS Santé - Chargement", "Chargement du graphe en cours...");
                    Thread aCharger = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(FonctionnalitesGraphe.chargerGraphe(lienOuvert)){
                                // On essaie de charger le graphe, si le chargement a réussi, alors on ouvre une nouvelle fenêtre
                                boutonDernierGraphe.setEnabled(true);
                                LsSante.fenetreGraphe = new FenetreGraphe();
                                // Chargement ternimé
                                fenCharge.dispose();
                                LsSante.fenetre.dispose();
                                LsSante.fenetreGraphe.setVisible(true);
                            }
                            else{
                                fenCharge.dispose();
                            }
                        }
                        
                    });
                    fenCharge.setVisible(true);
                    aCharger.start(); 
                    /* start() va exécuter la fonction run() sans bloquer la barre de chargement
                    (si run() n'est pas utilisé, la boucle de chargement démarrera uniquement à la fin de l'exécution du chargement
                    du graphe, ce qui n'a aucun intérêt). L'avantage est donc de savoir si le graphe est vraiment en cours de
                    chargement.*/
                    
                }
            }
        }
    }
}
