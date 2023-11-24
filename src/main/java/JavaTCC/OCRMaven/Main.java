package JavaTCC.OCRMaven;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Main {
    public static void main(String[] args) {
        // URL do arquivo temporário na internet
        String url = "https://araucariageneticabovina.com.br/arquivos/servico/pdfServico_57952bf8ca7af_24-07-2016_17-58-32.pdf";

        try {
            // Baixe o arquivo da internet
            byte[] fileBytes = IOUtils.toByteArray(new URL(url));
            Path tempFilePath = Files.createTempFile("tempFile", ".pdf");
            Files.write(tempFilePath, fileBytes);

            // Use o Tesseract OCR para extrair texto da imagem
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:\\Users\\Nicholas\\git\\OCRMaven\\tessdata");
            String textoExtraido = tesseract.doOCR(tempFilePath.toFile());

            // Imprima o texto extraído
            System.out.println(textoExtraido);
            FileUtils.deleteQuietly(tempFilePath.toFile());

        } catch (IOException | TesseractException e) {
            e.printStackTrace();
        }
    }
}
