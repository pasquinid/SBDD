package br.edu.ufu.sd.view;

import java.util.List;

import io.atomix.catalyst.transport.Address;

public class ReplicasView {
	public void exibir(List<Address> replicas) {
		System.out.println ("===========================================");
		System.out.println ("  Replicas ");
		System.out.println ("===========================================");
		
		if ( replicas != null ) {
			for (int i = 0; i < replicas.size(); i++) {
				System.out.println ("  Endereco: " + replicas.get(i).host());
				System.out.println ("  Porta   : " + replicas.get(i).port());
				System.out.println ();
			}
		} else {
			System.out.println ("  Endereco: null");
			System.out.println ("  Porta   : null");
			System.out.println();
		}
		
	}
}
