package JavaTCC.OCRMaven;

import java.io.*;
import java.net.*;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class SensitiveDataFinder implements Closeable, DataInspector {

	// verificar melhor metodo de retorno deste procurador de dados sensiveis
	// #1 FECHAR CLASSE APOS PROCURA
	// #2 MELHORAR RASPAGEM PARA APLICAR THREADS EM VALIDAÇÕES CONCORRENTE E QUE POSSAM SER CONCORRENTES
	public String resultado;

	public SensitiveDataFinder( ArquivoBase arquivoBase /* File args, String type */) throws IOException{

		if ( arquivoBase.tipoProcessamento.equals("PDFText") ) {

			try {
				String result = extractTextFromPDF(arquivoBase.pathLocation);
				if ( result.equals("") ) {
					arquivoBase.tipoProcessamento = "OCR";
					arquivoBase.tipoArquivo = "Imagem/PDF Imagem";
				}else {
					resultado = "Email:" + DataInspector.procuraEmail(result, 0) + "|CPF:" + DataInspector.procuraCPF(result, 0) + "|pathLocation:" + arquivoBase.pathLocation;
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}

		if ( arquivoBase.tipoProcessamento.equals("OCR") ) {
			Tesseract tess4j = new Tesseract();
			// tess path location ATUALIZAR EM CASO DE TROCA DE AREA DE DESENVOLVIMENTO
			tess4j.setDatapath("C:\\Users\\Nicholas\\git\\OCRMaven\\tessdata");
			try {
				String result = tess4j.doOCR(arquivoBase.arquivo);
				resultado = "Email:" + DataInspector.procuraEmail(result, 0) + "|CPF:" + DataInspector.procuraCPF(result, 0) + "|pathLocation:" + arquivoBase.pathLocation;
			} catch (TesseractException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	private static String extractTextFromPDF(String url) throws IOException {

			// Abra a conexão com a URI e obtenha um fluxo de entrada
			try (InputStream in = new BufferedInputStream(ValidateDataFormat.Codifier(url).openStream())) {

				PDDocument document = PDDocument.load(in);

				PDFTextStripper pdfStripper = new PDFTextStripper();
				String text = pdfStripper.getText(document);

				document.close();

				if(text.trim().isEmpty()) return "";

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
