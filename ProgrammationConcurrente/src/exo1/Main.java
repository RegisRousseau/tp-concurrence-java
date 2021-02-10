package exo1;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		Compteur compteur = new Compteur();
		
		
		//Question 2 :
		Runnable task = () -> {
			for (int i =0; i <100 ; i++) {
				compteur.increment();
			}
		};
		Thread thr = new Thread(task);
		thr.start();
		// Sleep pour attendre que le thread est le temps de faire ses incrémentations
		Thread.sleep(10);
		
		System.out.println("2)Le compteur est à "+ compteur.getCompteur());
		
		
		
		//Question 3 :
		compteur.reinitCompteur();
		Thread[] threads = new Thread[10];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(task);
			//threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			
			threads[i].start();
		}
		Thread.sleep(10);
		

		System.out.println("3)Le compteur est à "+ compteur.getCompteur());
		//On n'obtient pas 1000 comme attendu mais des valeurs compris entre 800 et 1000.
		//Des threads ont donc lu une valeur alors que d'autres étaient en train de la modifier.
		
		
		//Question 4 :
		//Il faut donc les synchroniser avec le mot 'synchronized'
		
		
		
		//Question 5 :
		AtomicCompteur atomicCompteur = new AtomicCompteur();
//		Thread[] threadsForAtomic = new Thread[10];
		Runnable task2 = () -> {
			for (int i =0; i <100 ; i++) {
				atomicCompteur.increment();
			}
		};
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(task2);
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}
		
		System.out.println("5)Le compteur Atomic est à "+ atomicCompteur.getCompteur());
		
		//Plus besoin d'utiliser le 'synchronized' puisque la valeur d'un AtomicLong est volatile.
		//Aussi, incrementAndGet() est une seule instruction assembleur, donc cette opération est inseccable.
		//Il y a donc un lien happens-before entre l'écriture et la lecture.
		//C'est une manière détourner d'obtenir le même résultat, qui est légèrement plus simple.
		
		
		
	}

}
