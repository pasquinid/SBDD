package br.edu.ufu.sd.model.bo.comando.escrita;

import br.edu.ufu.sd.model.vo.BancoDados;

import br.edu.ufu.sd.model.vo.Vertice;

public class ComandoInserirVertice extends ComandoEscrita{
	private Vertice vertice;

	public ComandoInserirVertice(BancoDados bancoDeDados, Vertice vertice) {
		super(bancoDeDados);
		this.vertice = vertice;
	}

	@Override
	public void escrever() {
		Vertice v = this.getBancoDados().inserir(this.vertice);
		if ( v == null ) // Não há um vertice
			setResultado(true);
		else setResultado(false);
	}
}
