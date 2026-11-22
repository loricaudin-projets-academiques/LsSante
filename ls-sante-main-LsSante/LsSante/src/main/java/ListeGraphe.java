import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;

public class ListeGraphe {

    private MaillonNoeud premier;
    public boolean grapheEnregistre = true;
    
    /**
     * premier : Premier noeud de la liste
     * grapheEnregistre : Indique s'il y a besoin ou non d'enregistrer le graphe dans un fichier
     */
    public ListeGraphe(){
        this.premier = null;
    }


    /**
     * Ajoute un Noeud à la fin afin d'avoir des facilités de lecture dans l'ordre
     * 
     * @param nNom : Nom du nouveau noeud
     * @param nCaracteristiques : Nouvelle caractéristique (soit Maternité, soit Bloc Opératoire, soit Nutrition)
     */
    public void ajouterNoeud(String nNom, String nCaracteristiques){
        MaillonNoeud nouveauMaillonPrincipal = new MaillonNoeud(nNom, nCaracteristiques);
        MaillonNoeud ptr = this.premier;
        if(ptr == null){
            this.premier = nouveauMaillonPrincipal;
        }
        else{
            while(ptr.getSuivant() != null){
                ptr = ptr.getSuivant();
            }
            ptr.setSuivant(nouveauMaillonPrincipal);
        }
    }
    
    /**
     * Ajoute une arête dans la liste de liste dans les 2 sens (non orienté)
     * 
     * @param origine : Nom du sommet d'origine
     * @param nDestination : nom du sommet d'arrivée
     * 
     */
    public void ajouterArete(String origine, String nDestination, int nFiabilite, int nDistance, int nDuree){ 
        MaillonArete nouveauMaillonSecondaire = new MaillonArete(nFiabilite, nDistance, nDuree, nDestination);
        MaillonNoeud ptr = this.premier;
        while(!ptr.getNom().equals(origine)){
            ptr = ptr.getSuivant();
        }
        nouveauMaillonSecondaire.setSuivant(ptr.getListeVoisins());
        ptr.setListeVoisins(nouveauMaillonSecondaire);
        
        nouveauMaillonSecondaire = new MaillonArete(nFiabilite, nDistance, nDuree, origine);
        ptr = this.premier;
        while(!ptr.getNom().equals(nDestination)){
            ptr = ptr.getSuivant();
        }
        nouveauMaillonSecondaire.setSuivant(ptr.getListeVoisins());
        ptr.setListeVoisins(nouveauMaillonSecondaire);
    }

    public void reinitialiserGraphe(){ // Effacer tout pour accueillir un nouveau graphe
        this.premier = null;
        grapheEnregistre = true;
    }
    
    
    /** 
     * @return String
     */
    /* Tâche 1.2 */
    @Override
    public String toString(){
        MaillonNoeud ptr1 = this.premier;
        MaillonArete ptr2;
        String message = "";
        while(ptr1 != null){
            message = message + ptr1.getNom() + " " + ptr1.getCaracteristiques() + " : ";
            ptr2 = ptr1.getListeVoisins();
            while(ptr2 != null){
                message = message + ptr2.getDestination() + " " + ptr2.getFiabilite() + " " + ptr2.getDistance() + " " + ptr2.getDuree();
                ptr2 = ptr2.getSuivant();
                message = message + "; ";
            }
            ptr1 = ptr1.getSuivant();
            message = message + "\n";
        }
        
        return message;
    }
    
    
    
