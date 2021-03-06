package br.com.imovelhunterweb.service.imp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import br.com.imovelhunterweb.dao.ImovelDAO;
import br.com.imovelhunterweb.entitys.Anunciante;
import br.com.imovelhunterweb.entitys.Caracteristica;
import br.com.imovelhunterweb.entitys.Imagem;
import br.com.imovelhunterweb.entitys.Imovel;
import br.com.imovelhunterweb.service.ImovelService;
import br.com.imovelhunterweb.util.EnderecoImagens;

import com.sun.xml.internal.bind.v2.model.core.ID;

@Service("imovelService")
public class ImovelServiceImp implements ImovelService,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1646179337352925215L;
	/**
	 * 
	 */
	
	@Resource(name = "imovelDAO")
	private ImovelDAO imovelDAO;

	@Override
	@Transactional
	@Rollback
	public Imovel inserir(Imovel imovel) {
		return this.imovelDAO.insert(imovel);
	}

	@Override
	@Transactional
	@Rollback
	public Imovel atualizar(Imovel imovel) {
		return this.imovelDAO.update(imovel);
	}

	@Override
	@Transactional
	@Rollback
	public Boolean remover(Imovel imovel) {
		try{
			imovel.setCaracteristicas(new ArrayList<Caracteristica>());
			this.imovelDAO.update(imovel);
			this.imovelDAO.remove(imovel);
			EnderecoImagens enderecoImagens = new EnderecoImagens();
			for(Imagem img : imovel.getImagens()){
				enderecoImagens.excluirImagensServidor(img.getCaminhoImagem());
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	@Rollback
	public Boolean removerPorId(Imovel imovel) {
		try{
			this.imovelDAO.removeById(imovel.getIdImovel());
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Imovel buscarPorId(long id) {
		Imovel i =  this.imovelDAO.findById(id);
		if(i != null)
		i.getCaracteristicas();
		return i;
	}

	@Override
	public List<Imovel> listarTodos() {
		return this.imovelDAO.findAll();
	}

	@Override
	public List<Imovel> listarTodos(int index, int quantidade) {
		return this.imovelDAO.findAll(index,quantidade);
	}

	@Override
	public List<Imovel> listarImoveisDoAnunciante(Anunciante anunciante) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idAnunciante", anunciante.getIdAnunciante());
		
		return this.imovelDAO.useQuery("FROM Imovel i WHERE i.anunciante.idAnunciante=:idAnunciante", param);
	}
	
	
	
	
}
