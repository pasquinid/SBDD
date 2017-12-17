package br.edu.ufu.sd.model.bo.comando.atomix.leitura;

import br.edu.ufu.sd.model.vo.Aresta;
import br.edu.ufu.sd.model.vo.ChaveAresta;
import io.atomix.copycat.Query;

public class ComandoAtomixGetAresta  implements Query<Aresta>{
	private static final long serialVersionUID = 1L;
	private ChaveAresta chaveAresta;
	
	public ComandoAtomixGetAresta(ChaveAresta chaveAresta) {
		this.setChaveAresta(chaveAresta);
	}

	public ChaveAresta getChaveAresta() {
		return chaveAresta;
	}

	public void setChaveAresta(ChaveAresta chaveAresta) {
		this.chaveAresta = chaveAresta;
	}

}
