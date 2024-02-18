package JavaTCC.OCRMaven.webVersion;

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
import java.util.HashSet;
import java.util.Set;

public class InputDomain implements ValidateDataFormat {


    private static final Set<String> visitedUrls = new HashSet<>();
    private static final Set<String> visitedPDFs = new HashSet<>();
    private static final Set<String> visitedImages = new HashSet<>();

    public static void main(String[] args) throws UnsupportedEncodingException {
        String domain = "https://www.camarapoa.rs.gov.br"; // Substitua pelo domínio do site que você deseja vasculhar

        crawl(domain);
    }


    private static void crawl(String domain) throws UnsupportedEncodingException {
        try {
            if (visitedUrls.contains(domain)) {
                return;
            }

            visitedUrls.add(domain);

            URI urlURI = new URI(domain);
            String url = urlURI.toASCIIString();

            Document doc = Jsoup.connect(url).userAgent("Mozilla").ignoreContentType(true).get();

            Elements links = doc.select("a[href]");

            for (Element link : links) {

                String href = link.attr("abs:href");
                if (isPDF(href) || isImage(href)) {

                    if (!visitedPDFs.contains(href)) {

                        visitedPDFs.add(href);

                        System.out.println("Encontrado PDF: " + href);

                        try (SensitiveDataFinder lerImagem = new SensitiveDataFinder(downloadArchive(href), "local")) {
                            System.out.println(lerImagem.resultado);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }else if (href.startsWith(String.valueOf(url))) {
                    crawl(href);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
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

    private static boolean isPDF(String url) {
        return url.toLowerCase().endsWith(".pdf");
    }

    private static boolean isImage(String url) {
        String lowercaseUrl = url.toLowerCase();
        return lowercaseUrl.endsWith(".jpeg") || lowercaseUrl.endsWith(".jpg") || lowercaseUrl.endsWith(".png");
        // Adicione outros formatos de imagem suportados aqui, se necessário
    }



}
