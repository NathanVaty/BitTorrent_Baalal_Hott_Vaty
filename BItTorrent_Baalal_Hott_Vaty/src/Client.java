
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
 * @author Nathan Vaty
 */
public class Client {
    public static void main(String args[]) throws Exception {
		// Création d'un socket client et connexion avec un serveur fonctionnant sur la même machine et sur le port 40000
		Socket sc = new Socket("localhost", 40000);
		
		// Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
		BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(sc.getInputStream()));
		// Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
		PrintStream sortieSocket = new PrintStream(sc.getOutputStream());
					
		String chaine = "";
		
		// Scanner sur System.in
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Tapez vos phrases ou exit pour arrêter:");
					
		while(!chaine.equalsIgnoreCase("exit")) {
			// lecture clavier
			chaine = scanner.nextLine();
			sortieSocket.println(chaine); // on envoie la chaine au serveur
			
			// lecture d'une chaine envoyée à travers la connexion socket
			chaine = entreeSocket.readLine();
			System.out.println("Chaine reçue : "+chaine);
		}
		
		// on ferme nous aussi la connexion
		sc.close();
	}
}



