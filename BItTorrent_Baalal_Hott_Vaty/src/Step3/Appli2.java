/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Step3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Nathan Vaty
 */
public class Appli2 {
     public static void main(String args[]) throws IOException {
        
        Scanner entree = new Scanner(System.in);
        String reponse;
        
        System.out.println("Possédez-vous le fichier ?");
        reponse = entree.nextLine();
        
        if (reponse.equals("oui")) {
        ServerSocket ssg = new ServerSocket(40001);
        ServerSocket ssB = new ServerSocket(39001);
        
            System.out.println("Vous devenez serveur qui envoie des "
                    + "blocs du fichier");
            String cheminFich;
        String chaine, ch;
        
        System.out.print("Entrez le chemin d'accès au dossier du fichier: ");
        cheminFich = entree.nextLine()+"\\";
        
        
        System.out.print("Entrez le nom du fichier avec son extension: ");
        chaine = entree.nextLine();
        cheminFich += chaine;
        ch = chaine;
        
        while (true) {
            
                Socket sss = ssg.accept();
                Socket sssB = ssB.accept();
                Thread t = new Thread(new ServeurThread(sss,sssB, cheminFich,ch));
                t.start();
            }
        
        } else {
            Client cli = new Client(2);
            cli.recupererFichier();
             
        }
      
    }
}







