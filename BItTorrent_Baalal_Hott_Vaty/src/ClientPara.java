
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author Vaty, Hott, Baalal
 */

public class ClientPara {
    public static void main(String args[]) throws IOException, InterruptedException {
        // Création d'un socket client pour la communication du nom de fichier
        Socket sFich; 
        
        // Création d'un socket pour la communication des données binaires
        Socket sBin; 
        
        
        try {
            sFich = new Socket("localhost", 40000);
            
            sBin = new Socket("localhost", 39000);
           
            // Construction d'un PrintStream pour spécifier le bloc à télécharger à partir du serveur
            PrintStream sortieNumBloc = new PrintStream(sFich.getOutputStream());
            
            // Construction d'un BufferedReader pour récupérer le nom du fichier à télécharger et son extension
            BufferedReader entreeFich = new BufferedReader(new InputStreamReader(sFich.getInputStream()));
            
            // Construction du BufferedInputStream pour lire les données binaires
            BufferedInputStream bis = new BufferedInputStream(sBin.getInputStream());
            
            String chaine="";
            byte[] buffer = new byte[250];
            int n;
            
             // On créé le dossier BitTorrentStep2 la racine du disque C pour stocker le fichier
                String destFich = "C:\\BitTorrentStep2\\";
                File dossier = new File(destFich);
                boolean estCree = dossier.mkdirs();
                
                
           
           // On lit le nom du fichier à copier
           chaine = entreeFich.readLine();
           System.out.println(chaine);    
           
           // Construction du FileOutputStream pour écrire dans le fichier
           FileOutputStream fop = new FileOutputStream(new File(destFich+chaine));
           ByteBuffer buffsec = ByteBuffer.allocate(2000000);
           ByteBuffer bufftrois = ByteBuffer.allocate(2000000);
          
           Scanner entree = new Scanner(System.in);
           int tailleA = 0;
           int tailleB = 0;
           for (int i=0; i<3;i++) {
               
               System.out.print("Entrez le numéro de bloc à télécharger sur le serveur (de 1 à 3): ");
               chaine = entree.nextLine();
              
               sortieNumBloc.println(chaine);
               
               
               
               switch(chaine) {
                   case "1":
                       System.out.println("1");
                       while((n=bis.read(buffer)) >0) {
                           fop.write(buffer,0,n);
                           if (n < buffer.length) {
                               break;
                           }
                           
                       }
                       break;
                   case "2":
                       System.out.println("2");
                       while((n=bis.read(buffer)) >0) {
                          
                           buffsec.put(buffer,0,n);
                           tailleA += n;
                           if (n < buffer.length) {
                               break;
                           }
                           
                            
                       }
                       break;
                   case "3":
                       System.out.println("3");
                       while((n=bis.read(buffer)) >0) {
                           
                           bufftrois.put(buffer,0,n);
                           tailleB += n;
                           
                           
                           if (n < buffer.length) {
                               break;
                           }
                            
                       }
                     
                       break;
               }
               
           }
           
           System.out.println("Tous les blocs ont été récupéré");
           
            byte[] buf = new byte[tailleA];
            byte[] bufb = new byte[tailleB];
           
           
            
            System.out.println(buf.length);
            
            for (int i=0; i < tailleA; i++) {
                buf[i] = buffsec.get(i);
            }
            for (int i=0; i < tailleB; i++) {
                bufb[i] = bufftrois.get(i);
            }
            fop.write(buf);
            fop.write(bufb);
            // on ferme nous aussi la connexion
            sFich.close();
            sBin.close();
            fop.close();
           
        } catch(IOException e) {
        }
    }
}
