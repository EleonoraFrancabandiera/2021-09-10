package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge> grafo;
	private Map<String, Business> idMap;
		
	private List<Business> listaMigliore; 
	
	public Model() {
		dao=new YelpDao();
		idMap=new HashMap<>();
		
		this.dao.getAllBusiness(idMap);
	}
	
	public void creaGrafo(String citta) {
		//creo il grafo
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(citta, idMap));
		
		//aggiungo gli archi
		for(Adiacenza a : this.dao.getArchi(citta, idMap)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getB1(), a.getB2(), a.getPeso());
		}
		
		System.out.println("Grafo creato!");
		System.out.println(String.format("# Vertici: %d", this.grafo.vertexSet().size()));
		System.out.println(String.format("# Archi: %d", this.grafo.edgeSet().size()));
		
	}
	
	public List<Adiacenza> getDistanzaMax(Business business){
    	List<Adiacenza> result = new ArrayList<>();
    	
    	double max=0;
    	
    	//dato un vertice, trovo tutti gli archi collegati a quel vertice 
    	//controllo il peso degli archi, se è maggiore del massimo lo aggiungo
    	//alla lista result
    	//Set<DefaultWeightedEdge> listaArchi = this.grafo.edgesOf(business);
    	List<Business> target = Graphs.neighborListOf(this.grafo, business);
    	
    	for(Business t : target) {
    		DefaultWeightedEdge e = this.grafo.getEdge(business, t);
    		double peso= this.grafo.getEdgeWeight(e);
    		if(peso>max) {
    			result.clear();
    			result.add(new Adiacenza(business, t, peso));
    			max=peso;
    		}else if(peso==max) {
    			result.add(new Adiacenza(business, t, peso));
    		}
    	}
    	return result;
    }

	
	public List<String> getCity(){
		return dao.getAllCity();
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Business> getVertici() {
		return new ArrayList<>(this.grafo.vertexSet());
	}
	
	public boolean grafoCreato() {
		if(this.grafo==null)
			return false;
		return true;
	}

	public List<Business> cercaPercorso(Business b1, Business b2, double x) {
		List<Business> localiValidi = new ArrayList<Business>();
				
		localiValidi.addAll(this.grafo.vertexSet());
		
		List<Business> parziale = new ArrayList<>();
		listaMigliore= new ArrayList<>();
		parziale.add(b1);
		
		cerca(parziale, localiValidi, b2, x);
		
		listaMigliore.add(b2);
		return listaMigliore;
	}

	private void cerca(List<Business> parziale, List<Business> localiValidi, Business b2, double x) {
		
		if(parziale.size()>listaMigliore.size()) {
			listaMigliore = new ArrayList<>(parziale);
		}
		
		for(Business b: localiValidi) {
			if(!parziale.contains(b) && b.getStars()>=x && !b.equals(b2)) {
				parziale.add(b);
				cerca(parziale, localiValidi, b2, x);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	public double contaKilometri(List<Business> listaLocali) {
		double result=0;
		for(int i=0; i<(listaLocali.size()-1); i++) {
			DefaultWeightedEdge e = this.grafo.getEdge(listaLocali.get(i), listaLocali.get(i+1));
			result+=this.grafo.getEdgeWeight(e);
		}
		return result;
	}
}
