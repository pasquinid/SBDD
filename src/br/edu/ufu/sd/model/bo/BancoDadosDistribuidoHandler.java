package br.edu.ufu.sd.model.bo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.apache.thrift.TException;

import br.edu.ufu.sd.model.bo.comando.atomix.escrita.ComandoAtomixAtualizarAresta;
import br.edu.ufu.sd.model.bo.comando.atomix.escrita.ComandoAtomixAtualizarVertice;
import br.edu.ufu.sd.model.bo.comando.atomix.escrita.ComandoAtomixDeletarAresta;
import br.edu.ufu.sd.model.bo.comando.atomix.escrita.ComandoAtomixDeletarVertice;
import br.edu.ufu.sd.model.bo.comando.atomix.escrita.ComandoAtomixInserirAresta;
import br.edu.ufu.sd.model.bo.comando.atomix.escrita.ComandoAtomixInserirVertice;
import br.edu.ufu.sd.model.bo.comando.atomix.leitura.ComandoAtomixGetAresta;
import br.edu.ufu.sd.model.bo.comando.atomix.leitura.ComandoAtomixGetTodasArestas;
import br.edu.ufu.sd.model.bo.comando.atomix.leitura.ComandoAtomixGetTodosVertices;
import br.edu.ufu.sd.model.bo.comando.atomix.leitura.ComandoAtomixGetVertice;
import br.edu.ufu.sd.model.bo.comando.leitura.ComandoGetTodosVertices;
import br.edu.ufu.sd.model.vo.Aresta;
import br.edu.ufu.sd.model.vo.ArestaNaoEncontrada;
import br.edu.ufu.sd.model.vo.BancoDados;
import br.edu.ufu.sd.model.vo.BancoDadosDistribuido;
import br.edu.ufu.sd.model.vo.ChaveAresta;
import br.edu.ufu.sd.model.vo.NaoExisteArestas;
import br.edu.ufu.sd.model.vo.NaoExisteVertices;
import br.edu.ufu.sd.model.vo.No;
import br.edu.ufu.sd.model.vo.NoNaoEncontrado;
import br.edu.ufu.sd.model.vo.Vertice;
import br.edu.ufu.sd.model.vo.VerticeNaoEncontrado;
import br.edu.ufu.sd.view.ArestaView;
import br.edu.ufu.sd.view.NoView;
import br.edu.ufu.sd.view.ReplicasView;
import br.edu.ufu.sd.view.TabelaRoteamentoView;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.server.StateMachine;

public class BancoDadosDistribuidoHandler extends StateMachine implements BancoDadosDistribuido.Iface {
	private No n;
	private int m;
	private List<No> tabela;
	private BancoDados bancoDados = new BancoDados();
	private FuncaoDistribuicao funcao;
	private List<Address> replicas = new ArrayList<Address>();
	private CopycatClient client;

	private NoView noView = new NoView();
	private TabelaRoteamentoView tabelaRoteamentoView = new TabelaRoteamentoView();
	private ReplicasView replicasView = new ReplicasView();
	private ArestaView arestaView = new ArestaView();
	
	public void inicializaNoAtual(String[] args) {
		this.m = Integer.parseInt(args[0]);
		this.funcao = new FuncaoDistribuicao(this.m);
		int identificador = Integer.parseInt(args[1]);
		String endereco = args[2];
		int porta = Integer.parseInt(args[3]);
		this.n = new No(identificador, endereco, porta);
	}
	
	public void inicializaTabelaRoteamento(String[] args) {
		this.tabela = new ArrayList<No>();
		if (args.length >= 4) {
			int i = 4;
			int tamanho = (this.m * 3)+4;
			while (i < tamanho) {
				int identificador = Integer.parseInt(args[i++]);
				String endereco = args[i++];
				int porta = Integer.parseInt(args[i++]);
				No no = new No (identificador, endereco, porta);
				tabela.add(no);
			}
		}
	}
	
	public void inicializaReplicas(String [] args) {
		int i = args.length-1;
		Address replica;
		
		replica = new Address(args[i-1], Integer.parseInt(args[i]));
		replicas.add(replica);
		i-=2;
		
		replica = new Address(args[i-1], Integer.parseInt(args[i]));
		replicas.add(replica);
		i-=2;
		
		replica = new Address(args[i-1], Integer.parseInt(args[i]));
		replicas.add(replica);
		i-=2;
	}
	
