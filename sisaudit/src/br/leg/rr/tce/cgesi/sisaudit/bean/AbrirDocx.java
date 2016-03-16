package br.leg.rr.tce.cgesi.sisaudit.bean;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

@Named
@SessionScoped
public class AbrirDocx extends AbstractBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	File arquivo = new File("G:/Teste.docx");
	

    public void getAbrir(){
        try{
            if(!arquivo.exists())
                arquivo.createNewFile();
        }catch(Exception e){
            System.out.println("Erro na criação do arquivo!");
        }
        try{
            Desktop.getDesktop().open(arquivo);
        }catch(Exception ex){
            ex.printStackTrace();    
            System.out.println(ex.getMessage());
        }
    }
    
    public void getCriarDocs(){
    	try {
    		   
    		   XWPFDocument document= new XWPFDocument(); 
    		   //Write the Document in file system
    		   FileOutputStream out = new FileOutputStream(
    		   new File("G:/createdocument.docx"));
    		   
    		   //create paragraph
    		   XWPFParagraph paragraph = document.createParagraph();
    		   
    		 //Set alignment paragraph to RIGHT
    		   paragraph.setAlignment(ParagraphAlignment.RIGHT);
    		   XWPFRun run=paragraph.createRun();
    		   run.setText("At tutorialspoint.com, we strive hard to " +
    		   "provide quality tutorials for self-learning " +
    		   "purpose in the domains of Academics, Information " +
    		   "Technology, Management and Computer Programming " +
    		   "Languages.");
    		   
    		   document.write(out);
    		   out.close();
    		   System.out.println(
    		   "createdocument.docx written successully");
    		   
    		
    		
		} catch (Exception e) {
			e.printStackTrace();    
            System.out.println(e.getMessage());
		}
    }
    
    
    
    
    
}
