/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Step3;

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
    
    public Client(int numAppli) {
        this.numApp = numAppli;
          entree = new Scanner(System.in);
          listeF = new ArrayList<>();
          listeB = new ArrayList<>();
          sortieNumBloc = new ArrayList<>();
          entreeFich = new ArrayList<>();
          entreeBin = new ArrayList<>();
    }
    
    /*
    * cette fonction nous permet de récupérer les différents blocs du fichier
    */
    public void recupererFichier() throws IOException {
        /*
        * On essaie d'abord de se connecter aux différentes applications
        * On stocke les applications qui nous répondent dans les 2 listes
        * Pour pas qu'un client se retrouve sans applications auxquelles se connecter
        * On boucle tant que l'on s'est pas connecté à au moins une application
        * qui possède le fichier
        */
        while(listeF.isEmpty()) {
            for (int i=0;i<5;i++){
                try {
                    listeF.add(new Socket("localhost", 40000+i));
                    listeB.add(new Socket("localhost", 39000+i));
                } catch (ConnectException e) {
                    
                }
            }
        }
        // le nombre d'application disponible correspond au nombre d'appli enregistrer dans la liste
        nbServ = listeF.size();
        
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
        
        String chaine="";
        int random;
        byte[] buffer = new byte[250];
        int n;
        
        // On créé le dossier BitTorrentStep2 la racine du disque C pour stocker le fichier
        String destFich = "C:\\BitTorrentStep3" + "Appli" + numApp +  "\\";
        File dossier = new File(destFich);
        boolean estCree = dossier.mkdirs();
        
        
        // Demande du nom de fichier à un serveur au hasard dans la liste
        while(chaine.equals("")) {
            random = (int) (Math.random()*(nbServ-1));
            sortieNumBloc.get(random).println("nom");
            chaine = entreeFich.get(random).readLine();
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
        while(bloc!=4) {
            random = (int) (Math.random()*(nbServ-1));
            sortieNumBloc.get(random).println(chaine);
            while ((n=entreeBin.get(random).read(buffer)) > 0){
                fop.write(buffer,0,n);
                if (n < buffer.length) {
                    break;
                }
                
            }
            bloc++;
            chaine=""+bloc;
        }

    }
    
}




