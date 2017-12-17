package br.edu.ufu.sd.model.bo.comando.atomix.leitura;


import br.edu.ufu.sd.model.vo.Aresta;


import io.atomix.copycat.Query;

import java.util.Set;

public class ComandoAtomixGetTodasArestas implements Query<Set<Aresta>> {
	private static final long serialVersionUID = 1L;
}
