package br.edu.ufu.sd.model.bo.comando.escrita;

import br.edu.ufu.sd.model.vo.Aresta;

import br.edu.ufu.sd.model.vo.BancoDados;

public class ComandoAtualizarAresta extends ComandoEscrita {
	private Aresta aresta;

	public ComandoAtualizarAresta(BancoDados bancoDeDados, Aresta aresta) {
		super(bancoDeDados);
		this.aresta = aresta;
	}

	@Override
	public void escrever() {
		Aresta a = this.getBancoDados().get(aresta.getChave());
		if ( a != null ) {
			this.getBancoDados().atualizar(this.aresta);
			setResultado(true);
		} else setResultado(false);
	}
}
