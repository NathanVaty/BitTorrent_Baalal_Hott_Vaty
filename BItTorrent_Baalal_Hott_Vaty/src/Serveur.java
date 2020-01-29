
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author Baalal,Hott,Vaty
 */
public class Serveur {
    public static void main(String args[]) throws Exception {
        
        // Création d'un socket pour le nom du fichier sur le port 40000
        ServerSocket ssF = new ServerSocket(40000);
        
        // Création d'un socket pour les données binaires
        ServerSocket ssB = new ServerSocket(39000);
        
        
        while(true) {
            // On attend une connexion puis on l'accepte
            Socket sssF;
            Socket sssB;
            try {
                sssF = ssF.accept();
                sssB = ssB.accept();
                
                // Construction d'un BufferedInputStream pour lire les données binaires à copier
                BufferedInputStream entreeSocket = new BufferedInputStream(sssB.getInputStream());
                
                // Construction d'un BufferedReader pour lire le nom du fichier envoyé à travers la connexion socket
                BufferedReader entreeNomFich = new BufferedReader(new InputStreamReader(sssF.getInputStream()));
                
                // On créé le fichier BitTorrentBHV à la racine du disque C pour stocker le fichier
                String destFich = "C:\\BitTorrentBHV\\";
                File dossier = new File(destFich);
                boolean estCree = dossier.mkdirs();
                String nomFich="";
                byte[] buffer = new byte[250];
                
                String chaine = "";
                int n = 0;
                
                if (chaine != null) {
                    // On lit le nom du fichier à copier
                    chaine = entreeNomFich.readLine();
                    nomFich = chaine;
                    
                } else {
                    System.err.println("Erreur nomFich");
                }
                // On crée le fichier destination dans le dossier BitTorrentBHV
                FileOutputStream fichDst = new FileOutputStream(new File(destFich+nomFich));
                
                // Tant que le buffer n'est pas vide on continue de lire les données
                while((n = entreeSocket.read(buffer)) >= 0) {
                    // On les écrits dans le fichier destinaton
                    fichDst.write(buffer,0,n);
                    
                }
                // Le fichier a bien été copié
                System.out.println("Fichier créé à la racine du fichier C:"
                        + " dans le dossier BitTorrentBHV");
                
                // on ferme nous aussi la connexion
                sssF.close();
                sssB.close();
                fichDst.close();
            } catch (IOException e){
            }    
        }
    }
    
}

