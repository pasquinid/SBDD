package br.edu.ufu.sd.model.bo.comando.atomix.escrita;

import io.atomix.copycat.Command;

public class ComandoAtomixDeletarVertice implements Command<Boolean>{
	private static final long serialVersionUID = 1L;
	private int nome;
	
	public ComandoAtomixDeletarVertice(int nome) {
		this.setNome(nome);
	}

	public int getNome() {
		return nome;
	}

	public void setNome(int nome) {
		this.nome = nome;
	}

}
