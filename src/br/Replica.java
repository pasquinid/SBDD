package br;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

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
import br.edu.ufu.sd.model.bo.comando.escrita.ComandoAtualizarAresta;
import br.edu.ufu.sd.model.bo.comando.escrita.ComandoAtualizarVertice;
import br.edu.ufu.sd.model.bo.comando.escrita.ComandoDeletarAresta;
import br.edu.ufu.sd.model.bo.comando.escrita.ComandoDeletarVertice;
import br.edu.ufu.sd.model.bo.comando.escrita.ComandoInserirAresta;
import br.edu.ufu.sd.model.bo.comando.escrita.ComandoInserirVertice;
import br.edu.ufu.sd.model.bo.comando.leitura.ComandoGetAresta;
import br.edu.ufu.sd.model.bo.comando.leitura.ComandoGetTodasArestas;
import br.edu.ufu.sd.model.bo.comando.leitura.ComandoGetTodosVertices;
import br.edu.ufu.sd.model.bo.comando.leitura.ComandoGetVertice;
import br.edu.ufu.sd.model.vo.Aresta;
import br.edu.ufu.sd.model.vo.ArestaNaoEncontrada;
import br.edu.ufu.sd.model.vo.BancoDados;
import br.edu.ufu.sd.model.vo.ChaveAresta;
import br.edu.ufu.sd.model.vo.NaoExisteArestas;
import br.edu.ufu.sd.model.vo.NaoExisteVertices;
import br.edu.ufu.sd.model.vo.Vertice;
import br.edu.ufu.sd.model.vo.VerticeNaoEncontrado;
import br.edu.ufu.sd.view.ArestaView;
import br.edu.ufu.sd.view.ReplicaView;
import br.edu.ufu.sd.view.ReplicasView;
import br.edu.ufu.sd.view.VerticeView;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.server.Commit;
import io.atomix.copycat.server.CopycatServer;
import io.atomix.copycat.server.StateMachine;
import io.atomix.copycat.server.storage.Storage;
import io.atomix.copycat.server.storage.StorageLevel;

public class Replica extends StateMachine {
	private static BancoDados bancoDados = new BancoDados();
	private static List<Address> replicas = new ArrayList<Address>();
	private static int id;
	private static CopycatServer server;
	private static ReplicaView replicaView = new ReplicaView();
	
	
	private static VerticeView verticeView = new VerticeView();
	private static ArestaView arestaView = new ArestaView();
	
	public static void inicializaReplicas(String[] args) {
		id = Integer.parseInt(args[0]);
		for(int i = 1; i < args.length; i+=2) {
            Address address = new Address(args[i], Integer.parseInt(args[i+1]));
            replicas.add(address);
    	}
	}
	
	public static void inicializaCopycatServer() {
		server = CopycatServer.builder(replicas.get(id))
                .withStateMachine(Replica::new)
                .withTransport( NettyTransport.builder()
                                .withThreads(40)
                                .build())
                .withStorage( Storage.builder()
                              .withDirectory(new File("logs" + "_" + id + "_" + replicas.get(id).host() + "_" + replicas.get(id).port() )) //Must be unique
                              .withStorageLevel(StorageLevel.DISK)
                              .build()).build();
	}
	
	public Boolean inserirVertice(Commit<ComandoAtomixInserirVertice> commit) throws TException {
		boolean resultado = false;
		Vertice vertice = commit.operation().getVertice();
		try {
			ComandoInserirVertice comando =  new ComandoInserirVertice(bancoDados, vertice);
			Thread thread = new Thread(comando);
			thread.start();
			thread.join();
			resultado = comando.getResultado();
		} catch (InterruptedException e) {
			
		} finally {
			commit.close();
		}
		if (resultado == true)
			verticeView.exibir("Vertice Inserido", vertice);
		return resultado;
	}

	public Vertice getVertice(Commit<ComandoAtomixGetVertice> commit) throws TException {
		Vertice vertice = null;
		ComandoGetVertice comando = null;
		Thread thread = null;
		try {
			int nome = commit.operation().getNome();
			comando = new ComandoGetVertice(bancoDados, nome);
			thread = new Thread(comando);
			thread.start();
			thread.join();
			vertice = (Vertice) comando.getResposta();
			if ( vertice == null )
				throw new VerticeNaoEncontrado();
			return vertice;
		} catch (InterruptedException e) {
			System.out.println("Houve um pequeno problema");
		} finally {
			commit.close();
		}
		throw new VerticeNaoEncontrado();
	}
	
	public boolean atualizarVertice(Commit<ComandoAtomixAtualizarVertice> commit) throws TException {
		boolean resultado = false;
		Vertice vertice = commit.operation().getVertice();
		try {
			ComandoAtualizarVertice comando = new ComandoAtualizarVertice(bancoDados, vertice);
			Thread thread = new Thread(comando);
			thread.start();	
			thread.join();
			resultado = comando.getResultado();
		} catch (InterruptedException e) {
				
		} finally {
			commit.close();
		}
		if (resultado == true)
			verticeView.exibir("Vertice Atualizado", vertice);
		return resultado;
	}
	
