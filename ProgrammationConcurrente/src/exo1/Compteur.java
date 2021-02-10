package exo1;

public class Compteur {
	private int compteur = 0;
	public synchronized void increment() {
		compteur++;
	}
	
	public synchronized int getCompteur() {
		return compteur;
	}
	
	public synchronized void reinitCompteur() {	//Utile pour la question 3
		compteur=0; 
	}
	
	//le mot 'synchronized' a été ajouté à chaque méthode qui doit avoir accès à la propriété 'compteur'
	
}
