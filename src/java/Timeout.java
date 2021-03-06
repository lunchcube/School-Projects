

public class Timeout extends Thread {
	private int timeout;
	private int elapsed;
	public int rate = 1000;
	public boolean finished;
	public int seqNum;
	private Sender sender;

	public Timeout(int seq_num, int millis, Sender sender) {
		this.seqNum = seq_num;
		this.timeout = millis;
		elapsed = 0;
		this.sender = sender;
		this.start();
	}

	public synchronized void reset() {
		elapsed = 0;
	}

	public void run() {
		while(!finished) {
			try {
				Thread.sleep(rate);
			} catch (InterruptedException ioe) {
				continue;
			}
			synchronized (this) {
				elapsed += rate;
				if (elapsed > this.timeout){
					timeout();
				}
			}
		}
	}

	public void timeout() {
		System.out.println("Message #" + this.seqNum + " timed out. Resending...");
		sender.transmit(this.seqNum);
		reset();
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
}