    /** 
     * @param critere
     * @return List<MaillonNoeud>
     */
    public List<MaillonNoeud> getNoeudSelonCritere(String critere){
        MaillonNoeud ptr = this.premier;
        List<MaillonNoeud> listeNoeuds = new LinkedList<MaillonNoeud>();
        while(ptr != null){
            if(ptr.getCaracteristiques().equals(critere)){
                listeNoeuds.add(ptr);
            }
            ptr = ptr.getSuivant();
        }
        return listeNoeuds;
    }
    

    
    /** 
     * @param source
     * @param maillonRecherche
     * @return MaillonArete
     */
    public MaillonArete afficherAreteALEnvers(String source, MaillonArete maillonRecherche){
        MaillonNoeud ptr1 = this.premier;
        MaillonArete ptr2 = null;
        boolean estTrouve = false;
        while(ptr1 != null && !estTrouve){
            if(ptr1.getNom().equals(maillonRecherche.getDestination())){
                ptr2 = ptr1.getListeVoisins();
                while(ptr2 != null && !estTrouve){
                    if(ptr2.getDestination().equals(source)){
                        estTrouve = true;
                    }
                    else{
                        ptr2 = ptr2.getSuivant();
                    }
                    
                }
            }
            else{
                ptr1 = ptr1.getSuivant();
            }
        }
        return ptr2;
    }


    
    /** 
     * @return List<MaillonNoeud>
     */
    /* Tache 1.4 */
    public List<MaillonNoeud> getListeNoeuds(){
        List<MaillonNoeud> listeNoeuds = new LinkedList<MaillonNoeud>();
        MaillonNoeud ptr = this.premier;
        while(ptr != null){
            listeNoeuds.add (ptr);
            ptr = ptr.getSuivant();
        }
        return listeNoeuds;
    }
    
    
    /** 
     * @return List<MaillonArete[]>
     */
    /* Tâche 1.5 */
    public List<MaillonArete[]> getListeAretes(){
        List<MaillonArete[]> listeAretes = new LinkedList<MaillonArete[]>();
        MaillonNoeud ptr1 = this.premier;
        MaillonArete ptr2;
        while(ptr1 != null){
            ptr2 = ptr1.getListeVoisins();
            while(ptr2 != null){
                MaillonArete[] maillon = {this.afficherAreteALEnvers(ptr1.getNom(), ptr2), ptr2};
                boolean estTrouve = false;
                for(int i = 0; i < listeAretes.size() && !estTrouve; i++){
                    if(listeAretes.get(i)[0] == maillon[1] && listeAretes.get(i)[1] == maillon[0]){
                        estTrouve = true;
                    }
                }
                if(listeAretes.isEmpty() || !estTrouve){
                    listeAretes.add(maillon);
                }
                ptr2 = ptr2.getSuivant();
            }
            ptr1 = ptr1.getSuivant();
        }
        return listeAretes;
    }

    
    /** 
     * @param nomNoeud
     * @param nomCaracteristique
     */
    /* Tâche 2.1 */
    public void changerCaracteristique(String nomNoeud, String nomCaracteristique){
        MaillonNoeud noeudAModifier = this.rechercherNoeud(nomNoeud);
        if(noeudAModifier != null){
            noeudAModifier.setCaracteristiques(nomCaracteristique);
            JOptionPane.showMessageDialog(LsSante.fenetre, "Le sommet " + noeudAModifier.getNom() + " a désormais pour caractéristique : " + nomCaracteristique, "LS Santé", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(LsSante.fenetre, "Le sommet recherché est introuvable.", "LS Santé", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    
    /** 
     * @param nom
     * @return MaillonNoeud
     */
    public MaillonNoeud rechercherNoeud(String nom){
        MaillonNoeud noeudRecherche = this.premier;
        while(noeudRecherche != null && !noeudRecherche.getNom().equals(nom)){
            noeudRecherche = noeudRecherche.getSuivant();
        }
        return noeudRecherche;
    }

    
    /** 
     * @param aller
     * @param retour
     * @return MaillonArete[]
     */
    public MaillonArete[] rechercherArete(String aller, String retour){
        MaillonArete[] areteRecherchee = {null, null};
        List<MaillonArete[]> listeAretes = this.getListeAretes();
        for(MaillonArete[] elt : listeAretes){
            if(elt[0].getDestination().equals(aller) && elt[1].getDestination().equals(retour) || elt[1].getDestination().equals(aller) && elt[0].getDestination().equals(retour)){
                areteRecherchee = elt;
                return areteRecherchee;
            }
        }
        return null;
    }
    
    
    /** 
     * @param sommet
     * @param type
     * @return List<MaillonArete>
     */
    /* Tache 2.2 et 2.4 */
    public List<MaillonArete> voisinsDirects(MaillonNoeud sommet, String type){
        List<MaillonArete> listeVoisinsDirects = new LinkedList<MaillonArete>();
        MaillonArete ptr = sommet.getListeVoisins();
        while(ptr != null){
            if(type.equals("Toutes") || type.equals(this.rechercherNoeud(ptr.getDestination()).getCaracteristiques())){
                listeVoisinsDirects.add(ptr);
            }
            
            ptr = ptr.getSuivant();
        }
        return listeVoisinsDirects;
    }

    
    /** 
     * @param noeud1
     * @param noeud2
     * @return boolean
     */
    public boolean estVoisin(MaillonNoeud noeud1, MaillonNoeud noeud2){
        MaillonArete ptrArete = noeud1.getListeVoisins();
        while(ptrArete != null){
            if(ptrArete.getDestination().equals(noeud2.getNom())){
                return true;
            }
            ptrArete = ptrArete.getSuivant();
        }
        return false;
    }

    
    /** 
     * @param sommet
     * @param critere
     * @param signe
     * @param nDistance
     * @return List<MaillonNoeud>
     */
    /* Tâche 3.3 */
    public List<MaillonNoeud> comparerSommetSelonCritereEtNDistance(String sommet, String critere, int signe, int nDistance){
        List<MaillonNoeud> listeCriteres = getNoeudSelonCritere(critere);
        List<MaillonNoeud> liste = new LinkedList<MaillonNoeud>();
        for(int i = 0; i < listeCriteres.size(); i++){
            if(signe == 0 && this.calculerNDistance(sommet, listeCriteres.get(i).getNom()) <= nDistance){ // <=
                liste.add(listeCriteres.get(i));
            }
            else if(signe == 1 && this.calculerNDistance(sommet, listeCriteres.get(i).getNom()) == nDistance){ // =
                liste.add(listeCriteres.get(i));
            }
            else if(signe == 2 && this.calculerNDistance(sommet, listeCriteres.get(i).getNom()) >= nDistance){ // >=
                liste.add(listeCriteres.get(i));
            }
        }

        return liste;
    }

    
    /** 
     * @param depart
     * @param null
     * @return int
     */
    /* Tâche 3.2, Tâche 3.3 */
    public int calculerNDistance(String depart, String arrive){ //Réadaptation du dijkstra (mais avec des arêtes qui ont pour poids 1)
        int poids = 0;
        MaillonNoeud ptrNoeud = this.premier; // Sommet à traiter
        MaillonArete ptrArete = null; // liste des voisins du sommet à traiter
        int nbNoeuds = this.getListeNoeuds().size();
        List<MaillonNoeud> sommetsTraites = new ArrayList<MaillonNoeud>(); // Sommets déjà traités
        int nbSommetsATraiter = nbNoeuds; // Nombre de sommets restants à traiter

        HashMap<MaillonNoeud, Integer> dictionnaireMeilleursChemins = new HashMap<MaillonNoeud, Integer>();

        // Initialisation
        while(ptrNoeud != null){
            // On met toutes les distances à infini, sauf le point de départ qui est à 0 et on met tous les antécédents à null
            if(ptrNoeud.getNom().equals(depart)){
                dictionnaireMeilleursChemins.put(ptrNoeud, 0);
            }
            else{
                dictionnaireMeilleursChemins.put(ptrNoeud, Integer.MAX_VALUE); // Integer.MAX_VALUE : equivalent à mettre infini (MAX_VALUE = 2147483647), -1 : equivalent à mettre -infini
            }
            ptrNoeud = ptrNoeud.getSuivant();
        }

        // On définit le sommet de départ comme sommet à traiter
        String nomSommetATraiter = depart;
        int resultatPrecedent = 0;
        do{
            // Partie 1 : On récupère le noeud et la liste des voisins
            ptrNoeud = this.rechercherNoeud(nomSommetATraiter);
            ptrArete = ptrNoeud.getListeVoisins();
            // Partie 2 : On va regarder quel sommets voisins dans ptrArete ne sont pas traités
            while(ptrArete != null){
                if(!sommetsTraites.contains(this.rechercherNoeud(ptrArete.getDestination()))){ // S'il ne contient pas le noeud qui a pour nom la destination de l'arête
                    boolean estPlusPetit = false;
                    // Partie 3 : On calcule la distance
                    int essaiNouvelleValeur = resultatPrecedent + 1;
                    // Partie 4 : On vérifie si la nouvelle valeur est plus petite que l'ancienne
                    int distanceAComparer = dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()));
                    if(distanceAComparer > essaiNouvelleValeur){
                        estPlusPetit = true;
                    }
                    
                    if(estPlusPetit){
                        dictionnaireMeilleursChemins.put(this.rechercherNoeud(ptrArete.getDestination()), essaiNouvelleValeur);
                    }
                }
                ptrArete = ptrArete.getSuivant();
            }
            // On note que le sommet est désormais traité et qu'il n'y a plus besoin de le retraiter
            sommetsTraites.add(ptrNoeud);
            // Partie 5 : On recherche le prochain sommet à traiter qui a les meilleures valeurs
            nomSommetATraiter = "";
            resultatPrecedent = Integer.MAX_VALUE;
            ptrNoeud = this.premier;
            while(ptrNoeud != null){
                boolean estMeilleur = false;
                if(!sommetsTraites.contains(this.rechercherNoeud(ptrNoeud.getNom()))){
                    if(resultatPrecedent >= dictionnaireMeilleursChemins.get(ptrNoeud)){
                        estMeilleur = true;
                    }
                }
                if(estMeilleur){
                    resultatPrecedent = dictionnaireMeilleursChemins.get(ptrNoeud);
                    nomSommetATraiter = ptrNoeud.getNom();
                }
                ptrNoeud = ptrNoeud.getSuivant();
            }
            
            ptrNoeud = this.rechercherNoeud(nomSommetATraiter);
            nbSommetsATraiter--;
        }while(nbSommetsATraiter > 0 && ptrNoeud.getNom() != arrive);

        // Partie 6 : On récupère le poids de depart vers arrive

        MaillonNoeud maillonArrive = this.rechercherNoeud(arrive);
        poids = dictionnaireMeilleursChemins.get(maillonArrive);

        return poids;
    }

    /* Tache 2.3 */
    /*public MaillonNoeud[] relieSommets(MaillonArete arete){
        MaillonNoeud[] tabResul = new MaillonNoeud[2];
        MaillonNoeud ptr1 = this.premier ;
        MaillonArete ptr2 = null ;
        while(ptr1 != null && ptr2 == null){
            ptr2 = ptr1.getListeVoisins();
            while(ptr2 != null && ptr2.equals(arete)){
                ptr2 = ptr2.getSuivant();
            }
            if(ptr2 == null){
                ptr1 = ptr1.getSuivant();
            }
        }
        if(ptr1 != null && ptr2 != null){
            tabResul[0] = ptr1;
            tabResul[1] = this.rechercherNoeud(ptr2.getDestination());
        }
        return tabResul;
    }*/
    
    /** 
     * @return String
     */
    /* Tâche 1.2 */
    public String getCarteFormatee(){ //Obtenir le graphe en format brut
        String retour = "";
        MaillonNoeud ptr1 = this.premier;
        MaillonArete ptr2;
        List<MaillonArete[]> listeAretes = this.getListeAretes();
        String ligne = "";
        while(ptr1 != null){
            retour = retour + ptr1.getNom() + ";";
            if(ptr1.getCaracteristiques().equals("Maternité")){
                retour = retour + "M";
            }
            else if(ptr1.getCaracteristiques().equals("Bloc Opératoire")){
                retour = retour + "O";
            }
            else{
                retour = retour + "N";
            }
            ligne = "";
            int i = listeAretes.size() - 1;
            while(i >= 0){
                ptr2 = ptr1.getListeVoisins();
                boolean existeArrete = false;
                while(ptr2 != null && !existeArrete){
                    if(listeAretes.get(i)[0].getDestination().equals(ptr2.getDestination()) && listeAretes.get(i)[1].getDestination().equals(ptr1.getNom()) || listeAretes.get(i)[0].getDestination().equals(ptr1.getNom()) && listeAretes.get(i)[1].getDestination().equals(ptr2.getDestination())){
                        ligne = (int)ptr2.getFiabilite() + ", " + (int)ptr2.getDistance() + ", " + (int)ptr2.getDuree() + ligne;
                        existeArrete = true;
                    }
                    else{
                        ptr2 = ptr2.getSuivant();
                    }
                    
                }
                if(!existeArrete){
                    ligne = "0" + ligne;
                }
                i = i - 1;
                ligne = ";" + ligne;
            }
            retour = retour + ligne + "\n";
            ptr1 = ptr1.getSuivant();
        }
        return retour;
    }

    
    /** 
     * @param depart
     * @param arrive
     * @param critere
     * @param critere2
     * @param critere3
     * @return Object[]
     */
    /* Tâches 2.5, 2.6, 3.6 */
    public Object[] dijkstra(String depart, String arrive, String critere, String critere2, String critere3){
        // return : [List<MaillonNoeud>, int[]]        
        List<MaillonNoeud> cheminALenvers = new ArrayList<MaillonNoeud>();
        List<MaillonNoeud> chemin = new ArrayList<MaillonNoeud>();
        int[] poids = new int[3];
        MaillonNoeud ptrNoeud = this.premier; // Sommet à traiter
        MaillonArete ptrArete = null; // liste des voisins du sommet à traiter
        int nbNoeuds = this.getListeNoeuds().size();
        List<MaillonNoeud> sommetsTraites = new ArrayList<MaillonNoeud>(); // Sommets déjà traités
        int nbSommetsATraiter = nbNoeuds; // Nombre de sommets restants à traiter

        // dictionnaireMeilleursChemins.get(MaillonNoeud)[0-2] sont des int;
        // dictionnaireMeilleursChemins.get(MaillonNoeud)[3] est le MaillonNoeud de l'antécédent

        // dictionnaireMeilleursChemins.get(MaillonNoeud)[0] : fiabilite
        // dictionnaireMeilleursChemins.get(MaillonNoeud)[1] : distance
        // dictionnaireMeilleursChemins.get(MaillonNoeud)[2] : duree

        HashMap<MaillonNoeud, Object[]> dictionnaireMeilleursChemins = new HashMap<MaillonNoeud, Object[]>();

        // Initialisation
        while(ptrNoeud != null){
            // On met toutes les fiabilites, distances et durees des sommets à infini, sauf le point de départ qui est à 0 pour la distance et la duree, et 1 pour la fiabilite, et on met tous les antécédents à null
            if(ptrNoeud.getNom().equals(depart)){
                dictionnaireMeilleursChemins.put(ptrNoeud, new Object[]{10, 0, 0, null});
            }
            else{
                dictionnaireMeilleursChemins.put(ptrNoeud, new Object[]{-1, Integer.MAX_VALUE, Integer.MAX_VALUE, null}); // Integer.MAX_VALUE : equivalent à mettre infini (MAX_VALUE = 2147483647), -1 : equivalent à mettre -infini
            }
            ptrNoeud = ptrNoeud.getSuivant();
        }

        // On définit le sommet de départ comme sommet à traiter
        String nomSommetATraiter = depart;
        int[] resultatsPrecedents = {10, 0, 0};
        do{
            // Partie 1 : On récupère le noeud et la liste des voisins
            ptrNoeud = this.rechercherNoeud(nomSommetATraiter);
            ptrArete = ptrNoeud.getListeVoisins();
            // Partie 2 : On va regarder quel sommets voisins dans ptrArete ne sont pas traités
            while(ptrArete != null){
                if(!sommetsTraites.contains(this.rechercherNoeud(ptrArete.getDestination()))){ // S'il ne contient pas le noeud qui a pour nom la destination de l'arête
                    int[] essaisNouvellesValeurs = new int[3];
                    boolean estPlusPetit = false;
                    // Partie 3 : On calcule la fiabilite, la duree et la distance
                    essaisNouvellesValeurs[0] = resultatsPrecedents[0] * ptrArete.getFiabilite() / 10;
                    essaisNouvellesValeurs[1] = resultatsPrecedents[1] + ptrArete.getDistance();
                    essaisNouvellesValeurs[2] = resultatsPrecedents[1] + ptrArete.getDuree();
                    // Partie 4 : On vérifie si les nouvelles valeurs sont plus petites que les anciennes
                    if(critere.equals("Fiabilite")){ // Cas 1 : Comparaison sur la fiabilité
                        int fiabiliteAComparer = (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[0];
                        if(critere2 != null && fiabiliteAComparer == essaisNouvellesValeurs[0]){ // Si les 2 valeurs sont identiques, alors on regarde le critère suivant
                            if(critere2.equals("Duree")){ // Cas 1A : Comparison sur la durée et la fiabilité
                                int dureeAComparer = (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[2];
                                if(dureeAComparer > essaisNouvellesValeurs[2]){
                                    estPlusPetit = true;
                                }
                                else if(critere3 != null && dureeAComparer == essaisNouvellesValeurs[2] && (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[1] > essaisNouvellesValeurs[1]){ // Cas spécial : Comparaison sur tous les critères
                                    estPlusPetit = true;
                                }
                            }
                            else /*(critere.equals("Distance"))*/{ // Cas 2B : Comparison sur la durée et la distance
                                int distanceAComparer = (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[1];
                                if(distanceAComparer > essaisNouvellesValeurs[1]){
                                    estPlusPetit = true;
                                }
                                else if(critere3 != null && distanceAComparer == essaisNouvellesValeurs[1] && (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[2] > essaisNouvellesValeurs[2]){ // Cas spécial : Comparaison sur tous les critères
                                    estPlusPetit = true;
                                }
                            }
                        }
                        else if(fiabiliteAComparer < essaisNouvellesValeurs[2]){
                            estPlusPetit = true;
                        }
                    }
                    else if(critere.equals("Distance")){ // Cas 2 : Comparaison sur la distance
                        int distanceAComparer = (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[1];
                        if(critere2 != null && distanceAComparer == essaisNouvellesValeurs[1]){ // Si les 2 valeurs sont identiques, alors on regarde le critère suivant
                            if(critere2.equals("Fiabilite")){ // Cas 2A : Comparison sur la distance et la fiabilité
                                int fiabiliteAComparer = (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[0];
                                if(fiabiliteAComparer < essaisNouvellesValeurs[0]){
                                    estPlusPetit = true;
                                }
                                else if(critere3 != null && fiabiliteAComparer == essaisNouvellesValeurs[0] && (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[2] > essaisNouvellesValeurs[2]){ // Cas spécial : Comparaison sur tous les critères
                                    estPlusPetit = true;
                                }
                            }
                            else /*(critere.equals("Duree"))*/{ // Cas 2B : Comparison sur la distance et la duree
                                int dureeAComparer = (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[2];
                                if(dureeAComparer > essaisNouvellesValeurs[2]){
                                    estPlusPetit = true;
                                }
                                else if(critere3 != null && dureeAComparer == essaisNouvellesValeurs[2] && (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[0] < essaisNouvellesValeurs[0]){ // Cas spécial : Comparaison sur tous les critères
                                    estPlusPetit = true;
                                }
                            }
                        }
                        else if(distanceAComparer > essaisNouvellesValeurs[1]){
                            estPlusPetit = true;
                        }
                    }
                    else /*(critere.equals("Duree"))*/{ // Cas 3 : Comparaison sur la durée
                        int dureeAComparer = (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[2];
                        if(critere2 != null && dureeAComparer == essaisNouvellesValeurs[2]){ // Si les 2 valeurs sont identiques, alors on regarde le critère suivant
                            if(critere2.equals("Fiabilite")){ // Cas 3A : Comparison sur la durée et la fiabilité
                                int fiabiliteAComparer = (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[0];
                                if(fiabiliteAComparer < essaisNouvellesValeurs[0]){
                                    estPlusPetit = true;
                                }
                                else if(critere3 != null && fiabiliteAComparer == essaisNouvellesValeurs[0] && (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[1] > essaisNouvellesValeurs[1]){ // Cas spécial : Comparaison sur tous les critères
                                    estPlusPetit = true;
                                }
                            }
                            else /*(critere.equals("Distance"))*/{ // Cas 3B : Comparison sur la durée et la distance
                                int distanceAComparer = (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[1];
                                if(distanceAComparer > essaisNouvellesValeurs[1]){
                                    estPlusPetit = true;
                                }
                                else if(critere3 != null && distanceAComparer == essaisNouvellesValeurs[1] && (int)dictionnaireMeilleursChemins.get(this.rechercherNoeud(ptrArete.getDestination()))[0] < essaisNouvellesValeurs[0]){ // Cas spécial : Comparaison sur tous les critères
                                    estPlusPetit = true;
                                }
                            }
                        }
                        else if(dureeAComparer > essaisNouvellesValeurs[2]){
                            estPlusPetit = true;
                        }
                    }
                    if(estPlusPetit){
                        dictionnaireMeilleursChemins.put(this.rechercherNoeud(ptrArete.getDestination()), new Object[]{essaisNouvellesValeurs[0], essaisNouvellesValeurs[1], essaisNouvellesValeurs[2], ptrNoeud});
                    }
                }
                ptrArete = ptrArete.getSuivant();
            }
            // On note que le sommet est désormais traité et qu'il n'y a plus besoin de le retraiter
            sommetsTraites.add(ptrNoeud);
            // Partie 5 : On recherche le prochain sommet à traiter qui a les meilleures valeurs
            nomSommetATraiter = "";
            resultatsPrecedents[0] = -1;
            resultatsPrecedents[1] = Integer.MAX_VALUE;
            resultatsPrecedents[2] = Integer.MAX_VALUE;
            ptrNoeud = this.premier;
            while(ptrNoeud != null){
                boolean estMeilleur = false;
                if(!sommetsTraites.contains(this.rechercherNoeud(ptrNoeud.getNom()))){
                    if(critere.equals("Fiabilite")){ // Cas 1 : Comparaison sur la fiabilité
                        if(critere2 != null && resultatsPrecedents[0] == (int)dictionnaireMeilleursChemins.get(ptrNoeud)[0]){ // Si les 2 valeurs sont identiques, alors on regarde le critère suivant
                            if(critere2.equals("Duree")){ // Cas 1A : Comparison sur la durée et la fiabilité
                                if(resultatsPrecedents[2] > (int)dictionnaireMeilleursChemins.get(ptrNoeud)[2]){
                                    estMeilleur = true;
                                }
                                else if(critere3 != null && resultatsPrecedents[2] == (int)dictionnaireMeilleursChemins.get(ptrNoeud)[2] && resultatsPrecedents[1] > (int)dictionnaireMeilleursChemins.get(ptrNoeud)[1]){ // Cas spécial : Comparaison sur tous les critères
                                    estMeilleur = true;
                                }
                            }
                            else /*(critere.equals("Distance"))*/{ // Cas 2B : Comparison sur la durée et la distance
                                if(resultatsPrecedents[1] > (int)dictionnaireMeilleursChemins.get(ptrNoeud)[1]){
                                    estMeilleur = true;
                                }
                                else if(critere3 != null && resultatsPrecedents[1] == (int)dictionnaireMeilleursChemins.get(ptrNoeud)[1] && resultatsPrecedents[2] > (int)dictionnaireMeilleursChemins.get(ptrNoeud)[2]){ // Cas spécial : Comparaison sur tous les critères
                                    estMeilleur = true;
                                }
                            }
                        }
                        else if(resultatsPrecedents[0] < (int)dictionnaireMeilleursChemins.get(ptrNoeud)[0]){
                            estMeilleur = true;
                        }
                    }
                    else if(critere.equals("Distance")){ // Cas 2 : Comparaison sur la distance
                        if(critere2 != null && resultatsPrecedents[1] == (int)dictionnaireMeilleursChemins.get(ptrNoeud)[1]){ // Si les 2 valeurs sont identiques, alors on regarde le critère suivant
                            if(critere2.equals("Fiabilite")){ // Cas 2A : Comparison sur la distance et la fiabilité
                                if(resultatsPrecedents[0] < (int)dictionnaireMeilleursChemins.get(ptrNoeud)[0]){
                                    estMeilleur = true;
                                }
                                else if(critere3 != null && resultatsPrecedents[0] == (int)dictionnaireMeilleursChemins.get(ptrNoeud)[0] && resultatsPrecedents[2] > (int)dictionnaireMeilleursChemins.get(ptrNoeud)[2]){ // Cas spécial : Comparaison sur tous les critères
                                    estMeilleur = true;
                                }
                            }
                            else /*(critere.equals("Duree"))*/{ // Cas 2B : Comparison sur la distance et la duree
                                if(resultatsPrecedents[2] > (int)dictionnaireMeilleursChemins.get(ptrNoeud)[2]){
                                    estMeilleur = true;
                                }
                                else if(critere3 != null && resultatsPrecedents[2] == (int)dictionnaireMeilleursChemins.get(ptrNoeud)[2] && resultatsPrecedents[0] < (int)dictionnaireMeilleursChemins.get(ptrNoeud)[0]){ // Cas spécial : Comparaison sur tous les critères
                                    estMeilleur = true;
                                }
                            }
                        }
                        else if(resultatsPrecedents[1] > (int)dictionnaireMeilleursChemins.get(ptrNoeud)[1]){
                            estMeilleur = true;
                        }
                    }
                    else /*(critere.equals("Duree"))*/{ // Cas 3 : Comparaison sur la durée
                        if(critere2 != null && resultatsPrecedents[2] == (int)dictionnaireMeilleursChemins.get(ptrNoeud)[2]){ // Si les 2 valeurs sont identiques, alors on regarde le critère suivant
                            if(critere2.equals("Fiabilite")){ // Cas 3A : Comparison sur la durée et la fiabilité
                                if(resultatsPrecedents[0] < (int)dictionnaireMeilleursChemins.get(ptrNoeud)[0]){
                                    estMeilleur = true;
                                }
                                else if(critere3 != null && resultatsPrecedents[0] == (int)dictionnaireMeilleursChemins.get(ptrNoeud)[0] && resultatsPrecedents[1] > (int)dictionnaireMeilleursChemins.get(ptrNoeud)[1]){ // Cas spécial : Comparaison sur tous les critères
                                    estMeilleur = true;
                                }
                            }
                            else /*(critere.equals("Distance"))*/{ // Cas 3B : Comparison sur la durée et la distance
                                if(resultatsPrecedents[1] > (int)dictionnaireMeilleursChemins.get(ptrNoeud)[1]){
                                    estMeilleur = true;
                                }
                                else if(critere3 != null && resultatsPrecedents[1] == (int)dictionnaireMeilleursChemins.get(ptrNoeud)[1] && resultatsPrecedents[0] < (int)dictionnaireMeilleursChemins.get(ptrNoeud)[0]){ // Cas spécial : Comparaison sur tous les critères
                                    estMeilleur = true;
                                }
                            }
                        }
                        else if(resultatsPrecedents[2] > (int)dictionnaireMeilleursChemins.get(ptrNoeud)[2]){
                            estMeilleur = true;
                        }
                    }
                }
                if(estMeilleur){
                    resultatsPrecedents[0] = (int)dictionnaireMeilleursChemins.get(ptrNoeud)[0];
                    resultatsPrecedents[1] = (int)dictionnaireMeilleursChemins.get(ptrNoeud)[1];
                    resultatsPrecedents[2] = (int)dictionnaireMeilleursChemins.get(ptrNoeud)[2];
                    nomSommetATraiter = ptrNoeud.getNom();
                }
                ptrNoeud = ptrNoeud.getSuivant();
            }
            
            ptrNoeud = this.rechercherNoeud(nomSommetATraiter);
            nbSommetsATraiter--;
        }while(nbSommetsATraiter > 0 && ptrNoeud.getNom() != arrive);

        // Partie 6 : On récupère le chemin final de depart vers arrive

        MaillonNoeud maillonArrive = this.rechercherNoeud(arrive);

        poids[0] = (int)dictionnaireMeilleursChemins.get(maillonArrive)[0]; // Fiabilité
        poids[1] = (int)dictionnaireMeilleursChemins.get(maillonArrive)[1]; // Distance
        poids[2] = (int)dictionnaireMeilleursChemins.get(maillonArrive)[2]; // Durée

        Object[] elt = dictionnaireMeilleursChemins.get(maillonArrive);
        String precedent = arrive;
        MaillonNoeud maillonPrecedent;
        cheminALenvers.add(maillonArrive);

        while(!precedent.equals(depart)){
            maillonPrecedent = (MaillonNoeud)elt[3];
            precedent = maillonPrecedent.getNom();
            cheminALenvers.add(maillonPrecedent);
            elt = dictionnaireMeilleursChemins.get(maillonPrecedent);
        }

        // Petit problème : Le chemin est affiché à l'envers
        for(int i = cheminALenvers.size()-1; i >= 0; i--){
            chemin.add(cheminALenvers.get(i));
        }

        return new Object[]{chemin, poids};
    }
}