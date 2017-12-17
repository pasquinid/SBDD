package br.edu.ufu.sd.model.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import br.edu.ufu.sd.model.vo.Aresta;
import br.edu.ufu.sd.model.vo.Vertice;

public class BancoDados {
	private Map<ChaveAresta, Aresta> aresta;
	private Map<Integer, Vertice> vertice;
	
	// Semaforo para acesso aos dados
	private Semaphore bd;
	
	// Semaforo para acesso a variavel numeroLeitores
	private Semaphore mutex;
	private int numeroLeitores;
	
	
	public BancoDados () {
		this.aresta = new HashMap<ChaveAresta, Aresta>();
		this.vertice = new HashMap<Integer, Vertice>();
		this.bd = SemaforoFabrica.getInstancia().get(SemaforoFabrica.BINARIO);
		this.mutex = SemaforoFabrica.getInstancia().get(SemaforoFabrica.BINARIO);
		this.numeroLeitores = 0;
	}
	
	public Semaphore getBD() {
		return this.bd;
	}
	
	public Semaphore getMutex() {
		return this.mutex;
	}
	
	public int getNumeroLeitores() {
		return this.numeroLeitores;
	}
	
	public void setNumeroLeitores(int numeroLeitores) {
		this.numeroLeitores = numeroLeitores;
	}
		
	public Set<Aresta> getTodasArestas(){
		return new HashSet<Aresta>(aresta.values());
	}
		
	public Set<Vertice> getTodosVertices(){
		return new HashSet<Vertice>(vertice.values());
	}
		
	public Aresta get(ChaveAresta chave) {
		return this.aresta.get(chave);
	}
		
	public Vertice get(int nome) {
		return this.vertice.get(nome);
	}
		
	public void atualizar(Aresta aresta) {
		Aresta a = this.get(aresta.getChave());
		if ( a != null ) {
			a.setPeso(aresta.getPeso());
			a.setDirecionada(aresta.isDirecionada());
			a.setDescricao(aresta.getDescricao());
		}
	}
		
	public void atualizar(Vertice vertice) {
		Vertice v = this.get(vertice.getNome());
		if ( v != null ){
			v.setCor(vertice.getCor());
			v.setDescricao(vertice.getDescricao());
		}
	}
	
	public Vertice inserir(Vertice vertice) {
		Vertice v = new Vertice(vertice);
		return this.vertice.put(v.getNome(), v);
	}
	
	public Aresta inserir(Aresta aresta) {
		Aresta a = new Aresta(aresta);
		return this.aresta.put(a.getChave(), a);
	}
	
	public Vertice deletar(int nome) {
		return this.vertice.remove(nome);
	}
		
	public Aresta deletar(ChaveAresta chaveAresta) {
		return this.aresta.remove(chaveAresta);
	}	
}
