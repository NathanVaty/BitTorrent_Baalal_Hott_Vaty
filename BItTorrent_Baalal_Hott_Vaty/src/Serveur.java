
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nathan Vaty
 */
public class Serveur {
    public static void main(String args[]) throws Exception {	
		
		// Création d'un socket serveur générique sur le port 40000
		ServerSocket ssg = new ServerSocket(40000);
                
                
		
		while(true) {
			// On attend une connexion puis on l'accepte
			Socket sss = ssg.accept();
			
			// Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
			BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(sss.getInputStream()));
			// Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
			PrintStream sortieSocket = new PrintStream(sss.getOutputStream());
                        
                        FileOutputStream fichier = new FileOutputStream(new File("dstServeur/testServ.txt"));
			byte[] buffer = new byte[250];
                        char[] bufferChar = new char[250];
                        
			String chaine = "";
			int n = 0;
			while((n = entreeSocket.read(bufferChar)) >= 0) {
				// lecture d'une chaine envoyée à travers la connexion socket
				//chaine = entreeSocket.readLine();
                               // Convertir char[] en byte[] buffer = toBytes(bufferChar);
                                fichier.write(buffer);
				
				// si elle est nulle c'est que le client a fermé la connexion
				if (chaine != null)
                                        System.out.println(chaine);
					sortieSocket.println(chaine); // on envoie la chaine au client
			}
			
			// on ferme nous aussi la connexion
			sss.close();
		}
	}
    
}






