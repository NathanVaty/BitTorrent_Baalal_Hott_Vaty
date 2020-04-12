/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Step45;

import Step4.*;
import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nathan Vaty
 */
public class ServeurMain {
    
    public static void main(String[] args) throws IOException {
        
          //  41000
        ServerSocket ss = new ServerSocket(41000);
        
        List<ServeurMainThread> listeAppli = new ArrayList<>();
        List<InetAddress> listeIp = new ArrayList<>();
        ListeBlocs lb = new ListeBlocs();
        
        int compteur = 0;
        
        
        while (true) {
            Socket sss = ss.accept();
            compteur++;
            //if (!listeIp.contains(sss.getInetAddress())) {
                listeIp.add(sss.getInetAddress());
            //}
            listeAppli.add(new ServeurMainThread(sss,compteur,lb));
            for (int i=0;i<listeAppli.size();i++){
                listeAppli.get(i).setListeIPPorts(listeIp,compteur);
            }
            Thread tServ = new Thread(listeAppli.get(listeAppli.size()-1));
            tServ.start();
            
        }
    }
}











