package JavaTCC.OCRMaven;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class InputDomain {


    private static final Set<String> visitedUrls = new HashSet<>();
    private static final Set<String> visitedPDFs = new HashSet<>();
    private static final Set<String> visitedImages = new HashSet<>();

    public static void main(String[] args) throws UnsupportedEncodingException {
        String domain = "https://www.camarapoa.rs.gov.br"; // Substitua pelo domínio do site que você deseja vasculhar

        crawl(domain);
    }

    public void pesquisaDominio(String args) throws UnsupportedEncodingException {
        crawl(args);
    }

    private static void crawl(String urlNaoModificada) throws UnsupportedEncodingException {
        try {
            if (visitedUrls.contains(urlNaoModificada)) {
                return;
            }

            visitedUrls.add(urlNaoModificada);

            URI urlURI = new URI(urlNaoModificada);
            String url = urlURI.toASCIIString();

            Document doc = Jsoup.connect(url).ignoreContentType(true).get();

            Elements links = doc.select("a[href]");

            for (Element link : links) {

                String href = link.attr("abs:href");
                if (isPDF(href)) {
                    if (!visitedPDFs.contains(href)) {

                        visitedPDFs.add(href);

                        System.out.println("Encontrado PDF: " + href);

                        try (SensitiveDataFinder lerImagem = new SensitiveDataFinder(href, "url")) {
                            System.out.println(lerImagem.resultado);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } else if (isImage(href)) {
                    System.out.println("Encontrada imagem: " + href);
                    // Processar imagem aqui (por exemplo, fazer OCR em imagem)
                    visitedImages.add(href);
                } else if (href.startsWith(String.valueOf(url))) {
                    crawl(href);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static File downloadArchive(String url) throws IOException {
        byte[] fileBytes = IOUtils.toByteArray(new URL(url));
        Path tempFilePath = Files.createTempFile("tempFile", extractFileExtension(url));
        Files.write(tempFilePath, fileBytes);

        return tempFilePath.toFile();
    }

    public static String extractFileExtension(String url) {
        try {
            URL urlObj = new URL(url);
            String path = urlObj.getPath();
            int lastDotIndex = path.lastIndexOf('.');

            if (lastDotIndex != -1) {
                return path.substring(lastDotIndex);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Trate exceções adequadamente em um ambiente de produção
        }

        return ""; // Se não encontrar a extensão, retorna uma string vazia ou outra indicação apropriada
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
