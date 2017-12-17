package br.edu.ufu.sd.model.dao;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;

public class ArquivoConfiguracaoDAO implements ConfiguracaoDAO {
	private String caminho = "conf/sbdd.conf";
	private Properties propriedades;
	
	public ArquivoConfiguracaoDAO(){
		this.propriedades = new Properties();
		try {
			FileInputStream arquivo = new FileInputStream(caminho);
			propriedades.load(arquivo);
		} catch (IOException e){
			this.propriedades.setProperty("cliente.endereco", "localhost");
			this.propriedades.setProperty("cliente.porta", "10051");
			this.propriedades.setProperty("servidor.endereco", "localhost");
			this.propriedades.setProperty("servidor.porta", "10050");
			this.propriedades.setProperty("servidor.maximo.threads", "5");
			this.propriedades.setProperty("servidor.minimo.threads", "3");
			this.propriedades.setProperty("servidor.bits.identificador", "5");
			this.propriedades.setProperty("servidor.tempo.espera.resposta", "3000");
			this.propriedades.setProperty("servidor.numero.maximo.sites", "5");
			this.propriedades.setProperty("servidor.coordenador", "false");
			this.propriedades.setProperty("servidor.endereco.coordenador", "localhost");
			this.propriedades.setProperty("servidor.porta.coordenador", "20050");
		}
		
	}
	
	public void salvar(){
		try {
			FileOutputStream arquivo = new FileOutputStream(caminho);
			this.propriedades.store(arquivo,"");
		} catch (IOException e){
			System.out.println ("Arquivo e/ou diretório de configuração não encontrado");
		}
	}
	
	public String get(String chave){
		return this.propriedades.getProperty(chave);
	}
	
	public void atualiza(String chave, String valor){
		this.propriedades.setProperty(chave, valor);
		this.salvar();
	}
	
	public void remover(String chave){
		this.propriedades.remove(chave);
		this.salvar();
	}
	
	public void inserir(String chave, String valor){
		this.propriedades.setProperty(chave, valor);
		this.salvar();
	}
}

