
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

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
                
                ServerSocket sscom = new ServerSocket(39000);
                
		
		while(true) {
			// On attend une connexion puis on l'accepte
			Socket sss = ssg.accept();
                        Socket sscoms = sscom.accept();
			
			// Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
			BufferedInputStream entreeSocket = new BufferedInputStream(sscoms.getInputStream());
                        BufferedReader entreeNomFich = new BufferedReader(new InputStreamReader(sss.getInputStream()));
                        
                        String destFich = "C:\\BitTorrentBHV\\";
                        File dossier = new File(destFich);
                        boolean estCree = dossier.mkdirs();
                        String nomFich="";
			byte[] buffer = new byte[250];
                        char[] bufferChar = new char[250];
                       
                        
			String chaine = "";
			int n = 0;
                      
                        
                        if (chaine != null) {
                            chaine = entreeNomFich.readLine();
                            System.out.println(chaine.length());
                            nomFich = chaine;
                            System.out.println(chaine);
                        } else {
                            System.err.println("Erreur nomFich");
                        }
                        System.out.println(nomFich);
                        FileOutputStream fichDst = new FileOutputStream(new File(destFich+nomFich));
                        
			while((n = entreeSocket.read(buffer)) >= 0) {
                           System.out.print((buffer));
                           System.out.print("Oui");
                           fichDst.write(buffer,0,n);
				
			}
			System.out.print("non");
			// on ferme nous aussi la connexion
			sss.close();
                        sscoms.close();
                        fichDst.close();
		}
	}
    
}


















































































