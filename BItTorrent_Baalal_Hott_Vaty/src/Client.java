
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
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

public class Client {
    public static void main(String args[]) throws IOException {
        // Création d'un socket client pour la communication du nom de fichier
        Socket sFich; 
        
        // Création d'un socket pour la communication des données binaires
        Socket sBin; 
        
        try {
            sFich = new Socket("192.168.43.128", 40000);
            sBin = new Socket("192.168.43.128", 39000);
            
            BufferedOutputStream sortieSocket = new BufferedOutputStream(sBin.getOutputStream());
            
            // Construction d'un PrintStream pour envoyer le nom du fichier au serveur
            PrintStream sortieNomFich = new PrintStream(sFich.getOutputStream());
            
            String chaine;
            String cheminFich;
            byte[] buffer = new byte[250];
            int n;
            
            Scanner entree = new Scanner(System.in);
            
            System.out.print("Entrez le chemin d'accès au dossier du fichier: ");
            cheminFich = entree.nextLine()+"\\";
            
            
            //System.out.println("Tapez vos phrases ou exit pour arrêter:");
            System.out.print("Entrez le nom du fichier avec son extension: ");
            chaine = entree.nextLine();
            cheminFich += chaine;
            System.out.print(cheminFich);
            
            // Construction du FileInputStream qui permet d'envoyer les données binaires au serveur
            FileInputStream fichierSrc = new FileInputStream(new File(cheminFich));
            
            sortieNomFich.print(chaine);
            
            while ((n=fichierSrc.read(buffer))>0) {
                
                sortieSocket.write(buffer,0,n);
                sortieSocket.flush(); // flush si texte < 250 caract
                System.out.println(buffer);
                
            }
            
            // Le fichier est bien envoyé
            System.out.println("Fichier bien transmis");
            // on ferme nous aussi la connexion
            sFich.close();
            sBin.close();
        } catch(IOException e) {
        }
    }
}





