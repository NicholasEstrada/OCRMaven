package JavaTCC.OCRMaven;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

    private static void crawl(String urlNaoModificado) throws UnsupportedEncodingException {


        try {
            if (visitedUrls.contains(urlNaoModificado)) {
                return;
            }

            visitedUrls.add(urlNaoModificado);

            URI Url0 = new URI(urlNaoModificado);
            String url = Url0.toASCIIString();


            //Document doc = Jsoup.connect(String.valueOf(url)).get();

            Document doc = Jsoup.connect(url).ignoreContentType(true).get();

            Elements links = doc.select("a[href]");

            for (Element link : links) {


                String href = link.attr("abs:href");
                if (isPDF(href)) {
                    if (!visitedPDFs.contains(href)) {

                        visitedPDFs.add(href);

                        System.out.println("Encontrado PDF: " + href);

                        LerImagem lerImagem = new LerImagem(href, "url");
                        System.out.println(lerImagem.resultado);


                    }
                } else if (isImage(href)) {
                    // System.out.println("Encontrada imagem: " + href);
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

    private static boolean isPDF(String url) {
        return url.toLowerCase().endsWith(".pdf");
    }

    private static boolean isImage(String url) {
        String lowercaseUrl = url.toLowerCase();
        return lowercaseUrl.endsWith(".jpeg") || lowercaseUrl.endsWith(".jpg") || lowercaseUrl.endsWith(".png");
        // Adicione outros formatos de imagem suportados aqui, se necessário
    }



}
