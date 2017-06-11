package it.polito.tdp.flight.model;

public class Evento implements Comparable<Evento>{
	
	private Airport a;
	private double time;
	
	public Evento(Airport a, double time) {
		super();
		this.a = a;
		this.time = time;
	}
	public Airport getA() {
		return a;
	}
	public void setA(Airport a) {
		this.a = a;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	@Override
	public int compareTo(Evento e) {
		return Double.compare(this.time, e.time);
	}
}
