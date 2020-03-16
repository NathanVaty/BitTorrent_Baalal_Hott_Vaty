/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Step3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Nathan Vaty
 */
public class Appli1 {
    
    public static void main(String args[]) throws IOException {
        
        Scanner entree = new Scanner(System.in);
        String reponse;
        
        System.out.println("Possédez-vous le fichier ? (oui/non)");
        reponse = entree.nextLine();
        
        /*
        * Si l'utilisateur possède le fichier, il devient serveur et envoie 
        * des blocs du fichiers aux applications demandeuses
        * S'il ne le possède pas il devient client et va chercher à reconstituer
        * le fichier
        */
        if (reponse.equals("oui")) {
            
            // On possède le fichier, on initalise le serveur
            ServerSocket ssg = new ServerSocket(40000);
            ServerSocket ssB = new ServerSocket(39000);
            
            String cheminFich="";
            String chaine, ch="";
            boolean fichValide = false;
            
            while (!fichValide) {
                System.out.print("Entrez le chemin d'accès au dossier du fichier: ");
                cheminFich = entree.nextLine()+"\\";
                
                
                System.out.print("Entrez le nom du fichier avec son extension: ");
                chaine = entree.nextLine();
                cheminFich += chaine;
                ch = chaine;
                
                try (FileInputStream fichierSrc = new FileInputStream(new File(cheminFich))) {
                    fichValide = true;
                    
                } catch (FileNotFoundException e) {
                    fichValide = false;
                    System.out.println("chemin non valide");
                }
            }
           
            
            
            System.out.println("Vous devenez serveur qui envoie des "
                    + "blocs du fichier");
            
            // On crée un thread pour chaque applications qui se connecteront à ce serveur
            while (true) {
                
                Socket sss = ssg.accept();
                Socket sssB = ssB.accept();
                Thread t = new Thread(new ServeurThread(sss,sssB, cheminFich,ch));
                t.start();
            }
        
        } else {
           // Si on possède pas le fichier on devient client et on cherche
           // à reconstituer le ficher
           Client cli = new Client(1);
            cli.recupererFichier();
        }
      
    }
}










