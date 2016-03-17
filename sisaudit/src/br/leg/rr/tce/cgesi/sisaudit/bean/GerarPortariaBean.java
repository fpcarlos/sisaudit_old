package br.leg.rr.tce.cgesi.sisaudit.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.leg.rr.tce.cgesi.sisaudit.comum.util.Util;
import br.leg.rr.tce.cgesi.sisaudit.ejb.AuditoriaEjb;
import br.leg.rr.tce.cgesi.sisaudit.ejb.PortariaEjb;
import br.leg.rr.tce.cgesi.sisaudit.entity.Portaria;

@Named
@SessionScoped
public class GerarPortariaBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private AuditoriaEjb auditoriaEjb;

	@EJB
	private PortariaEjb portariaEjb;

	private StreamedContent file;

	public GerarPortariaBean() {
		super();
	}

	public void mesclarPortariaComModelo(Portaria aux) throws Exception {
		String filePath = "G:\\difip\\Teste.doc";
		String tipoArq="doc";
		POIFSFileSystem fs = null;
		Portaria portaria = new Portaria();
		portaria = portariaEjb.pegarPortaria(aux.getId());
		SimpleDateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
		try {
			fs = new POIFSFileSystem(new FileInputStream(filePath));
			HWPFDocument doc = new HWPFDocument(fs);
			Range range = doc.getRange();
			range.replaceText("#NUMEROANOPORTARIA#", portaria.getNumeroPortaria() + "/" + portaria.getAnoPortaria());
			if (portaria.getTipoFiscalizacao().getId() != null)
				range.replaceText("#TIPOAUDITORIA#", portaria.getTipoFiscalizacao().getNome());
			else
				range.replaceText("#TIPOAUDITORIA#", "TIPO DA AUDITORIA NAO FOI DEFINIDO");
			// #ListaUG
			range.replaceText("#LISTAUG#", portaria.getListaSiglaUnidadeGestoraDaPortaria());
			// #PlanInicio
			// String PlanInicio = fd.format(portaria.getPlanInicio());
			range.replaceText("#PLANINICIO#", fd.format(portaria.getPlanInicio()));
			// #RelFim
			// String RelFim = fd.format(portaria.getRelaFim());
			range.replaceText("#RELFIM#", fd.format(portaria.getRelaFim()));
			// #Objetivo
			range.replaceText("#OBJETIVO#", portaria.getObjetivo());
			// #DATAATUAL#
			range.replaceText("#DATAATUAL#", Util.hoje().toString());

			saveWord(filePath, doc, tipoArq);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * private HWPFDocument replaceText(HWPFDocument doc, String findText,
	 * String replaceText) { Range r1 = doc.getRange();
	 * 
	 * for (int i = 0; i < r1.numSections(); ++i) { Section s =
	 * r1.getSection(i); for (int x = 0; x < s.numParagraphs(); x++) { Paragraph
	 * p = s.getParagraph(x); for (int z = 0; z < p.numCharacterRuns(); z++) {
	 * CharacterRun run = p.getCharacterRun(z); String text = run.text(); if
	 * (text.contains(findText)) { run.replaceText(findText, replaceText); } } }
	 * } return doc; }
	 */
	private void saveWord(String filePath, HWPFDocument doc, String tipoArq) throws FileNotFoundException, IOException {
		FileOutputStream out = null;
		try {
			String filePath2 = "G:\\difip\\Teste200.doc";
			out = new FileOutputStream(filePath2);			
			doc.write(out);
			downloadPortaria(filePath2, tipoArq);
		} finally {
			out.close();
		}
	}

	
	public void downloadPortaria(String nomeArq, String tipoArq) {
		File f = new File(nomeArq);

		try {
			FileInputStream stream = new FileInputStream(f);
			if(tipoArq=="doc")
				file = new DefaultStreamedContent(stream, "application/vnd.ms-word", "Portaria.doc");
			if(tipoArq=="pdf")
				file = new DefaultStreamedContent(stream, "application/pdf", "Portaria.pdf");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

}
