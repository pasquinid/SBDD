package br;

import java.util.HashSet;


import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import br.edu.ufu.sd.model.bo.FuncaoDistribuicao;
import br.edu.ufu.sd.model.dao.ArquivoConfiguracaoDAO;
import br.edu.ufu.sd.model.vo.Aresta;
import br.edu.ufu.sd.model.vo.BancoDadosDistribuido;
import br.edu.ufu.sd.model.vo.ChaveAresta;
import br.edu.ufu.sd.model.vo.No;
import br.edu.ufu.sd.model.vo.Vertice;
import br.edu.ufu.sd.view.ArestaView;
import br.edu.ufu.sd.view.VerticeView;

public class Cliente {
    private static final ArquivoConfiguracaoDAO dao = new ArquivoConfiguracaoDAO();
	private static Scanner teclado = new Scanner(System.in);;
	private static FuncaoDistribuicao funcao;
	private static BancoDadosDistribuido.Client client;
	private static VerticeView verticeView= new VerticeView();
	private static ArestaView arestaView = new ArestaView();
	
	private static void inicializarFuncaoDistribuicao(String[] args) {
		Cliente.funcao = new FuncaoDistribuicao(Integer.parseInt(args[0]));
	}
	
	private static void inicializarConexao(String[] args) {
		try {
			String endereco = args[1];
			int porta = Integer.parseInt(args[2]);
			TTransport transport;
	        transport = new TSocket(endereco, porta);
	        transport.open();
	        TProtocol protocol = new TBinaryProtocol(transport);
	        Cliente.client = new BancoDadosDistribuido.Client(protocol);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void menuPrincipal() {
		clear_terminal();
        System.out.println("==================================================================");
        System.out.println("| 1 - No                                                         |");
        System.out.println("| 2 - Vertices                                                   |");
        System.out.println("| 3 - Arestas                                                    |");
        System.out.println("| 0 - Sair                                                       |");
        System.out.println("==================================================================");
	}
	
	
	public static void menuNo() {
		clear_terminal();
		System.out.println("==============================================================================");
        System.out.println("| 1 - Adicionar No                                                           |");
        System.out.println("| 2 - Adicionar No com Indice                                                |");
        System.out.println("| 3 - Remover No                                                             |");
        System.out.println("| 4 - Remover No com Indice                                                  |");
        System.out.println("| 0 - Sair                                                                   |");
        System.out.println("==============================================================================");
	}
	
	public static void menuAdicionarNo() {
		try {
			clear_terminal();
			System.out.println("==================================================================");
	        System.out.println("| Inserir o identificador(int) do novo No                        |");
	        System.out.println("==================================================================");
	        int id = teclado.nextInt();
	        
	        System.out.println("==================================================================");
	        System.out.println("| Inserir o endereco(string) do novo no                          |");
	        System.out.println("==================================================================");
	        String endereco = teclado.next();
	        
	        System.out.println("==================================================================");
	        System.out.println("| Inserir a porta(int) do novo no                                |");
	        System.out.println("==================================================================");
	        int porta = teclado.nextInt();
	        
	        No no = new No (id, endereco, porta);
	        Cliente.client.adicionarNo(no);
		} catch(Exception e) {
			System.out.println("==================================================================");
	        System.out.println("| No nao encontrado                                              |");
	        System.out.println("==================================================================");
	        sair();
		}
        
	}
	
	public static void menuAdicionarNoIndice() {
		try {
			clear_terminal();
			System.out.println("==================================================================");
	        System.out.println("| Inserir o identificador(int) do novo No                        |");
	        System.out.println("==================================================================");
	        int id = teclado.nextInt(); teclado.nextLine();
	        
	   
	        System.out.println("==================================================================");
	        System.out.println("| Inserir o endereco(string) do novo no                          |");
	        System.out.println("==================================================================");
	        String endereco = teclado.next();
	        
	        System.out.println("==================================================================");
	        System.out.println("| Inserir a porta(int) do novo no                                |");
	        System.out.println("==================================================================");
	        int porta = teclado.nextInt();
	        
	        System.out.println("==================================================================");
	        System.out.println("| Inserir o indice na tabela                                     |");
	        System.out.println("==================================================================");
	        int indice = teclado.nextInt();
	        
	        No no = new No (id, endereco, porta);
	        Cliente.client.adicionarNoIndice(indice, no);
		} catch (Exception e) {
			System.out.println("==================================================================");
	        System.out.println("| No nao encontrado                                              |");
	        System.out.println("==================================================================");
	        sair();
		}
        
	}
	
	public static void menuRemoverNo() {
		try {
			clear_terminal();
			System.out.println("==================================================================");
	        System.out.println("| Inserir o identificador(int) do no a ser removido              |");
	        System.out.println("==================================================================");
	        int id = teclado.nextInt();
	        
	        System.out.println("==================================================================");
	        System.out.println("| Inserir o endereco(string) do no a ser removido                |");
	        System.out.println("==================================================================");
	        String endereco = teclado.next();
	        
	        System.out.println("==================================================================");
	        System.out.println("| Inserir a porta(int) do no a ser removido                                |");
	        System.out.println("==================================================================");
	        int porta = teclado.nextInt();
	        
	        No no = new No (id, endereco, porta);
	        Cliente.client.removerNo(no);
		} catch (Exception e) {
			System.out.println("==================================================================");
	        System.out.println("| No nao encontrado                                              |");
	        System.out.println("==================================================================");
	        sair();
		}
        
	}
	
	public static void menuRemoverNoIndice() {
		try {
			clear_terminal();
			System.out.println("==================================================================");
		    System.out.println("| Inserir o indice na tabela                                     |");
		    System.out.println("==================================================================");
		    int indice = teclado.nextInt();
	        Cliente.client.removerNoIndice(indice);
		} catch (Exception e){
			System.out.println("==================================================================");
	        System.out.println("| No nao encontrado                                              |");
	        System.out.println("==================================================================");
	        sair();
		}
	}
	
	public static void menuVertice() {
		 clear_terminal();
		 System.out.println("==============================================================================");
         System.out.println("| 1 - Buscar Vertice                                                         |");
         System.out.println("| 2 - Inserir Vertice                                                        |");
         System.out.println("| 3 - Atualizar Vertice                                                      |");
         System.out.println("| 4 - Deletar Vertice                                                        |");
         System.out.println("| 5 - Todos os Vertices                                                      |");
         System.out.println("| 6 - Vertices Adjacentes                                                    |");
         System.out.println("| 0 - Sair                                                                   |");
         System.out.println("==============================================================================");
	}
	
	public static void menuAresta() {
		 clear_terminal();                 
         System.out.println("==============================================================================");
         System.out.println("| 1 - Buscar Aresta                                                          |");
         System.out.println("| 2 - Inserir Aresta                                                         |");
         System.out.println("| 3 - Atualizar Aresta                                                       |");
         System.out.println("| 4 - Deletar Aresta                                                         |");
         System.out.println("| 5 - Todas as Arestas                                                       |");
         System.out.println("| 6 - Arestas incidentes a um vertice                                        |");
         System.out.println("| 0 - Sair                                                                   |");
         System.out.println("==============================================================================");
	}
	
	
	public static void menuBuscarVertice() {
		try {
    		clear_terminal();
            System.out.println("==================================================================");
            System.out.println("| Digite o nome (int) do vertice a ser buscado                   |");
            System.out.println("==================================================================");
            int opt_busca_v = teclado.nextInt();
            Vertice vertice_buscado = new Vertice(opt_busca_v, 0, null, funcao.executar(opt_busca_v));
            vertice_buscado = get_vertice(vertice_buscado);
            Cliente.verticeView.exibir("Vertice Encotrado", vertice_buscado);
            sair();
    	} catch (Exception e) {
         	System.out.println("==================================================================");
            System.out.println("| Vertice Nao Encotrado                                          |");
            System.out.println("==================================================================");
            sair();
        }
	}
	
	public static void menuInserirVertice() {
		try {
			clear_terminal();
	        System.out.println("==================================================================");
	        System.out.println("| Digite o nome (int) do vertice a ser inserido                  |");
	        System.out.println("==================================================================");                     
	        int nome_v_insere = teclado.nextInt();
	        
	        System.out.println("==================================================================");
	        System.out.println("| Digite a cor (int) do vertice a ser inserido                   |");
	        System.out.println("==================================================================");
	        int cor_v_insere = teclado.nextInt();
	        
	        System.out.println("==================================================================");
	        System.out.println("| Digite a descricao (string) do vertice a ser inserido          |");
	        System.out.println("==================================================================");                      
	        teclado.nextLine();
	        String des_v_insere = teclado.nextLine();
	        
	        Vertice vertice_inserido = new Vertice(nome_v_insere,cor_v_insere,des_v_insere,funcao.executar(nome_v_insere));
	        boolean r_insere = inserir_vertice(vertice_inserido,0);
	        printResultado(r_insere);
		} catch (Exception e) {
	        System.out.println("==================================================================");
	        System.out.println("| Houve um erro interno, por favor repita o processo             |");
	        System.out.println("==================================================================");   
		}
	}
	
	public static void menuAtualizarVertice() {
		clear_terminal();
        try {
        	System.out.println("==================================================================");
            System.out.println("| Digite o nome (int) do vertice a ser atualizado                |");
            System.out.println("==================================================================");               
            int opt_att_v = teclado.nextInt();
            Vertice vertice_a_ser_att = new Vertice(opt_att_v, 0, null, funcao.executar(opt_att_v));
       
            vertice_a_ser_att = get_vertice(vertice_a_ser_att);
         	verticeView.exibir("Vertice a ser atualizado", vertice_a_ser_att);
         
             
            System.out.println("==================================================================");
            System.out.println("| Qual destes voce deseja alterar                                |");
            System.out.println("| 1 - Cor                                                        |");
            System.out.println("| 2 - Descricao                                                  |");
            System.out.println("==================================================================");
            int opt_att_v_2 = Integer.parseInt(teclado.next());

            if(opt_att_v_2 == 1){
            	System.out.println("==================================================================");
                System.out.println("| Digite a nova cor (int) do vertice a ser atualizado            |");
                System.out.println("==================================================================");                   
                int nova_cor = Integer.parseInt(teclado.next());
                vertice_a_ser_att.cor = nova_cor;
            }
             
            if(opt_att_v_2 == 2){
             	System.out.println("==================================================================");
                System.out.println("| Digite a nova descricao (string) do vertice a ser atualizado   |");
                System.out.println("==================================================================");
                
                teclado.nextLine();
                String des_v_att = teclado.next();
                vertice_a_ser_att.descricao=des_v_att;
            }
            boolean res_att = atualizar_vertice(vertice_a_ser_att);
            printResultado(res_att);
            
         } catch (Exception e) {
         	System.out.println("==================================================================");
            System.out.println("| Vertice nao encontrado                                         |");
            System.out.println("==================================================================");
            sair();
         }
	}
	
	public static void menuDeletarVertice() {
		try {
			clear_terminal();
        	System.out.println("==================================================================");
            System.out.println("| Digite o nome (int) do vertice a ser deletado                  |");
            System.out.println("==================================================================");                       
            int opt_del_v = teclado.nextInt();

            Vertice vertice_a_ser_del = new Vertice(opt_del_v, 0, null, funcao.executar(opt_del_v));
            vertice_a_ser_del = get_vertice(vertice_a_ser_del);                      
            verticeView.exibir("Vertice", vertice_a_ser_del);
            
            System.out.println("==============================================================================");
            System.out.println("| As arestas incidentes também serao deletadas, tem certeza que quer apagar? |");
            System.out.println("| 1 - Sim                                                                    |");
            System.out.println("| 2 - Nao                                                                    |");
            System.out.println("==============================================================================");
            int opt_del_v2 = teclado.nextInt();

            if(opt_del_v2 == 2){
                sair();
            }
            else{
                boolean res_del = deletar_vertice(vertice_a_ser_del);
                printResultado(res_del);
            }
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.out.println("==================================================================");
            System.out.println("| Vertice nao encontrado                                         |");
            System.out.println("==================================================================");
            sair();
    	}
	}
	
	public static void menuTodosVertices() {
		try {
			Set<Vertice> todosVertices = get_todos_vertices();
	    	for (Vertice vertice : todosVertices)
	    		(new VerticeView()).exibir("Vertice", vertice);
	    	sair();
		} catch (Exception e) {
			System.out.println("==================================================================");
            System.out.println("| Nao há vertices                                                |");
            System.out.println("==================================================================");
		}
	}
	
	public static void menuVerticesAdjacentes()  {
		try {
			clear_terminal();
	    	System.out.println("==================================================================");
	        System.out.println("| Digite o nome (int) do vertice para ver seus adjacentes        |");
	        System.out.println("==================================================================");                       
	        int opt_busca_v = teclado.nextInt();
	        
	        Vertice vertice = new Vertice(opt_busca_v, 0, null, funcao.executar(opt_busca_v));
	        
	        List<Vertice> adjacentes = get_adjacentes(vertice);
	      
	        for (Vertice v : adjacentes) {
	        	 verticeView.exibir("Vertice", v);
	        }
	        
	        sair();
		} catch (Exception e) {
			System.out.println("==================================================================");
            System.out.println("| Nao há vertices adjacentes                                     |");
            System.out.println("==================================================================");
		}
	}
	
	public static void menuBuscarAresta() {
		 clear_terminal();
         System.out.println("==================================================================");
         System.out.println("| Digite o nome (int) do primeiro vertice incidente              |");
         System.out.println("==================================================================");
         int opt_busca_a1 = teclado.nextInt();       

         System.out.println("==================================================================");
         System.out.println("| Digite o nome (int) do segundo vertice incidente               |");
         System.out.println("==================================================================");
         int opt_busca_a2 = teclado.nextInt();
         
         ChaveAresta chaveAresta1 = new ChaveAresta(opt_busca_a1, opt_busca_a2, funcao.executar(opt_busca_a1));
         ChaveAresta chaveAresta2 = new ChaveAresta(opt_busca_a2, opt_busca_a2, funcao.executar(opt_busca_a2));
         Aresta aresta = null;
         try {
         	aresta= get_aresta(chaveAresta1);
         	arestaView.exibir("Aresta Encontrada", aresta);
         	printResultado(true);
         } catch (Exception e) {
         	try {
         		aresta = get_aresta(chaveAresta2);
         		(new ArestaView()).exibir("Aresta Encontrada", aresta);
         		printResultado(true);
         	}
         	catch (Exception e1) {
         		printResultado(false);
         	}
         }
	}
	
	public static void menuInserirAresta() {
		try {
			clear_terminal();
			System.out.println("==================================================================");
			System.out.println("| Digite o nome (int) do primeiro vertice incidente              |");
			System.out.println("==================================================================");
			int opt_busca_a1 = teclado.nextInt();
			
			System.out.println("==================================================================");
			System.out.println("| Digite o nome (int) do segundo vertice incidente               |");
			System.out.println("==================================================================");
			int opt_busca_a2 = teclado.nextInt();
			
			ChaveAresta cha_ins1 = new ChaveAresta(opt_busca_a1,opt_busca_a2, funcao.executar(opt_busca_a1));
			ChaveAresta cha_ins2 = new ChaveAresta(opt_busca_a1,opt_busca_a2, funcao.executar(opt_busca_a2));
         
			System.out.println("==================================================================");
			System.out.println("| Digite o peso (int) da aresta a ser inserida                   |");
			System.out.println("==================================================================");
			int peso_a_insere = teclado.nextInt();

			System.out.println("==================================================================");
			System.out.println("| Digite '1' se direcionada, '0' caso contrario                  |");
			System.out.println("==================================================================");
			teclado.nextLine();
			int dir_a_insere_aux = teclado.nextInt();
			
			boolean dir_a_insere = false;
			if(dir_a_insere_aux == 1)
				dir_a_insere = true;
			else
				dir_a_insere = false;
			
			System.out.println("==================================================================");
			System.out.println("| Digite a descricao (string) da aresta a ser inserida           |");
			System.out.println("==================================================================");
			
			String des_a_insere = teclado.next();
			

			Aresta ar_ins1 = new Aresta(cha_ins1,peso_a_insere,dir_a_insere,des_a_insere);
			Aresta ar_ins2 = new Aresta(cha_ins2,peso_a_insere,dir_a_insere,des_a_insere);
             
             
             
			boolean res_ins_ar1 = inserir_aresta(ar_ins1);
			if ( res_ins_ar1 == true) {
				boolean res_ins_ar2 = inserir_aresta(ar_ins2);
				if (res_ins_ar2 == true) {
					printResultado(true);
             	}
             	else deletar_aresta(cha_ins1);
            } 
            else printResultado(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void menuAtualizarAresta() {
		try {
			clear_terminal();
			System.out.println("==================================================================");
			System.out.println("| Digite o nome (int) do primeiro vertice incidente              |");
			System.out.println("==================================================================");
			int opt_busca_a1 = teclado.nextInt();
        

        
			System.out.println("==================================================================");
			System.out.println("| Digite o nome (int) do segundo vertice incidente               |");
			System.out.println("==================================================================");
			int opt_busca_a2 = teclado.nextInt();
        
			ChaveAresta cha_att1 = new ChaveAresta(opt_busca_a1,opt_busca_a2, funcao.executar(opt_busca_a1));
			ChaveAresta cha_att2 = new ChaveAresta(opt_busca_a1,opt_busca_a2, funcao.executar(opt_busca_a2));
        
			Aresta a_buscada = null;
			try {
				a_buscada = get_aresta(cha_att1);
			} catch (Exception e) {
					try {
						a_buscada = get_aresta(cha_att2);
					} catch (Exception e1) {
        		
					}
			}
        
			if (a_buscada == null)
				printResultado(false);
			else {
				arestaView.exibir("Aresta a ser atualizada", a_buscada);
				System.out.println("==============================================================================");
				System.out.println("| O que voce deseja alterar na aresta?                                       |");
				System.out.println("| 1 - Peso                                                                   |");
				System.out.println("| 2 - Direcionada                                                            |");
				System.out.println("| 3 - Descricao                                                              |");
				System.out.println("==============================================================================");
				int opt_att_a_2 = teclado.nextInt();

				if(opt_att_a_2 == 1){
					System.out.println("==================================================================");
					System.out.println("| Digite o novo peso (int) da aresta a ser atualizada            |");
					System.out.println("==================================================================");
					int novo_peso = teclado.nextInt();
					a_buscada.peso = novo_peso;
				}
            
				if(opt_att_a_2 == 2){
					System.out.println("==================================================================");
					System.out.println("| Digite 'true' se direcionada, 'false' caso contrario           |");
					System.out.println("==================================================================");
					teclado.nextLine();
					String des_a_att = teclado.nextLine();
					a_buscada.direcionada = Boolean.parseBoolean(des_a_att);
				}
				if(opt_att_a_2 == 3){
					System.out.println("==================================================================");
					System.out.println("| Digite a nova descricao (string) da aresta a ser atualizada    |");
					System.out.println("==================================================================");
					String des_a_att = teclado.nextLine();
					a_buscada.descricao = des_a_att;
				}
				
				Aresta a_att1 = new Aresta(a_buscada);
				a_att1.chave = cha_att1;
				
				Aresta a_att2 = new Aresta(a_buscada);
				a_att2.chave = cha_att2;
            
				boolean res_att_v1 = atualizar_aresta(a_att1);
				if ( res_att_v1 == true ) {

					boolean res_att_v2 = atualizar_aresta(a_att2);
					if ( res_att_v2 == true)
						printResultado(true);
					else {
						try {
							deletar_aresta( a_att1.getChave());
							inserir_aresta(a_buscada);
						}
						catch (Exception e) {
							System.out.println("==================================================================");
							System.out.println("| Processo Refeito                                               |");
							System.out.println("==================================================================");
						}
					}
				} else printResultado(false);
			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}

	public static void menuDeletarAresta() {
		clear_terminal();
        System.out.println("==================================================================");
        System.out.println("| Digite o nome (int) do primeiro vertice incidente              |");
        System.out.println("==================================================================");
        int opt_busca_a1 = teclado.nextInt();
        

        
        System.out.println("==================================================================");
        System.out.println("| Digite o nome (int) do segundo vertice incidente               |");
        System.out.println("==================================================================");
        int opt_busca_a2 = teclado.nextInt();
        
        ChaveAresta cha_att1 = new ChaveAresta(opt_busca_a1,opt_busca_a2, funcao.executar(opt_busca_a1));
        ChaveAresta cha_att2 = new ChaveAresta(opt_busca_a1,opt_busca_a2, funcao.executar(opt_busca_a2));
        Aresta a_a_ser_del = null;
        try {
        	a_a_ser_del = get_aresta(cha_att1);
        } catch (Exception e) {
        	try {
        		a_a_ser_del = get_aresta(cha_att2);
        	} catch (Exception e1) {
        		printResultado(false);
        	}
        }
        
        arestaView.exibir("Aresta a ser deletada", a_a_ser_del);
        if ( a_a_ser_del != null ) {
        	System.out.println("==================================================================");
            System.out.println("| Tem certeza que quer apagar?                                   |");
            System.out.println("| 1 - Sim                                                        |");
            System.out.println("| 2 - Nao                                                        |");
            System.out.println("==================================================================");
       
            int opt_del_a3 = teclado.nextInt();

            if(opt_del_a3 == 2)
            	sair();
            else { 
            	try {
            		boolean res1 = deletar_aresta(cha_att1);
            		boolean res2 = deletar_aresta(cha_att2);
            	
            		printResultado(res1 || res2);
            	} catch (Exception e) {
            		printResultado(false);
            	}
            }
            
        }
	}
	
	public static void menuTodasArestas() {
		try {
			Set<Aresta> todasArestas = get_todas_arestas();
	    	for (Aresta aresta : todasArestas)
	    		arestaView.exibir("Aresta", aresta);
	    	sair();
		} catch (Exception e) {
			System.out.println("==================================================================");
	        System.out.println("| Ocorreu um erro interno por favor reinicie o processo          |");
	        System.out.println("==================================================================");
		}
	}
	
	public static void menuArestasIncidentes() {
		try {
			System.out.println("==================================================================");
	        System.out.println("| Digite o nome (int) do vertice                                 |");
	        System.out.println("==================================================================");
	        int opt_busca_v = teclado.nextInt();
	        
	        Vertice vertice = new Vertice(opt_busca_v, 0, null, funcao.executar(opt_busca_v));
	        
	        List<Aresta> arestasIncidentes = get_incidentes(vertice);
	        
	        for (Aresta aresta : arestasIncidentes)
	        	arestaView.exibir("Aresta", aresta);
	        sair();
		} catch (Exception e) {
			System.out.println("==================================================================");
	        System.out.println("| Não há arestas incidentes                                      |");
	        System.out.println("==================================================================");
	        sair();      
		}
	}
	
	public static void menuInvalido() {
		clear_terminal();
		System.out.println("==================================================================");
		System.out.println("| Menu Invalido                                                  |");
		System.out.println("==================================================================");
		sair();
	}
	
	
		
	
	public static void main(String[] args) {
		try {
			inicializarFuncaoDistribuicao(args);
			inicializarConexao(args);
			
			int opt = -1;
			while (opt != 0){
				menuPrincipal();
				opt = teclado.nextInt();
				
				if (opt == 1) { // NO
                    int optNo = -1;
                    while (optNo != 0) {
                       menuNo();
                       optNo = teclado.nextInt();
                       switch ( optNo ) {
                       		case 1 : menuAdicionarNo(); break;
                       		case 2 : menuAdicionarNoIndice(); break;
                       		case 3 : menuRemoverNo(); break;
                       		case 4 : menuRemoverNoIndice(); break;
                       		case 0 : break;
                       		default : menuInvalido();
                       }
                    }
                }
				
                if (opt == 2) { // VERTICES
                    int optVertices = -1;
                    while (optVertices != 0) {
                       menuVertice();
                       optVertices = teclado.nextInt();
                       switch ( optVertices ) {
                       		case 1 : menuBuscarVertice(); break;
                       		case 2 : menuInserirVertice(); break;
                       		case 3 : menuAtualizarVertice(); break;
                       		case 4 : menuDeletarVertice(); break;
                       		case 5 : menuTodosVertices(); break;
                       		case 6 : menuVerticesAdjacentes(); break;
                       		case 0: break;
                       		default : menuInvalido();
                       }
                    }
                }
                
                if (opt == 3) { // ARESTAS
                    int optArestas = -1;
                    while (optArestas != 0) {
                       menuAresta();
                       optArestas = teclado.nextInt();
                       switch ( optArestas ) {
                       		case 1 : menuBuscarAresta(); break;
                       		case 2 : menuInserirAresta(); break;
                       		case 3 : menuAtualizarAresta(); break;
                       		case 4 : menuDeletarAresta(); break;
                       		case 5 : menuTodasArestas(); break;
                       		case 6 : menuArestasIncidentes(); break;
                       		case 0 : break;
                       		default: menuInvalido();
                       }
                    }
                }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

/// ----------------------------------------------------------------------------------------------------

    public static void clear_terminal(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); // cls
    }

/// ----------------------------------------------------------------------------------------------------

    private static boolean inserir_vertice(Vertice vertice,int flag) throws TException {
        boolean resultado = client.inserirVertice(vertice,flag);
        return resultado;
    }

    private static Vertice get_vertice(Vertice vertice) throws TException {
        return client.getVertice(vertice);
    }

    // listar - > escolhe - > altera tudo menos chave se quiser
    private static boolean atualizar_vertice(Vertice vertice) throws TException {
        boolean resultado = client.atualizarVertice(vertice);
        return resultado;
    }

    private static boolean deletar_vertice(Vertice vertice) throws TException {
        boolean resultado = client.deletarVertice(vertice);
        return resultado;
    }
    
    
/// ----------------------------------------------------------------------------------------------------

    private static boolean inserir_aresta(Aresta aresta) throws TException {
        boolean resultado = client.inserirAresta(aresta);
        return resultado;
    }

    private static Aresta get_aresta(ChaveAresta chaveAresta) throws TException {
        Aresta aresta_buscada = client.getAresta(chaveAresta);
        return aresta_buscada;
    }

    // listar - > escolhe - > altera tudo menos chave se quiser
    private static boolean atualizar_aresta(Aresta aresta) throws TException {
        boolean resultado = client.atualizarAresta(aresta);
        return resultado;
    }

    private static boolean deletar_aresta(ChaveAresta chaveAresta) throws TException {
        boolean resultado = client.deletarAresta(chaveAresta);
        return resultado;
    }

    private static Set<Vertice> get_todos_vertices() throws TException {
    	Set<No> nosVisitados = new HashSet<No>();
    	nosVisitados.add(client.get());
        return client.getTodosVertices(nosVisitados);
    }   

    
    private static Set<Aresta> get_todas_arestas() throws TException {
    	Set<No> nosVisitados = new HashSet<No>();
    	nosVisitados.add(client.get());
        return client.getTodasArestas(nosVisitados);
    }

    private static List<Aresta> get_incidentes(Vertice vertice) throws TException {
        return client.getIncidentes(vertice);
    }

    private static List<Vertice> get_adjacentes(Vertice vertice) throws TException {
        return client.getAdjacentes(vertice);
    }
    
    private static void printResultado(boolean resultado) {
    	if(resultado){
        	System.out.println("==================================================================");
            System.out.println("| Sucesso!                                                       |");
            System.out.println("==================================================================");
        }
        else{
        	System.out.println("==================================================================");
            System.out.println("| Falha!                                                         |");
            System.out.println("==================================================================");
        }
    	sair();
    }
    
    private static void sair() {
    	System.out.println("==================================================================");
        System.out.println("| Digite 0 para sair                                             |");
        System.out.println("==================================================================");

        while (true) {
            int opt_sair = teclado.nextInt();
            if(opt_sair == 0){
                break;
            }
        }
    }
    	
    	
}
