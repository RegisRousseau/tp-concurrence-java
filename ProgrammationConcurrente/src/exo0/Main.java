package exo0;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	public static void main(String [ ] args) {
		//Question 1
		System.out.println("Nom du thread : "+Thread.currentThread().getName());

		//Question 2
		Runnable task = 
				() -> System.out.println("Nom du thread : "+Thread.currentThread().getName());
		task.run();
		
		//Question 3
		Thread thr = new Thread(task);
		thr.start();
		//Le nom obtenu est : Thread-0
		
		//Question 4
		Executor exe = Executors.newSingleThreadExecutor();
		exe.execute(
				()-> System.out.println("Nom du thread : "+Thread.currentThread().getName() )
				); 
		//Le nom de ce thread est : pool-1-thread-1
	}			
}
