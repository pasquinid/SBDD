package br.edu.ufu.sd.view;

import br.edu.ufu.sd.model.vo.No;

public class NoView {
	
	public void exibir(No no) {
		System.out.println ("===========================================");
		System.out.println ("  Sistema Banco de Dados Distribuído - No  ");
		System.out.println ("===========================================");
		if ( no != null ) {
			System.out.println ("  Identificador: " + no.getIdentificador());
			System.out.println ("  Endereco     : " + no.getEndereco());
			System.out.println ("  Porta        : " + no.getPorta());
		} else {
			System.out.println ("  Identificador: null");
			System.out.println ("  Endereco     : null");
			System.out.println ("  Porta        : null");
			
		}
	}

}
