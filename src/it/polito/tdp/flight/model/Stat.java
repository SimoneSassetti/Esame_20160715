package it.polito.tdp.flight.model;

public class Stat implements Comparable<Stat>{
	private Airport a;
	private int passeggeri;
	public Stat(Airport a, int passeggeri) {
		super();
		this.a = a;
		this.passeggeri = passeggeri;
	}
	public Airport getA() {
		return a;
	}
	public void setA(Airport a) {
		this.a = a;
	}
	public int getPasseggeri() {
		return passeggeri;
	}
	public void setPasseggeri(int passeggeri) {
		this.passeggeri = passeggeri;
	}
	@Override
	public int compareTo(Stat s) {
		return -(this.passeggeri-s.passeggeri);
	}
}
