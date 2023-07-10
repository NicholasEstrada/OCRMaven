package JavaTCC.OCRMaven;

import net.sourceforge.tess4j.ITesseract;
import java.net.URL;
import java.nio.file.Files;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.DecimalFormat;

public class LerImagem {
	public String resultado;

	public LerImagem(String args, String type) throws IOException {
		if (type == "local") {
			File imageFile = new File(args);
			Tesseract tess4j = new Tesseract();
			// tess path location ATUALIZAR EM CASO DE TROCA DE AREA DE DESENVOLVIMENTO
			tess4j.setDatapath("C:\\Users\\Nicholas\\git\\OCRMaven\\tessdata");
			try {
				String result = tess4j.doOCR(imageFile);
				resultado = " Email: " + procuraEmail(result, 0) + " CPF: " + procuraCPF(result, 0);

			} catch (TesseractException e) {
				System.err.println(e.getMessage());
			}

		} else if (type == "url") {
			URL url = new URL(args);

			File arquivo = downloadArquivo(url);

			ITesseract tesseract = new Tesseract();

			tesseract.setLanguage("eng");

			try {
				String result = tesseract.doOCR(arquivo);
				resultado = " Email: " + procuraEmail(result, 0) + " CPF: " + procuraCPF(result, 0);

			} catch (TesseractException e) {
				System.err.println(e.getMessage());
			}

		}
	}

	private static File downloadArquivo(URL url) throws IOException {
		File arquivoTemporario = File.createTempFile("temp", ".tmp");
	
		if (arquivoTemporario.exists()) {
			arquivoTemporario.delete();
		}
	
		Files.copy(url.openStream(), arquivoTemporario.toPath());
	
		return arquivoTemporario;
	}
	

	public static void main(String args) {

	}

	private static String procuraEmail(String result, int inicio) {
		String email = "";
		Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b");
		Matcher matcher = pattern.matcher(result.substring(inicio));

		if (matcher.find()) {
			email = matcher.group();
			int nextIndex = inicio + matcher.start() + email.length();
			if (nextIndex < result.length()) {
				String nextEmail = procuraEmail(result, nextIndex);
				if (!nextEmail.equals("")) {
					email += ";" + nextEmail;
				}
			}
		}

		return email;
	}

	public static boolean validarCPF(String cpf) {
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

	public static String formatarCPF(String cpfNumeros) {
		DecimalFormat formatador = new DecimalFormat("00000000000");

		try {
			String cpfFormatado = formatador.format(Long.parseLong(cpfNumeros));
			return cpfFormatado.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
		} catch (NumberFormatException e) {
			return cpfNumeros;
		}
	}

	public static String regex_find(String regex, String texto, int inicio) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(texto);

		StringBuilder result = new StringBuilder();
		boolean hasMatch = false;

		while (matcher.find(inicio)) {
			String cpf = matcher.group();

			if (!cpf.isEmpty()) {
				if (validarCPF(cpf)) {
					cpf = formatarCPF(cpf);
				} else {
					cpf = formatarCPF(cpf) + " -> Não Valido";
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

	private static String procuraCPF(String result, int inicio) {

		int i;
		String cpfs_achados = "";
		String regex_0 = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
		String regex_1 = "\\d{9}-\\d{2}";
		String regex_2 = "\\d{11}";
		String[] regexes = { regex_0, regex_1, regex_2 };

		for (i = 0; i < regexes.length; i++) {
			cpfs_achados = cpfs_achados + regex_find(regexes[i], result, inicio);
		}

		return cpfs_achados;

	}

	public static void procuraCPF(Object extractTextFromPDF) {
	}

}