	public void inicializaCopycatClient() {
		CopycatClient.Builder builder = CopycatClient.builder()
                .withTransport( NettyTransport.builder()
                .withThreads(40)
                .build());
		this.client = builder.build();
	}
	
	public BancoDadosDistribuidoHandler(String[] args) {
		inicializaNoAtual(args);
		inicializaTabelaRoteamento(args);
		inicializaReplicas(args);
		inicializaCopycatClient();
		this.noView.exibir(n);
		this.tabelaRoteamentoView.exibir(tabela);
		this.replicasView.exibir(replicas);
	}
	
	private boolean getMenorAnel(int x, int y){
		if((x<y && Math.abs(x-y)<16) || (x>y && Math.abs(x-y)>16)){
			return true;
		}
		return false;
	}

	public No pesquisar(int k) {

		int i = 0;

		for ( i  = 0; i < tabela.size() && !getMenorAnel(k,tabela.get(i).getIdentificador()); i++);

		if (i == 0){
			if(this.n.getIdentificador() < k || (k < tabela.get(0).getIdentificador() && tabela.get(0).getIdentificador() < this.n.getIdentificador())){
				return tabela.get(0);
			}
			else{
				return null;
			}
		}
		else if(i == tabela.size()) {
			if(this.n.getIdentificador() < k){
				return null;
			}
			else{
				tabela.get(i-1);
			}
		}

		return tabela.get(i-1);
	}
	
	@Override
	public boolean inserirVertice(Vertice vertice, int flag) throws TException {
		No noResponsavel = pesquisar(vertice.getHash());
		if(flag == 1){
			noResponsavel = null;
		}
		if ( noResponsavel == null ) {
			CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
			try {
				CompletableFuture<CopycatClient> CopycatClientFuture = client.connect(replicas);
				CopycatClientFuture.join();
				future = client.submit(new ComandoAtomixInserirVertice(vertice));
				return future.get();
			} catch (Exception e) {
				System.out.println("Erro ao inserir nas replicas");
				e.printStackTrace();
				
			}
			return false;
		}
		else {
			ConexaoNoFabrica fabrica = ConexaoNoFabrica.getInstancia();
			BancoDadosDistribuido.Client client = fabrica.get(noResponsavel);
			if(noResponsavel.getIdentificador() < this.n.getIdentificador() && (vertice.nome > this.n.getIdentificador() || vertice.nome < noResponsavel.getIdentificador())) {
				return client.inserirVertice(vertice, 1);
			}
			return client.inserirVertice(vertice, 0);
		}
	}

	@Override
	public Vertice getVertice(Vertice vertice) throws TException {
		No noResponsavel = pesquisar(vertice.getHash());
		if ( noResponsavel == null ) {
			try {
				CompletableFuture<CopycatClient> CopycatClientFuture = client.connect(replicas);
				CopycatClientFuture.join();
				Vertice resultado = client.submit(new ComandoAtomixGetVertice(vertice.getNome())).get();
				if ( vertice == null )
					throw new VerticeNaoEncontrado();
				return resultado;
			} catch (Exception e) {
				System.out.println("Houve um pequeno problema");
			}
			throw new VerticeNaoEncontrado();
		}
		else {
			ConexaoNoFabrica fabrica = ConexaoNoFabrica.getInstancia();
			BancoDadosDistribuido.Client client = fabrica.get(noResponsavel);
			return client.getVertice(vertice);
		}
	}

	@Override
	public boolean atualizarVertice(Vertice vertice) throws TException {
		No noResponsavel = pesquisar(vertice.getHash());
		
		if ( noResponsavel == null ) {
			CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
			try {
				CompletableFuture<CopycatClient> CopycatClientFuture = client.connect(replicas);
				CopycatClientFuture.join();
				future = client.submit(new ComandoAtomixAtualizarVertice(vertice));	
				return future.get();
			} catch (Exception e) {
				System.out.println("Erro ao atualizar vertice nas replicas");
				e.printStackTrace();
			}
			return false;
		} else {
			ConexaoNoFabrica fabrica = ConexaoNoFabrica.getInstancia();
			BancoDadosDistribuido.Client client = fabrica.get(noResponsavel);
			return client.atualizarVertice(vertice);
		}
		
	}
	
