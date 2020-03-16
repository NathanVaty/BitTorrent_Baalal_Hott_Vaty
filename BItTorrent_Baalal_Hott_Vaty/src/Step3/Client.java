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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Nathan Vaty
 */
public class Client {
    
    int numApp;
    int nbServ;
    
    List<Socket> listeF;
    List<Socket> listeB;
    
    List<PrintStream> sortieNumBloc;
    List<BufferedReader> entreeFich;
    List<BufferedInputStream> bis;
    
    Scanner entree;
    
    public Client(int numAppli) {
        this.numApp = numAppli;
          entree = new Scanner(System.in);
          listeF = new ArrayList<>();
          listeB = new ArrayList<>();
          sortieNumBloc = new ArrayList<>();
          entreeFich = new ArrayList<>();
          bis = new ArrayList<>();
    }
    
    public void recupererFichier() throws IOException {
        
        for (int i=0;i<5;i++){
            try {
                listeF.add(new Socket("localhost", 40000+i));
                listeB.add(new Socket("localhost", 39000+i));
            } catch (ConnectException e) {
                
            }
        }
        System.out.println(listeF.isEmpty());
        nbServ = listeF.size();
            
             System.out.println("Vous devenez client qui cherche à "
                    + "récupérer le fichier");
       for (int i=0;i<listeF.size();i++){
            try {
                sortieNumBloc.add(new PrintStream(listeF.get(i).getOutputStream()));
                entreeFich.add(new BufferedReader(new InputStreamReader(listeF.get(i).getInputStream())));
                bis.add(new BufferedInputStream(listeB.get(i).getInputStream()));
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
                
              
                
            //System.out.println(random);
                
            // Demande du nom de fichier
            while(chaine.equals("")) {
                random = (int) (Math.random()*(nbServ-1));
                sortieNumBloc.get(random).println("nom");
                chaine = entreeFich.get(random).readLine();
            }
              
                  
           // Construction du FileOutputStream pour écrire dans le fichier
           FileOutputStream fop = new FileOutputStream(new File(destFich+chaine));
           int bloc = 1;
           chaine = ""+bloc;
           
           while(bloc!=4) {
               random = (int) (Math.random()*(nbServ-1));
               sortieNumBloc.get(random).println(chaine);
               while ((n=bis.get(random).read(buffer)) > 0){
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

