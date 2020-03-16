/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Step3;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Nathan Vaty
 */
public class ServeurThread implements Runnable {
    
    //String
    private final Socket sss;
    
    //Binaire
    private final Socket ssB;
    
    String cheminFich;
    String nomFich;
    public ServeurThread(Socket sss, Socket ssB, String chemin, String nomFich) {
        this.sss = sss;
        this.ssB = ssB;
        this.cheminFich = chemin;
        this.nomFich = nomFich;
    }
    
    public void run() { 
        try {
            
        // traitement d'un client
        BufferedOutputStream sortiePartie = new BufferedOutputStream(ssB.getOutputStream());
        
        // Construction d'un BufferedReader pour lire le bloc que l'on doit renvoyer
        BufferedReader entreeClient = new BufferedReader(new InputStreamReader(sss.getInputStream()));
        
        //Construction d'un PrintStream pour envoyer le nom du fichier à récupérer
        PrintStream ps = new PrintStream(sss.getOutputStream());
        
         // Construction du FileInputStream qui permet d'envoyer les données binaires au serveur
        FileInputStream fichierSrc = new FileInputStream(new File(cheminFich));
        
        int taille=0;
        taille = fichierSrc.available();
        System.out.println(taille);
        
         // On découpe le fichier en 3
        int tailleDiv = taille/3;
        byte[] buffer = new byte[tailleDiv];
          byte[] buf = new byte[tailleDiv+2];
        
        boolean valide = false;
        
        while (!valide) {
            switch (entreeClient.readLine()){
                case "1":
                    try {
                        while(fichierSrc.skip(-1) != 0) {
                            
                        }
                    } catch(IOException e) {
                        
                    }
                    fichierSrc.read(buffer, 0, tailleDiv);
                    sortiePartie.write(buffer);
                   // valide = true;
                    break;
                    
                case "2":
                    try {
                        while(fichierSrc.skip(-1) != 0) {
                            
                        }
                    } catch(IOException e) {
                        
                    }
                    fichierSrc.skip(tailleDiv);
                    switch(taille % 3){
                        case 0: fichierSrc.read(buffer, 0, tailleDiv);
                        sortiePartie.write(buffer);
                        break;
                        
                        case 1: fichierSrc.read(buf, 0, tailleDiv+1);
                        sortiePartie.write(buf, 0, tailleDiv+1);
                        break;
                        
                        case 2: fichierSrc.read(buf, 0, tailleDiv+2);
                        sortiePartie.write(buf, 0, tailleDiv+1);
                        break;
                    }
                    
                    //valide = true;
                    break;
                    
                case "3":
                    try {
                        while(fichierSrc.skip(-1) != 0) {
                            
                        }
                    } catch(IOException e) {
                        
                    }
                    fichierSrc.skip(taille-tailleDiv);
                    fichierSrc.read(buffer, 0, tailleDiv);
                    sortiePartie.write(buffer);
                    
                    valide = true;
                    break;
                    
                case "nom":  ps.println(nomFich);
                break;
                
                
            }
        }
        sss.close();
        ssB.close();
        } catch (IOException ex) {
        }
    }
}










