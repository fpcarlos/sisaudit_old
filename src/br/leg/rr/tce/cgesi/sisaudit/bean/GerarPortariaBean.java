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
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.leg.rr.tce.cgesi.sisaudit.ejb.AuditoriaEjb;
import br.leg.rr.tce.cgesi.sisaudit.ejb.PortariaEjb;
import br.leg.rr.tce.cgesi.sisaudit.entity.Portaria;
import java.util.List;
import org.apache.commons.lang.StringUtils;

@Named
@SessionScoped
public class GerarPortariaBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AuditoriaEjb auditoriaEjb;

    @EJB
    private PortariaEjb portariaEjb;

    private StreamedContent file;

    private String searchValue;
    private String replacement;

    public GerarPortariaBean() {
        super();
    }

    public void mesclarPortariaComModelo(Portaria aux) throws Exception {

        ServletContext servletContext = (ServletContext) FacesContext
                .getCurrentInstance().getExternalContext().getContext();

        String filePath = servletContext
                .getRealPath("/resources/modeloDocs/Teste.docx");
        //response.setContentType("application/pdf");

        POIFSFileSystem fs = null;
        String tipoArq = "docx";
        Portaria portaria = new Portaria();
        portaria = portariaEjb.pegarPortaria(aux.getId());
        SimpleDateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
        try {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(filePath));
            List<XWPFParagraph> paragraphs = doc.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                for (XWPFRun r : paragraph.getRuns()) {
                    String text = r.getText(0);

                    if (text.contains("#NUMEROANOPORTARIA#")) {
                        text = text.replace("#NUMEROANOPORTARIA#", portaria.getNumeroPortaria()+"/"+portaria.getAnoPortaria());
                        r.setText(text, 0);
                    }
                    

                }

            }

            /*
	        XWPFTable table    = doc.createTable();
	        XWPFParagraph para = doc.createParagraph();
	        XWPFRun run        = para.createRun();
	        
	        run.setText("Hi");
	        //create first row
	        XWPFTableRow tableRowOne = table.getRow(0);
	        tableRowOne.getCell(0).setText("col one, row one");
	        tableRowOne.addNewTableCell().setText("col two, row one");
	        tableRowOne.addNewTableCell().setText("col three, row one");
	        //create second row
	        XWPFTableRow tableRowTwo = table.createRow();
	        tableRowTwo.getCell(0).setText("col one, row two");
	        tableRowTwo.getCell(1).setText("col two, row two");
	        tableRowTwo.getCell(2).setText("col three, row two");
	        //create third row
	        XWPFTableRow tableRowThree = table.createRow();
	        tableRowThree.getCell(0).setText("col one, row three");
	        tableRowThree.getCell(1).setText("col two, row three");
	        tableRowThree.getCell(2).setText("col three, row three");

	        run.setText("Bye");
             */
            saveDocx(filePath, doc, tipoArq);
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
            downloadPortaria(filePath2, tipoArq);
        } finally {
            out.close();
        }
    }

    private void saveDocx(String filePath, XWPFDocument doc, String tipoArq) throws FileNotFoundException, IOException {
        FileOutputStream out = null;
        ServletContext servletContext = (ServletContext) FacesContext
                .getCurrentInstance().getExternalContext().getContext();

        String filePath2 = servletContext
                .getRealPath("/resources/modeloDocs/Teste2.docx");

        try {
            //String filePath2 = "G:\\difip\\Teste200x.doc";
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
            if (tipoArq == "doc") {
                file = new DefaultStreamedContent(stream, "application/vnd.ms-word", "Portaria.doc");
            }
            if (tipoArq == "docx") {
                file = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml", "Portaria.docx");
            }

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
