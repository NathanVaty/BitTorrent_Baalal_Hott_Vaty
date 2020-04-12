/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Step45;

import Step4.*;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Nathan Vaty
 */
public class ServeurMainThread implements Runnable{
    
    List<JFrame> affichage;
    JLabel label;
    JLabel statut;
    Socket ss;
    int numAppli;
    BufferedReader entree;
     Socket test;
    List<String> listeIp;
    List<String> listePorts;
    PrintStream envoie;
    List<Socket> listeSock;
    List<InetAddress> listeForMaintenance;
    JFrame affichServ;
    List<String> listeIpCorr;
    List<String> listePortsCorr;
    List<String> listeblocsDispo;
    ListeBlocs listeBlocs;
    
    public ServeurMainThread(Socket sss,int num, ListeBlocs lb) throws IOException {
        ss = sss;
        numAppli = num;
        entree = new BufferedReader(new InputStreamReader(ss.getInputStream()));
        listeIp = new ArrayList<>();
        listePorts = new ArrayList<>();
        listeSock = new ArrayList<>();
        listeIpCorr = new ArrayList<>();
        listePortsCorr = new ArrayList<>();
        envoie = new PrintStream(sss.getOutputStream());
        listeForMaintenance = new ArrayList<>();
        listeblocsDispo = new ArrayList<>();
        listeBlocs = lb;
        listeBlocs.addNbBlocs(0);
        
        affichServ = new JFrame();
        affichServ.setLayout(new BoxLayout(affichServ.getContentPane(), BoxLayout.Y_AXIS));
        label = new JLabel("En attente de connexion");
        statut = new JLabel("statut: En attente");
        affichServ.add(statut);
        affichServ.add(label);
        affichServ.setTitle("Appli " + (num));
        affichServ.setSize(500,200);
        affichServ.getContentPane().setBackground(Color.LIGHT_GRAY);
        affichServ.setLocation(1400,200*num);
        affichServ.setVisible(true);
        affichServ.add(label);
        affichServ.setAlwaysOnTop(true);
    }
    
    public void setListeIPPorts(List<InetAddress> liste, int num) {
        listeForMaintenance = liste;
        listeIp.clear();
        listePorts.clear();
        for (int i=0; i<liste.size();i++){
            listeIp.add(liste.get(i).getHostAddress());
        }
        for (int i=1;i<=num;i++){
            listePorts.add((40000+i)+" " + (39000+i));
        }
    }
   
    /**
     * Ecrire une méthode qui se co aux serveurs dispo et regarde les blocs qu'ils ont
     * pour renvoyer la liste correct
     * @param blocsDemandes
     */
    public void getServToConnect(int blocsDemandes){
        listeIpCorr.clear();
        listePortsCorr.clear();
        int blocsDispo=0;
        List<Integer> listeBServ = listeBlocs.getListe();
        for (int i=0;i<listeBServ.size();i++) {
            blocsDispo = listeBServ.get(i);
            switch(blocsDemandes){
                case 1:
                    if (blocsDispo == 1 || blocsDispo==3 || blocsDispo == 5 || blocsDispo == 7){
                        listeIpCorr.add(listeIp.get(i));
                        listePortsCorr.add(listePorts.get(i));
                        listeblocsDispo.add("1");
                    }
                    break;
                case 2:
                    if (blocsDispo == 2 || blocsDispo == 3 || blocsDispo >= 6){
                        listeIpCorr.add(listeIp.get(i));
                        listePortsCorr.add(listePorts.get(i));
                        listeblocsDispo.add("2");
                    }
                    break;
                case 3:
                    if (blocsDispo <= 3 || blocsDispo >= 5){
                        listeIpCorr.add(listeIp.get(i));
                        listePortsCorr.add(listePorts.get(i));
                        listeblocsDispo.add("12");
                    }
                    break;
                case 4:
                    if (blocsDispo >= 4){
                        listeIpCorr.add(listeIp.get(i));
                        listePortsCorr.add(listePorts.get(i));
                        listeblocsDispo.add("3");
                    }
                    break;
                case 5:
                    if (blocsDispo == 1 || blocsDispo >=3) {
                        listeIpCorr.add(listeIp.get(i));
                        listePortsCorr.add(listePorts.get(i));
                        listeblocsDispo.add("13");
                    }
                    break;
                case 6:
                    if (blocsDispo >= 2) {
                        listeIpCorr.add(listeIp.get(i));
                        listePortsCorr.add(listePorts.get(i));
                        listeblocsDispo.add("23");
                    }
                    break;
                case 7:
                    listeIpCorr.add(listeIp.get(i));
                    listePortsCorr.add(listePorts.get(i));
                    listeblocsDispo.add("123");
                    break;
            }
        }
       
    }
    
