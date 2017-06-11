package it.polito.tdp.flight.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flight.db.FlightDAO;

public class Model {
	FlightDAO dao;
	private List<Airport> aeroporti;
	private Map<Integer,Airport> mappaPorti;
	private SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	
	public Model(){
		dao=new FlightDAO();
		mappaPorti=new HashMap<Integer, Airport>();
	}
	
	public List<Airport> getPorti(){
		if(aeroporti==null){
			aeroporti=dao.getAllAirports();
			for(Airport a: aeroporti){
				mappaPorti.put(a.getAirportId(), a);
			}
		}
		return aeroporti;
	}
	
	public String creaGrafo(int dist){
		grafo=new SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, aeroporti);
		
		//prendo gli archi dal DB
		List<Misurazioni> lista=dao.getMisure(dist,mappaPorti);
		for(Misurazioni m: lista){
			DefaultWeightedEdge arco=grafo.addEdge(m.getSource(), m.getDestination());
			if(arco!=null){
				grafo.setEdgeWeight(arco, m.getDurata());
			}
		}
		
		ConnectivityInspector<Airport,DefaultWeightedEdge> ci=new ConnectivityInspector<Airport,DefaultWeightedEdge>(grafo);
		
		String s="";
		if(ci.isGraphConnected()){
			s+="Grafo connesso.\n";
		}else{
			s+="Grafo non connesso: non è possibile raggiungere tutte le destinazioni da ogni partenza.\n";
		}
		return s;
	}
	
	public Airport getFiumicino(){
		int idFium=0;
		for(Airport fium: mappaPorti.values()){
			if(fium.getName().compareTo("Fiumicino")==0){
				idFium=fium.getAirportId();
				break;
			}
		}
		Airport trovato = null;
		for(Airport a: grafo.vertexSet()){
			if(a.getAirportId()==idFium){
				List<Airport> lista=Graphs.successorListOf(grafo, a);
				double dist=0;
				for(Airport l: lista){
					double temp=LatLngTool.distance(a.getCoord(),l.getCoord(), LengthUnit.KILOMETER);
					if(temp>dist){
						dist=temp;
						trovato=l;
					}
				}
				break;
			}
		}
		return trovato;
	}
	
	public List<Stat> simula(int passeggeri){
		
		Simulatore sim=new Simulatore(grafo);
		
		sim.inserisci(passeggeri);
		sim.run();
		return sim.listaPortiEPasseggeri();
	}
}
