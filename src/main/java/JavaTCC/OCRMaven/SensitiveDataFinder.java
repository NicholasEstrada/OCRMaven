package JavaTCC.OCRMaven;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class SensitiveDataFinder implements Closeable, DataInspector {

	// verificar melhor metodo de retorno deste procurador de dados sensiveis
	// #1 FECHAR CLASSE APOS PROCURA
	// #2 MELHORAR RASPAGEM PARA APLICAR THREADS EM VALIDAÇÕES CONCORRENTE E QUE POSSAM SER CONCORRENTES
	public String resultado;

	public SensitiveDataFinder( ArquivoBase arquivoBase) throws IOException{

		if (arquivoBase.tipoProcessamento.isEmpty()) {

			String result = "";

            try{
                result = extractTextFromStream(arquivoBase.arquivo);
				arquivoBase.tipoProcessamento = "PDFText";
            }catch(Exception e){
                System.err.println(e.getMessage());
            }

            if ( result == null || result.isEmpty()) {
                arquivoBase.tipoProcessamento = "OCR";
            }else{
                resultado = arquivoBase.pathLocation.replaceAll("\\\\tempFile.*", "") +
							"|" + DataInspector.procuraEmail(result, 0) +
                            "," + DataInspector.procuraCPF(result, 0) +
							"," + DataInspector.ProcuraOpiniaoPolitica(result) +
                            "|" + arquivoBase.extensaoArquivo +
                            "|" + arquivoBase.tipoProcessamento;
            }
        }

		if ( arquivoBase.tipoProcessamento.equals("OCR") ) {
			Tesseract tess4j = new Tesseract();
			// tess path location ATUALIZAR EM CASO DE TROCA DE AREA DE DESENVOLVIMENTO
			tess4j.setDatapath("C:\\Users\\Nicholas\\Dev\\TCC\\Back-End\\OCRMaven\\tessdata");
			try {
				if(arquivoBase.arquivo.length() > 0) {
					String result = tess4j.doOCR(arquivoBase.arquivo); // at JavaTCC.OCRMaven.SensitiveDataFinder.<init>(SensitiveDataFinder.java:50)
					resultado = arquivoBase.pathLocation.replaceAll("\\\\tempFile.*", "") +
								"|" + DataInspector.procuraEmail(result, 0) +
								"," + DataInspector.procuraCPF(result, 0) +
								"," + DataInspector.ProcuraOpiniaoPolitica(result) +
								"|" + arquivoBase.extensaoArquivo +
								"|" + arquivoBase.tipoProcessamento;
				}else{
					System.err.println("Arquivo PDF vazio ou corrompido: " + arquivoBase.arquivo.getName());
				}
			} catch (TesseractException e) {
				System.err.println(e.getMessage());
			} finally {
				tess4j = null;
			}
		}
	}

	private static String extractTextFromStream(File file) throws IOException {
		try (PDDocument document = PDDocument.load(file)) {
			PDFTextStripper pdfStripper = new PDFTextStripper();
			String text = pdfStripper.getText(document);
			return text.trim();
		}
	}


	@Override
	public void close() throws IOException {
	}

}
