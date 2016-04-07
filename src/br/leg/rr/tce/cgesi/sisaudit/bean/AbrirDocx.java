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
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.leg.rr.tce.cgesi.sisaudit.comum.util.Util;
import br.leg.rr.tce.cgesi.sisaudit.ejb.AuditoriaEjb;
import br.leg.rr.tce.cgesi.sisaudit.ejb.PortariaEjb;
import br.leg.rr.tce.cgesi.sisaudit.entity.Portaria;

@Named
@SessionScoped
public class AbrirDocx extends AbstractBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*
	@EJB
	private AuditoriaEjb auditoriaEjb;

	@EJB
	private PortariaEjb portariaEjb;

	//File arquivo = new File("G:/difip/Teste.docx");

	private StreamedContent file;

	public void downloadAnexo(String nomeArq) {
		File f = new File(nomeArq);

		try {
			FileInputStream stream = new FileInputStream(f);
			file = new DefaultStreamedContent(stream, "application/vnd.ms-word", "Portaria.doc");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void getPegaDocx(Portaria aux) throws Exception {
		String filePath = "G:\\difip\\Teste.doc";

		// File aux2 = new File(filePath);

		POIFSFileSystem fs = null;

		Portaria portaria = new Portaria();
		portaria = portariaEjb.pegarPortaria(aux.getId());

		// Date d = new Date();
		SimpleDateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
		// String year = fd.format(d);
		try {
			fs = new POIFSFileSystem(new FileInputStream(filePath));
			HWPFDocument doc = new HWPFDocument(fs);
			// $NumeroAnoPortaria
			doc = replaceText(doc, "$NUMEROANOPORTARIA",portaria.getNumeroPortaria() + "/" + portaria.getAnoPortaria());
			// $TipoAuditoria
			if (portaria.getTipoFiscalizacao().getId() != null)
				doc = replaceText(doc, "$TIPOAUDITORIA ", portaria.getTipoFiscalizacao().getNome());
			else
				doc = replaceText(doc, "$TIPOAUDITORIA ", "TIPO DA AUDITORIA NAO FOI DEFINIDO");
			// $ListaUG
			doc = replaceText(doc, "$LISTAUG", portaria.getListaSiglaUnidadeGestoraDaPortaria());
			// $PlanInicio
			// String PlanInicio = fd.format(portaria.getPlanInicio());
			doc = replaceText(doc, "$PLANINICIO", fd.format(portaria.getPlanInicio()));
			// $RelFim
			// String RelFim = fd.format(portaria.getRelaFim());
			doc = replaceText(doc, "$RELFIM", fd.format(portaria.getRelaFim()));
			// $Objetivo
			doc = replaceText(doc, "$OBJETIVO", portaria.getObjetivo());
			saveWord(filePath, doc);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mesclarPortariaComModelo2(Portaria aux) throws Exception {
		String filePath = "+G:\\difip\\Teste.doc";
		String tipoArq = "doc";
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
			// range.replaceText("#TABELASERVIDORES#", table);

			//saveWord(filePath, doc, tipoArq);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private  HWPFDocument replaceText(HWPFDocument doc, String findText, String replaceText) {
		Range r1 = doc.getRange();

		for (int i = 0; i < r1.numSections(); ++i) {
			Section s = r1.getSection(i);
			for (int x = 0; x < s.numParagraphs(); x++) {
				Paragraph p = s.getParagraph(x);
				for (int z = 0; z < p.numCharacterRuns(); z++) {
					CharacterRun run = p.getCharacterRun(z);
					String text = run.text();
					if (text.contains(findText)) {
						run.replaceText(findText, replaceText);
					}
				}
			}
		}
		return doc;
	}

	private void saveWord(String filePath, HWPFDocument doc) throws FileNotFoundException, IOException {
		FileOutputStream out = null;
		try {
			String filePath2 = "G:\\difip\\Teste200.doc";
			out = new FileOutputStream(filePath2);
			doc.write(out);
			downloadAnexo(filePath2);
		} finally {
			out.close();
		}
	}
*/
	/*
	 * public void getAbrir() { try { if (!arquivo.exists())
	 * arquivo.createNewFile(); } catch (Exception e) { System.out.println(
	 * "Erro na criação do arquivo!"); } try {
	 * Desktop.getDesktop().open(arquivo); } catch (Exception ex) {
	 * ex.printStackTrace(); System.out.println(ex.getMessage()); } }
	 * 
	 * 
	 * public void getCriarDocs() { try {
	 * 
	 * XWPFDocument document = new XWPFDocument(); // Write the Document in file
	 * system FileOutputStream out = new FileOutputStream(new
	 * File("G:/difip/createdocument.docx"));
	 * 
	 * // create paragraph XWPFParagraph paragraph = document.createParagraph();
	 * 
	 * // Set alignment paragraph to RIGHT
	 * paragraph.setAlignment(ParagraphAlignment.RIGHT); XWPFRun run =
	 * paragraph.createRun(); run.setText(
	 * "At tutorialspoint.com, we strive hard to " +
	 * "provide quality tutorials for self-learning " +
	 * "purpose in the domains of Academics, Information " +
	 * "Technology, Management and Computer Programming " + "Languages.");
	 * 
	 * document.write(out); out.close(); System.out.println(
	 * "createdocument.docx written successully");
	 * 
	 * } catch (Exception e) { e.printStackTrace();
	 * System.out.println(e.getMessage()); } }
	 */
/*
	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}
*/
}
