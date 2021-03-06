package br.edu.ufu.sd.model.bo.comando.atomix.escrita;

import br.edu.ufu.sd.model.vo.Vertice;
import io.atomix.copycat.Command;

public class ComandoAtomixAtualizarVertice implements Command<Boolean> {
	private static final long serialVersionUID = 1L;
	private Vertice vertice;
	
	public ComandoAtomixAtualizarVertice(Vertice vertice) {
		this.setVertice(vertice);
	}

	public Vertice getVertice() {
		return vertice;
	}

	public void setVertice(Vertice vertice) {
		this.vertice = vertice;
	}
	
}
