import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class FonctionnalitesGraphe {

    /* Tâche 1.1 */
    public static boolean chargerGraphe(String chemin_fichier){
        boolean resultatExecution = false;
        if(!"".equals(chemin_fichier)){
            LsSante.carte.reinitialiserGraphe();
            try{
                FileReader fichier = new FileReader(chemin_fichier);
                Scanner entree = new Scanner(fichier);

                ArrayList<String> lignes = new ArrayList<String>();
                int numero_ligne = 0;
                while(entree.hasNextLine()){
                    String l = entree.nextLine();
                    if(l.charAt(0) != '/' && l.charAt(1) != '/'){
                        lignes.add(l);
                        int i = 0;
                        int nbSeparations = 0;
                        String nomSommet = "", typeSommet = "";
                        String nombreAConvertir = "";
                        int nbVirgules = 0;
                        int fiabilite = 0;
                        int distance = 0;
                        int duree = 0;
                        String destination = "";
                        while(i < lignes.get(numero_ligne).length()){
                            if(nbSeparations == 0){ // Colonne 0 et 1 : ajouter noeuds
                                while(lignes.get(numero_ligne).charAt(i) != ';'){
                                    nomSommet = nomSommet + lignes.get(numero_ligne).charAt(i);
                                    i = i + 1;
                                }
                                nbSeparations = nbSeparations + 1;
                                i = i + 1;
                                while(lignes.get(numero_ligne).charAt(i) != ';'){
                                    typeSommet = typeSommet + lignes.get(numero_ligne).charAt(i);
                                    i = i + 1;
                                }
                                switch(typeSommet){
                                    case "M":
                                        typeSommet = "Maternité";
                                        break;
                                    case "O":
                                        typeSommet = "Bloc Opératoire";
                                        break;
                                    case "N":
                                        typeSommet = "Centre de nutrition";
                                        break;
                                    default:
                                        break;
                                }
                                LsSante.carte.ajouterNoeud(nomSommet, typeSommet);
                                nbSeparations = nbSeparations + 1;
                                i = i + 1;
                                
                            }
                            else{ // Autres colonnes contenant les suivants (démarrant à la colonne 2)
                                nombreAConvertir = "";
                                if(lignes.get(numero_ligne).charAt(i) == ';'){
                                    nbSeparations = nbSeparations + 1;
                                    i = i + 1;
                                }
                                else if(lignes.get(numero_ligne).charAt(i) == '0'){
                                    i = i + 1;
                                }
                                else{
                                    // On récupère d'abord la fiabilite, puis la distance, puis la duree et enfin le sommet de destination
                                    while(i < lignes.get(numero_ligne).length() && lignes.get(numero_ligne).charAt(i) != ',' && lignes.get(numero_ligne).charAt(i) != ';'){ // -1 car sinon, on sort de la chaîne
                                        if(lignes.get(numero_ligne).charAt(i) != ' '){
                                            nombreAConvertir = nombreAConvertir + lignes.get(numero_ligne).charAt(i);
                                        }
                                        i = i + 1;
                                    }
                                    if(nbVirgules == 0){// fiabilite
                                        fiabilite = Integer.parseInt(nombreAConvertir);
                                        nbVirgules = nbVirgules + 1;
                                    }
                                    else if(nbVirgules == 1){// distance
                                        distance = Integer.parseInt(nombreAConvertir);
                                        nbVirgules = nbVirgules + 1;
                                    }
                                    else if(nbVirgules == 2){// duree
                                        duree = Integer.parseInt(nombreAConvertir);
                                        nbVirgules = 0;
                                        // On recherche maintenant le sommet qui a la valeur différente de zéro à la même case, on l'insérera uniquement si on a déjà inséré les 2 sommets concernés
                                        boolean sommetTrouve = false;
                                        int numeroLigneRecherchee = 0;
                                        int nbSeparationsAComparer = 0;
                                        int j;
                                        while(numeroLigneRecherchee < lignes.size() && !sommetTrouve){
                                            j = 0;
                                            while(nbSeparations != nbSeparationsAComparer){
                                                if(lignes.get(numeroLigneRecherchee).charAt(j) == ';'){
                                                    nbSeparationsAComparer = nbSeparationsAComparer + 1;
                                                }
                                                j = j + 1;
                                            }
                                            if(lignes.get(numeroLigneRecherchee).charAt(j) != '0' && numeroLigneRecherchee != numero_ligne){
                                                sommetTrouve = true;
                                            }
                                            else{
                                                nbSeparationsAComparer = 0;
                                                numeroLigneRecherchee = numeroLigneRecherchee + 1;
                                            }
                                            
                                        }
                                        if(sommetTrouve){// Si le sommet a été trouvé, alors on récupère le nom et on l'insère
                                            //Récupération du nom du sommet de destination
                                            j = 0;
                                            destination = "";
                                            while(lignes.get(numeroLigneRecherchee).charAt(j) != ';'){
                                                destination = destination + lignes.get(numeroLigneRecherchee).charAt(j);
                                                j = j + 1;
                                            }
                                            //Insertion de l'arête
                                            LsSante.carte.ajouterArete(nomSommet, destination, fiabilite, distance, duree);
                                        }
                                        nbSeparations = nbSeparations + 1;
                                    }
                                    
                                    i = i + 1;
                                }
                            }
                            
                        }
                        numero_ligne = numero_ligne + 1;
                    }
                }

                entree.close();
                fichier.close();

                resultatExecution = true;
                
            }
            catch(IOException e){
                JOptionPane.showMessageDialog(LsSante.fenetre, "Impossible de lire le fichier. Vérifiez qu'il n'est pas utilisé par un autre processus et qu'il peut être ouvert.", "LS Santé", JOptionPane.ERROR_MESSAGE);
            }
        }
        return resultatExecution;
    }

    public static boolean sauvegarderGraphe(String lienEnregistrement){
        try {
            FileWriter nouveauFichier = new FileWriter(lienEnregistrement);
            nouveauFichier.write(LsSante.carte.getCarteFormatee());
            nouveauFichier.close();
            LsSante.carte.grapheEnregistre = true;
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    /* Tâche 1.4 */
    public static String afficherNoeuds(){
        String chaine = "";
        LinkedList<MaillonNoeud> listeNoeuds = (LinkedList<MaillonNoeud>) LsSante.carte.getListeNoeuds();
        for(int i = 0; i < listeNoeuds.size(); i++){
            chaine = chaine + "Nom : " + listeNoeuds.get(i).getNom() + " ; Caracteristiques : " + listeNoeuds.get(i).getCaracteristiques()+"\n" ;
        }
        return chaine;
    }

    /* Tâche 1.5 */
    public static String afficherAretes(){
        String chaine = "";
        LinkedList<MaillonArete[]> listeArete = (LinkedList<MaillonArete[]>) LsSante.carte.getListeAretes();
        for(int i = 0 ; i < listeArete.size(); i++){
            chaine = chaine + "Source : "+ listeArete.get(i)[1].getDestination() + " Destination : " + listeArete.get(i)[0].getDestination()+ " ; Fiabilité : " + listeArete.get(i)[0].getFiabilite() + " ; Distance : " + listeArete.get(i)[0].getDistance() + " ; Duree : " + listeArete.get(i)[0].getDuree() +  " \n ";
        }
        return chaine;
    }

    /* Tâche 1.4 */
    public static String afficherNoeudsSelonCritere(){
        String chaine = "";
        String caracteristique = selectionnerCaracteristique("Sélectionner votre caractéristique", false);
        if(caracteristique != null){
            LinkedList<MaillonNoeud> listeNoeuds = (LinkedList<MaillonNoeud>) LsSante.carte.getNoeudSelonCritere(caracteristique);
            for(int i = 0; i < listeNoeuds.size(); i++){
                chaine = chaine + "Nom : " + listeNoeuds.get(i).getNom() + " ; Caracteristiques : " + listeNoeuds.get(i).getCaracteristiques()+ "\n" ;
            }
        }
        
        return chaine;
    }

    public static String selectionnerSommet(String message){
        String selection = null;
        LinkedList<MaillonNoeud> listeSommets = (LinkedList<MaillonNoeud>) LsSante.carte.getListeNoeuds();
        String[] tableauSommets = new String[listeSommets.size()]; // Seul les tableaux sont acceptés dans la fonction showInputDialog
        for(int i = 0; i < listeSommets.size(); i++){
            tableauSommets[i] = listeSommets.get(i).getNom();
        }
        selection = (String) JOptionPane.showInputDialog(LsSante.fenetreGraphe, message, "LS Santé", JOptionPane.QUESTION_MESSAGE, null, tableauSommets, tableauSommets[0]);
        return selection;
    }
    
    public static String[] selectionner2Sommets(String message1, String message2){
        String[] selections = new String[2];
        LinkedList<MaillonNoeud> listeSommets = (LinkedList<MaillonNoeud>) LsSante.carte.getListeNoeuds();
        String[] tableauSommets = new String[listeSommets.size()]; // Seul les tableaux sont acceptés dans la fonction showInputDialog
        for(int i = 0; i < listeSommets.size(); i++){
            tableauSommets[i] = listeSommets.get(i).getNom();
        }
        selections[0] = (String) JOptionPane.showInputDialog(LsSante.fenetreGraphe, message1, "LS Santé", JOptionPane.QUESTION_MESSAGE, null, tableauSommets, tableauSommets[0]);

        if(selections[0] == null){
            return null;
        }
        
        for(int i = 0; i < listeSommets.size(); i++){
            if(listeSommets.get(i).getNom().equals(selections[0])){
                listeSommets.remove(i);
            }
        }
        tableauSommets = new String[listeSommets.size()];
        for(int i = 0; i < listeSommets.size(); i++){
            tableauSommets[i] = listeSommets.get(i).getNom();
        }
        selections[1] = (String) JOptionPane.showInputDialog(LsSante.fenetreGraphe, message2, "LS Santé", JOptionPane.QUESTION_MESSAGE, null, tableauSommets, tableauSommets[0]);
        if(selections[1] == null){
            return null;
        }
        return selections;
    }

    public static String selectionnerCaracteristique(String message, boolean peutEtreTousChoisis){
        String[] liste;
        if(peutEtreTousChoisis){
            liste = new String[]{"Maternité", "Bloc Opératoire", "Nutrition", "Toutes les caractéristiques"};
        }
        else{
            liste = new String[]{"Maternité", "Bloc Opératoire", "Nutrition"};
        }
        
        String nomCaracteristique = null;
        int nouvelleCaracteristique = JOptionPane.showOptionDialog(LsSante.fenetreGraphe, message, "LS Santé", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, liste, null);
        switch (nouvelleCaracteristique) {
            case 0:
                nomCaracteristique = "Maternité";
                break;
            case 1:
                nomCaracteristique = "Bloc Opératoire";
                break;
            case 2:
                nomCaracteristique = "Nutrition";
                break;
            case 3:
                if(peutEtreTousChoisis){
                    nomCaracteristique = "Toutes";
                }
                break;
            default:
                break;
        }
        return nomCaracteristique;
    }

    /* Tâche 2.1 */
    public static void modifierCaracteristique(){
        String nomCaracteristique;
        String sommet = selectionnerSommet("Sélectionnez votre sommet");
        if(sommet != null){
            nomCaracteristique = selectionnerCaracteristique("Choisissez votre nouvelle caractéristique", false);
            if(nomCaracteristique != null){
                LsSante.carte.changerCaracteristique(sommet, nomCaracteristique);

                LsSante.carte.grapheEnregistre = false; // Dire qu'il faut penser à enregistrer
            }
        }
        
    }

    /* Tâche 2.6 */
    public static String plusCourtChemin() {
        String chaine = "";
        String[] tabSommets = FonctionnalitesGraphe.selectionner2Sommets("Sélectionnez votre sommet de départ", "Sélectionnez votre sommet de destination");
        
        if(tabSommets != null){
            class Critere extends JDialog implements ActionListener,ItemListener{
                JComboBox<String> ch1, ch2 , ch3;
                JButton btOK,btAnnuler;
                boolean OKchoisi = false;
                Critere(JFrame f){
                    super(f, "Choisissez vos critère", true);
                    this.setSize(350,170);
                    this.setResizable(false);
                    this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                    
                    this.setLocationRelativeTo(null);
                    this.setContentPane(this.creerPanel());
                    this.setVisible(true);
                }

                public String[] getSelections(){
                    String[] listeCriteres = new String[]{(String)ch1.getSelectedItem(), (String)ch2.getSelectedItem(), (String)ch3.getSelectedItem()};
                    if(listeCriteres[0].equals("Sélectionnez")){
                        listeCriteres[0] = null;
                    }
                    if(listeCriteres[1] != null && listeCriteres[1].equals("Aucun")){
                        listeCriteres[1] = null;
                    }
                    if(listeCriteres[2] != null && listeCriteres[2].equals("Aucun")){
                        listeCriteres[2] = null;
                    }
                    return listeCriteres;
                }

                JPanel panel;

                private JPanel creerPanel(){
                    panel = (JPanel) this.getContentPane();
                    panel.setLayout(new BorderLayout());
                    panel.add(panneau_millieu(), BorderLayout.CENTER);
                    panel.add(panneau_bas(), BorderLayout.SOUTH);
                    return panel;
                }

                String[] listeCriteres = {"Aucun", "Fiabilite" , "Distance", "Duree"};

                JPanel panneau_millieu(){
                    JPanel milieu = new JPanel();
                    milieu.setLayout(new GridLayout(2, 3, 10, 10));
                    milieu.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

                    JLabel c1 = new JLabel("Critère 1");
                    milieu.add(c1);

                    JLabel c2 = new JLabel("Critère 2");
                    milieu.add(c2);

                    JLabel c3 = new JLabel("Critère 3");
                    milieu.add(c3);
                    
                    Dimension d = new Dimension(80, 25);
                    String s1[] = {"Sélectionnez", "Fiabilite" , "Distance", "Duree"};
                    ch1 = new JComboBox<String>(s1);
                    ch1.addItemListener(this);
                    ch2 = new JComboBox<String>();
                    ch2.addItemListener(this);
                    ch3 = new JComboBox<String>();
                    ch1.setPreferredSize(d);
                    ch2.setPreferredSize(d);
                    ch3.setPreferredSize(d);
                    
                    milieu.add(ch1);
                    milieu.add(ch2);
                    milieu.add(ch3);

                    ch2.setEnabled(false);
                    ch3.setEnabled(false);
                    
                    return milieu;
                }
                JPanel panneau_bas(){
                    JPanel pan = new JPanel();
                    pan.setLayout( new FlowLayout());
                    
                    btOK = new JButton("OK");
                    btOK.addActionListener(this);
                    btAnnuler = new JButton("Annuler");
                    btAnnuler.addActionListener(this);
                    
                    pan.add(btOK);
                    pan.add(btAnnuler);

                    btOK.setEnabled(false);
                    
                    return pan;
                }
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource() == btOK){
                        OKchoisi = true;
                        this.dispose();
                    }
                    if (e.getSource() == btAnnuler) {
                        this.dispose();
                    }
                    
                }

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if(e.getSource() == ch1){
                        ch2.removeAllItems();
                        String itemSelectionne1 = (String)ch1.getSelectedItem();
                        if(itemSelectionne1.equals("Sélectionnez")){
                            btOK.setEnabled(false);
                            ch2.setEnabled(false);
                            ch3.setEnabled(false);
                        }
                        else{
                            btOK.setEnabled(true);
                            ch2.setEnabled(true);
                            for(int i = 0; i < listeCriteres.length; i++){
                                if(!listeCriteres[i].equals(itemSelectionne1)){
                                    ch2.addItem(listeCriteres[i]);
                                }
                            }
                        }
                    }
                    else if(e.getSource() == ch2){
                        ch3.removeAllItems();
                        String itemSelectionne1 = (String)ch1.getSelectedItem();
                        String itemSelectionne2 = (String)ch2.getSelectedItem();
                        if(itemSelectionne2 == null || itemSelectionne2.equals("Aucun")){
                            ch3.setEnabled(false);
                        }
                        else{
                            ch3.setEnabled(true);
                            for(int i = 0; i < listeCriteres.length; i++){
                                if(!listeCriteres[i].equals(itemSelectionne1) && !listeCriteres[i].equals(itemSelectionne2)){
                                    ch3.addItem(listeCriteres[i]);
                                }
                            }
                        }
                    }
                }
            }
            Critere fenetreDialog = new Critere(LsSante.fenetreGraphe);
            if(fenetreDialog.OKchoisi){
                String[] criteresSelectionnes = fenetreDialog.getSelections();
                Object[] resultat = LsSante.carte.dijkstra(tabSommets[0], tabSommets[1], criteresSelectionnes[0], criteresSelectionnes[1], criteresSelectionnes[2]);
                ArrayList<MaillonNoeud> chemin = (ArrayList<MaillonNoeud>) resultat[0];
                chaine = "le meilleur chemin est : " ; 
                for(int i= 0 ; i < chemin.size() ;i++){
                    chaine = chaine + chemin.get(i).getNom()+" ;\n";
                }
                int[] tab = (int[])resultat[1];
                chaine ="\n"+ chaine + "Fiabilite : "+ tab[0]+" ;\n";
                chaine = chaine + "Distance : "+ tab[1]+" ;\n";
                chaine = chaine + "Duree : "+ tab[2]+" ;";
            }
            

            
        }
        return chaine;
    }
    
    /* Tache 2.2 et 2.4 */
    public static String afficherVoisinsDirects(){
        List<MaillonArete> liste ;
        String sommet = selectionnerSommet("Sélectionnez votre sommet");
        String chaine = "";
        if(sommet != null){  
            String type = selectionnerCaracteristique("Choisissez votre caractéristique :", true);
            if(type != null){
                chaine = "Voisins directs de " + sommet + " :\n";
                liste = LsSante.carte.voisinsDirects(LsSante.carte.rechercherNoeud(sommet), type);
                for(int i = 0; i < liste.size(); i++){
                    chaine = chaine + liste.get(i).getDestination() + "\n" ;
                }
            }
        }
        return chaine;
    }

    public static String selectionnerSommetVoisin(String sommet, String message){ //A importer
        String selection = null;
        LinkedList<MaillonArete> listeAretes = (LinkedList<MaillonArete>) LsSante.carte.voisinsDirects(LsSante.carte.rechercherNoeud(sommet), "Toutes");
        String[] tableauSommets = new String[listeAretes.size()]; // Seul les tableaux sont acceptés dans la fonction showInputDialog
        for(int i = 0; i < listeAretes.size(); i++){
            tableauSommets[i] = listeAretes.get(i).getDestination();
        }
        selection = (String) JOptionPane.showInputDialog(LsSante.fenetreGraphe, message, "LS Santé", JOptionPane.QUESTION_MESSAGE, null, tableauSommets, tableauSommets[0]);
        return selection;
    }

    /* Tache 2.3 */
    // TODO : Difficile à réaliser
    
    /* Tache 3.1 */
    public static String afficherVoisins2Sommets(){
        String chaine = "";
        List<MaillonArete> liste ;
        String[] sommets = selectionner2Sommets("Sélectionner le premier sommet","Sélectionner le deuxième sommet");
        if(sommets != null){
            String caracteristique = selectionnerCaracteristique("Sélectionner une caractéristique", true);
            if(caracteristique != null){
                chaine = "1) Voisins directs de " + sommets[0] + " :\n";
                liste = LsSante.carte.voisinsDirects(LsSante.carte.rechercherNoeud(sommets[0]), caracteristique);
                for(int i = 0; i < liste.size(); i++){
                    chaine = chaine + liste.get(i).getDestination() + "\n" ;
                }
                chaine = chaine + "\n2) Voisins directs de " + sommets[1] + " :\n";
                liste = LsSante.carte.voisinsDirects(LsSante.carte.rechercherNoeud(sommets[1]), caracteristique);
                for(int i = 0; i < liste.size(); i++){
                    chaine = chaine + liste.get(i).getDestination() + "\n" ;
                }
            }
        }
        return chaine;
    }

    public static MaillonArete[] selectionnerArete(){
        String sommetAller = selectionnerSommet("Sélectionner le premier sommet");
        if(sommetAller == null){
            return null;
        }
        String sommetRetour = selectionnerSommetVoisin(sommetAller, "Sélectionner le deuxième sommet");
        if(sommetRetour == null){
            return null;
        }
        return LsSante.carte.rechercherArete(sommetAller, sommetRetour);
    }

    public static void modifierValeurArete(){
        MaillonArete[] areteAModifier = selectionnerArete();
        if(areteAModifier != null){
            System.out.println(areteAModifier[0].getDestination() + " " + areteAModifier[1].getDestination());

            class FenetreValeurAretes extends JDialog implements ActionListener{
                JTextField s1, s2 , s3;
                JButton btOK, btAnnuler;
                boolean OKchoisi = false;
                
                static int fiabilite = 1, distance = 0, duree = 0;

                FenetreValeurAretes(JFrame f, int a, int b, int c){
                    super(f, "Choisissez vos nouvelles valeurs", true);
                    this.setSize(350,170);
                    this.setResizable(false);
                    this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

                    fiabilite = a;
                    distance = b;
                    duree = c;
                    
                    this.setLocationRelativeTo(null);
                    this.setContentPane(this.creerPanel());

                    this.setVisible(true);
                }

                public int[] getValeurs(){
                    int[] listeValeurs = new int[3];
                    listeValeurs[0] = fiabilite;
                    listeValeurs[1] = distance;
                    listeValeurs[2] = duree;
                    return listeValeurs;
                }

                JPanel panel;

                private JPanel creerPanel(){
                    panel = (JPanel) this.getContentPane();
                    panel.setLayout(new BorderLayout());
                    panel.add(panneau_millieu(), BorderLayout.CENTER);
                    panel.add(panneau_bas(), BorderLayout.SOUTH);
                    return panel;
                }

                JPanel panneau_millieu(){
                    JPanel milieu = new JPanel();
                    milieu.setLayout(new GridLayout(2, 3, 10, 10));
                    milieu.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

                    JLabel c1 = new JLabel("Fiabilité");
                    milieu.add(c1);

                    JLabel c2 = new JLabel("Distance");
                    milieu.add(c2);

                    JLabel c3 = new JLabel("Durée");
                    milieu.add(c3);
                    
                    Dimension d = new Dimension(80, 25);
                    s1 = new JTextField();
                    s1.setText(Integer.toString(fiabilite));
                    s1.addKeyListener(new VerifierSaisieChiffres());
                    s2 = new JTextField();
                    s2.setText(Integer.toString(distance));
                    s2.addKeyListener(new VerifierSaisieChiffres());
                    s3 = new JTextField();
                    s3.setText(Integer.toString(duree));
                    s3.addKeyListener(new VerifierSaisieChiffres());
                    s1.setPreferredSize(d);
                    s2.setPreferredSize(d);
                    s3.setPreferredSize(d);
                    
                    milieu.add(s1);
                    milieu.add(s2);
                    milieu.add(s3);
                    
                    return milieu;
                }
                JPanel panneau_bas(){
                    JPanel pan = new JPanel();
                    pan.setLayout( new FlowLayout());
                    
                    btOK = new JButton("OK");
                    btOK.addActionListener(this);
                    btAnnuler = new JButton("Annuler");
                    btAnnuler.addActionListener(this);
                    
                    pan.add(btOK);
                    pan.add(btAnnuler);
                    
                    return pan;
                }
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource() == btOK){
                        OKchoisi = true;

                        fiabilite = Integer.parseInt(s1.getText());
                        distance = Integer.parseInt(s2.getText());
                        duree = Integer.parseInt(s3.getText());

                        this.dispose();
                    }
                    if (e.getSource() == btAnnuler) {
                        this.dispose();
                    }
                    
                }

                class VerifierSaisieChiffres extends KeyAdapter{

                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        if ((((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) || (e.getSource() == s1 && s1.getText().length() > 1)) { // KeyEvent.VK_BACK_SPACE : Retour arrière | s1.getText().length() > 1 Pour une fiabilité entre 0 et 10 (1 chiffre avant la saisie du deuxième)
                            e.consume();  // ignorer les autres touches
                        }
                        if(s1.getText().equals("") || s2.getText().equals("") || s3.getText().equals("")){
                            btOK.setEnabled(false);
                        }
                        else{
                            btOK.setEnabled(true);
                        }
                    }
                }
            }
            FenetreValeurAretes fenetreDialog = new FenetreValeurAretes(LsSante.fenetreGraphe, areteAModifier[0].getFiabilite(), areteAModifier[0].getDistance(), areteAModifier[0].getDuree());
            if(fenetreDialog.OKchoisi){
                int[] nouvellesValeurs = fenetreDialog.getValeurs();

                areteAModifier[0].setFiabilite(nouvellesValeurs[0]);
                areteAModifier[0].setDistance(nouvellesValeurs[1]);
                areteAModifier[0].setDuree(nouvellesValeurs[2]);

                areteAModifier[1].setFiabilite(nouvellesValeurs[0]);
                areteAModifier[1].setDistance(nouvellesValeurs[1]);
                areteAModifier[1].setDuree(nouvellesValeurs[2]);

                JOptionPane.showMessageDialog(LsSante.fenetre, "L'arête reliant  " + areteAModifier[0].getDestination() + " à " + areteAModifier[1].getDestination() + " a désormais pour valeurs :\nFiabilité : " + areteAModifier[0].getFiabilite() + "/10\nDistance : " + areteAModifier[0].getDistance() + "\nDurée : " + areteAModifier[0].getDuree(), "LS Santé", JOptionPane.INFORMATION_MESSAGE);
                
                LsSante.carte.grapheEnregistre = false; // Dire qu'il faut penser à enregistrer
            }
        }
    }

    /* Tâche 3.2 */

    public static String afficherDistance(){
        String chaine = "";
        String[] tabSommets = FonctionnalitesGraphe.selectionner2Sommets("Sélectionnez votre sommet de départ", "Sélectionnez votre sommet de destination");
        if(tabSommets != null){
            int x_Distance = LsSante.carte.calculerNDistance(tabSommets[0], tabSommets[1]);
            chaine = "Les sommets " + tabSommets[0] + " et " + tabSommets[1] + " sont à " + Integer.toString(x_Distance) + "-distance.";
        }
        return chaine;
    }

    /* Tâche 3.3 */

    public static String comparer2SommetsSurCritere(){
        String chaine = "";
        String[] tabSommets = selectionner2Sommets("Sélectionnez votre premier sommet", "Sélectionnez votre deuxième sommet");
        if(tabSommets != null){
            String critere = selectionnerCaracteristique("Sélectionner la caractéristique à comparer", false);
            if(critere != null){
                int signe = JOptionPane.showOptionDialog(LsSante.fenetreGraphe, "Quelle N-Distance souhaitez-vous garder (nombre qui sera entré par la suite) ?", "LS Santé", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"N-Distances plus petites ou égales", "N-Distance égales", "N-Distance plus grandes ou égales"}, null);
                if(signe != -1){
                    String r = JOptionPane.showInputDialog(LsSante.fenetreGraphe, "Entrer la valeur de N-Distance");
                    if(r != null){
                        try{
                            int n_distance = Integer.parseInt(r);
                            LinkedList<MaillonNoeud> liste1 = (LinkedList<MaillonNoeud>) LsSante.carte.comparerSommetSelonCritereEtNDistance(tabSommets[0], critere, signe, n_distance);
                            LinkedList<MaillonNoeud> liste2 = (LinkedList<MaillonNoeud>) LsSante.carte.comparerSommetSelonCritereEtNDistance(tabSommets[1], critere, signe, n_distance);
                            chaine = tabSommets[0] + " :\n";
                            for(int i = 0; i < liste1.size(); i++){
                                chaine = chaine + "Nom : " + liste1.get(i).getNom() + " ; Caracteristiques : " + liste1.get(i).getCaracteristiques() + " ; " + LsSante.carte.calculerNDistance(tabSommets[0], liste1.get(i).getNom()) + "-Distance de "+ tabSommets[0] +"\n" ;
                            }
                            chaine = chaine + "\n" + tabSommets[1] + " :\n";
                            for(int i = 0; i < liste2.size(); i++){
                                chaine = chaine + "Nom : " + liste2.get(i).getNom() + " ; Caracteristiques : " + liste2.get(i).getCaracteristiques() + " ; " + LsSante.carte.calculerNDistance(tabSommets[1], liste2.get(i).getNom()) + "-Distance de "+ tabSommets[1] +"\n" ;
                            }
                        }
                        catch(Exception e){
                            JOptionPane.showMessageDialog(LsSante.fenetre, "Vous n'avez pas entré un nombre valide.", "LS Santé", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
        return chaine;
    }
}
