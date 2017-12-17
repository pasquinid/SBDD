package br.edu.ufu.sd.view;

import io.atomix.catalyst.transport.Address;

public class ReplicaView {
	public void exibir(Address endereco) {
		System.out.println ("===========================================");
		System.out.println ("  Replica ");
		System.out.println ("===========================================");
		try {
			System.out.println ("  Endereco: " + endereco.host());
			System.out.println ("  Porta   : " + endereco.port());
		} catch (NullPointerException e) {
			System.out.println ("  Endereco: null");
			System.out.println ("  Porta   : null");
		}
	}
}
