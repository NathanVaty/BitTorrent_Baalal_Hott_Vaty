
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nathan Vaty
 */
public class Client {
    public static void main(String args[]) throws IOException {
		// Création d'un socket client et connexion avec un serveur fonctionnant sur la même machine et sur le port 40000
		Socket sc = new Socket("localhost", 40000);
		
		// Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
		BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(sc.getInputStream()));
		// Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
		BufferedOutputStream sortieSocket = new BufferedOutputStream(sc.getOutputStream());
                
                PrintStream sortieNomFich = new PrintStream(sc.getOutputStream());
                
//                 BufferedReader fichier = new BufferedReader(new FileReader("C:"
//                         + "\\Users\\vanat\\OneDrive\\Documents"
//                        + "\\NetBeansProjects\\BitTorrent_Baalal_Hott_Vaty"
//                        + "\\BItTorrent_Baalal_Hott_Vaty\\src\\srcClient\\fonddecrantropbeau.jpg"));
//				
//                 FileInputStream fich = new FileInputStream(new File("C:"
//                         + "\\Users\\vanat\\OneDrive\\Documents"
//                        + "\\NetBeansProjects\\BitTorrent_Baalal_Hott_Vaty"
//                        + "\\BItTorrent_Baalal_Hott_Vaty\\src\\srcClient\\testSrc.txt"));
                 
                
                  
                  //FileOutputStream sortie = new FileOutputStream(new File(sc.getOutputStream()));
                 
                 
		String chaine = "";
                String cheminFich;
                byte[] buffer = new byte[250];
                int n=0;
		
                Scanner entree = new Scanner(System.in);
                
                System.out.print("Entrez le chemin d'accès au dossier du fichier: ");
                cheminFich = entree.nextLine()+"\\";
                System.out.println(cheminFich);
		
		//System.out.println("Tapez vos phrases ou exit pour arrêter:");
                System.out.print("Entrez le nom du fichier avec son extension: ");
                chaine = entree.nextLine();
                System.out.println(chaine);
                
                cheminFich += chaine;
                System.out.print(cheminFich);
                    
                
                FileInputStream fichierSrc = new FileInputStream(new File(cheminFich));
                  
                sortieNomFich.println(chaine);
                
                while ((n=fichierSrc.read(buffer))>0) {
                   
                    sortieSocket.write(buffer,0,n);
                    //sortieSocket.flush(); // flush si texte < 250 caract
                    System.out.println(buffer);
                   
                }

		// on ferme nous aussi la connexion
		sc.close();
	}
}





















































































