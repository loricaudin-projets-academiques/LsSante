
    public int espaceEntre2Sommets(String depart, String arrive){
        List<MaillonNoeud> cheminALenvers = new ArrayList<MaillonNoeud>();
        List<MaillonNoeud> chemin = new ArrayList<MaillonNoeud>();
        int poids;
        int resultat = 0;
        MaillonNoeud ptrNoeud = this.premier;
        MaillonArete ptrArete = null;
        int nbNoeuds = this.getListeNoeuds().size();
        List<MaillonNoeud> sommetsTraites = new ArrayList<MaillonNoeud>();
        int nbSommetsATraiter = nbNoeuds;

        // sommetsTraites[i][0-2] est un int; sommetsTraites[i][3] est un MaillonNoeud
        // sommetsTaites[i][0] : fiabilite
        // sommetsTaites[i][1] : distance
        // sommetsTaites[i][2] : duree

        HashMap<MaillonNoeud, Integer> dictionnaireMeilleursChemins = new HashMap<MaillonNoeud, Integer>();

        // Initialisation
        while(ptrNoeud != null){
            if(ptrNoeud.getNom().equals(depart)){
                dictionnaireMeilleursChemins.put(ptrNoeud, 0);
            }
            else{
                dictionnaireMeilleursChemins.put(ptrNoeud, Integer.MAX_VALUE);
            }
            ptrNoeud = ptrNoeud.getSuivant();
            
        }

        // On définit le sommet de départ comme sommet à traiter
        String nomSommetATraiter = depart;
        int resultatsPrecedents = 0;
        do{
            // Partie 1 : On récupère le noeud et la liste des voisins
            ptrNoeud = this.rechercherNoeud(nomSommetATraiter);
            ptrArete = ptrNoeud.getListeVoisins();
            // Partie 2 : On va regarder quel sommets voisins dans ptrArete ne sont pas traités
            while(ptrArete != null){
                if(!sommetsTraites.contains(this.rechercherNoeud(ptrArete.getDestination()))){ // S'il ne contient pas le noeud qui a pour nom la destination de l'arête
                    int essaisNouvellesValeurs = new int[3];
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

        System.out.println(arrive);

        while(!precedent.equals(depart)){
            maillonPrecedent = (MaillonNoeud)elt[3];
            precedent = maillonPrecedent.getNom();
            cheminALenvers.add(maillonPrecedent);
            elt = dictionnaireMeilleursChemins.get(maillonPrecedent);
        }

        // Petit problème : Le chemin est affiché à l'envers
        for(int i = cheminALenvers.size(); i >= 0; i--){
            chemin.add(cheminALenvers.get(i));
        }
        resultat[0] = chemin;
        resultat[1] = poids;

        return resultat;
    }