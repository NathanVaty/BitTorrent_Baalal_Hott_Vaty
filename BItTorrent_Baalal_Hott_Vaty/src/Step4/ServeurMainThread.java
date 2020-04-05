/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Step4;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Nathan Vaty
 */
public class ServeurMainThread implements Runnable{
    
    List<JFrame> affichage;
    List<JLabel> labels;
    Socket ss;
    int numAppli;
    BufferedReader entree;
     Socket test;
    List<String> listeIp;
    PrintStream envoieListe;
    List<Socket> listeSock;
    List<InetAddress> listeForMaintenance;
    
    public ServeurMainThread(Socket sss, List<JFrame> affServ, List<JLabel> labs) throws IOException {
        ss = sss;
        affichage = affServ;
        labels = labs;
        numAppli = 0;
        entree = new BufferedReader(new InputStreamReader(ss.getInputStream()));
        listeIp = new ArrayList<>();
        listeSock = new ArrayList<>();
        envoieListe = new PrintStream(sss.getOutputStream());
        listeForMaintenance = new ArrayList<>();
    }
    
    public void setListeIP(List<InetAddress> liste) {
        listeForMaintenance = liste;
        listeIp.clear();
        for (int i=0; i<liste.size();i++){
            listeIp.add(liste.get(i).getHostAddress());
        }
    }
    
   

    public synchronized void lireEntree(){
        try {
            switch(entree.readLine()){
                case "1": numAppli = 1;
                break;
                
                case "2": numAppli = 2;
                break;
                
                case "3": numAppli = 3;
                break;
                
                case "4": numAppli = 4;
                break;
                
                case "5": numAppli = 5;
                break;
                
                case "possede":
                    labels.get(numAppli-1).setText("Possède le fichier et peut le transférer");
                    affichage.get(numAppli-1).getContentPane().setBackground(Color.GREEN);
                    break;
                    
                case "possede pas":
                    labels.get(numAppli-1).setText("Ne possède pas le fichier et cherche à le télécharger");
                    affichage.get(numAppli-1).getContentPane().setBackground(Color.ORANGE);
                    for(int i=0;i<listeIp.size();i++){
                        envoieListe.println(listeIp.get(i));
                    }
                    envoieListe.println("fin");
                    break;
                    
                case "fin connexion":
                    labels.get(numAppli-1).setText("En attente de connexion");
                    affichage.get(numAppli-1).getContentPane().setBackground(Color.CYAN);
            }
        } catch (IOException ex) {
        }
            
    }
    
    
    @Override
    public void run() {

        // Il faut que l'on envoie la liste des serveurs dispo auprès des clients !!
        // regarder aussi quand un serveur est fermé il reste en mode on peut dl sauf que non
        // Envoyer message périodique ?
         while(true){
            lireEntree();
            for(int i=0;i<listeForMaintenance.size(); i++) {
                try {
                    if (!listeForMaintenance.get(i).isReachable(1000)) {
                      labels.get(numAppli-1).setText("En attente de connexion");
                      affichage.get(numAppli-1).getContentPane().setBackground(Color.CYAN);  
                    }
                } catch (IOException ex) {
                }
            }
//            try {
//                test = new Socket("localhost", 40000+numAppli-1);
//                test.close();
//            } catch (IOException ex) {
//                if (labels.get(numAppli-1).getText().equals("Possède le fichier et peut le transférer")) {
//                     affichage.get(numAppli-1).getContentPane().setBackground(Color.CYAN);
//                     labels.get(numAppli-1).setText("En attente de connexion");
//                }
//            }
        }
        
    }
}



















































































































