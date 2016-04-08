package br.leg.rr.tce.cgesi.sisaudit.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
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

		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
				.getContext();

		String filePath = servletContext.getRealPath("/resources/modeloDocs/Teste.docx");
		// response.setContentType("application/pdf");

		// POIFSFileSystem fs = null;

		String tipoArq = "docx";
		Portaria portaria = new Portaria();
		portaria = portariaEjb.pegarPortaria(aux.getId());
		SimpleDateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
		try {
			// fs = new POIFSFileSystem(new FileInputStream(filePath));
			XWPFDocument doc = new XWPFDocument(new FileInputStream(filePath));

			XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
			String conteudo = extractor.getText();

			// conteudo = conteudo.replace("#nate_oper#", "Simpsons");
			// extractor.getText().replace("", "");

			extractor.getText().replace("#NUMEROANOPORTARIA#",
					portaria.getNumeroPortaria() + "/" + portaria.getAnoPortaria());
			System.out.println(extractor.getText());

			if (portaria.getTipoFiscalizacao().getId() != null)
				conteudo = conteudo.replace("#TIPOAUDITORIA#", portaria.getTipoFiscalizacao().getNome());
			else
				conteudo = conteudo.replace("#TIPOAUDITORIA#", "TIPO DA AUDITORIA NAO FOI DEFINIDO");
			// #ListaUG
			conteudo = conteudo.replace("#LISTAUG#", portaria.getListaSiglaUnidadeGestoraDaPortaria());
			// #PlanInicio
			// String PlanInicio = fd.format(portaria.getPlanInicio());
			conteudo = conteudo.replace("#PLANINICIO#", fd.format(portaria.getPlanInicio()));
			// #RelFim
			// String RelFim = fd.format(portaria.getRelaFim());
			conteudo = conteudo.replace("#RELFIM#", fd.format(portaria.getRelaFim()));
			// #Objetivo
			conteudo = conteudo.replace("#OBJETIVO#", portaria.getObjetivo());
			// #DATAATUAL#
			conteudo = conteudo.replace("#DATAATUAL#", Util.hoje().toString());
			// range.replaceText("#TABELASERVIDORES#", table);

			// FileWriter fw = new FileWriter(filePath);
			// fw.write(extractor.getText());
			// fw.close();
			saveDocx(filePath, conteudo, tipoArq);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveWord(String filePath, HWPFDocument doc, String tipoArq) throws FileNotFoundException, IOException {
		FileOutputStream out = null;

		try {
			String filePath2 = "G:\\difip\\Teste200.doc";
			out = new FileOutputStream(filePath2);
			doc.write(out);
			// downloadPortaria(filePath2, tipoArq);
		} finally {
			out.close();
		}
	}

	private void saveDocx(String filePath, String doc, String tipoArq) throws FileNotFoundException, IOException {
		// FileOutputStream out = null;
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
				.getContext();

		String filePath2 = servletContext.getRealPath("/resources/modeloDocs/Teste2.docx");

		XWPFDocument vTexto = new XWPFDocument(new FileInputStream(filePath2));

		// XWPFParagraph p = vTexto.createParagraph();

		OutputStream out = new FileOutputStream(filePath2);

		out.write(doc.getBytes());
		out.flush();
		out.close();

		try {
			// out = new FileOutputStream(filePath2);
			// doc.write(out);

			downloadPortaria(filePath2, tipoArq);
		} finally {
			out.close();
		}
	}

	public void downloadPortaria(String nomeArq, String tipoArq) {
		File f = new File(nomeArq);
		try {
			FileInputStream stream = new FileInputStream(f);
			file = new DefaultStreamedContent(stream,
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml",
					"Portaria.docx");
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
