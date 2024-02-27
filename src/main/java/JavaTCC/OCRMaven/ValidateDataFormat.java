package JavaTCC.OCRMaven;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public interface ValidateDataFormat {

    static boolean validarCPF(String cpf) {
        // Remover caracteres especiais
        String cpfNumeros = cpf.replaceAll("[^0-9]", "");

        // Verificar se possui 11 dígitos
        if (cpfNumeros.length() != 11) {
            return false;
        }

        // Verificar se todos os dígitos são iguais
        if (cpfNumeros.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Validar primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpfNumeros.charAt(i)) * (10 - i);
        }
        int digitoVerificador1 = 11 - (soma % 11);
        if (digitoVerificador1 > 9) {
            digitoVerificador1 = 0;
        }
        if (Character.getNumericValue(cpfNumeros.charAt(9)) != digitoVerificador1) {
            return false;
        }

        // Validar segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpfNumeros.charAt(i)) * (11 - i);
        }
        int digitoVerificador2 = 11 - (soma % 11);
        if (digitoVerificador2 > 9) {
            digitoVerificador2 = 0;
        }
        if (Character.getNumericValue(cpfNumeros.charAt(10)) != digitoVerificador2) {
            return false;
        }

        return true;
    }

    static String formatarCPF(String cpf) {
        DecimalFormat formatador = new DecimalFormat("00000000000");

        try {
            String cpfFormatado = formatador.format(Long.parseLong(cpf));
            return cpfFormatado.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        } catch (NumberFormatException e) {
            return cpf;
        }
    }

    static String extractFileExtension(String url) {
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

    static URL Codifier(String url) {
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

    static boolean isPDF(String url) {
        return url.toLowerCase().endsWith(".pdf");
    }

    static boolean isImage(String url) {

        List<String> IMAGE_EXTENSIONS = Arrays.asList(
                ".bmp", ".gif", ".jpg", ".jpeg", ".png", ".tiff", ".tif", ".ico", ".webp"
        );

        String lowercaseUrl = url.toLowerCase();

        for (String extension : IMAGE_EXTENSIONS) {
            if (lowercaseUrl.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
