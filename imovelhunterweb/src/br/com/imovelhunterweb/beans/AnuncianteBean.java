package br.com.imovelhunterweb.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.imovelhunterweb.entitys.Anunciante;
import br.com.imovelhunterweb.service.AnuncianteService;
import br.com.imovelhunterweb.util.EmailValidator;
import br.com.imovelhunterweb.util.Navegador;
import br.com.imovelhunterweb.util.PrimeUtil;
import br.com.imovelhunterweb.util.ValidaCPF;

@ManagedBean(name ="anuncianteBean")
@ViewScoped
public class AnuncianteBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2842024280524632375L;
	private Anunciante anunciante;
	private String confirmarSenha;
	private String dataDeNascimento;
	private SimpleDateFormat simpleDateFormat;
	private Navegador navegador;
	private EmailValidator validadorDeEmail;
	private ValidaCPF validaCpf;
	
	/**
	 * Este atributo � o que ir� utilizar a persistencia do anunciante, O spring cuida da parte de fazer a inje��o de depend�ncia desse atributo
	 * por sess�o do escopo criado de cada usu�rio, para que funcione deve ser colocado o set deste atributo, para que o 
	 * spring consiga injetar o service corretamente
	 */
	@ManagedProperty("#{anuncianteService}")
	private AnuncianteService anuncianteService;
	
	/**
	 * Esse atributo serve para voc� mandar mensagem para tela do usu�rio,
	 * atualizar componentes da tela, e mandar executar algum javascript de algum componente da tela tamb�m
	 */
	private PrimeUtil primeUtil;
	
	
	@PostConstruct
	public void init(){		
		this.anunciante = new Anunciante();			
		this.anunciante.setDataDeVencimento(new Date());
		this.confirmarSenha = "";
		
		this.dataDeNascimento = new String();
		this.primeUtil = new PrimeUtil();
		
		this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
		this.navegador = new Navegador();
		this.validadorDeEmail = new EmailValidator();
		this.validaCpf = new ValidaCPF();
	}
	
	
	public Anunciante getAnunciante() {
		return anunciante;
	}

	public void setAnunciante(Anunciante anunciante) {
		this.anunciante = anunciante;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
	
	
	public void cadastrarAnunciante(){
		try{	
			if(!this.validarCampos()){
				return;
			}
			if(this.anuncianteService.existeEmail(this.anunciante.getEmail())){
				this.primeUtil.mensagem(FacesMessage.SEVERITY_WARN,"Cadastro","Este email j� est� cadastrado");
				//Exibe uma mensagem para o usu�rio
				this.primeUtil.update("cadastroAnunciantes");
				//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
				this.primeUtil.update("idFormMensagem");
				return;
			}
			String cpfValidacao = this.anunciante.getCpf().replace(".","").replace("-","");
			if(this.anuncianteService.existeCpf(cpfValidacao)){
				this.primeUtil.mensagem(FacesMessage.SEVERITY_WARN,"Cadastro","Este cpf j� est� cadastrado");
				//Exibe uma mensagem para o usu�rio
				this.primeUtil.update("cadastroAnunciantes");
				//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
				this.primeUtil.update("idFormMensagem");
				return;
			}
			if(!this.anuncianteService.existeLogin(this.anunciante.getLogin())){
				this.anunciante.setDataDeNascimento(this.simpleDateFormat.parse(this.dataDeNascimento));
				Date dataAgora = new Date();
				Calendar calendario = Calendar.getInstance();
				calendario.setTime(dataAgora);
				calendario.add(Calendar.DAY_OF_MONTH,30);
				Date dataVencimento = calendario.getTime();
				this.anunciante.setDataDeCriacao(dataAgora);
				this.anunciante.setDataDeVencimento(dataVencimento);
				
				this.anunciante.setCpf(cpfValidacao);
				this.anunciante = this.anuncianteService.inserir(this.anunciante);
				//Valida se o anunciante foi inserido
				if(this.anunciante.getIdAnunciante() != 0 ){
					//Exibe uma mensagem para o usu�rio
					this.primeUtil.mensagem(FacesMessage.SEVERITY_INFO,"Cadastro","Anunciante cadastrado com sucesso");
					//Instancia um novo anunciante para que seja cadastrado um novo
					this.anunciante = new Anunciante();
					//Limpa a caixinha do confirmar senha
					this.confirmarSenha = "";
					//Limpa data de nascimento
					this.dataDeNascimento = "";
					//Exibe uma mensagem para o usu�rio
					this.primeUtil.update("cadastroAnunciantes");
					
					this.navegador.redirecionarPara("login.xhtml");
				}else{
					//Exibe uma mensagem para o usu�rio
					this.primeUtil.mensagem(FacesMessage.SEVERITY_ERROR,"Cadastro","Erro ao cadastrar o anunciante");
				}
			}else{
				this.primeUtil.mensagem(FacesMessage.SEVERITY_WARN,"Cadastro","Login j� est� em uso");
				//Exibe uma mensagem para o usu�rio
				this.primeUtil.update("cadastroAnunciantes");
			}
		}catch(Exception e){
			e.printStackTrace();
			//Exibe uma mensagem para o usu�rio
			this.primeUtil.mensagem(FacesMessage.SEVERITY_ERROR,"Erro","Erro ao cadastrar o anunciante");
		}
		//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
		this.primeUtil.update("idFormMensagem");
	}
	
	private boolean validarCampos(){
		
		if(this.anunciante.getNome().length() == 0){
			this.primeUtil.mensagem(FacesMessage.SEVERITY_INFO,"Campo inv�lido","O nome do anunciante deve ser informado");
			//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
			this.primeUtil.update("idFormMensagem");
			return false;
		}
		if(this.anunciante.getSobreNome().length() == 0){
			this.primeUtil.mensagem(FacesMessage.SEVERITY_INFO,"Campo inv�lido","O sobrenome do anunciante deve ser informado");
			//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
			this.primeUtil.update("idFormMensagem");
			return false;
		}
		if(this.anunciante.getEmail().length() == 0){
			this.primeUtil.mensagem(FacesMessage.SEVERITY_INFO,"Campo inv�lido","O email do anunciante deve ser informado");
			//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
			this.primeUtil.update("idFormMensagem");
			return false;
		}else if(!this.validadorDeEmail.isEmailValid(this.anunciante.getEmail())){
			this.primeUtil.mensagem(FacesMessage.SEVERITY_INFO,"Email inv�lido","O email do anunciante � inv�lido");
			//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
			this.primeUtil.update("idFormMensagem");
			return false;
		}
		if(this.anunciante.getCpf().length() != 14){
			this.primeUtil.mensagem(FacesMessage.SEVERITY_INFO,"Campo inv�lido","O cpf do anunciante deve ser preenchido");
			//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
			this.primeUtil.update("idFormMensagem");
			return false;
		}else{
			String cpfValidacao = this.anunciante.getCpf().replace(".","").replace("-","");
			if(!this.validaCpf.isCPF(cpfValidacao)){
				this.primeUtil.mensagem(FacesMessage.SEVERITY_INFO,"Campo inv�lido","O cpf informado � inv�lido");
				//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
				this.primeUtil.update("idFormMensagem");
				return false;
			}
		}
		if(this.anunciante.getCreci().length() == 0){
			this.primeUtil.mensagem(FacesMessage.SEVERITY_INFO,"Campo inv�lido","O creci deve ser informado");
			//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
			this.primeUtil.update("idFormMensagem");
			return false;
		}
		if(this.anunciante.getLogin().length() == 0){
			this.primeUtil.mensagem(FacesMessage.SEVERITY_INFO,"Campo inv�lido","O login deve ser informado");
			//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
			this.primeUtil.update("idFormMensagem");
			return false;
		}
		if(this.anunciante.getSenha().length() == 0){
			this.primeUtil.mensagem(FacesMessage.SEVERITY_INFO,"Campo inv�lido","A senha deve ser informado");
			//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
			this.primeUtil.update("idFormMensagem");
			return false;
		}
		if(confirmarSenha.length() == 0){
			this.primeUtil.mensagem(FacesMessage.SEVERITY_INFO,"Campo inv�lido","A senha deve ser confirmada");
			//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
			this.primeUtil.update("idFormMensagem");
			return false;
		}
		if(!this.anunciante.getSenha().equals(this.confirmarSenha)){
			this.primeUtil.mensagem(FacesMessage.SEVERITY_INFO,"Valores diferentes","A senha diferente de confirmar senha");
			//Atualiza o componente que exibe a mensagem para o usu�rio, para que ele exiba
			this.primeUtil.update("idFormMensagem");
			return false;
		}
		
		
		return true;
	}


	public void setAnuncianteService(AnuncianteService anuncianteService) {
		this.anuncianteService = anuncianteService;
	}


	public String getDataDeNascimento() {
		return dataDeNascimento;
	}


	public void setDataDeNascimento(String dataDeNascimento) {
		this.dataDeNascimento = dataDeNascimento;
	} 
	


}
