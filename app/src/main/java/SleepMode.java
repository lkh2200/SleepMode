import java.util.Random;
import java.lang.Thread;
import java.lang.Runnable;


public class SleepMode{

	public static void main(String[] args){

		Thread likeMonitor = new Thread(new Monitor());
		Thread shareMonitor = new Thread(new Monitor());
		Thread commentMonitor = new Thread(new Monitor());
		Thread dislikeMonitor = new Thread(new Monitor());
		
		likeMonitor.start();
		shareMonitor.start();
		commentMonitor.start();
		dislikeMonitor.start();

		while (true){
			
			try{
				
				System.out.println(Monitor.mood());
				Thread.sleep(1000);

			}catch(Exception e){e.printStackTrace();}

		}

	}
		
	private static class Monitor implements Runnable{
		
		public static int mood = 0; 

		Monitor(){}
			
		public void run(){
			

			try{
			
				Random r = new Random();
				while(true){
				
					if (r.nextInt(10) == 1){
				
						moodChange();
				
					}

					Thread.sleep(1000);
				
				}

			}catch(Exception e){e.printStackTrace();}	

		}

		public static int mood(){

			return mood;

		}

		private synchronized void moodChange(){

			mood++;	

		}

	}

}
