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

    public static void main(String[] args) {
        String domain = "https://www.araquari.ifc.edu.br"; // Substitua pelo domínio do site que você deseja vasculhar

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
                if (href.endsWith(".pdf")) {
                    System.out.println("Encontrado PDF: " + href);
                    downloadPDF(href);
                    extractTextFromPDF(href);
                } else if (href.startsWith(url)) {
                    crawl(href);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadPDF(String url) throws IOException {
        URL pdfUrl = new URL(url);
        try (BufferedInputStream in = new BufferedInputStream(pdfUrl.openStream())) {
            // Implemente o código para salvar o arquivo PDF localmente
        }
    }

    private static void extractTextFromPDF(String url) throws IOException {
        try (PDDocument document = PDDocument.load(new URL(url).openStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            System.out.println("Texto extraído do PDF:\n" + text);
        }
    }
}
