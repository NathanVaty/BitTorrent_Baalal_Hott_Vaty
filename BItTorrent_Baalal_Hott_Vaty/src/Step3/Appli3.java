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
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 *
 * @author Nathan Vaty
 */
public class Appli3 {
     public static void main(String args[]) throws IOException {
        
        Scanner entree = new Scanner(System.in);
        String reponse;
        
        System.out.println("Possédez-vous le fichier ?");
        reponse = entree.nextLine();
        
        if (reponse.equals("oui")) {
        ServerSocket ssg = new ServerSocket(40002);
        ServerSocket ssB = new ServerSocket(39002);
        
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
            Client cli = new Client(3);
            cli.recupererFichier();
        }
      
    }
}






