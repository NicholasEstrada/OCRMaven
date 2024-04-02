package JavaTCC.OCRMaven;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImprovedWebCrawler {

    private static final int MAX_DEPTH = 3;
    private static final int MAX_THREADS = 10;
    private static final Set<String> visitedUrls = new HashSet<>();
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

    public static void main(String[] args) {
        String domain = "https://ifc.edu.br"; // Substitua pelo domínio do site que você deseja vasculhar

        crawl(domain, 0);

        // Aguardar a conclusão do processamento de todas as threads
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void crawl(String url, int depth) {
        if (depth > MAX_DEPTH || visitedUrls.contains(url)) {
            return;
        }
        visitedUrls.add(url);

        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String href = link.absUrl("href");
                if (!visitedUrls.contains(href)) {
                    threadPool.submit(() -> {
                        try {
                            processLink(href);
                            crawl(href, depth + 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processLink(String url) throws IOException {
        if (isPdf(url) || isImage(url)) {
            downloadAndProcessFile(url);
        }
    }

    private static boolean isPdf(String url) {
        return url.toLowerCase().endsWith(".pdf");
    }

    private static boolean isImage(String url) {
        return url.toLowerCase().endsWith(".jpg") || url.toLowerCase().endsWith(".jpeg") ||
                url.toLowerCase().endsWith(".png") || url.toLowerCase().endsWith(".gif");
    }

    private static void downloadAndProcessFile(String url) throws IOException {
        try (InputStream in = new URL(url).openStream()) {
            Path tempFile = Files.createTempFile("tempFile", "");
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            // Chamada para método de detecção de dados sensíveis
            // SensitiveDataFinder.detect(tempFile);
            System.out.println("Processed file: " + url);
        }
    }
}
