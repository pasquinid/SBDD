package br.edu.ufu.sd.model.vo;

import java.util.ArrayList;
import java.util.List;

public class TabelaRoteamento {
	public static List<No> get(int identificador){
		List<No> tabela = new ArrayList<No>();
		
		// Portas sao indicadas por 10050 + numeroDoIdentificador
		No n1 = new No (1, "localhost", 10051);
		No n4 = new No (4, "localhost", 10054);
		No n9 = new No (9, "localhost", 10059);
		No n11 = new No (11, "localhost", 10061);
		No n14 = new No (14, "localhost", 10064);
		No n18 = new No (18, "localhost", 10068);
		No n20 = new No (20, "localhost", 10070);
		No n21 = new No (21, "localhost", 10071);
		No n28 = new No (28, "localhost", 10078);
	
		if ( identificador == 1) {
			tabela.add(n4);
			tabela.add(n4);
			tabela.add(n9);
			tabela.add(n9);
			tabela.add(n18);
		}
		else if ( identificador == 4 ) {
			tabela.add(n9);
			tabela.add(n9);
			tabela.add(n9);
			tabela.add(n14);
			tabela.add(n20);
		}
		
		else if ( identificador == 9 ) {
			tabela.add(n11);
			tabela.add(n11);
			tabela.add(n14);
			tabela.add(n18);
			tabela.add(n28);
		}
		
		else if ( identificador == 11 ) {
			tabela.add(n14);
			tabela.add(n14);
			tabela.add(n18);
			tabela.add(n20);
			tabela.add(n28);
		}
		
		else if ( identificador == 14 ) {
			tabela.add(n18);
			tabela.add(n18);
			tabela.add(n18);
			tabela.add(n28);
			tabela.add(n1);
		}
		
		else if ( identificador == 18) {
			tabela.add(n20);
			tabela.add(n20);
			tabela.add(n28);
			tabela.add(n28);
			tabela.add(n4);
		}

		else if ( identificador == 20 ) {
			tabela.add(n21);
			tabela.add(n28);
			tabela.add(n28);
			tabela.add(n28);
			tabela.add(n4);
		}
		else if ( identificador == 21 ) {
			tabela.add(n28);
			tabela.add(n28);
			tabela.add(n28);
			tabela.add(n1);
			tabela.add(n9);
		}
		else if ( identificador == 28 ) {
			tabela.add(n1);
			tabela.add(n1);
			tabela.add(n1);
			tabela.add(n4);
			tabela.add(n14);
		}
		
		return tabela;
		
	}

}
