package br.leg.rr.tce.cgesi.sisaudit.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import com.sun.enterprise.universal.StringUtils;

import br.leg.rr.tce.cgesi.sisaudit.comum.entity.UnidadeGestora;
import br.leg.rr.tce.cgesi.sisaudit.comum.util.Util;
import br.leg.rr.tce.cgesi.sisaudit.ejb.AuditoriaEjb;
import br.leg.rr.tce.cgesi.sisaudit.ejb.EquipeFiscalizacaoEjb;
import br.leg.rr.tce.cgesi.sisaudit.ejb.PortariaEjb;
import br.leg.rr.tce.cgesi.sisaudit.ejb.ServidorEjb;
import br.leg.rr.tce.cgesi.sisaudit.ejb.UnidadeGestoraPortariaEjb;
import br.leg.rr.tce.cgesi.sisaudit.entity.Auditoria;
import br.leg.rr.tce.cgesi.sisaudit.entity.EquipeFiscalizacao;
import br.leg.rr.tce.cgesi.sisaudit.entity.Portaria;
import br.leg.rr.tce.cgesi.sisaudit.entity.Servidor;
import br.leg.rr.tce.cgesi.sisaudit.entity.TipoFiscalizacao;
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
	private List<TipoFiscalizacao> tipoFiscalizacaoList = new ArrayList<TipoFiscalizacao>();
	
	private String msgTexto;

	private boolean skip;

	private boolean exibir;


	
	public PortariaWizardBean() {
		super();
	}
	
	public String abrirListaPortaria() throws Exception {
		portaria = new Portaria();
		portariaList = new ArrayList<>();
		portariaList = portariaEjb.listaPortaria();
		
		portaria.setMostraCampo(true);
		
		portaria.setMostraCampo(true);
		/*
		Map<Integer, Portaria> mapPortaria = new HashMap<Integer, Portaria>();
		String listaId = "";
		List<UnidadeGestoraPortaria> listaUGP = new ArrayList<UnidadeGestoraPortaria>();
		listaUGP=unidadeGestoraPortariaEjb.findIAll();

		for (Portaria ptemp : portariaList) {
			mapPortaria.put(ptemp.getId(), ptemp);

			if (listaId.length() > 0) {
				listaId = listaId + "," + ptemp.getId();
			} else {
				listaId = ptemp.getId().toString();
			}

		}
		listaUGP = unidadeGestoraPortariaEjb.listaUGDasPortaria(listaId);

		for (UnidadeGestoraPortaria ltemp : listaUGP) {
			mapPortaria.get(ltemp.getPortaria().getId()).getListaUnidadeGestoraDaPortaria().add(ltemp);

			ltemp.setUnidadeGestora(sistemaBean.getUnidadeGestoraMap().get(ltemp.getUnidadeGestora()));
			ltemp.setPortaria(mapPortaria.get(ltemp.getPortaria()));
		}
		
*/
		

		return redirect("/sistema/portaria/listaPortarias.xhtml");
	}
	
	public String editarWizardPortaria(Portaria aux) {
		try {
			portaria = new Portaria();
			portaria = portariaEjb.pegarPortaria(aux.getId());
			servidorAutoridadeList = new ArrayList<Servidor>();

			unidadeGestoraDaAuditoria = new ArrayList<UnidadeGestora>();
			unidadeGestoraSelecionadas = new ArrayList<UnidadeGestora>();
			unidadeGestoraDaPortaria = new ArrayList<UnidadeGestora>();
			
			tipoFiscalizacaoList = new ArrayList<TipoFiscalizacao>();
			tipoFiscalizacaoList = sistemaBean.getTipoFiscalizacaoList();
			
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
	
	// prepara editar portaria de auditoria
		public void selecionandoUGP() {
			try {
				unidadeGestoraPortariaList = new ArrayList<UnidadeGestoraPortaria>();
				unidadeGestoraPortariaList2 = new ArrayList<UnidadeGestoraPortaria>();

				Map<Integer, UnidadeGestora> mapUGS = new HashMap<Integer, UnidadeGestora>();
				Map<Integer, UnidadeGestora> mapUGE = new HashMap<Integer, UnidadeGestora>();
				Map<Integer, UnidadeGestora> mapUGP = new HashMap<Integer, UnidadeGestora>();
				// unidades selecionadas
				for (UnidadeGestora x : unidadeGestoraSelecionadas) {
					mapUGS.put(x.getId(), x);
					UnidadeGestoraPortaria ugp = new UnidadeGestoraPortaria();
					ugp.setPortaria(portaria);
					ugp.setUnidadeGestora(x);
					unidadeGestoraPortariaList.add(ugp);
				}
				// da auditoria
				if (portaria.getIdAuditoria() != null) {
					for (UnidadeGestoraAuditoria x : portaria.getAuditoria().getUnidadeGestoraAuditorias()) {
						mapUGE.put(x.getUnidadeGestora().getId(), x.getUnidadeGestora());
						UnidadeGestoraPortaria ugp = new UnidadeGestoraPortaria();
						ugp.setPortaria(portaria);
						ugp.setUnidadeGestora(x.getUnidadeGestora());
						// portaria.getUnidadeGestoraPortariaExcluidas().add(ugp);
					}
				}
				// daportaria
				for (UnidadeGestoraPortaria x : portaria.getUnidadeGestoraPortarias()) {
					mapUGP.put(x.getUnidadeGestora().getId(), x.getUnidadeGestora());
					UnidadeGestoraPortaria ugp = new UnidadeGestoraPortaria();
					ugp.setId(x.getId());
					ugp.setPortaria(portaria);
					ugp.setUnidadeGestora(x.getUnidadeGestora());
					unidadeGestoraPortariaList2.add(ugp);
					if (mapUGS.containsKey(x.getUnidadeGestora().getId())) {
						portaria.removeUnidadeGestoraPortaria(ugp);
					}
				}
				portaria.setUnidadeGestoraPortarias(getUnidadeGestoraPortariaList());
				portaria.setUnidadeGestoraPortariaExcluidas(unidadeGestoraPortariaList2);
				portaria.setEquipeFiscalizacaoList(getEquipeFiscalizacaoList());
				this.salvarMinutaPortaria();
				// this.salvar();

			} catch (Exception e) {
				e.printStackTrace();
				showFacesMessage(e.getMessage(), 4);
			}

		}


	public void salvarMinutaPortaria() {
		try {
			portariaEjb.salvarMinuta(portaria);
		} catch (Exception e) {
			e.printStackTrace();
			showFacesMessage(e.getMessage(), 4);
		}
	}

	
	public String onFlowProcess(FlowEvent event) {
		if (skip) {
			skip = false; // reset in case user goes back
			return "confirm";
		} else {
			
			if (event.getNewStep().equals("equipe")) {
				this.selecionandoUGP();
				//this.salvarMinutaPortaria();
			} else {
				this.salvarMinutaPortaria();
			}
			
			return event.getNewStep();
		}
	}

	public void dateChange() {
		if (portaria.getPlanInicio() != null && portaria.getPlanFim() != null) {
			portaria.setPlanDiasUteis(Util.diasEntreDatas(portaria.getPlanInicio(), portaria.getPlanFim()));
		}
		if (portaria.getExecInicio() != null && portaria.getExecFim() != null) {
			portaria.setExecDiasUteis(Util.diasEntreDatas(portaria.getExecInicio(), portaria.getExecFim()));
		}
		if (portaria.getRelaInicio() != null && portaria.getRelaFim() != null) {
			portaria.setRelaDiasUteis(Util.diasEntreDatas(portaria.getRelaInicio(), portaria.getRelaFim()));
		}
	}
	
	
	public void dateChangeDias(){
		/*
		portaria.setPlanFim(Util.addDiasUteis(portaria.getPlanInicio(), portaria.getPlanDiasUteis()));
		portaria.setExecFim(Util.addDiasUteis(portaria.getExecInicio(), portaria.getExecDiasUteis()));
		portaria.setRelaFim(Util.addDiasUteis(portaria.getRelaInicio(), portaria.getRelaDiasUteis()));
		*/
	}
	// pega evento autocomplet remo��o de sele��o e atualiza listas
		public void unselectUGA(final UnselectEvent event) {
			try {
				final UnidadeGestora tmp = (UnidadeGestora) event.getObject();
				portaria.adincionarNaListaExcluidos(tmp);
				unidadeGestoraLista.add(tmp);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		// pega evento autocomplet sele��o de objetos e atualiza listas
		public void selectUGA(final SelectEvent event) {
			try {
				final UnidadeGestora tmp = (UnidadeGestora) event.getObject();
				unidadeGestoraLista.remove(tmp);
				portaria.selecionarUGA(tmp);
			} catch (Exception e) {
				// TODO: handle exception
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

	public List<TipoFiscalizacao> getTipoFiscalizacaoList() {
		return tipoFiscalizacaoList;
	}

	public void setTipoFiscalizacaoList(List<TipoFiscalizacao> tipoFiscalizacaoList) {
		this.tipoFiscalizacaoList = tipoFiscalizacaoList;
	}



	
	
	

}
