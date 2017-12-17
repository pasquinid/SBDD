package br.edu.ufu.sd.view;

import br.edu.ufu.sd.model.vo.Vertice;

public class VerticeView {
	public void exibir(String mensagem, Vertice vertice) {
		System.out.println ("===========================================");
		System.out.println ("  " + mensagem);
		System.out.println ("===========================================");
		if ( vertice != null ) {
			System.out.println ("  Nome      : " + vertice.getNome() );
			System.out.println ("  Cor       : " + vertice.getCor());
			System.out.println ("  Descricao : " + vertice.getDescricao() );
			System.out.println ("  Hash      : " + vertice.getHash());
		}
		else {
			System.out.println ("  Nome      : null");
			System.out.println ("  Cor       : null");
			System.out.println ("  Descricao : null");
			System.out.println ("  Hash      : null");
		}
	}
}
