package br.edu.ufu.sd.model.bo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class FuncaoDistribuicao {
	private int numeroMaximo;
	private MessageDigest messageDigest;
	private byte[] resultadoDigest;
	
	public FuncaoDistribuicao (int numeroBits) {
		this.numeroMaximo = ((int)Math.pow(2,numeroBits));
	}
	
	private int executar(String entrada) throws NoSuchAlgorithmException  {
		this.messageDigest = MessageDigest.getInstance("SHA1");
		this.resultadoDigest = messageDigest.digest(entrada.getBytes());
		StringBuffer stringBuilder = new StringBuffer();
		for (int i = 0 ; i < resultadoDigest.length; i++)
			stringBuilder.append(Integer.toString((resultadoDigest[i] & 0xff) + 0x100, 16).substring(1));

		BigInteger bigInteger = new BigInteger(stringBuilder.toString(), 16);
		return (bigInteger.mod(BigInteger.valueOf(numeroMaximo))).intValue();
	}
	/*
	public int executar(int chave) {
		try {
			return executar(chave+"");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Funcao de Distribuicao Inexistente");
		}
		return 0;
	}
	*/
	
	public int executar(int chave) {
		return (chave % this.numeroMaximo);
	}
}