    public void envoieListesCorr() {
        for(int i=0;i<listeIpCorr.size();i++){
            envoie.println(listeIpCorr.get(i));
        }
        envoie.println("fin");
        for (int i=0; i<listePortsCorr.size();i++){
            envoie.println(listePortsCorr.get(i));
        }
        envoie.println("fin");
        for(int i=0; i<listeblocsDispo.size();i++){
            envoie.println(listeblocsDispo.get(i));
        }
        envoie.println("fin");
    }
    
    public synchronized void lireEntree(){
        try {
            switch(entree.readLine()){
                
                case "Tout":
                    label.setText("Possède le fichier et peut le transférer");
                    statut.setText("Statut: dispo pour envoi");
                    affichServ.getContentPane().setBackground(Color.GREEN);
                    listeBlocs.setNbBlocsIndice(numAppli-1, 7);
                    break;
                    
                case "bloc 1":
                    label.setText("Possède le 1er bloc");
                    statut.setText("Statut: reconsitution du fichier et distribution de bloc");
                    affichServ.getContentPane().setBackground(Color.CYAN);
                    listeBlocs.setNbBlocsIndice(numAppli-1, 1);
                    // appeler la méthode pour récupérer les blocs;
                    getServToConnect(6);
                    System.out.println(listeIpCorr.size());
                    envoieListesCorr();
                    
                    
                    break;
                case "bloc 2":
                    label.setText("Possède le 2e bloc");
                    statut.setText("Statut: reconsitution du fichier et distribution de bloc");
                    affichServ.getContentPane().setBackground(Color.CYAN);
                    listeBlocs.setNbBlocsIndice(numAppli-1, 2);
                    getServToConnect(5);
                    envoieListesCorr();
                    break;
                case "bloc 3":
                    label.setText("Possède le 3e bloc");
                    statut.setText("Statut: reconsitution du fichier et distribution de bloc");
                    affichServ.getContentPane().setBackground(Color.CYAN);
                    listeBlocs.setNbBlocsIndice(numAppli-1, 4);
                    getServToConnect(3);
                    envoieListesCorr();
                    break;
                case "bloc 1 & 2":
                    label.setText("Possède le 1er et 2e blocs");
                    statut.setText("Statut: reconsitution du fichier et distribution de blocs");
                    affichServ.getContentPane().setBackground(Color.CYAN);
                    listeBlocs.setNbBlocsIndice(numAppli-1, 3);
                    getServToConnect(4);
                    envoieListesCorr();
                    break;
                case "bloc 1 & 3":
                    label.setText("Possède le 1er et 3e blocs");
                    statut.setText("Statut: reconsitution du fichier et distribution de blocs");
                    affichServ.getContentPane().setBackground(Color.CYAN);
                    listeBlocs.setNbBlocsIndice(numAppli-1, 5);
                    getServToConnect(2);
                    envoieListesCorr();
                    break;
                case "bloc 2 & 3":
                    label.setText("Possède le 2e et 3e blocs");
                    statut.setText("Statut: reconsitution du fichier et distribution de blocs");
                    affichServ.getContentPane().setBackground(Color.CYAN);
                    listeBlocs.setNbBlocsIndice(numAppli-1, 6);
                    getServToConnect(1);
                    envoieListesCorr();
                    break;
                case "possede pas":
                    label.setText("Ne possède pas le fichier et cherche à le télécharger");
                    affichServ.getContentPane().setBackground(Color.ORANGE);
                    getServToConnect(7);
                    envoieListesCorr();
                    
                case "getNum" : envoie.println(numAppli);
                    break;
                
                case "fin connexion":
                    affichServ.dispose();
                    break;
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
//            for(int i=0;i<listeForMaintenance.size(); i++) {
//                try {
//                    if (!listeForMaintenance.get(i).isReachable(1000)) {
//                      label.setText("En attente de connexion");
//                      affichServ.getContentPane().setBackground(Color.CYAN);  
//                    }
//                } catch (IOException ex) {
//                }
//            }
        }
        
    }
}











































































































































































































