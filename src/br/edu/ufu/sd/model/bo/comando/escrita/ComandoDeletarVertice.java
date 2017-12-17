package br.edu.ufu.sd.model.bo.comando.escrita;

import java.util.Set;

import br.edu.ufu.sd.model.vo.Aresta;
import br.edu.ufu.sd.model.vo.BancoDados;
import br.edu.ufu.sd.model.vo.ChaveAresta;
import br.edu.ufu.sd.model.vo.Vertice;

public class ComandoDeletarVertice extends ComandoEscrita{
	private Vertice vertice;
	private int nome;

	public ComandoDeletarVertice(BancoDados bancoDeDados, int nome) {
		super(bancoDeDados);
		this.nome = nome;
	}
	
	public Vertice getVertice() {
		return vertice;
	}

	@Override
	public void escrever() {
		this.vertice = this.getBancoDados().get(nome);
		if ( this.vertice != null ) { // Verifica se o vertice existe
			Set<Aresta> todasArestas = this.getBancoDados().getTodasArestas();
			if ( !todasArestas.isEmpty() )
				for (Aresta a : todasArestas) { // Remove todas arestas adjacentes ao vertice
					ChaveAresta chave = a.getChave();
					if (chave.getVertice1() == vertice.getNome() || chave.getVertice2() == vertice.getNome())
						this.getBancoDados().deletar(chave);
				}
			this.getBancoDados().deletar(nome);
			setResultado(true);
		} else setResultado(false);
	}
}
