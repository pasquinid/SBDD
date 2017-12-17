package br.edu.ufu.sd.model.bo.comando.atomix.escrita;

import br.edu.ufu.sd.model.vo.Aresta;
import io.atomix.copycat.Command;

public class ComandoAtomixInserirAresta implements Command<Boolean> {
	private static final long serialVersionUID = 1L;
	private Aresta aresta;
	
	public ComandoAtomixInserirAresta(Aresta aresta) {
		this.setAresta(aresta);
	}

	public Aresta getAresta() {
		return aresta;
	}

	public void setAresta(Aresta aresta) {
		this.aresta = aresta;
	}
	
}
