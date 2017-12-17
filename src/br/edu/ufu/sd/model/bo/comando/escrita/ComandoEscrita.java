package br.edu.ufu.sd.model.bo.comando.escrita;

import java.util.concurrent.Semaphore;

import br.edu.ufu.sd.model.vo.BancoDados;

public abstract class ComandoEscrita implements Runnable {
	private BancoDados bancoDados;
	private Semaphore bd;
	private boolean resultado;
	
	public ComandoEscrita(BancoDados bancoDeDados) {
		this.bancoDados = bancoDeDados;
		this.bd = this.bancoDados.getBD();
	}
	
	public void run() {
		try {
			this.bd.acquire();
			escrever();
			this.bd.release();
		} catch (InterruptedException e) {
			
		}
	}
	public abstract void escrever();
	
	protected BancoDados getBancoDados() {
		return this.bancoDados;
	}
	
	public boolean getResultado() {
		return this.resultado;
	}
	
	public void setResultado(boolean resultado) {
		this.resultado = resultado;
	}
}
