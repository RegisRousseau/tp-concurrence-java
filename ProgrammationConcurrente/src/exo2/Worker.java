package exo2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Worker {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		//Question 5:
		Warehouse entrepot = new Warehouse(10);
		
		Runnable add = () -> {
			try {
				entrepot.add();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		Runnable remove = () -> {
			try {
				entrepot.remove();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		

//		add.run(); add.run(); add.run();
//		remove.run();
//		System.out.println("Articles dans l'entrepot : "+ entrepot.content()); 
		//Valeur attendu : 2
		//Valeur obtenue : 2   -> fonctionne correctement pour le moment
		
		
		
		//Question 7 : Si dans notre pool l'intégralité des threads sont pris par des consumer et que la quantité est nulle ou que des producer et la quantité est pleine, alors nous nous retrouvons bloqué.
		//Question 8 :
		// Tous les producer sont mis dans un pool de producer et tous les consumer sont mis dans un pool de consumer.
		// Chacun des consumer/producer auront donc lieu.
		ExecutorService executorServiceAdd = Executors.newFixedThreadPool(5) ;
		ExecutorService executorServiceRemove = Executors.newFixedThreadPool(5) ;
		
		for (int i = 0; i < 100; i++) {
			executorServiceAdd.submit(add);
		}
		for (int i = 0; i < 95; i++) {
			
			executorServiceRemove.submit(remove);
		}

		executorServiceAdd.shutdown();
		executorServiceRemove.shutdown();
	}

}