	public boolean deletarVertice(Commit<ComandoAtomixDeletarVertice> commit) throws TException {
		boolean resultado = false;
		int nome = commit.operation().getNome();
		ComandoDeletarVertice comando = new ComandoDeletarVertice(bancoDados, nome);
		Vertice vertice = null;
		try {
			Thread thread = new Thread(comando);
			thread.start();
			thread.join();
			resultado = comando.getResultado();
			vertice = comando.getVertice();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			commit.close();
		}
		
		if (resultado == true)
			verticeView.exibir("Vertice Deletado", vertice);
		return resultado;
	}
	
	public Boolean inserirAresta(Commit<ComandoAtomixInserirAresta> commit) throws TException {
		boolean resultado = false;
		Aresta aresta = commit.operation().getAresta();
		try {
			ComandoInserirAresta comando =  new ComandoInserirAresta(bancoDados, aresta);
			Thread thread = new Thread(comando);
			thread.start();
			thread.join();
			resultado = comando.getResultado();
		} catch (InterruptedException e) {
				
		} finally {
			commit.close();
		}
		if (resultado == true)
			arestaView.exibir("Aresta Inserida", aresta);
		return resultado;
	}
	
	public Aresta getAresta(Commit<ComandoAtomixGetAresta> commit) throws TException {
		ComandoGetAresta comando = null;
		Thread thread = null;
		try {
			ChaveAresta chaveAresta = commit.operation().getChaveAresta();
			comando = new ComandoGetAresta(bancoDados, chaveAresta);
			thread = new Thread(comando);
			thread.start();
			thread.join();
			Aresta aresta = (Aresta) comando.getResposta();
			if (aresta == null)
				throw new ArestaNaoEncontrada();
			else return aresta;			
		} catch (Exception e) {
				
		} finally {
			commit.close();
		}
		throw new ArestaNaoEncontrada();
	}
	
	public boolean atualizarAresta(Commit<ComandoAtomixAtualizarAresta> commit) throws TException {
		boolean resultado = false;
		Aresta aresta = commit.operation().getAresta();
		try {
			ComandoAtualizarAresta comando = new ComandoAtualizarAresta(bancoDados, aresta);
			Thread thread = new Thread(comando);
			thread.start();
			thread.join();
			resultado = comando.getResultado();
		} catch (InterruptedException e) {
				
		} finally {
			commit.close();
		}
		
		if (resultado == true)
			arestaView.exibir("Aresta Atualizada", aresta);
		
		return resultado;
	}

	public boolean deletarAresta(Commit<ComandoAtomixDeletarAresta> commit) throws TException {
		boolean resultado = false;
		ChaveAresta chaveAresta = commit.operation().getChaveAresta();
		ComandoDeletarAresta comando = new ComandoDeletarAresta(bancoDados, chaveAresta);
		Aresta aresta = null;
		try {
			Thread thread = new Thread(comando);
			thread.start();
			thread.join();
			resultado = comando.getResultado();
			aresta = comando.getAresta();
		} catch (InterruptedException e) {
				
		} finally {
			commit.close();
		}
		
		if (resultado == true)
			arestaView.exibir("Aresta Deletada", aresta);
		
		return resultado;
	}
	
	public Set<Vertice> getTodosVertices(Commit<ComandoAtomixGetTodosVertices> commit) throws TException {
		try {
			ComandoGetTodosVertices comando = new ComandoGetTodosVertices(bancoDados);
			Thread thread = new Thread(comando);
			thread.start();
			thread.join();
			Set<Vertice> conjunto = (Set<Vertice>) comando.getResposta();
			if ( conjunto.isEmpty() )
				throw new NaoExisteVertices();
			return conjunto;
		} catch (InterruptedException e) {
			
		} finally {
			commit.close();
		}
		throw new NaoExisteVertices();
	}

	public Set<Aresta> getTodasArestas(Commit<ComandoAtomixGetTodasArestas> commit) throws TException {
		try {
			ComandoGetTodasArestas comando = new ComandoGetTodasArestas(bancoDados);
			Thread thread = new Thread(comando);
			thread.start();
			thread.join();
			Set<Aresta> conjunto = (Set<Aresta>) comando.getResposta();
			if ( conjunto.isEmpty() )
				throw new NaoExisteArestas();
			
			return conjunto;
		} catch (InterruptedException e) {

		} finally {
			commit.close();
		}
		throw new NaoExisteArestas();
	}

	public static void main(String[] args) {
		inicializaReplicas(args);
		inicializaCopycatServer();
		replicaView.exibir(replicas.get(id));
		if ( id == 0 )
			server.bootstrap().join();
		else server.join(replicas).join();
	}
}
