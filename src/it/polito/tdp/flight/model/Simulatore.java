package it.polito.tdp.flight.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class Simulatore {

	private SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	private Map<Airport,Integer> distribuzione;
	private PriorityQueue<Evento> coda;
	
	public Simulatore(SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> grafo) {
		this.grafo=grafo;
		
		distribuzione=new HashMap<Airport,Integer>();
		
		coda=new PriorityQueue<Evento>();
		
	}
	
	public void inserisci(int pass){
		for(Airport a: grafo.vertexSet()){
			int numerpPasseggeri=(int) (pass*Math.random());
			distribuzione.put(a, numerpPasseggeri);
			
			for(int i=0; i<numerpPasseggeri;i++){
				Evento e=new Evento(a,6);
				coda.add(e);
			}
		}
	}
	
	public void run(){
		
		while(!coda.isEmpty()){
			
			Evento e=coda.poll();
			if(e.getTime()>=47){
				continue;//deve passare all'evento successivo e non fare niente con questo-> QUINDI CONTINUE; SE METTESSI RETURN O BREAK INTERROMPO TUTTO IL CICLO
			}
			List<Airport> listaPortiDisponibili=Graphs.successorListOf(grafo, e.getA());
			if(listaPortiDisponibili.isEmpty()){
				continue;
			}
			int passPartenza=distribuzione.get(e.getA());
			int nuovoPassPartenza=passPartenza-1;
			distribuzione.replace(e.getA(), passPartenza, nuovoPassPartenza);
			
			int scelta=(int) (Math.random()*listaPortiDisponibili.size());
			Airport a=listaPortiDisponibili.get(scelta);
			
			double distanza=LatLngTool.distance(e.getA().getCoord(), a.getCoord(), LengthUnit.KILOMETER);
			double durata=distanza/800.0;
			double arrivo;
			if(e.getTime()==6){
				arrivo=e.getTime()+1+durata;
			}
			else{
				arrivo=e.getTime()+durata;
			}
			
			int prossimaPartenza=(int) Math.ceil(arrivo);
			if(prossimaPartenza%2==0)
				prossimaPartenza+=1;
			
			coda.add(new Evento(a,prossimaPartenza));
			int nuovaDest=distribuzione.get(a);
			int newDest=nuovaDest+1;
			distribuzione.replace(a, nuovaDest, newDest);
		}
	}
	
	public List<Stat> listaPortiEPasseggeri(){
		List<Stat> lista=new ArrayList<Stat>();
		
		for(Airport a: distribuzione.keySet()){
			if(distribuzione.get(a)!=0)
				lista.add(new Stat(a,distribuzione.get(a)));
		}
		
		Collections.sort(lista);
		return lista;
	}
}
