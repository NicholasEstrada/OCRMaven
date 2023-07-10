package JavaTCC.OCRMaven;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class InputDomain {
    private static Set<String> visitedUrls = new HashSet<>();
    private static Set<String> visitedPDFs = new HashSet<>();
    private static Set<String> visitedImages = new HashSet<>();

    public static void main(String[] args) {
        String domain = "https://www.camarapoa.rs.gov.br"; // Substitua pelo domínio do site que você deseja vasculhar

        crawl(domain);
    }

    private static void crawl(String url) {
        try {
            if (visitedUrls.contains(url)) {
                return;
            }

            visitedUrls.add(url);

            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String href = link.attr("abs:href");
                if (isPDF(href)) {
                    if (!visitedPDFs.contains(href)) {
                        visitedPDFs.add(href);
                        System.out.println("Encontrado PDF: " + href);
                        //downloadPDF(href);
                        //texto = extractTextFromPDF(href);

                        LerImagem info = new LerImagem(href, "url");
                        System.out.println(info.resultado + "TESTEEE");
                    }
                } else if (isImage(href)) {
                    if (!visitedImages.contains(href)) {
                        visitedImages.add(href);
                        System.out.println("Encontrada imagem: " + href);
                        // Processar imagem aqui (por exemplo, fazer OCR em imagem)
                    }
                } else if (href.startsWith(url)) {
                    crawl(href);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    private static void downloadPDF(String url) throws IOException {
        URL pdfUrl = new URL(url);
        try (BufferedInputStream in = new BufferedInputStream(pdfUrl.openStream())) {
            // Implemente o código para salvar o arquivo PDF localmente
        }
    }

    private static String extractTextFromPDF(String url) throws IOException {
        try (PDDocument document = PDDocument.load(new URL(url).openStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            //System.out.println("Texto extraído do PDF:\n" + text);
            return text;
        }
    }
}
