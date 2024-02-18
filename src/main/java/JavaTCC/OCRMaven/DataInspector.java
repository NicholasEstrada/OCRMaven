package JavaTCC.OCRMaven;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 +  FUNÇÕES DE BUSCA DE DADOS SENSIVEIS DENTRO DOS ARQUIVOS
 */
public interface DataInspector extends ValidateDataFormat {

    static String procuraEmail(String result, int inicio) {
        String email = "";
        Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b");
        Matcher matcher = pattern.matcher(result.substring(inicio));

        if (matcher.find()) {
            email = matcher.group();
            int nextIndex = inicio + matcher.start() + email.length();
            if (nextIndex < result.length()) {
                String nextEmail = procuraEmail(result, nextIndex);
                if (!nextEmail.isEmpty()) {
                    email += "; " + nextEmail;
                }
            }
        }

        return email;
    }

    static String procuraCPF(String result, int inicio) {

        if (result == null) {
            return "";
        }

        int i;
        StringBuilder cpfs_achados = new StringBuilder();
        String regex_0 = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        String regex_1 = "\\d{9}-\\d{2}";
        String regex_2 = "\\d{11}";
        String[] regexes = { regex_0, regex_1, regex_2 };

        for (i = 0; i < regexes.length; i++) {
            String matchCPF = FounderRecursiveCPF(regexes[i], result, inicio);
            if (matchCPF != null && !matchCPF.isEmpty()) {
                if (cpfs_achados.length() > 0) {
                    cpfs_achados.append("; ");
                }
                cpfs_achados.append(matchCPF);
            }
        }

        return cpfs_achados.toString();

    }

    private static String FounderRecursiveCPF(String regex, String texto, int inicio) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texto);

        StringBuilder result = new StringBuilder();
        boolean hasMatch = false;

        while (matcher.find(inicio)) {
            String cpf = matcher.group();

            if (!cpf.isEmpty()) {
                if (ValidateDataFormat.validarCPF(cpf)) {
                    cpf = ValidateDataFormat.formatarCPF(cpf);
                } else {
                    cpf = "";
                }

                if (hasMatch) {
                    result.append("; ");
                }
                result.append(cpf);

                inicio = matcher.end();
                hasMatch = true;
            }
        }

        if (!hasMatch) {
            return null;
        }

        return result.toString();
    }
}
