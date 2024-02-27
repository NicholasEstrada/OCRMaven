package JavaTCC.OCRMaven.webVersion;

import JavaTCC.OCRMaven.ArquivoBase;
import JavaTCC.OCRMaven.SensitiveDataFinder;
import JavaTCC.OCRMaven.ValidateDataFormat;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InputDomain implements ValidateDataFormat {


    private static final Set<String> visitedUrls = new HashSet<>();
    private static final Set<String> visitedPDFs = new HashSet<>();
    private static final Set<String> visitedImages = new HashSet<>();

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(5); // Limite de 10 threads


    private static List<String> InvetorDataSensetive(String domain) throws UnsupportedEncodingException {
        try {

            if (visitedUrls.contains(domain)) {
                return null;
            }
            visitedUrls.add(domain);


            List<String> dadosColetados = new ArrayList<>();

            URI urlURI = new URI(domain);
            String url = urlURI.toASCIIString();

            Document doc = Jsoup.connect(url).userAgent("Mozilla").ignoreContentType(true).get();

            Elements links = doc.select("a[href]");

            for (Element link : links) {

                String href = link.attr("abs:href");
                if (ValidateDataFormat.isPDF(href) || ValidateDataFormat.isImage(href)) {

                    if (!visitedPDFs.contains(href)) {

                        visitedPDFs.add(href);

                        // PARTE DO RETURN 1 #pathLocation
                        System.out.println("Encontrado PDF: " + href.replaceAll(" ", "%20"));

                        ArquivoBase arquivoBase = new ArquivoBase(downloadArchive(href), "", "", href);

                        // fazer taratamento tipoProcessamento
                        if( ValidateDataFormat.isPDF(href) ) {
                            arquivoBase.tipoArquivo = "PDF";
                            arquivoBase.tipoProcessamento = "PDFText";
                        }
                        if( ValidateDataFormat.isImage(href) ) {
                            arquivoBase.tipoArquivo = "Imagem/PDF Imagem";
                            arquivoBase.tipoProcessamento = "OCR";
                        }

                        threadPool.submit(() -> {
                            try{
                                SensitiveDataFinder lerImagem = new SensitiveDataFinder(arquivoBase);
                                System.out.println(lerImagem.resultado);
                                dadosColetados.add(lerImagem.resultado);
                                lerImagem.close();
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }else if (href.startsWith(String.valueOf(url))) {
                    InvetorDataSensetive(href);
                }
            }

            return dadosColetados;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static File downloadArchive(String url) {
        try (InputStream in = ValidateDataFormat.Codifier(url).openStream()) {
            byte[] fileBytes = IOUtils.toByteArray(in);
            Path tempFilePath = Files.createTempFile("tempFile", ValidateDataFormat.extractFileExtension(url));
            Files.write(tempFilePath, fileBytes);

            return tempFilePath.toFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String domain = "https://www.camarapoa.rs.gov.br"; // Substitua pelo domínio do site que você deseja vasculhar

        InvetorDataSensetive(domain);
    }
}
