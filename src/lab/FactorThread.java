package lab;

public class FactorThread implements Runnable {
	private Factor ui;
	private boolean exit = false;
	//need to be volatile so JVM can see updates in while loop (not cached)
	volatile boolean isDone;
	volatile Long n;
	volatile boolean result;
	
	public FactorThread(Factor quiz, Long n) {
		this.ui = quiz;
		this.n = n;
		this.isDone = false;
	}
	
	@Override
	public synchronized void run() {
	    
	    //do a while loop checking time and for a wrong answer
		while (!exit && this.isDone != true) {
			
		    {
		        // Corner case
		    	System.out.println(this.n);
		        if (this.n <= 1) {
		            this.result = false;
		        	System.out.println("TRUE");
		        	isDone = true;
		        }
		  
		        // Check from 2 to n-1
		        for (int i = 2; i < this.n; i++)
		        {
		            if (this.n % i == 0) {
		                this.result = false;
		        		System.out.println("FALSE" + i);
		        		isDone = true;
		        		break;
		            }
		    	}
		        if(isDone!= true) {
		        	this.result = true;
		        	System.out.println("TRUE");
		        	isDone = true;
		        }
		    }
		}
		ui.threadFinished(this,n,result);
	}
	
	public void stop()
    {
        exit = true;
    }
	
	
}
