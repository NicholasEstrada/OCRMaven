package JavaTCC.OCRMaven;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.DecimalFormat;

public class LerImagem {
	public String resultado;
	
	public LerImagem(String args) throws IOException {
		File imageFile = new File(args);
		Tesseract tess4j = new Tesseract();
		
		// tess path location ATUALIZAR EM CASO DE TROCA DE AREA DE DESENVOLVIMENTO
		tess4j.setDatapath("C:\\Users\\Nicholas\\git\\OCRMaven\\tessdata");
		try {
			String result = tess4j.doOCR(imageFile);
			resultado = " Email: "+procuraEmail(result, 0)+" CPF: "+procuraCPF(result, 0);

		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
		
	}

	public static void main(String args) {

	}

	private static String procuraEmail(String result, int inicio) {
		// TODO Auto-generated method stub
		int res = result.indexOf("@", inicio);
		if (res != -1) {
			String[] ary = result.split("");
			String email = "";

			for (int i = (res - 1); i >= (res - 30); i--) {
				String ss = ary[i].toLowerCase();
				char c = ss.charAt(0);
				if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (c == '-') || (c == '.') || (c == 'à')
						|| (c == 'á') || (c == 'â') || (c == 'ã') || (c == 'é') || (c == 'ê') || (c == 'í')
						|| (c == 'ó') || (c == 'ô') || (c == 'õ') || (c == 'ú') || (c == 'ü') || (c == 'ç')) {
					email = c + email;
				} else {
					break;
				}
			}

			email = email + "@";

			for (int i = (res + 1); i <= (res + 26); i++) {
				String ss = ary[i].toLowerCase();
				char c = ss.charAt(0);
				if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (c == '-') || (c == '.') || (c == 'à')
						|| (c == 'á') || (c == 'â') || (c == 'ã') || (c == 'é') || (c == 'ê') || (c == 'í')
						|| (c == 'ó') || (c == 'ô') || (c == 'õ') || (c == 'ú') || (c == 'ü') || (c == 'ç')) {
					email = email + c;
				} else {
					break;
				}
			}
			
			// solução com recursão para 
			int reinicializador = res+(email.length());
			if( (ary.length-1) >= reinicializador-(email.length()) ) {
				res = result.indexOf("@", reinicializador);
				if (res != -1) {
					email = email + ";" + procuraEmail(result, res);
				}
			}
			
			// se no return houver mais que um email sera considerado um return nome@dominio;nome@dominio
			return email;		
		
		} else {
			return "Sem email";
		}

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
        
        // Validar dígitos verificadores
        int[] cpfArray = new int[11];
        for (int i = 0; i < 11; i++) {
            cpfArray[i] = Character.getNumericValue(cpfNumeros.charAt(i));
        }
        
        int soma1 = 0;
        int soma2 = 0;
        for (int i = 0; i < 9; i++) {
            soma1 += cpfArray[i] * (10 - i);
            soma2 += cpfArray[i] * (11 - i);
        }
        
        int digitoVerificador1 = (soma1 * 10) % 11;
        int digitoVerificador2 = (soma2 * 10) % 11;
        
        if (digitoVerificador1 == 10) {
            digitoVerificador1 = 0;
        }
        if (digitoVerificador2 == 10) {
            digitoVerificador2 = 0;
        }
        
        return cpfArray[9] == digitoVerificador1 && cpfArray[10] == digitoVerificador2;
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
	
	
	public static String regex_find() {
		
	}
	
	
	private static String procuraCPF(String result, int inicio) {
		
		String regex_0 = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";	// 14
		//String regex_1 = "\\d{9}-\\d{2}";					// 12
		//String regex_2 = "\\d{11}";							// 11
		
		Pattern pattern = Pattern.compile(regex_0);
		Matcher matcher = pattern.matcher(result);
		
		while (matcher.find(inicio)) {
			String cpf = matcher.group();
			
			if (cpf != "" ) {
				
				if (validarCPF(cpf)) {
					cpf = formatarCPF(cpf);
				}else {
					cpf = cpf+" -> Não Valido";
				}
				
				// alterar função para que o res_0 seja o indexof() do ultimo caracter
				// solução com recursão para 
				
				int res_0 = matcher.end();
				int reinicializador = res_0+(cpf.length());
				if( (result.length()-1) >= reinicializador-(cpf.length()) ) {
						cpf = cpf + ";" + procuraCPF(result, res_0);
				}
				
				// se no return houver mais que um email sera considerado um return nome@dominio;nome@dominio
				return cpf;
			
			} else {
				return "Sem CPF";
			}
			
		}
		return null;
		
		//int res_0 = result.indexOf(regex_0, inicio);
		//int res_1 = result.indexOf(regex_1, inicio);
		//int res_2 = result.indexOf(regex_2, inicio);
		
		
		
		
	}
	
}