package br.edu.ufu.sd.model.bo.comando.atomix.leitura;

import br.edu.ufu.sd.model.vo.Vertice;
import io.atomix.copycat.Query;

public class ComandoAtomixGetVertice implements Query<Vertice>{
	private static final long serialVersionUID = 1L;
	private int nome;
	
	public ComandoAtomixGetVertice(int nome) {
		this.setNome(nome);
	}

	public int getNome() {
		return nome;
	}

	public void setNome(int nome) {
		this.nome = nome;
	}

}
