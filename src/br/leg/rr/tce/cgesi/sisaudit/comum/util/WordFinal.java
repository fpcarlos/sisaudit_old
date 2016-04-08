package br.leg.rr.tce.cgesi.sisaudit.comum.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlException;

public class WordFinal {
	public static void main(String[] args) throws IOException, XmlException {
		XWPFDocument doc = new XWPFDocument(new FileInputStream("G:\\difip\\Teste.docx"));
		XWPFDocument destDoc = new XWPFDocument();
		OutputStream out = new FileOutputStream("G:\\difip\\Destination.docx");
		XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
		
		
		
		String copy = extractor.getText();
		copy.replace("#NUMEROANOPORTARIA#","Teste de Muda");
		
		//System.out.println(extractor.getText());
		
		//XWPFParagraph pr = doc.getParagraphs().;
		//String copy = pr.getText();
		XWPFParagraph paragraphOne = destDoc.createParagraph();
		XWPFRun paragraphOneRunOne = paragraphOne.createRun();
		paragraphOneRunOne.setText(copy);
		destDoc.write(out);
	}

}
