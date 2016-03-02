package br.leg.rr.tce.cgesi.sisaudit.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.sun.enterprise.universal.StringUtils;

import br.leg.rr.tce.cgesi.sisaudit.comum.entity.UnidadeGestora;
import br.leg.rr.tce.cgesi.sisaudit.ejb.AuditoriaEjb;
import br.leg.rr.tce.cgesi.sisaudit.ejb.EquipeFiscalizacaoEjb;
import br.leg.rr.tce.cgesi.sisaudit.ejb.PortariaEjb;
import br.leg.rr.tce.cgesi.sisaudit.ejb.ServidorEjb;
import br.leg.rr.tce.cgesi.sisaudit.ejb.UnidadeGestoraPortariaEjb;
import br.leg.rr.tce.cgesi.sisaudit.entity.Auditoria;
import br.leg.rr.tce.cgesi.sisaudit.entity.EquipeFiscalizacao;
import br.leg.rr.tce.cgesi.sisaudit.entity.Portaria;
import br.leg.rr.tce.cgesi.sisaudit.entity.Servidor;
import br.leg.rr.tce.cgesi.sisaudit.entity.UnidadeGestoraAuditoria;
import br.leg.rr.tce.cgesi.sisaudit.entity.UnidadeGestoraPortaria;

@Named
@SessionScoped
public class PortariaWizardBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private transient SistemaBean sistemaBean;
	
	@Inject
	private Portaria portaria;
	
	@Inject
	private Auditoria auditoria;

	@EJB
	private PortariaEjb portariaEjb;

	@EJB
	private AuditoriaEjb auditoriaEjb;
	
	@EJB
	private UnidadeGestoraPortariaEjb unidadeGestoraPortariaEjb;
	
	@EJB
	private EquipeFiscalizacaoEjb equipeFiscalizacaoEjb;

	@EJB
	private ServidorEjb servidorEjb;



	//Listas
	private List<UnidadeGestora> unidadeGestoraLista = new ArrayList<UnidadeGestora>();
	private List<UnidadeGestoraPortaria> unidadeGestoraPortariaList = new ArrayList<UnidadeGestoraPortaria>();
	private List<UnidadeGestoraPortaria> unidadeGestoraPortariaList2 = new ArrayList<UnidadeGestoraPortaria>();

	private List<UnidadeGestora> unidadeGestoraDaAuditoria = new ArrayList<UnidadeGestora>();
	private List<UnidadeGestora> unidadeGestoraSelecionadas = new ArrayList<UnidadeGestora>();
	private List<UnidadeGestora> unidadeGestoraDaPortaria = new ArrayList<UnidadeGestora>();

	private List<Portaria> portariaList = new ArrayList<Portaria>();
	private List<Servidor> servidorList = new ArrayList<Servidor>();
	private List<EquipeFiscalizacao> equipeFiscalizacaoList = new ArrayList<EquipeFiscalizacao>();
	private List<Servidor> servidorAutoridadeList = new ArrayList<Servidor>();
	
	private String msgTexto;

	private boolean skip;

	private boolean exibir;


	
	public PortariaWizardBean() {
		super();
	}
	
	public String abrirListaPortaria() throws Exception {
		portariaList = new ArrayList<Portaria>();
		portariaList = portariaEjb.listaPortaria();

		//List<UnidadeGestoraPortaria> listaUGP = new ArrayList<UnidadeGestoraPortaria>();
		//listaUGP=unidadeGestoraPortariaEjb.findIAll();

		portaria.setMostraCampo(true);

		return redirect("/sistema/portaria/listaPortarias.xhtml");
	}
	
	public String editarWizardPortaria(Portaria aux) {
		try {
			portaria = aux;
			servidorAutoridadeList = new ArrayList<Servidor>();

			unidadeGestoraDaAuditoria = new ArrayList<UnidadeGestora>();
			unidadeGestoraSelecionadas = new ArrayList<UnidadeGestora>();
			unidadeGestoraDaPortaria = new ArrayList<UnidadeGestora>();
			
			if(portaria.getIdAuditoria()!=null){
				auditoria = new Auditoria();
				auditoria = auditoriaEjb.carregarAuditoria(aux.getIdAuditoria());
				
				portaria.setAuditoria(auditoria);	
				for(UnidadeGestoraAuditoria x: portaria.getAuditoria().getUnidadeGestoraAuditorias()){
					UnidadeGestora unG = new UnidadeGestora();
					UnidadeGestoraPortaria unGP = new UnidadeGestoraPortaria();
					unG=x.getUnidadeGestora();
					unidadeGestoraDaAuditoria.add(unG);
					unGP.setPortaria(portaria);
					unGP.setUnidadeGestora(unG);
					portaria.getListaUnidadeGestoraDaPortaria().add(unGP);
				}
			}else{
				for(UnidadeGestora x: sistemaBean.getUnidadeGestoraList()){
					UnidadeGestoraPortaria unGP = new UnidadeGestoraPortaria();
					unidadeGestoraDaAuditoria.add(x);
					unGP.setPortaria(portaria);
					unGP.setUnidadeGestora(x);
					portaria.getListaUnidadeGestoraDaPortaria().add(unGP);
				}
			}
			for (UnidadeGestoraPortaria x : unidadeGestoraPortariaEjb.findIdPortaria(portaria.getId())) {
				UnidadeGestora unG = new UnidadeGestora();
				unG = sistemaBean.selecionarUnidadeGestora(x.getUnidadeGestora().getId());
				unidadeGestoraSelecionadas.add(unG);
			}			
			

			equipeFiscalizacaoList = new ArrayList<EquipeFiscalizacao>();
			equipeFiscalizacaoList = equipeFiscalizacaoEjb.findIdPortaria(aux.getId());

			for (Servidor stemp : servidorEjb.findAll()) {
				String vtipo = stemp.getAutoridade();
				if (vtipo.contains("S"))
					servidorAutoridadeList.add(stemp);
			}
			portaria.setNumeroPortaria(StringUtils.padLeft(portaria.getNumeroPortaria(), 3, '0'));

			return redirect("/sistema/portaria/cadastro/frmCadPortariaEtapa1.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
			showFacesMessage(e.getMessage(), 4);
			return null;
		}
	}

	

	public Portaria getPortaria() {
		return portaria;
	}

	public void setPortaria(Portaria portaria) {
		this.portaria = portaria;
	}

	public List<Portaria> getPortariaList() {
		return portariaList;
	}

	public void setPortariaList(List<Portaria> portariaList) {
		this.portariaList = portariaList;
	}

	public List<Servidor> getServidorList() {
		return servidorList;
	}

	public void setServidorList(List<Servidor> servidorList) {
		this.servidorList = servidorList;
	}

	public List<Servidor> getServidorAutoridadeList() {
		return servidorAutoridadeList;
	}

	public void setServidorAutoridadeList(List<Servidor> servidorAutoridadeList) {
		this.servidorAutoridadeList = servidorAutoridadeList;
	}

	public String getMsgTexto() {
		return msgTexto;
	}

	public void setMsgTexto(String msgTexto) {
		this.msgTexto = msgTexto;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public boolean isExibir() {
		return exibir;
	}

	public void setExibir(boolean exibir) {
		this.exibir = exibir;
	}

	public List<UnidadeGestora> getUnidadeGestoraLista() {
		return unidadeGestoraLista;
	}

	public void setUnidadeGestoraLista(List<UnidadeGestora> unidadeGestoraLista) {
		this.unidadeGestoraLista = unidadeGestoraLista;
	}

	public List<UnidadeGestoraPortaria> getUnidadeGestoraPortariaList() {
		return unidadeGestoraPortariaList;
	}

	public void setUnidadeGestoraPortariaList(List<UnidadeGestoraPortaria> unidadeGestoraPortariaList) {
		this.unidadeGestoraPortariaList = unidadeGestoraPortariaList;
	}

	public List<UnidadeGestoraPortaria> getUnidadeGestoraPortariaList2() {
		return unidadeGestoraPortariaList2;
	}

	public void setUnidadeGestoraPortariaList2(List<UnidadeGestoraPortaria> unidadeGestoraPortariaList2) {
		this.unidadeGestoraPortariaList2 = unidadeGestoraPortariaList2;
	}

	public List<UnidadeGestora> getUnidadeGestoraDaAuditoria() {
		return unidadeGestoraDaAuditoria;
	}

	public void setUnidadeGestoraDaAuditoria(List<UnidadeGestora> unidadeGestoraDaAuditoria) {
		this.unidadeGestoraDaAuditoria = unidadeGestoraDaAuditoria;
	}

	public List<UnidadeGestora> getUnidadeGestoraSelecionadas() {
		return unidadeGestoraSelecionadas;
	}

	public void setUnidadeGestoraSelecionadas(List<UnidadeGestora> unidadeGestoraSelecionadas) {
		this.unidadeGestoraSelecionadas = unidadeGestoraSelecionadas;
	}

	public List<UnidadeGestora> getUnidadeGestoraDaPortaria() {
		return unidadeGestoraDaPortaria;
	}

	public void setUnidadeGestoraDaPortaria(List<UnidadeGestora> unidadeGestoraDaPortaria) {
		this.unidadeGestoraDaPortaria = unidadeGestoraDaPortaria;
	}

	public Auditoria getAuditoria() {
		return auditoria;
	}

	public void setAuditoria(Auditoria auditoria) {
		this.auditoria = auditoria;
	}

	public List<EquipeFiscalizacao> getEquipeFiscalizacaoList() {
		return equipeFiscalizacaoList;
	}

	public void setEquipeFiscalizacaoList(List<EquipeFiscalizacao> equipeFiscalizacaoList) {
		this.equipeFiscalizacaoList = equipeFiscalizacaoList;
	}



	
	
	

}