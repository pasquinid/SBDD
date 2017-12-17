package br.edu.ufu.sd.view;

import java.util.List;

import br.edu.ufu.sd.model.vo.No;


public class TabelaRoteamentoView {
	public void exibir(List<No> tabela) {
		System.out.println ("===========================================");
		System.out.println ("  Tabela de Roteamento ");
		System.out.println ("===========================================");
		
		if ( tabela != null ) {
			for (int i = 0; i < tabela.size(); i++) {
				System.out.println ("  Entrada : " + (i+1));
				System.out.println ("  Id      : " + tabela.get(i).getIdentificador());
				System.out.println ("  Endereco: " + tabela.get(i).getEndereco());
				System.out.println ("  Porta   : " + tabela.get(i).getPorta());
				System.out.println();
			}
		} else {
			System.out.println ("  Entrada : null");
			System.out.println ("  Id      : null");
			System.out.println ("  Endereco: null");
			System.out.println ("  Porta   : null");
			System.out.println();
		}
		
	}

}
