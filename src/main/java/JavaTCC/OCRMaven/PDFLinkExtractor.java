package JavaTCC.OCRMaven;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PDFLinkExtractor {

    private static final int RESULTS_PER_PAGE = 1;

    public static void main(String[] args) {
        String domain = "ifc.edu.br"; // Substitua pelo domínio desejado
        List<String> pdfUrls = findPdfUrls(domain, 1);

        // Imprime as URLs dos arquivos PDF encontrados
        for (String pdfUrl : pdfUrls) {
            System.out.println(pdfUrl);
        }
    }

    private static List<String> findPdfUrls(String domain, int start) {
        List<String> pdfUrls = new ArrayList<>();

        try {
            while (true) {
                List<String> partialResults = searchForPdfUrls(domain, start);
                pdfUrls.addAll(partialResults);

                // Verifica se há mais resultados disponíveis
                if (partialResults.size() < RESULTS_PER_PAGE) {
                    break;
                }

                // Incrementa o ponto de partida para a próxima página de resultados
                start += RESULTS_PER_PAGE;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pdfUrls;
    }

    private static List<String> searchForPdfUrls(String domain, int start) throws IOException {
        List<String> pdfUrls = new ArrayList<>();

        // Constrói a URL da API de busca do Google
        String googleSearchUrl = "https://www.googleapis.com/customsearch/v1?key=AIzaSyAau-snolNWYIghcigkGBdjAdT9fvmHsEw&cx=81c0664f9c3584af7&q=site:"
                + domain + "+filetype:pdf&num=" + RESULTS_PER_PAGE + "&start=" + start;
        System.out.println(googleSearchUrl);
        URL url = new URL(googleSearchUrl);

        // Realiza a solicitação HTTP e analisa a resposta JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(new InputStreamReader(url.openStream()), JsonObject.class);
        JsonArray items = jsonObject.getAsJsonArray("items");

        // Extrai as URLs dos arquivos PDF dos resultados da pesquisa
        for (JsonElement item : items) {
            String pdfUrl = item.getAsJsonObject().get("link").getAsString();
            pdfUrls.add(pdfUrl);
        }

        return pdfUrls;
    }
}
