package exo2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class Warehouse {
		
	private int capacity;
	private int nbElement = 0;
	private Lock lock = new ReentrantLock();
	private Condition notFull = lock.newCondition();
	private Condition notEmpty = lock.newCondition();

	public Warehouse(int capacity) {
		this.capacity=capacity;
	}

//	private Object lock = new Object();
//	public synchronized boolean add() throws InterruptedException {
//		synchronized(lock) {
//			while(this.nbElement == this.capacity ) {
//				lock.wait();
////				return false;
//			}
//			this.nbElement++;
//			System.out.println("+++ | "+ this.content()+"  "+Thread.currentThread().getName());
//			lock.notifyAll();
//			return true;
//		}
//	}
//	
//	public synchronized boolean remove() throws InterruptedException {
//		synchronized(lock) {
//			while(this.nbElement < 1 ) {
//				lock.wait();
//			}
//			this.nbElement--;
//			System.out.println("--- | "+ this.content()+"  "+Thread.currentThread().getName());
//			lock.notifyAll();
//			return true;
//		}
//	}
	public  boolean add() throws InterruptedException {
		try{
			lock.lock();
			while(this.nbElement == this.capacity ) {
				notFull.await();
			}
			this.nbElement++;
			System.out.println("+++ | "+ this.content()+"  "+Thread.currentThread().getName());
			notEmpty.signalAll();
			return true;
		} finally {
			lock.unlock();
		}
	}
	
	public  boolean remove() throws InterruptedException {
		try {
			lock.lock();
			while(this.nbElement == 0 ) {
				notEmpty.await();
			}
			this.nbElement--;
			System.out.println("--- | "+ this.content()+"  "+Thread.currentThread().getName());
			notFull.signalAll();
			return true;
		} finally {
			lock.unlock();
		}
	}
	
	public  int content() {
		return nbElement;
	}
	

}
