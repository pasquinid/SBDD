package br.edu.ufu.sd.model.vo;

import java.util.concurrent.Semaphore;

import br.edu.ufu.sd.model.dao.ConfiguracaoDAO;

import br.edu.ufu.sd.model.dao.ArquivoConfiguracaoDAO;

public class SemaforoFabrica {
	public static final int BINARIO = 0;
	public static final int COMPARTILHADO_EXCLUSIVO = 1;

	private static SemaforoFabrica instancia;
	
	private SemaforoFabrica() {
		
	}
	
	public static synchronized SemaforoFabrica getInstancia() {
		if (instancia == null)
			SemaforoFabrica.instancia = new SemaforoFabrica();
		return instancia;
	}
	
	public Semaphore get(int tipo) {
		if (tipo == 0)
			return new Semaphore(1);
		if (tipo == 1) {
			ConfiguracaoDAO dao = new ArquivoConfiguracaoDAO();
			String numeroMaximoDeClientesString = dao.get("numero.maximo.clientes");
			try {
				int numeroMaximoDeClientes = Integer.parseInt(numeroMaximoDeClientesString);
				return new Semaphore(numeroMaximoDeClientes);
			} catch (NumberFormatException e) {
				return get(0);
			}
		}
		throw new IllegalArgumentException ("Tipo de Semáforo Inválido");
	}
	
	

}
