package exo1;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicCompteur {
	private AtomicLong compteur = new AtomicLong(0);
	
	public void increment() {
		compteur.incrementAndGet();
	}
	
	public long getCompteur() {
		return compteur.get();
	}
}
