import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FenetreGraphe extends JFrame implements WindowListener, ActionListener, ComponentListener{
    public FenetreGraphe(){
        this.setTitle("LS Santé - " + ManipulationFichiers.getLienOuvert());
        this.setMinimumSize(new Dimension(1000, 700));
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Mettre la fanêtre en plain écran
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setIconImage(new ImageIcon("logo.png").getImage());
        this.setLocationRelativeTo(null);
        
        try { // Changer le style de l'application en style par défaut (dépendant des systèmes d'exploitation)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { // En cas d'echec, on laisse le style de java
            // Rien n'est fait si le chargement du style n'a pas fonctionné
        }

        this.addWindowListener(this); // Action lié à la fenêtre
        this.addComponentListener(this); // Action lié au redimensionnement de la fenêtre
        this.setJMenuBar(creerBarreDeMenu());
        this.setContentPane(this.creerPanel());
    }
    
    
    
    /* Elements de la barre de menu */

    private JMenuBar barreDeMenu;
    private JMenu menuFichier;
    private JMenuItem fichierOuvrir, fichierEnregistrer, fichierEnregistrerSous, fichierFermer;
    private JMenu menuEdition;
    private JMenuItem editionCaracteristique, editionValeurChemin;
    private JMenu menuAffichage;
    private JMenuItem affichageNoeuds, affichageAretes, affichageNoeudsSelonCritere, affichageGrapheDetaille, affichageNoeudsSelonCritereEtNDistance;
    private JMenu menuCalculer;
    private JMenuItem calculerMeilleurChemin, calculerNDistance;

    /* ---------------------------- */

    private JMenuBar creerBarreDeMenu(){
        barreDeMenu = new JMenuBar();
        
        menuFichier = new JMenu("Fichier");
        ImageIcon icon = new ImageIcon("file.png"); 
        Image image0AModifier = icon.getImage();
        image0AModifier = image0AModifier.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        icon = new ImageIcon(image0AModifier);
        menuFichier.setIcon(icon);

        fichierOuvrir = new JMenuItem("Ouvrir");
        ImageIcon ouv = new ImageIcon("ouvrir.png"); 
        Image image1 = ouv.getImage();
        image1 = image1.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ouv = new ImageIcon(image1);
        fichierOuvrir.setIcon(ouv);
        fichierOuvrir.addActionListener(this);
        menuFichier.add(fichierOuvrir);

        fichierEnregistrer = new JMenuItem("Enregistrer");
        ImageIcon enr = new ImageIcon("enregistrer.png"); 
        Image image2 = enr.getImage();
        image2 = image2.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        enr = new ImageIcon(image2);
        fichierEnregistrer.setIcon(enr);
        fichierEnregistrer.addActionListener(this);
        menuFichier.add(fichierEnregistrer);

        fichierEnregistrerSous = new JMenuItem("Enregistrer sous");
        ImageIcon enr_so = new ImageIcon("enregistrer_sous.png"); 
        Image image3 = enr_so.getImage();
        image3 = image3.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        enr_so = new ImageIcon(image3);
        fichierEnregistrerSous.setIcon(enr_so);
        fichierEnregistrerSous.addActionListener(this);
        menuFichier.add(fichierEnregistrerSous);

        fichierFermer = new JMenuItem("Fermer");
        ImageIcon fer = new ImageIcon("fermer.png"); 
        Image image4 = fer.getImage();
        image4 = image4.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        fer = new ImageIcon(image4);
        fichierFermer.setIcon(fer);
        fichierFermer.addActionListener(this);
        menuFichier.add(fichierFermer);

        barreDeMenu.add(menuFichier);


        menuEdition = new JMenu("Edition");
        ImageIcon edi = new ImageIcon("edition.png"); 
        Image image1AModifier = edi.getImage();
        image1AModifier = image1AModifier.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        edi = new ImageIcon(image1AModifier);
        menuEdition.setIcon(edi);

        editionCaracteristique = new JMenuItem("Modifier une caractéristique");
        ImageIcon mod = new ImageIcon("modifier.png"); 
        Image image7 = mod.getImage();
        image7 = image7.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        mod = new ImageIcon(image7);
        editionCaracteristique.setIcon(mod);
        editionCaracteristique.addActionListener(this);
        menuEdition.add(editionCaracteristique);
        editionValeurChemin = new JMenuItem("Valeurs d'un chemin");
        editionValeurChemin.setIcon(mod);
        editionValeurChemin.addActionListener(this);
        menuEdition.add(editionValeurChemin);

        barreDeMenu.add(menuEdition);


        menuAffichage = new JMenu("Affichage");
        ImageIcon aff = new ImageIcon("affichage.png"); 
        Image image2AModifier = aff.getImage();
        image2AModifier = image2AModifier.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        aff = new ImageIcon(image2AModifier);
        menuAffichage.setIcon(aff);

        affichageNoeuds = new JMenuItem("Afficher tous les noeuds");
        ImageIcon noe = new ImageIcon("noeud.png"); 
        Image image5 = noe.getImage();
        image5 = image5.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        noe = new ImageIcon(image5);
        affichageNoeuds.setIcon(noe);
        affichageNoeuds.addActionListener(this);
        menuAffichage.add(affichageNoeuds);

        affichageAretes = new JMenuItem("Afficher toutes les arêtes");
        ImageIcon are = new ImageIcon("arete.png"); 
        Image image6 = are.getImage();
        image6 = image6.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        are = new ImageIcon(image6);
        affichageAretes.setIcon(are);
        affichageAretes.addActionListener(this);
        menuAffichage.add(affichageAretes);

        affichageNoeudsSelonCritere = new JMenuItem("Afficher les noeuds selon un critère");
        affichageNoeudsSelonCritere.setIcon(noe);
        affichageNoeudsSelonCritere.addActionListener(this);
        menuAffichage.add(affichageNoeudsSelonCritere);

        affichageNoeudsSelonCritereEtNDistance = new JMenuItem("Afficher les noeuds selon un critère et N-Distance");
        affichageNoeudsSelonCritereEtNDistance.setIcon(noe);
        affichageNoeudsSelonCritereEtNDistance.addActionListener(this);
        menuAffichage.add(affichageNoeudsSelonCritereEtNDistance);

        affichageGrapheDetaille = new JMenuItem("Afficher le graphe détaillé");
        affichageGrapheDetaille.setIcon(are);
        affichageGrapheDetaille.addActionListener(this);
        menuAffichage.add(affichageGrapheDetaille);

        barreDeMenu.add(menuAffichage);


        menuCalculer = new JMenu("Calculer");
        ImageIcon calc = new ImageIcon("calculer.png"); 
        Image image3AModifier = calc.getImage();
        image3AModifier = image3AModifier.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        calc = new ImageIcon(image3AModifier);
        menuCalculer.setIcon(calc);

        calculerMeilleurChemin = new JMenuItem("Meilleur chemin");
        ImageIcon che = new ImageIcon("chemin.png"); 
        Image image8 = che.getImage();
        image8 = image8.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        che = new ImageIcon(image8);
        calculerMeilleurChemin.setIcon(che);
        calculerMeilleurChemin.addActionListener(this);
        menuCalculer.add(calculerMeilleurChemin);

        calculerNDistance = new JMenuItem("N-Distance");
        calculerNDistance.setIcon(che);
        calculerNDistance.addActionListener(this);
        menuCalculer.add(calculerNDistance);
        
        barreDeMenu.add(menuCalculer);
        
        return barreDeMenu;
    }

    /* Elements du panel */

    JPanel panel, zoneGraphe, zoneDetails, panelImage;
    ZoneGraphique dessin;
    JLabel texteInformations;
    JTextArea resultat = new JTextArea(5, 20);

    /* ----------------- */

    private JPanel creerPanel(){
        panel = (JPanel)this.getContentPane();

        panel.setLayout(new BorderLayout());

        zoneGraphe = new JPanel();
        
        zoneGraphe.setLayout(new BorderLayout());
        panelImage = new JPanel();
       
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("logo.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));

        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(imageIcon);
        imageLabel.setAlignmentX(CENTER_ALIGNMENT);
        panelImage.setBackground(Color.white);
        panelImage.add(imageLabel);
        
        dessin = new ZoneGraphique();
        dessin.setBackground(Color.white);
        zoneGraphe.add(panelImage,BorderLayout.NORTH);
        zoneGraphe.add(dessin,BorderLayout.CENTER);

        panel.add(zoneGraphe, BorderLayout.CENTER);
        

        zoneDetails = new JPanel();
        JScrollPane scrollPanelDetails;
        scrollPanelDetails = new JScrollPane(zoneDetails);
        scrollPanelDetails.setPreferredSize(new Dimension(235, 210));
        scrollPanelDetails.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanelDetails.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        zoneDetails.setLayout(new BoxLayout(zoneDetails, BoxLayout.Y_AXIS));
        zoneDetails.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        zoneDetails.add(Box.createRigidArea(new Dimension(200, 10)));
        zoneDetails.setBackground(new Color(12, 224, 224));

        texteInformations = new JLabel("Résultat");
        texteInformations.setFont(new Font("Arial", Font.BOLD, 16));
        texteInformations.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        resultat.setEditable(false);
        resultat.setBackground(new Color(12, 224, 224));
        
        zoneDetails.add(texteInformations);
        zoneDetails.add(resultat);
        

        panel.add(scrollPanelDetails, BorderLayout.EAST);
        
    
        return panel;
    }

    /* Evénements */
    /* WindowListener */

    @Override
    public void windowClosing(WindowEvent e) {
        this.confirmerFermeture();
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    /* ComponentListener */
    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        dessin.placementANEPasChanger = false; // Réadapter le placement des noeuds
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    /* ActionListenenr */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == fichierOuvrir){
            if(LsSante.carte.grapheEnregistre == false){
                int reponse = JOptionPane.showConfirmDialog(this, "Voulez-vous enregistrer les modifications réalisées sur votre graphe ?", "LS Santé", JOptionPane.YES_NO_CANCEL_OPTION);
                if (reponse == JOptionPane.YES_OPTION) {
                    FonctionnalitesGraphe.sauvegarderGraphe(ManipulationFichiers.getLienOuvert());
                    LsSante.carte.grapheEnregistre = true;
                }
            }
            if(LsSante.carte.grapheEnregistre = true && FonctionnalitesGraphe.chargerGraphe(ManipulationFichiers.getLienOuvertureFichier(this))){
                
                LsSante.fenetreGraphe.dispose();
                LsSante.fenetreGraphe = new FenetreGraphe();
                LsSante.fenetreGraphe.setVisible(true);
            }
            
        }
        if(e.getSource() == fichierEnregistrer || e.getSource() == fichierEnregistrerSous){
            String lienEnregistrement;
            if(e.getSource() == fichierEnregistrer){
                lienEnregistrement = ManipulationFichiers.getLienOuvert();
            }
            else{
                lienEnregistrement = ManipulationFichiers.getLienEnregistrementFichier(this);
            }
            if(!"".equals(lienEnregistrement) && FonctionnalitesGraphe.sauvegarderGraphe(lienEnregistrement)){
                JOptionPane.showMessageDialog(LsSante.fenetre, "Les modififications ont été enregistrées.", "LS Santé", JOptionPane.INFORMATION_MESSAGE);
                this.setTitle("LS Santé - " + ManipulationFichiers.getLienOuvert());
            }
            else{
                JOptionPane.showMessageDialog(LsSante.fenetre, "Les modififications n'ont pas pu être enregistrées. Vérifiez que vous avez les autorisations d'écriture", "LS Santé", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(e.getSource() == fichierFermer){
            this.confirmerFermeture();
        }
        if(e.getSource() == editionCaracteristique){
            FonctionnalitesGraphe.modifierCaracteristique();
        }
        if(e.getSource() == editionValeurChemin){
            FonctionnalitesGraphe.modifierValeurArete();
        }
        if(e.getSource() == affichageNoeuds){
            String resu = FonctionnalitesGraphe.afficherNoeuds(); 
            resultat.setText(resu);
        }
        if(e.getSource() == affichageNoeudsSelonCritere){
            String resu = FonctionnalitesGraphe.afficherNoeudsSelonCritere(); 
            resultat.setText(resu);
        }
        if(e.getSource() == affichageNoeudsSelonCritereEtNDistance){
            String resu = FonctionnalitesGraphe.comparer2SommetsSurCritere(); 
            resultat.setText(resu);
        }
        if(e.getSource() == affichageAretes){
            String resu = FonctionnalitesGraphe.afficherAretes(); 
            resultat.setText(resu);
        }
        if(e.getSource() == affichageGrapheDetaille){
            String resu = LsSante.carte.toString();
            resultat.setText(resu);
        }
        if(e.getSource() == calculerMeilleurChemin){
            String resu = FonctionnalitesGraphe.plusCourtChemin(); 
            resultat.setText(resu);
        }
        if(e.getSource() == calculerNDistance){
            String resu = FonctionnalitesGraphe.afficherDistance(); 
            resultat.setText(resu);
        }
    }

    public void confirmerFermeture(){
        int reponse = JOptionPane.NO_OPTION;
        if(LsSante.carte.grapheEnregistre == false){
            reponse = JOptionPane.showConfirmDialog(this, "Voulez-vous enregistrer les modifications réalisées sur votre graphe ?", "LS Santé", JOptionPane.YES_NO_CANCEL_OPTION);
            if (reponse == JOptionPane.YES_OPTION) {
                FonctionnalitesGraphe.sauvegarderGraphe(ManipulationFichiers.getLienOuvert());
                LsSante.carte.grapheEnregistre = true;
            }
        }
        if(reponse != JOptionPane.CANCEL_OPTION){
            reponse = JOptionPane.showConfirmDialog(this, "Voulez-vous retourner à l'écran d'accueil ?\n(Annuler : Rester sur cette fenêtre)", "LS Santé", JOptionPane.YES_NO_CANCEL_OPTION);
            if (reponse == JOptionPane.YES_OPTION) {
                LsSante.fenetreGraphe.dispose();
                LsSante.fenetre.setVisible(true);
            }
            else if(reponse == JOptionPane.NO_OPTION){
                System.exit(0);
            }
        }
    }
}