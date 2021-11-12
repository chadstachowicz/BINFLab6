package lab;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Factor extends JFrame {
	private static final long serialVersionUID = -8723649827349837249L;
	private JPanel mainFrame = new JPanel();
	public JButton cancelCheck = new JButton("Cancel Check");
	public JButton submitAnswer = new JButton("Check");
	private JLabel enterAnswerLabel = new JLabel("<html><u><bold><b1>List of numbers</u></bold></b1></html>");
	private JLabel numberLabel = new JLabel("<html><u><bold><b1>Number</u></bold></b1></html>");
	private JLabel isPrimeLabel = new JLabel("<html><u><bold><b1>isPrime</u></bold></b1></html>");
	public JLabel numberCountLabel = new JLabel("");
	public JLabel primeLabel = new JLabel("");
	public JLabel proteinSwitchLabel = new JLabel("<Start Quiz>");
	private JLabel statusLabel = new JLabel("Status:");
	private JLabel runtimeLabel = new JLabel("runtime (msec) :");
	private JLabel timeLabel = new JLabel("");
	private JLabel primesLabel = new JLabel("primes found:");
	private JLabel primesFoundLabel = new JLabel("");
	private int threadFinished;
	private int valuesCount;
	private int threadStarted;
	private int primesFound;
	private String[] valArr;
	private long timeMilli;
	public JLabel statusCounterLabel = new JLabel("NOT STARTED");
	private JLabel holder = new JLabel("<html><u><bold><b1></u></bold></b1></html>");
	//set default
	public JTextField answerField = new JTextField("2147483647,1000000087,1000273817,1007050321,1016668999,4,2987,38643,1000111103,1007050321,1016668999,1000111103,1000000087,1000111103");
	//set default
	public JTextField threadCount = new JTextField("4");
	private JLabel threadLabel = new JLabel("Thread Count:");
	private static Factor UIPass;
	private static FactorThread factorThread;
	private FactorThread[] ft;
	//list of numbers to test with
	//2147483647,1000000087,1000273817,1007050321,1016668999,4,2987,38643,1000111103,1007050321,1016668999,1000111103,1000000087,1000111103
	
	public Factor (String title) {
		super(title);
		setSize(300,300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainFrame, BorderLayout.CENTER);
		buildMainFrame();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	* Application quizzes the user on Amino Acids
	* @param  seconds  first param - amount of seconds to run quiz for
	* @return      voice
	*/
	public static void main(String args[])
	{
		Factor UI = new Factor("Prime Number Threader v0.01");
		UIPass = UI;
		
	}
	
	public void buildMainFrame() {
		mainFrame.setLayout(new GridLayout(9,2));
		mainFrame.add(enterAnswerLabel);
		mainFrame.add(holder);
		mainFrame.add(answerField);
		mainFrame.add(submitAnswer);

		mainFrame.add(numberLabel);
		mainFrame.add(isPrimeLabel);
		mainFrame.add(numberCountLabel);
		mainFrame.add(primeLabel);
		mainFrame.add(primesLabel);
		mainFrame.add(primesFoundLabel);
		mainFrame.add(threadLabel);
		mainFrame.add(threadCount);
		mainFrame.add(statusLabel);
		mainFrame.add(statusCounterLabel);
		mainFrame.add(runtimeLabel);
		mainFrame.add(timeLabel);
		mainFrame.add(cancelCheck);
		cancelCheck.setEnabled(false);
		submitAnswer.setEnabled(true);
		cancelCheck.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent ae) {
				 int ftTotal = ft.length;
				 for(int i=0; i< ftTotal; i++) {
					 if (ft[i] != null) {
						 ft[i].stop();
					 }
				 }
				 primesFound = 0;
				 threadFinished = 0;
				 threadStarted = 0;
				 statusCounterLabel.setText("CANCELLED");
			     submitAnswer.setEnabled(true);
			     cancelCheck.setEnabled(false);
			 }
		});
		submitAnswer.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent ae) {
				 valArr = answerField.getText().split(",");
				 valuesCount = valArr.length;
				 int count = Integer.parseInt(threadCount.getText());
				 if(count > valuesCount) {
					 statusCounterLabel.setText("THREAD CT > VALS");
				 } else {
					 Date date = new Date();
					 primesFound = 0;
					 timeMilli = date.getTime();
					 submitAnswer.setEnabled(false);
					 statusCounterLabel.setText("RUNNING");
					 cancelCheck.setEnabled(true);
				 ft = new FactorThread[valuesCount];
				 for(int i=0; i< count; i++) {
					 Long longObj1 = new Long(valArr[i]);
						 factorThread = new FactorThread(UIPass,longObj1,i);
						 ft[i] = factorThread;
						 new Thread(factorThread).start();
						 threadStarted++;
				 }
				 }
			 }
		});
	}
	
	public void threadFinished(FactorThread thread, Long num, boolean prime, int threadId) {
		threadFinished++;
		numberCountLabel.setText(num.toString());
		ft[threadId] = null;
		if(prime == true)
		{
			primesFound++;
			primeLabel.setText("TRUE");
			primesFoundLabel.setText(String.valueOf(primesFound));
		} else {
			primeLabel.setText("FALSE");
		}
		if(threadFinished == valuesCount) {
			statusCounterLabel.setText("COMPLETE");
			cancelCheck.setEnabled(false);
			submitAnswer.setEnabled(true);
			Date date = new Date();
			Long runtime = date.getTime() - timeMilli;
			timeLabel.setText(runtime.toString());
			threadFinished = 0;
			threadStarted = 0;
			primesFound = 0;
			primesFoundLabel.setText("");
		} else if (threadStarted < valuesCount){
			 Long longObj1 = new Long(valArr[threadStarted]);
			 factorThread = new FactorThread(UIPass,longObj1,threadStarted);
			 ft[threadStarted] = factorThread;
			 new Thread(factorThread).start();
			 threadStarted++;
		}
	}

}
