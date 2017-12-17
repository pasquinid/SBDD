package br.edu.ufu.sd.model.bo.comando.escrita;

import br.edu.ufu.sd.model.vo.Aresta;

import br.edu.ufu.sd.model.vo.BancoDados;
import br.edu.ufu.sd.model.vo.ChaveAresta;
import br.edu.ufu.sd.model.vo.Vertice;


public class ComandoInserirAresta extends ComandoEscrita {
	private Aresta aresta;

	public ComandoInserirAresta(BancoDados bancoDeDados, Aresta aresta) {
		super(bancoDeDados);
		this.aresta = aresta;
	}

	@Override
	public void escrever() {
		ChaveAresta chave = aresta.getChave();
		
		Vertice v1 = this.getBancoDados().get(chave.getVertice1());
		Vertice v2 = this.getBancoDados().get(chave.getVertice2());
		
		if (v1 != null || v2 != null) { //
			Aresta a = this.getBancoDados().inserir(this.aresta);
			if ( a == null ) // Não há uma aresta
				setResultado(true);
			else setResultado(false);
		}
		else setResultado(false);
	}
		
}
