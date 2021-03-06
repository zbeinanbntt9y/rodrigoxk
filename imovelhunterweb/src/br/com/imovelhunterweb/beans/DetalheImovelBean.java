package br.com.imovelhunterweb.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.imovelhunterweb.entitys.Anunciante;
import br.com.imovelhunterweb.entitys.Caracteristica;
import br.com.imovelhunterweb.entitys.Imovel;
import br.com.imovelhunterweb.service.ImovelService;
import br.com.imovelhunterweb.util.Navegador;
import br.com.imovelhunterweb.util.UtilSession;

@ManagedBean(name = "detalheImovelBean")
@ViewScoped
public class DetalheImovelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7757743129870117596L;

	
	private Navegador navegador;
	private Imovel imovel;
	private Anunciante anunciante;
	private List<Caracteristica> listaCaracteristica;
	
	@ManagedProperty("#{imovelService}")
	private ImovelService imovelService;
	
	@PostConstruct
	public void init() {
		this.navegador = new Navegador();	

		this.anunciante = (Anunciante) UtilSession.getHttpSessionObject("anuncianteLogado");
		
		
		if (this.anunciante == null) {
			this.navegador.redirecionarPara("login.xhtml");
			return;
		}
		
		this.imovel = (Imovel) UtilSession.getHttpSessionObject("imovelSelecionado");	
		if(this.imovel == null){			
			this.navegador.redirecionarPara("listarImovel.xhtml");
			return;
		}
		
		this.imovel = this.imovelService.buscarPorId(this.imovel.getIdImovel());
		UtilSession.getHttpSession().removeAttribute("imovelSelecionado");
		
		this.listaCaracteristica = this.imovel.getCaracteristicas();
		
		if(this.listaCaracteristica == null){
			this.listaCaracteristica = new ArrayList<Caracteristica>();
		}
		
	}
	public Imovel getImovel() {
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}

	public Anunciante getAnunciante() {
		return anunciante;
	}

	public void setAnunciante(Anunciante anunciante) {
		this.anunciante = anunciante;
	}
	public List<Caracteristica> getListaCaracteristica() {
		return listaCaracteristica;
	}
	public void setListaCaracteristica(List<Caracteristica> listaCaracteristica) {
		this.listaCaracteristica = listaCaracteristica;
	}
	public void setImovelService(ImovelService imovelService) {
		this.imovelService = imovelService;
	}

	
}