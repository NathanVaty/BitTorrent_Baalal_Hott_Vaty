/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Step45;

import Step4.*;
import Step3.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;

/**
 * Programme qui décrit le comportement d'une application qui ne possède pas le 
 * fichier
 * @author Nathan Vaty
 */
public class Client {
    
    int numApp;
    int nbServ;
    
    // Liste qui contient les sockets pour l'échange de fichiers en format String
    List<Socket> listeF;
    
    // Liste qui contient les sockets pour l'échange de données binaires 
    List<Socket> listeB;
    
    // Liste qui permet d'envoyer le texte sous forme de String à la bonne application
    List<PrintStream> sortieNumBloc;
    
    // Liste qui permet de recevoir le texte suivant l'application à qui on a demandé des infos
    List<BufferedReader> entreeFich;
    
    // Liste qui permet de lire les données binaires suivant le socket concerné
    List<BufferedInputStream> entreeBin;
    
    //Scanner pour les entrées claviers
    Scanner entree;
    
    
    boolean fichierReconstitue;
    
    String destFich;
    String chaine;
    
    Socket ssm;
    
    public Client(Socket sserv) {
        //this.numApp = numAppli;
          entree = new Scanner(System.in);
          listeF = new ArrayList<>();
          listeB = new ArrayList<>();
          sortieNumBloc = new ArrayList<>();
          entreeFich = new ArrayList<>();
          entreeBin = new ArrayList<>();
          fichierReconstitue = true;
          destFich="";
          chaine="";
          ssm = sserv;
    }
    public synchronized List<Socket> listeFServeur(List<String> ip,List<Integer> ports) throws IOException {
        List<Socket> listeFi = new ArrayList<>(); 
        for(int j=0;j<ip.size();j++){
            try {
                listeFi.add(new Socket(ip.get(j),ports.get(j)));
            } catch (ConnectException e) {
            }
            
        }
         
        return listeFi;
    }
    
    public synchronized List<Socket> listeBServeur(List<String> ip,List<Integer> ports) throws IOException {
        List<Socket> listeBi = new ArrayList<>();
        for(int j=0;j<ip.size();j++){
            try {
                listeBi.add(new Socket(ip.get(j),ports.get(j)));
            } catch (ConnectException e) {
            }
            
        }
        return listeBi;
    }
   
