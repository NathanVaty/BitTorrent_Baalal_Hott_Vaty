/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Step4;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Nathan Vaty
 */
public class ServeurMain {
    
    String tabServ;
    
    public static void main(String[] args) throws IOException {
        
          // Création d'un socket pour le nom du fichier sur le port 40000
        ServerSocket ss = new ServerSocket(40010);
        
        List<JFrame> affichServ;
        affichServ = new ArrayList<>();
        List<JLabel> labels = new ArrayList<>();
        List<ServeurMainThread> listeAppli = new ArrayList<>();
        List<InetAddress> listeIp = new ArrayList<>();
        
        for (int i=0;i<5;i++){
            affichServ.add(new JFrame());
            labels.add(new JLabel("En attente de connexion"));
            affichServ.get(i).setTitle("Appli " + (i+1));
            affichServ.get(i).setSize(500,200);
            affichServ.get(i).getContentPane().setBackground(Color.CYAN);
            affichServ.get(i).setLocation(1400,200*i);
            affichServ.get(i).setVisible(true);
            affichServ.get(i).add(labels.get(i));
            affichServ.get(i).setAlwaysOnTop(true);
        }
        
        while (true) {
            Socket sss = ss.accept();
            if (!listeIp.contains(sss.getInetAddress())) {
                listeIp.add(sss.getInetAddress());
            }
            listeAppli.add(new ServeurMainThread(sss, affichServ, labels));
            for (int i=0;i<listeAppli.size();i++){
                listeAppli.get(i).setListeIP(listeIp);
            }
            Thread tServ = new Thread(listeAppli.get(listeAppli.size()-1));
            tServ.start();
        }
    }
}















