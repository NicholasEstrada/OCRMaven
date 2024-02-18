package JavaTCC.OCRMaven;

import java.io.*;
import java.net.*;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class SensitiveDataFinder implements Closeable, DataInspector {
	public String resultado;

	public SensitiveDataFinder(File args, String type) throws IOException{
		if (type.equals("local")) {
			Tesseract tess4j = new Tesseract();
			// tess path location ATUALIZAR EM CASO DE TROCA DE AREA DE DESENVOLVIMENTO
			tess4j.setDatapath("C:\\Users\\Nicholas\\git\\OCRMaven\\tessdata");
			try {
				String result = tess4j.doOCR(args);
				resultado = " Email: " + DataInspector.procuraEmail(result, 0) + " CPF: " + DataInspector.procuraCPF(result, 0);

			} catch (TesseractException e) {
				System.err.println(e.getMessage());
			}

		} /*else if (type.equals("url")) {

			try {
				String result = extractTextFromPDF(args);
				resultado = " Email: " + procuraEmail(result, 0) + " CPF: " + procuraCPF(result, 0) + " de " +  args;

			} catch (IOException e) {
				System.err.println(e.getMessage());
			}

		}*/
	}

	private static String extractTextFromPDF(String url) throws IOException {

			// Abra a conexão com a URI e obtenha um fluxo de entrada
			try (InputStream in = new BufferedInputStream(ValidateDataFormat.Codifier(url).openStream())) {

				PDDocument document = PDDocument.load(in);

				PDFTextStripper pdfStripper = new PDFTextStripper();
				String text = pdfStripper.getText(document);

				document.close();

				return text;

			} catch (IOException e) {
				e.printStackTrace();
			}
        return url;
    }

	@Override
	public void close() throws IOException {

	}

}
