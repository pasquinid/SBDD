package br.edu.ufu.sd.model.bo.comando.escrita;

import br.edu.ufu.sd.model.vo.BancoDados;
import br.edu.ufu.sd.model.vo.Vertice;

public class ComandoAtualizarVertice extends ComandoEscrita {
	private Vertice vertice;

	public ComandoAtualizarVertice(BancoDados bancoDeDados, Vertice vertice) {
		super(bancoDeDados);
		this.vertice = vertice;
	}

	@Override
	public void escrever() {
		Vertice v = this.getBancoDados().get(vertice.getNome());
		if ( v != null ) {
			this.getBancoDados().atualizar(this.vertice);
			setResultado(true);
		} else setResultado(false);
	}
}
