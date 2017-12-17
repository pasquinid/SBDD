package br.edu.ufu.sd.model.bo.comando.atomix.escrita;

import br.edu.ufu.sd.model.vo.ChaveAresta;
import io.atomix.copycat.Command;

public class ComandoAtomixDeletarAresta implements Command<Boolean>{
	private static final long serialVersionUID = 1L;
	private ChaveAresta chaveAresta;
	
	public ComandoAtomixDeletarAresta(ChaveAresta chaveAresta) {
		this.setChaveAresta(chaveAresta);
	}

	public ChaveAresta getChaveAresta() {
		return chaveAresta;
	}

	public void setChaveAresta(ChaveAresta chaveAresta) {
		this.chaveAresta = chaveAresta;
	}

}
