package br.com.imovelhunterweb.util;

import java.io.File;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import br.com.imovelhunterweb.entitys.Anunciante;

public class EnderecoImagens implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 5281410381151588527L;
	private String caminhoServidor;
	private String caminhoTemp;
	private String caminhoServidorRedz;
	private String caminhoTempRedz;
	
	
	public EnderecoImagens(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
		this.caminhoServidor = scontext.getRealPath("") +"/servidor/imagens/";
		Anunciante anunciante = (Anunciante) UtilSession.getHttpSessionObject("anuncianteLogado");
		this.caminhoTemp = scontext.getRealPath("") + "/uploads"+anunciante.getIdAnunciante()+"/imagens/";
		this.caminhoServidorRedz = "/servidor/imagens/";
		this.caminhoTempRedz = "/uploads"+anunciante.getIdAnunciante()+"/imagens/";
	}
	
	public void excluirImagensServidor(String nome){
		File f = new File(caminhoServidor + nome);
		f.delete();
	}
	
	

	public String getCaminhoServidor() {
		return caminhoServidor;
	}


	public String getCaminhoTemp() {
		return caminhoTemp;
	}
	
	public String getCaminhoServidorRedz() {
		return caminhoServidorRedz;
	}

	public String getCaminhoTempRedz() {
		return caminhoTempRedz;
	}	
	
	
}
