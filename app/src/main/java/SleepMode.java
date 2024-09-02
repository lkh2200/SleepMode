
import java.util.Random;
import java.lang.Thread;
import java.lang.Runnable;

public class SleepMode {

	private static final int GRID_WIDTH = 250; // Width of the grid
	private static final int GRID_HEIGHT = 125; // Height of the grid

	public static void main(String[] args) {

		MoodPoint moodPoint = new MoodPoint(0, 0);
		// Start at an initial position (e.g., (50, 50))

		Thread likeMonitor = new Thread(new Monitor(moodPoint, new ForceVector(3, 1)));
		Thread shareMonitor = new Thread(new Monitor(moodPoint, new ForceVector(1, 3)));
		// Example force for shares
		Thread commentMonitor = new Thread(new Monitor(moodPoint, new ForceVector(1, 3))); 
		// Example force for comments
		Thread dislikeMonitor = new Thread(new Monitor(moodPoint, new ForceVector(-5, 1))); 
		// Example force for dislikes

		likeMonitor.start();
		shareMonitor.start();
		commentMonitor.start();
		dislikeMonitor.start();

		while (true) {
			
			try {
				
				moodPoint.applyGravity();
				moodPoint.updatePosition();
				printMoodPlane(moodPoint);
				Thread.sleep(1000);
		
			} catch (Exception e) {e.printStackTrace();}
		
		}
	
	}

	private static void printMoodPlane(MoodPoint moodPoint) {
		
		char[][] grid = new char[GRID_HEIGHT][GRID_WIDTH];

		// Initialize the grid with dots
		for (int y = 0; y < GRID_HEIGHT; y++) {
		
			for (int x = 0; x < GRID_WIDTH; x++) {
				
				grid[y][x] = '.';
			
			}
		
		}

		// Map mood position to grid coordinates
		int x = (int) Math.min(
			GRID_WIDTH - 1, Math.max(
				0, moodPoint.getX() + GRID_WIDTH / 2));
		int y = (int) Math.min(
			GRID_HEIGHT - 1, Math.max(
				0, moodPoint.getY()));

		// Ensure y value is within positive area
		if (y >= 0 && y < GRID_HEIGHT) {
			
			grid[y][x] = 'M';
		
		}

		// Print the positive y area of the grid
		for (int j = GRID_HEIGHT - 1; j >= 0; j--) {
			
			for (int i = 0; i < GRID_WIDTH; i++) {
				
				System.out.print(grid[j][i] + " ");
			
			}
		
			System.out.println();
		
		}
		
		System.out.println();
	
	}

	private static class Monitor implements Runnable {

		private final MoodPoint moodPoint;
		private final ForceVector force;

		Monitor(MoodPoint moodPoint, ForceVector force) {
			
			this.moodPoint = moodPoint;
			this.force = force;
		
		}

		public void run() {
			
			Random r = new Random();
			
			while (true) {
				
				if (r.nextInt(10) == 1) {
					
					moodPoint.applyForce(force);
					
				}
			
				try{Thread.sleep(10000);
				}catch(Exception e) {e.printStackTrace();}
			
			}
		
		}
	
	}

	private static class MoodPoint {
		
		private double x;
		private double y;
		private ForceVector velocity = new ForceVector(0, 0);

		MoodPoint(double x, double y) {
			
			this.x = x;
			this.y = y;
		
		}

		public synchronized void applyForce(ForceVector force) {
			
			velocity.add(force);
		
		}

		public synchronized void applyGravity() {
			
			// Calculate the direction vector from current position (x, y) to y = 0
			double directionX = -x; // x coordinate towards 0
			double directionY = -y; // y coordinate towards 0

			// Calculate the magnitude of the direction vector
			double magnitude = Math.sqrt(
				directionX * directionX + directionY * directionY);

			// Normalize the direction vector
			if (magnitude != 0) {
                
				directionX /= magnitude;
				directionY /= magnitude;
		
			}

			// Define the strength of the gravity
			double gravityStrength = 0.4; // Adjust as needed

			// Create a gravity force vector
			ForceVector gravity = new ForceVector(
				directionX * gravityStrength, directionY * gravityStrength);

			// Apply the gravity force
			velocity.add(gravity);
        
		}

		public synchronized void updatePosition() {
			
			this.x += velocity.getX();
			this.y += velocity.getY();
            
			// Ensure y does not go below 0
			if (this.y < 0) {
				
				this.y = 0;
			
			}
		
		}

		public synchronized double getX() {
			
			return x;
		
		}

		public synchronized double getY() {
			
			return y;
		
		}

		@Override
		public synchronized String toString() {
            
			return String.format("(%.2f, %.2f)", x, y);
        
		}
    
	}

	private static class ForceVector {
		
		private double x;
		private double y;

		ForceVector(double x, double y) {
            
			this.x = x;
			this.y = y;
        
		}

		public double getX() {
            
			return x;
        
		}

		public double getY() {
            
			return y;
        
		}

		public void add(ForceVector other) {
            
			this.x += other.x;
			this.y += other.y;
        
		}

		public void multiply(double factor) {
            
			this.x *= factor;
			this.y *= factor;
        
		}
    
	}

}

