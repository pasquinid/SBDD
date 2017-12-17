package br.edu.ufu.sd.view;

import br.edu.ufu.sd.model.vo.Aresta;

public class ArestaView {
	public void exibir(String mensagem, Aresta aresta) {
		System.out.println ("===========================================");
		System.out.println ("  " + mensagem);
		System.out.println ("===========================================");
		if ( aresta != null ) {
			System.out.println ("  Vertice 1  : " + aresta.getChave().getVertice1());
			System.out.println ("  Vertice 2  : " + aresta.getChave().getVertice2());
			System.out.println ("  Peso       : " + aresta.getPeso() );
			System.out.println ("  Direcionada: " + aresta.isSetDirecionada() );
			System.out.println ("  Descricao  : " + aresta.getDescricao() );
			System.out.println ("  Hash       : " + aresta.getChave().getHash());
		}
		else {
			System.out.println ("  Vertice 1  : null");
			System.out.println ("  Vertice 2  : null");
			System.out.println ("  Peso       : null");
			System.out.println ("  Direcionada: null");
			System.out.println ("  Descricao  : null");
			System.out.println ("  Hash       : null");
		}
	}
}