	private void deletandoIncidentes(List<Aresta> incidentes) throws TException {
		for (Aresta aresta: incidentes) {
			try {
				ChaveAresta chave1 = new ChaveAresta(aresta.getChave());
				chave1.setHash(funcao.executar(chave1.getVertice1()));
				
				ChaveAresta chave2 = new ChaveAresta(aresta.getChave());
				chave2.setHash(funcao.executar(chave2.getVertice2()));
				
				deletarAresta(chave1);
				deletarAresta(chave2);
			} catch (Exception e) {
				
			}
		}
	}

	@Override
	public boolean deletarVertice(Vertice vertice) throws TException {
		No noResponsavel = pesquisar(vertice.getHash());
		
		
		if ( noResponsavel == null ) {
			boolean resultado = false;
			
			// Enviando a requisicao para outros nos no chord
			try {
				List<Aresta> incidentes = getIncidentes(vertice);
				deletandoIncidentes(incidentes);
			} catch (Exception e) { // Nao existe arestas incidentes
				
			}
			
			
			// Executando localmente a delecao
			CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
			try {
				CompletableFuture<CopycatClient> CopycatClientFuture = client.connect(replicas);
				CopycatClientFuture.join();
				future = client.submit(new ComandoAtomixDeletarVertice(vertice.getNome()));	
				return future.get();
			} catch (Exception e) {
				System.out.println("Erro ao remover o vertice nas replicas");
				e.printStackTrace();	
			}
			return false;
		} else {
			ConexaoNoFabrica fabrica = ConexaoNoFabrica.getInstancia();
			BancoDadosDistribuido.Client client = fabrica.get(noResponsavel);
			return client.deletarVertice(vertice);
		}
	}

	@Override
	public boolean inserirAresta(Aresta aresta) throws TException {
		No noResponsavel = pesquisar(aresta.getChave().getHash());

		if ( noResponsavel == null ) {
			CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
			try {
				CompletableFuture<CopycatClient> CopycatClientFuture = client.connect(replicas);
				CopycatClientFuture.join();
				future = client.submit(new ComandoAtomixInserirAresta(aresta));	
				return future.get();
			} catch (Exception e) {
				System.out.println("Erro ao inserir aresta nas replicas");
				e.printStackTrace();
			}
			return false;
		}
		else {
			ConexaoNoFabrica fabrica = ConexaoNoFabrica.getInstancia();
			BancoDadosDistribuido.Client client = fabrica.get(noResponsavel);
			return client.inserirAresta(aresta);
		}	
	}
	
	public Aresta getAresta(ChaveAresta chaveAresta) throws TException {
		No noResponsavel = pesquisar(chaveAresta.getHash());
		
		if ( noResponsavel == null ) {
			try {
				CompletableFuture<CopycatClient> CopycatClientFuture = client.connect(replicas);
				CopycatClientFuture.join();
				Aresta resultado = client.submit(new ComandoAtomixGetAresta(chaveAresta)).get();
				if ( resultado == null )
					throw new ArestaNaoEncontrada();
				return resultado;
			} catch (Exception e) {
				
			}
			throw new ArestaNaoEncontrada();
		} else {
			ConexaoNoFabrica fabrica = ConexaoNoFabrica.getInstancia();
			BancoDadosDistribuido.Client client = fabrica.get(noResponsavel);
			return client.getAresta(chaveAresta);
		}
	}
	
	@Override
	public boolean atualizarAresta(Aresta aresta) throws TException {
		No noResponsavel = pesquisar(aresta.getChave().getHash());
		
		if ( noResponsavel == null ) {
			CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
			try {
				CompletableFuture<CopycatClient> CopycatClientFuture = client.connect(replicas);
				CopycatClientFuture.join();
				future = client.submit(new ComandoAtomixAtualizarAresta(aresta));	
				return future.get();
			} catch (Exception e) {
				System.out.println("Erro ao atualizar aresta nas replicas");
				e.printStackTrace();
			}
			return false;
		} else {
			ConexaoNoFabrica fabrica = ConexaoNoFabrica.getInstancia();
			BancoDadosDistribuido.Client client = fabrica.get(noResponsavel);
			return client.atualizarAresta(aresta);
		}
	}

