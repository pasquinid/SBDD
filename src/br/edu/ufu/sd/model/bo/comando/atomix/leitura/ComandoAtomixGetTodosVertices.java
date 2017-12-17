package br.edu.ufu.sd.model.bo.comando.atomix.leitura;

import java.util.Set;

import br.edu.ufu.sd.model.vo.Vertice;
import io.atomix.copycat.Query;

public class ComandoAtomixGetTodosVertices implements Query<Set<Vertice>> {
	private static final long serialVersionUID = 1L;
}
