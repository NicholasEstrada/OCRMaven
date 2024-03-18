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
                result = extractTextFromPDF(arquivoBase.pathLocation);
				arquivoBase.tipoProcessamento = "PDFText";
            }catch(Exception e){
                System.err.println(e.getMessage());
            }

            if ( result == null || result.isEmpty()) {
                arquivoBase.tipoProcessamento = "OCR";
            }else{
                resultado = "|Email:" + DataInspector.procuraEmail(result, 0) +
                            "|CPF:" + DataInspector.procuraCPF(result, 0)+
							"|OpiniaoPolitica:" + DataInspector.ProcuraOpiniaoPolitica(result)+
                            "|Extensao:" + arquivoBase.extensaoArquivo +
                            "|tipoProcessamento:" + arquivoBase.tipoProcessamento +
                            "|pathLocation:" + arquivoBase.pathLocation;
            }
        }

		if ( arquivoBase.tipoProcessamento.equals("OCR") ) {
			Tesseract tess4j = new Tesseract();
			// tess path location ATUALIZAR EM CASO DE TROCA DE AREA DE DESENVOLVIMENTO
			tess4j.setDatapath("C:\\Users\\Nicholas\\Dev\\TCC\\Back-End\\OCRMaven\\tessdata");
			try {
				String result = tess4j.doOCR(arquivoBase.arquivo);
				resultado = "|Email:" + DataInspector.procuraEmail(result, 0) +
							"|CPF:" + DataInspector.procuraCPF(result, 0)+
							"|OpiniaoPolitica:" + DataInspector.ProcuraOpiniaoPolitica(result)+
							"|Extensao:" + arquivoBase.extensaoArquivo +
							"|tipoProcessamento:" + arquivoBase.tipoProcessamento +
							"|pathLocation:" + arquivoBase.pathLocation.replaceAll("\\\\tempFile.*","");
			} catch (TesseractException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	private static String extractTextFromPDF(String urlOrFilePath) throws IOException {
		if(urlOrFilePath.matches("^(https?|ftp)://.*$")) {
			return extractTextFromURL(urlOrFilePath);
		} else {
			return extractTextFromFile(urlOrFilePath);
		}
    }

	private static String extractTextFromURL(String url) throws IOException {
		try (InputStream in = new BufferedInputStream(ValidateDataFormat.Codifier(url).openStream())) {
			return extractTextFromStream(in);
		}catch (EOFException e){
			System.out.println(e.getMessage());
		}
        return url;
    }

	private static String extractTextFromFile(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		try (InputStream in = Files.newInputStream(path)) {
			return extractTextFromStream(in);
		} catch (IOException e) {
			System.out.println("Erro ao ler arquivo: " + e.getMessage());
		}
		return null;
	}



	private static String extractTextFromStream(InputStream in) throws IOException {
		System.out.println("OTEXTOOOO" + in.toString());
		PDDocument document = PDDocument.load(in);
		PDFTextStripper pdfStripper = new PDFTextStripper();
		String text = pdfStripper.getText(document);
		document.close();

		if (text.trim().isEmpty()) return "";

		return text;
	}

	@Override
	public void close() throws IOException {

	}

}
