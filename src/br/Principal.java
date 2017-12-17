package br;

import java.io.File;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

import br.edu.ufu.sd.model.bo.BancoDadosDistribuidoHandler;
import br.edu.ufu.sd.model.vo.BancoDadosDistribuido;
import br.edu.ufu.sd.model.vo.No;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.server.CopycatServer;
import io.atomix.copycat.server.storage.Storage;
import io.atomix.copycat.server.storage.StorageLevel;

public class Principal {
	private static BancoDadosDistribuidoHandler handler;
	
	private static BancoDadosDistribuido.Processor processor;
	
	public static TServer criarThreadedServer(int porta) throws TException {
		int numeroMaximoThreads = 40;
		int numeroMinimoThreads = 40;
		TServerTransport serverTransport = new TServerSocket(porta);
		TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(serverTransport);
	    serverArgs.processor(processor);
	    serverArgs.transportFactory(new TTransportFactory());
	    serverArgs.protocolFactory(new TBinaryProtocol.Factory(true,true));
	    serverArgs.maxWorkerThreads(numeroMaximoThreads);
	    serverArgs.minWorkerThreads(numeroMinimoThreads);
	    TServer server = new TThreadPoolServer(serverArgs);
	    return server;
	}
	
	public static void main(String[] args){
		TServer server;
		try {
			Principal.handler = new BancoDadosDistribuidoHandler(args);
			Principal.processor = new BancoDadosDistribuido.Processor(handler);
			server = criarThreadedServer(handler.get().porta);
			server.serve();			
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
