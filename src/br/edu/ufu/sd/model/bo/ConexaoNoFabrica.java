package br.edu.ufu.sd.model.bo;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import br.edu.ufu.sd.model.vo.BancoDadosDistribuido;
import br.edu.ufu.sd.model.vo.No;

public class ConexaoNoFabrica {
	private static ConexaoNoFabrica instancia = null;
	
	private ConexaoNoFabrica() {}
	
	public synchronized static ConexaoNoFabrica getInstancia() {
		if ( instancia == null )
			instancia = new ConexaoNoFabrica();
		return instancia;
	}
	
	public BancoDadosDistribuido.Client get(No no) throws TTransportException{
		TTransport transport = new TSocket(no.getEndereco(), no.getPorta());
		transport.open();
		TProtocol protocol = new TBinaryProtocol(transport);
		BancoDadosDistribuido.Client client = new BancoDadosDistribuido.Client(protocol);
		return client;
	}
	
	public BancoDadosDistribuido.Client get(String endereco, int porta) throws TTransportException{
		TTransport transport = new TSocket(endereco,porta);
		transport.open();
		TProtocol protocol = new TBinaryProtocol(transport);
		BancoDadosDistribuido.Client client = new BancoDadosDistribuido.Client(protocol);
		return client;
	}
}