    public boolean[] recupBlocs(boolean[] blocs,List<String> listIP, List<Integer> listPortsF,
            List<Integer> listPortsB,List<String> listeBlocs,String chemin, String fich) throws IOException{
        
        System.out.println("dans le cli");
        PrintStream pms = new PrintStream(ssm.getOutputStream());
       
       listeF = listeFServeur(listIP,listPortsF);
       listeB = listeBServeur(listIP,listPortsB);
        
        // le nombre d'application disponible correspond au nombre d'appli enregistrer dans la liste
        nbServ = listeF.size();
        System.out.println(nbServ);
      
        /*
        * On initialise les différentes listes qui permettront d'echanger des données
        * avec les autres applications
        */
        for (int i=0;i<listeF.size();i++){
            try {
                sortieNumBloc.add(new PrintStream(listeF.get(i).getOutputStream()));
                entreeFich.add(new BufferedReader(new InputStreamReader(listeF.get(i).getInputStream())));
                entreeBin.add(new BufferedInputStream(listeB.get(i).getInputStream()));
            } catch (IOException e) {
                
            }
        }
        
        int random;
        byte[] buffer = new byte[250];
        int n;
        
        // Construction du FileOutputStream pour écrire dans le fichier
        FileOutputStream fop = new FileOutputStream(chemin,true);
        //FileWriter fop = new FileWriter(chemin+fich);
        int bloc = !blocs[0] ? 1 : (!blocs[1]? 2:(!blocs[3]? 3: 4));
        chaine = ""+bloc;
        
        List<Integer> listeIndiceBloc1 = new ArrayList<>();
        List<Integer> listeIndiceBloc2 = new ArrayList<>();
        List<Integer> listeIndiceBloc3 = new ArrayList<>();
        for (int i=0; i<listeBlocs.size();i++){
            System.out.println("chaine blocs");
            System.out.println(listeBlocs.get(i));
            if(listeBlocs.get(i).contains("1")) {
                listeIndiceBloc1.add(i);
            }
            if(listeBlocs.get(i).contains("2")) {
                listeIndiceBloc2.add(i);
            }
            if(listeBlocs.get(i).contains("3")) {
                listeIndiceBloc3.add(i);
            }
        }
        System.out.println(listeIndiceBloc1.size());
        /*
        * on demande à une application au hasard qui possède le fichier de nous
        * renvoyer le bloc demandé
        * tant que l'on a pas récupérer les 3 blocs on boucle
        */
       // int debEcrit =0;
        while(bloc!=4) {
            System.out.println("passe");
            System.out.println(bloc);
            switch(bloc){
                case 1: 
                    if (!listeIndiceBloc1.isEmpty()){
                        random = (int) (Math.random()*((listeIndiceBloc1.size())-1));
                        sortieNumBloc.get(listeIndiceBloc1.get(random)).println(chaine);
                        while ((n=entreeBin.get(listeIndiceBloc1.get(random)).read(buffer)) > 0){
                            System.out.println("dans 2e while");
                            synchronized(fop){
                                fop.write(buffer,0,n);
                            }
                           
                            //debEcrit+=n;
                            if (n < buffer.length) {
                                break;
                            }
                            
                        }
                        blocs[0]=true;
                    } else {
                        fichierReconstitue=false;
                    }
                    if (!blocs[1]) {
                        bloc++;
                    } else if (!blocs[2]) {
                        bloc+=2;
                    } else {
                        bloc = 4;
                    }
                    break;
                case 2: 
                    if (!listeIndiceBloc2.isEmpty()){
                        random = (int) (Math.random()*((listeIndiceBloc2.size())-1));
                        sortieNumBloc.get(listeIndiceBloc2.get(random)).println(chaine);
                        // ATTention ici
                        while ((n=entreeBin.get(listeIndiceBloc2.get(random)).read(buffer)) > 0){
                            System.out.println("dans 2e while");
                            synchronized(fop){
                                fop.write(buffer,0,n);
                            }
                            if (n < buffer.length) {
                                break;
                            }
                            
                        }
                        blocs[1]=true;
                    } else {
                        fichierReconstitue=false;
                    }
                    if (!blocs[2]) {
                        bloc++;
                    } else {
                        bloc = 4;
                    }
                    break;
                case 3:
                    if (!listeIndiceBloc3.isEmpty()) {
                        random = (int) (Math.random()*((listeIndiceBloc3.size())-1));
                        sortieNumBloc.get(listeIndiceBloc3.get(random)).println(chaine);
                        while ((n=entreeBin.get(listeIndiceBloc3.get(random)).read(buffer)) > 0){
                            System.out.println("dans 2e while");
                            synchronized(fop){
                                fop.write(buffer,0,n);
                            }
                            if (n < buffer.length) {
                                break;
                            }
                            
                        }
                        blocs[2]=true;
                    } else {
                        fichierReconstitue=false;
                    }
                    bloc++;
                    break;
            }
            chaine=""+bloc;
        }
        System.out.println("sortieWhile");
        if (fichierReconstitue) {
            pms.println("Tout");
        } else {
            int totB=0;
            if(blocs[0] && blocs [1] && blocs[2]){
                totB = 7;
            } else {
                if (blocs[0]) {
                    totB++;
                }
                if (blocs[1]) {
                    totB+=2;
                }
                if (blocs[2]) {
                    totB+=4;
                }
            }
            switch(totB){
                case 1: pms.println("bloc 1");
                
                break;
                case 2: pms.println("bloc 2");
                
                break;
                case 3: pms.println("bloc 1 & 2");
                
                break;
                case 4: pms.println("bloc 3");
                
                break;
                case 5: pms.println("bloc 1 & 3");
                break;
                case 6: pms.println("bloc 2 & 3");
                break;
                case 7: pms.println("Tout");
                break;
            }
        }
        
        return blocs;
        
    }
    /*
    * cette fonction nous permet de récupérer les différents blocs du fichier
    */
    public boolean recupererFichier() throws IOException {
         
        PrintStream pms = new PrintStream(ssm.getOutputStream());
        BufferedReader readerMain = new BufferedReader(new InputStreamReader(ssm.getInputStream()));
        List <String> listeBlocsDispo = new ArrayList<>();
        List<String> listeIp = new ArrayList<>();
        List<Integer> listePortsF = new ArrayList<>();
        List<Integer> listePortsB = new ArrayList<>();
        
        // Demander au serveur les serveurs dispo pour connexion    
        
        pms.println("possede pas");
        String ch="";
        while(!ch.equals("fin")) {
            ch = readerMain.readLine();
            if (!ch.equals("fin")) {
                System.out.println(ch);
                listeIp.add(ch);
            }
        }
        ch="";
        while(!ch.equals("fin")) {
            ch = readerMain.readLine();
            if (!ch.equals("fin")) {
                listePortsF.add(Integer.parseInt(ch.substring(0,5)));
                System.out.println(ch.substring(0,5));
                listePortsB.add(Integer.parseInt(ch.substring(6,11)));
                System.out.println(ch.substring(6,11));
            }
        }
        ch="";
        while(!ch.equals("fin")) {
            ch = readerMain.readLine();
            System.out.println(ch);
            if (!ch.equals("fin")) {
                listeBlocsDispo.add(ch);
            }
        }

        
       listeF = listeFServeur(listeIp,listePortsF);
       listeB = listeBServeur(listeIp,listePortsB);
        
        // le nombre d'application disponible correspond au nombre d'appli enregistrer dans la liste
        nbServ = listeF.size();
        System.out.println(nbServ);
        
        System.out.println("Vous devenez client qui cherche à "
                + "récupérer le fichier");
        
        /*
        * On initialise les différentes listes qui permettront d'echanger des données
        * avec les autres applications
        */
        for (int i=0;i<listeF.size();i++){
            try {
                sortieNumBloc.add(new PrintStream(listeF.get(i).getOutputStream()));
                entreeFich.add(new BufferedReader(new InputStreamReader(listeF.get(i).getInputStream())));
                entreeBin.add(new BufferedInputStream(listeB.get(i).getInputStream()));
            } catch (IOException e) {
                
            }
        }
        System.out.println("ici");
        int random;
        byte[] buffer = new byte[250];
        int n;
        
        // On créé le dossier BitTorrentStep2 la racine du disque C pour stocker le fichier
        destFich = "C:\\BitTorrentStep4" + "\\";
        File dossier = new File(destFich);
        boolean estCree = dossier.mkdirs();
        
        
        chaine="";
        // Demande du nom de fichier à un serveur au hasard dans la liste
        while(chaine.equals("")) {
            System.out.println("demande du nom");
            random = (int) (Math.random()*(nbServ-1));
            System.out.println(random);
            System.out.println(sortieNumBloc.size());
            sortieNumBloc.get(random).println("nom");
            chaine = entreeFich.get(random).readLine();
            System.out.println("nom");
        }
        
        // Construction du FileOutputStream pour écrire dans le fichier
        FileOutputStream fop = new FileOutputStream(new File(destFich+chaine));
        int bloc = 1;
        chaine = ""+bloc;
        
        /*
        * on demande à une application au hasard qui possède le fichier de nous
        * renvoyer le bloc demandé
        * tant que l'on a pas récupérer les 3 blocs on boucle
        */
        // Pas bon faudra changer pour chercher dans les serveurs qui possèdent le blocs en question
        List<Integer> listeIndiceBloc1 = new ArrayList<>();
        List<Integer> listeIndiceBloc2 = new ArrayList<>();
        List<Integer> listeIndiceBloc3 = new ArrayList<>();
        for (int i=0; i<listeBlocsDispo.size();i++){
            if(listeBlocsDispo.get(i).contains("1")) {
                listeIndiceBloc1.add(i);
            }
            if(listeBlocsDispo.get(i).contains("2")) {
                listeIndiceBloc2.add(i);
            }
            if(listeBlocsDispo.get(i).contains("3")) {
                listeIndiceBloc3.add(i);
            }
        }
        /*
        * on demande à une application au hasard qui possède le fichier de nous
        * renvoyer le bloc demandé
        * tant que l'on a pas récupérer les 3 blocs on boucle
        */
        while(bloc!=4) {
            System.out.println("passe");
            System.out.println(bloc);
            switch(bloc){
                case 1: 
                    if (!listeIndiceBloc1.isEmpty()){
                        random = (int) (Math.random()*((listeIndiceBloc1.size())-1));
                        sortieNumBloc.get(listeIndiceBloc1.get(random)).println(chaine);
                        while ((n=entreeBin.get(listeIndiceBloc1.get(random)).read(buffer)) > 0){
                            System.out.println("dans 2e while");
                            synchronized(fop){
                                fop.write(buffer,0,n);
                            }
                            if (n < buffer.length) {
                                break;
                            }
                            
                        }
                        
                    } else {
                        fichierReconstitue=false;
                    }
                    bloc++;
                    break;
                case 2: 
                    if (!listeIndiceBloc2.isEmpty()){
                        random = (int) (Math.random()*((listeIndiceBloc2.size())-1));
                        sortieNumBloc.get(listeIndiceBloc2.get(random)).println(chaine);
                      
                        while ((n=entreeBin.get(listeIndiceBloc2.get(random)).read(buffer)) > 0){
                            System.out.println("dans 2e while");
                           synchronized(fop){
                                fop.write(buffer,0,n);
                            }
                            if (n < buffer.length) {
                                break;
                            }
                            
                        }
                        
                    } else {
                        fichierReconstitue=false;
                    }
                   bloc++;
                    break;
                case 3:
                    if (!listeIndiceBloc3.isEmpty()) {
                        random = (int) (Math.random()*((listeIndiceBloc3.size())-1));
                        sortieNumBloc.get(listeIndiceBloc3.get(random)).println(chaine);
                        while ((n=entreeBin.get(listeIndiceBloc3.get(random)).read(buffer)) > 0){
                            System.out.println("dans 2e while");
                            synchronized(fop){
                                fop.write(buffer,0,n);
                            }
                            if (n < buffer.length) {
                                break;
                            }
                            
                        }
                    } else {
                        fichierReconstitue=false;
                    }
                    bloc++;
                    break;
            }
            chaine=""+bloc;
        }
        if (fichierReconstitue) {
            pms.println("Tout");
        } else {
            int totB=0;
            
            switch(totB){
                case 1: pms.println("bloc 1");
                
                break;
                case 2: pms.println("bloc 2");
                
                break;
                case 3: pms.println("bloc 1 & 2");
                
                break;
                case 4: pms.println("bloc 3");
                
                break;
                case 5: pms.println("bloc 1 & 3");
                break;
                case 6: pms.println("bloc 2 & 3");
                break;
                case 7: pms.println("Tout");
                break;
            }
        }
       // pms.println("Tout");
        
       return true;

    }
    
    
}







