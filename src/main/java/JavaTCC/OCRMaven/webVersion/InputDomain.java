package JavaTCC.OCRMaven.webVersion;

import JavaTCC.OCRMaven.SensitiveDataFinder;
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

            //System.out.println("Chegou aqui 1");

            Document doc = Jsoup.connect(url).userAgent("Mozilla").ignoreContentType(true).get();

            // Document doc = Jsoup.connect(url).ignoreContentType(true).get();

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
        // Corrigir para abrir uma conexão e obter um InputStream
        try (InputStream in = Codifier(url).openStream()) {
            byte[] fileBytes = IOUtils.toByteArray(in);
            Path tempFilePath = Files.createTempFile("tempFile", extractFileExtension(url));
            Files.write(tempFilePath, fileBytes);

            return tempFilePath.toFile();
        } catch (IOException e) {
            // Trate a exceção aqui, pode imprimir uma mensagem de erro ou fazer algo mais apropriado para sua aplicação.
            e.printStackTrace();
            return null; // Ou outra ação apropriada para indicar que o download falhou
        }
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

    private static URL Codifier(String url) {
        try {

            // Use URLEncoder para codificar a URL inteira, incluindo o caminho para o arquivo
            String encodedURL = URLEncoder.encode(url, "UTF-8").replace("+", "%20");

            // Construa a URI com a URL codificada
            URL uri = new URL(encodedURL
                    .replaceAll("%2F", "/")
                    .replaceAll("%3A", ":")
                    .replaceAll("%25", "%")
            );
            return uri;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
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