	public boolean deletarAresta(ChaveAresta chaveAresta) throws TException {
		No noResponsavel = pesquisar(chaveAresta.getHash());
		
		if ( noResponsavel == null ) {
			CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
			try {
				CompletableFuture<CopycatClient> CopycatClientFuture = client.connect(replicas);
				CopycatClientFuture.join();
				future = client.submit(new ComandoAtomixDeletarAresta(chaveAresta));	
				return future.get();
			} catch (Exception e) {
				System.out.println("Erro ao inserir nas replicas");
				e.printStackTrace();
				
			}
			return false;
		} else {
			ConexaoNoFabrica fabrica = ConexaoNoFabrica.getInstancia();
			BancoDadosDistribuido.Client client = fabrica.get(noResponsavel);
			return client.deletarAresta(chaveAresta);
		}
	}

	
	public List<Vertice> getTodosVerticesAux() throws TException {
		ComandoGetTodosVertices comando = null;
		List<Vertice> lista;
		Thread thread = null;
		try {
			comando = new ComandoGetTodosVertices(this.bancoDados);
			thread = new Thread(comando);
			thread.start();
			thread.join();
			lista = (List<Vertice>) comando.getResposta();
			if (lista.isEmpty())
				throw new NaoExisteVertices();
			return lista;
		} catch (InterruptedException e) {
			
		}
		throw new NaoExisteVertices();
	}
	
	@Override
	public Set<Vertice> getTodosVertices(Set<No> nosVisitados) throws TException {
		//Execucao Local
		Set<Vertice> conjunto = null;
		try {
			CompletableFuture<CopycatClient> CopycatClientFuture = client.connect(replicas);
			CopycatClientFuture.join();
			conjunto = client.submit(new ComandoAtomixGetTodosVertices()).get();
		} catch (Exception e) {
			conjunto = new HashSet<Vertice>();
		}
		
		// Pegando os outros nos do circuito chord
		nosVisitados.add(this.n);
			
		List<No> nosASeremPesquisados = new ArrayList<No>();
		for (No noTabela : tabela) {
			if (!nosVisitados.contains(noTabela)){
				nosASeremPesquisados.add(noTabela);		
			}
			nosVisitados.add(noTabela);
		}
			
		for (No no : nosASeremPesquisados) {
			try {
				ConexaoNoFabrica fabrica = ConexaoNoFabrica.getInstancia();
				BancoDadosDistribuido.Client client = fabrica.get(no);
				conjunto.addAll(client.getTodosVertices(nosVisitados));
			} catch (Exception e) {
	
			}
		}
		return conjunto;
	}

	private boolean contem(Set<Aresta> conjunto, Aresta aresta) {
		for (Aresta a : conjunto) {
			int vertice1 = a.getChave().getVertice1();
			int vertice2 = a.getChave().getVertice2();
			
			if (vertice1 == aresta.getChave().getVertice1() && vertice2 == aresta.getChave().getVertice2())
				return true;
		}
		return false;
	}
	
