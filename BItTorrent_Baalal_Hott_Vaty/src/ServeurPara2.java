import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/*
* Ce serveur servira à envoyer la deuxième partie du fichier
*/

/**
 *
 * @author Baalal,Hott,Vaty
 */
public class ServeurPara2 {
    public static void main(String args[]) throws Exception {
        
        // Création d'un socket pour le nom du fichier sur le port 40000
        ServerSocket ss = new ServerSocket(40001);
        
          // Création d'un socket pour les données binaires
        ServerSocket ssB = new ServerSocket(39001);
        
        Scanner entree = new Scanner(System.in);
        String cheminFich;
        String chaine, ch;
        
        System.out.print("Entrez le chemin d'accès au dossier du fichier: ");
        cheminFich = entree.nextLine()+"\\";
        
        
        System.out.print("Entrez le nom du fichier avec son extension: ");
        chaine = entree.nextLine();
        cheminFich += chaine;
        ch = chaine;
        System.out.println(cheminFich);
        
        // Construction du FileInputStream qui permet d'envoyer les données binaires au serveur
        FileInputStream fichierSrc = new FileInputStream(new File(cheminFich));
        
        
        int taille=0;
        taille = fichierSrc.available();
        System.out.println(taille);
       
        // On découpe le fichier en 3
        int tailleDiv = taille/3;
        byte[] buffer = new byte[tailleDiv];
        byte[] buf = new byte[tailleDiv+2];
        
        
        while(true) {
            Socket sss;
            Socket sssB;
            
            try {
                sss = ss.accept();
                sssB = ssB.accept();
                
                
                
                // Construction d'un BufferedReader pour lire le nom du fichier envoyé à travers la connexion socket
                BufferedReader entreeClient = new BufferedReader(new InputStreamReader(sss.getInputStream()));
                
                BufferedOutputStream sortiePartie = new BufferedOutputStream(sssB.getOutputStream());
                
                System.out.println(ch);
                
                // Si le client choisi le deuxième bloc on lit la deuxième partie du fichier et on envoie
                switch (entreeClient.readLine()){
                    
                    case "2":
                        // On vérifie que l'on est bien au début du fichier
                        try {
                            while(fichierSrc.skip(-1) != 0) {
                                
                            }
                        } catch(IOException e) {
                            
                        }
                        // On passe la première partie du fichier
                        fichierSrc.skip(tailleDiv);
                        
                        // Lors de la divion en 3 du fichier, on peut avoir un reste de 1 ou 2
                        // Pour pas perdre ces données on vérifie la taille modulo 3 et on adapte
                        switch(taille % 3){
                            case 0: fichierSrc.read(buffer, 0, tailleDiv);
                            sortiePartie.write(buffer);
                            break;
                            
                            case 1: fichierSrc.read(buf, 0, tailleDiv+1);
                            sortiePartie.write(buf, 0, tailleDiv+1);
                            break;
                            
                            case 2: fichierSrc.read(buf, 0, tailleDiv+2);
                            sortiePartie.write(buf, 0, tailleDiv+2);
                            break;
                        }
                        
                        
                        break;
                        
                }
                
                sss.close();
                sssB.close();
            } catch(IOException e) {
                
            }
            
        }
    }
    
}
















