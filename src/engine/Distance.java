package engine;

public class Distance {
	private String from; //READ ONLY
	private String to; //READ ONLY
	private int distance; //READ ONLY
	
	public Distance(String from, String to, int distance) {
		this.from = from;
		this.to = to;
		this.distance = distance;
	}
	
//	Getters
	public String getFrom() {
		return this.from;
	}
	public String getTo() {
		return this.to;
	}
	public int getDistance() {
		return this.distance;
	}
	
}
