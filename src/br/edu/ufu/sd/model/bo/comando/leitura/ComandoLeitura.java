package br.edu.ufu.sd.model.bo.comando.leitura;

import java.util.concurrent.Semaphore;

import br.edu.ufu.sd.model.vo.BancoDados;

public abstract class ComandoLeitura implements Runnable {
	private BancoDados bancoDados;
	private Semaphore bd;
	private Semaphore mutex;
	private Object resposta;
	
	public ComandoLeitura(BancoDados bancoDeDados) {
		this.bancoDados = bancoDeDados;
		this.bd = this.bancoDados.getBD();
		this.mutex = this.bancoDados.getMutex();
	}
	
	public void run() {
		try {
			this.mutex.acquire();
			
			this.bancoDados.setNumeroLeitores(this.bancoDados.getNumeroLeitores()+1);
			
			if (this.bancoDados.getNumeroLeitores() == 1)
				this.bd.acquire();
			
			this.mutex.release();
			
			ler();
			
			this.mutex.acquire();
			this.bancoDados.setNumeroLeitores(this.bancoDados.getNumeroLeitores()-1);
			
			if (this.bancoDados.getNumeroLeitores() == 0)
				this.bd.release();
			
			this.mutex.release();
			
		} catch (InterruptedException e) {
			
		}
	}
	
	public abstract void ler();
	
	protected BancoDados getBancoDados() {
		return this.bancoDados;
	}
	
	public Object getResposta() {
		return this.resposta;
	}
	
	public void setResposta(Object resposta) {
		this.resposta = resposta;
	}
	
}
