import java.io.*;
import java.util.Scanner;
import javax.swing.*;


public class ManipulationFichiers {

    private static String lien = "";

    public static String getLienOuvertureFichier(JFrame fenetreCible){
        JOptionPane.showMessageDialog(LsSante.fenetre, "Sélectionnez un jeu d'essai valide.", "LS Santé", JOptionPane.INFORMATION_MESSAGE);
        JFileChooser fenetreOuvertureFichier = new JFileChooser(new File("."));
        String nouvlien = "";
        File fichier;
        if (fenetreOuvertureFichier.showOpenDialog(fenetreCible) == JFileChooser.APPROVE_OPTION) { // Si un fichier est sélectionné dans la fenêtre ouverte grâce à fenetreOuvertureFichier.showOpenDialog(null)
	        fichier = fenetreOuvertureFichier.getSelectedFile(); // Récupérer le fichier
            nouvlien = fichier.getPath();
            if(fichierValide(nouvlien)){
                lien = nouvlien;
                try {
                    FileWriter fichierAEcrire = new FileWriter("src/dernierFichier.txt");
                    fichierAEcrire.write(lien);
                    fichierAEcrire.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(LsSante.fenetre, "Attention ! Le fichier stockant le dernier graphe est actuellement innaccessible.", "LS Santé", JOptionPane.WARNING_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(LsSante.fenetre, "Le fichier est invalide ou corrompu.", "LS Santé", JOptionPane.WARNING_MESSAGE);
                nouvlien = "";
            }
        }
        return nouvlien;
    }

    public static String getLienEnregistrementFichier(JFrame fenetreCible){
        JFileChooser fenetreEnregistrementFichier = new JFileChooser(new File("."));
        String nouvlien = "";
        File fichier;
        if (fenetreEnregistrementFichier.showSaveDialog(fenetreCible) == JFileChooser.APPROVE_OPTION) {
	        fichier = fenetreEnregistrementFichier.getSelectedFile(); // Récupérer le fichier
            nouvlien = fichier.getPath();
            lien = nouvlien;
            try {
                FileWriter fichierAEcrire = new FileWriter("src/dernierFichier.txt");
                fichierAEcrire.write(lien);
                fichierAEcrire.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(LsSante.fenetre, "Attention ! Le fichier stockant le dernier graphe est actuellement innaccessible.", "LS Santé", JOptionPane.WARNING_MESSAGE);
            }
        }
        return nouvlien;
    }

    public static String getLienOuvert(){
        return lien;
    }

    public static String getLienFichier(){
        String lienFichier = "";
        try {
            FileReader fichier;
            try{
                fichier = new FileReader("src/dernierFichier.txt");
            }
            catch(Exception e){
                try{
                    FileWriter nouveauFichier = new FileWriter("src/dernierFichier.txt");
                    nouveauFichier.close();
                }
                catch(Exception ebis){
                    JOptionPane.showMessageDialog(LsSante.fenetre, "Erreur : L'application n'a pas les autorisations d'écriture.", "LS Santé", JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
                fichier = new FileReader("src/dernierFichier.txt"); 
            }
            Scanner scanFichier = new Scanner(fichier);
            if(scanFichier.hasNextLine()){
                lienFichier = scanFichier.nextLine();
            }
            scanFichier.close();
            fichier.close();
            lien = lienFichier;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(LsSante.fenetre, "Erreur : Le fichier stockant le dernier graphe est inaccessible.", "LS Santé", JOptionPane.ERROR_MESSAGE);
            LsSante.fenetre.boutonDernierGraphe.setEnabled(false);
        }
        return lienFichier;
    }

    public static boolean fichierValide(String chemin_fichier){
        boolean resultat = true;
        try{
            FileReader fichier = new FileReader(chemin_fichier);
            Scanner entree = new Scanner(fichier);
            int nbPointsVirgulesAVerifier = -1;
            while(entree.hasNextLine() && resultat){
                String ligne = entree.nextLine();
                int nbPointsVirgules = 0;
                int nbVirgules = 0;
                if(ligne.charAt(0) != '/' && ligne.charAt(1) != '/'){
                    int i = 0;
                    while(i < ligne.length()){
                        if(ligne.charAt(i) == ';'){
                            nbPointsVirgules = nbPointsVirgules + 1;
                            if(nbVirgules != 0 && nbVirgules != 2){
                                resultat = false;
                            }
                            nbVirgules = 0;
                        }
                        else if(ligne.charAt(i) == ','){
                            nbVirgules = nbVirgules + 1;
                            if(nbPointsVirgules < 2){
                                resultat = false;
                            }
                        }
                        else if(nbPointsVirgules == 1 && ligne.charAt(i) != 'M' && ligne.charAt(i) != 'O' && ligne.charAt(i) != 'N'){
                            resultat = false;
                        }
                        i = i + 1;
                    }
                    if(nbPointsVirgulesAVerifier == -1 && nbPointsVirgules > 1){
                        nbPointsVirgulesAVerifier = nbPointsVirgules;
                    }
                    else if(nbPointsVirgulesAVerifier != nbPointsVirgules){
                        resultat = false;
                    }
                }
            }                
            entree.close();
            fichier.close();
        }
        catch(Exception e){
            return false;
        }
        

        return resultat;
    }
}