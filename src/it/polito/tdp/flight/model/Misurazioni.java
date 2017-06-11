package it.polito.tdp.flight.model;

public class Misurazioni {
	
	private Airport source;
	private Airport destination;
	private double distanza;
	private double durata;
	public Misurazioni(Airport source, Airport destination, double distanza, double durata) {
		super();
		this.source = source;
		this.destination = destination;
		this.distanza = distanza;
		this.durata = durata;
	}
	public Airport getSource() {
		return source;
	}
	public void setSource(Airport source) {
		this.source = source;
	}
	public Airport getDestination() {
		return destination;
	}
	public void setDestination(Airport destination) {
		this.destination = destination;
	}
	public double getDistanza() {
		return distanza;
	}
	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}
	public double getDurata() {
		return durata;
	}
	public void setDurata(double durata) {
		this.durata = durata;
	}
	
	
}
