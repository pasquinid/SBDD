package br.edu.ufu.sd.model.bo.comando.escrita;

import br.edu.ufu.sd.model.vo.Aresta;

import br.edu.ufu.sd.model.vo.BancoDados;
import br.edu.ufu.sd.model.vo.ChaveAresta;

public class ComandoDeletarAresta extends ComandoEscrita {
	private Aresta aresta;
	private ChaveAresta chaveAresta;

	public ComandoDeletarAresta(BancoDados bancoDeDados, ChaveAresta chaveAresta) {
		super(bancoDeDados);
		this.chaveAresta = chaveAresta;
	}

	public Aresta getAresta() {
		return aresta;
	}
	
	@Override
	public void escrever() {
		this.aresta = this.getBancoDados().get(chaveAresta);
		if ( this.aresta  != null ) {
			this.getBancoDados().deletar(chaveAresta);
			setResultado(true);
		} else setResultado(false);
	}
}
