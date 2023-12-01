package JavaTCC.OCRMaven;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;

public class Main {
    public static void main(String[] args) {
        String url = "https://www.genoprimer.com.br/wp-content/uploads/2022/04/encode-1200x565-1-1024x482.jpg";

        File downloadedFile = downloadArchive(url);
        System.out.println("Arquivo baixado em: " + downloadedFile.getAbsolutePath());

        String extension = extractFileExtension(url);
        System.out.println("Extensão do arquivo: " + extension);
    }

    private static File downloadArchive(String url) {
        // Corrigir para abrir uma conexão e obter um InputStream
        try (InputStream in = new URL(url).openStream()) {
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
}
