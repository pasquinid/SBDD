package br.edu.ufu.sd.model.bo.comando.leitura;

import br.edu.ufu.sd.model.vo.BancoDados;

import br.edu.ufu.sd.model.vo.ChaveAresta;

public class ComandoGetAresta extends ComandoLeitura {
	private ChaveAresta chave;

	public ComandoGetAresta(BancoDados bancoDeDados, ChaveAresta chave) {
		super(bancoDeDados);
		this.chave = chave;
	}

	@Override
	public void ler() {
		this.setResposta(this.getBancoDados().get(chave));
	}
}