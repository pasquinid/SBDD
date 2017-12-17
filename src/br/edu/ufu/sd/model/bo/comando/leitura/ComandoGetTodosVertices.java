package br.edu.ufu.sd.model.bo.comando.leitura;

import br.edu.ufu.sd.model.vo.BancoDados;

public class ComandoGetTodosVertices extends ComandoLeitura{
	public ComandoGetTodosVertices(BancoDados bancoDeDados) {
		super(bancoDeDados);
	}

	@Override
	public void ler() {
		this.setResposta(this.getBancoDados().getTodosVertices());
	}
}