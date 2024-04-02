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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InputDomain implements ValidateDataFormat {

    private static final Set<String> visitedUrls = new HashSet<>();
    private static final Set<String> visitedArchives = new HashSet<>();
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10); // Limite de 5 threads

    private static final int MAX_DEPTH = 4;

    public static void main(String[] args) throws UnsupportedEncodingException {
        String domain = "https://ifc.edu.br"; // Substitua pelo domínio do site que você deseja vasculhar
        try {
            List<String> listadepdf = InvetorDataSensetive(domain, 0);
            threadPool.shutdown();
            for (String content : listadepdf) {
                System.out.println(content);
            }
        }catch (UnsupportedEncodingException e){
            System.out.println("NAO PROCESSO PQQ: "+e.getMessage());
        }
    }

    private static List<String> InvetorDataSensetive(String domain, int depth) throws UnsupportedEncodingException {
        if (depth > MAX_DEPTH || visitedUrls.contains(domain)) {
            return List.of(new String[0]);
        }

        List<String> dadosColetados = new ArrayList<>();

        visitedUrls.add(domain);

        try {
            URI urlURI = new URI(domain); // ver a partir daqui o problema da iteração ##
            String url = urlURI.toASCIIString();
            Document doc = Jsoup.connect(url)
                                .referrer(domain)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                                .ignoreContentType(true)
                                .get();  // at JavaTCC.OCRMaven.webVersion.InputDomain.InvetorDataSensetive(InputDomain.java:60)
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String href = link.attr("abs:href");
                if ((ValidateDataFormat.isPDF(href) || ValidateDataFormat.isImage(href) && href.contains(domain.replaceAll("https?://","")))) {
                    if (!visitedArchives.contains(href)) {
                        // dadosColetados.add(href);
                        visitedArchives.add(href);
                        threadPool.submit(() -> dadosColetados.add(processArchive(href))); // at JavaTCC.OCRMaven.webVersion.InputDomain.lambda$InvetorDataSensetive$0(InputDomain.java:70)

                        Thread.sleep(10); // COM ISSO NAO FUNCIONOU
                    }
                } else if (href.startsWith(String.valueOf(url))) {
                    InvetorDataSensetive(href, depth + 1);
                }
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            logError("Error while processing URL: " + domain, e);
        }
        return dadosColetados;
    }

    private static String processArchive(String href) {
        try {
            ArquivoBase arquivoBase = new ArquivoBase(Objects.requireNonNull(downloadArchive(href)), "", href);
            SensitiveDataFinder sensitiveDataFinder = new SensitiveDataFinder(arquivoBase);
            String resultado = sensitiveDataFinder.resultado;
            //System.out.println(resultado);
            sensitiveDataFinder.close();
            return resultado;
        } catch (Exception e) {
            logError("Error while processing archive: " + href, e);
        }
        return "";
    }

    private static File downloadArchive(String url) {
        try (InputStream in = ValidateDataFormat.Codifier(url).openStream()) {
            byte[] fileBytes = IOUtils.toByteArray(in);
            Path tempFilePath = Files.createTempFile("tempFile", ValidateDataFormat.extractFileExtension(url));
            Files.write(tempFilePath, fileBytes);
            return tempFilePath.toFile();
        } catch (IOException e) {
            logError("Error while downloading archive: " + url, e);
            return null;
        }
    }

    private static void logError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }
}
