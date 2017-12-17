package br.edu.ufu.sd.model.bo.comando.leitura;

import br.edu.ufu.sd.model.vo.BancoDados;

public class ComandoGetVertice extends ComandoLeitura {
	private int nomeVertice;

	public ComandoGetVertice(BancoDados bancoDeDados, int nomeVertice) {
		super(bancoDeDados);
		this.nomeVertice = nomeVertice;
	}

	@Override
	public void ler() {
		this.setResposta(this.getBancoDados().get(nomeVertice));
	}
}