	@Override
	public Set<Aresta> getTodasArestas(Set<No> nosVisitados) throws TException {
		//Execucao Local
		Set<Aresta> conjunto = null;
		try {
			CompletableFuture<CopycatClient> CopycatClientFuture = client.connect(replicas);
			CopycatClientFuture.join();
			conjunto = client.submit(new ComandoAtomixGetTodasArestas()).get();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		System.out.println("AQUI1");
		
		// Pegando os outros nos do circuito chord
		nosVisitados.add(this.n);
			
		List<No> nosASeremPesquisados = new ArrayList<No>();
		for (No noTabela : tabela) {
			if (!nosVisitados.contains(noTabela)){
				nosASeremPesquisados.add(noTabela);
			}
			nosVisitados.add(noTabela);
		}
		
		System.out.println("AQUI2");
		for (No no : nosASeremPesquisados) {
			try {
				ConexaoNoFabrica fabrica = ConexaoNoFabrica.getInstancia();
				BancoDadosDistribuido.Client client = fabrica.get(no);
				Set<Aresta> conjuntoRetornado = client.getTodasArestas(nosVisitados);
				
				for (Aresta a : conjuntoRetornado)
					if (!contem(conjunto, a))
						conjunto.add(a);
				} catch (Exception e) {
	
				}
		}
		System.out.println("AQUI3");
		return conjunto;
	}
	
	@Override
	public List<Aresta> getIncidentes(Vertice vertice) throws TException {
		No noResponsavel = pesquisar(vertice.getHash());
		
		if ( noResponsavel == null) {
			List<Aresta> incidentes = null;
			Set<Aresta> todasArestas = null;
			
			try {
				CompletableFuture<CopycatClient> CopycatClientFuture = client.connect(replicas);
				CopycatClientFuture.join();
				todasArestas = client.submit(new ComandoAtomixGetTodasArestas()).get();
			} catch (Exception e) {
					
			}
			
			incidentes = new ArrayList<Aresta>();
			
			for (Aresta a : todasArestas) {
				ChaveAresta chave = a.getChave();
				if (chave.getVertice1() == vertice.getNome() || chave.getVertice2() == vertice.getNome())
					incidentes.add(a);
			}
			
			if (incidentes.isEmpty())
				throw new NaoExisteArestas();
			
			return incidentes;
		}
		else {
			ConexaoNoFabrica fabrica = ConexaoNoFabrica.getInstancia();
			BancoDadosDistribuido.Client client = fabrica.get(noResponsavel);
			return client.getIncidentes(vertice);
		}
		
	}

	@Override
	public List<Vertice> getAdjacentes(Vertice vertice) throws TException {
		List<Aresta> arestaIncidentes = getIncidentes(vertice);
		
		List<Vertice> adjacentes = new ArrayList<Vertice>();
		
		for (Aresta aresta : arestaIncidentes) {
			ChaveAresta chave = aresta.getChave();
		
			Vertice verticeAdjacente = null;
			
			try {
				CompletableFuture<CopycatClient> CopycatClientFuture = client.connect(replicas);
				CopycatClientFuture.join();
				
				if (chave.getVertice1() == vertice.getNome() ) {
					verticeAdjacente = client.submit(new ComandoAtomixGetVertice(chave.getVertice2())).get();				
				}
				else {
					verticeAdjacente = client.submit(new ComandoAtomixGetVertice(chave.getVertice1())).get();
				}
			} catch (Exception e) {
				
			}
			if (verticeAdjacente != null )
				adjacentes.add(getVertice(verticeAdjacente));
		}
		
		if ( adjacentes.isEmpty() )
			throw new NaoExisteVertices();
		return adjacentes;
		
	}
	
	@Override
	public boolean adicionarNo(No no) throws TException {
		try {
			// Teste de existencia
			ConexaoNoFabrica fabrica = ConexaoNoFabrica.getInstancia();
			BancoDadosDistribuido.Client cliente = fabrica.get(no);
			
			tabela.add(no);
			return true;

		} catch (Exception e) {
			throw new NoNaoEncontrado();
		}
	}

	@Override
	public boolean adicionarNoIndice(int indice, No no) throws TException {
		try {
			// Teste de existencia
			ConexaoNoFabrica fabrica = ConexaoNoFabrica.getInstancia();
			BancoDadosDistribuido.Client cliente = fabrica.get(no);
			tabela.add(indice, no);
			return true;
		} catch (Exception e) {
			throw new NoNaoEncontrado();
		}
	}

	@Override
	public boolean removerNo(No no) throws TException {
		return tabela.remove(no);
	}

	@Override
	public boolean removerNoIndice(int indice) throws TException {
		if (tabela.size() > indice) {
			tabela.remove(indice);
			return true;
		}
		throw new NoNaoEncontrado();
	}

	@Override
	public No get() throws TException {
		if ( this.n == null )
			throw new NoNaoEncontrado();
		return this.n;
	}
}
