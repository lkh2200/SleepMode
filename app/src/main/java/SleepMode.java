
import java.util.Random;
import java.lang.Thread;
import java.lang.Runnable;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class SleepMode {

	public static void main(String[] args) {

		try{
			
			File config = new File("sleepMode.config");
			Scanner sc = new Scanner(config);
			sc.nextLine();
			int gridWidth = sc.nextInt();
			sc.nextLine();sc.nextLine();
			int gridHeight = sc.nextInt();
			sc.nextLine();sc.nextLine();
			int startX = sc.nextInt();
			sc.nextLine();sc.nextLine();
			int startY = sc.nextInt();

			MoodPoint moodPoint = new MoodPoint(startX, startY, gridWidth, gridHeight);

			ArrayList<Thread> threadList = new ArrayList<Thread>();
			threadList.add(
				//likes
				new Thread(new Monitor(moodPoint, "like")));
			threadList.add(
				//shares
				new Thread(new Monitor(moodPoint, "share")));
			threadList.add(
				//comments
				new Thread(new Monitor(moodPoint, "comment")));
			threadList.add(
				//dislikes
				new Thread(new Monitor(moodPoint, "dislike")));

			for(int i = 0; i < threadList.size(); i++){

				threadList.get(i).start();

			}

			while (true) {
				
				moodPoint.applyGravity();
				moodPoint.updatePosition();
				System.out.println(moodPoint);
				Thread.sleep(1000);

			}

		}catch (Exception e){e.printStackTrace();}

	}

	private static class Monitor implements Runnable {

		private final MoodPoint moodPoint;
		private ForceVector force;
		private String id;
		private String apiKey = "";

		Monitor(MoodPoint moodPoint, String id) {

			this.moodPoint = moodPoint;
			this.id = id;

		}

		private void updateVector(){

			try{

				File config = new File("sleepMode.config");
				Scanner sc = new Scanner(config);
				sc.useDelimiter(id);
				sc.next();
				sc.nextLine();
				sc = new Scanner(sc.nextLine());
				sc.useDelimiter(",");
				this.force = new ForceVector(sc.nextInt(),sc.nextInt());

			}catch(Exception e){e.printStackTrace();}

		}

			public void run() {

				Random r = new Random();

				while (true) {

					updateVector();

					if (r.nextInt(10) == 1) {

						moodPoint.applyForce(force);

					}

					try{Thread.sleep(10000);
					}catch(Exception e) {e.printStackTrace();}

				}

			}

		}

	}

