package JavaTCC.OCRMaven;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;

public class LerImagem {
	public String resultado;
	
	public LerImagem(String args) throws IOException {
		File imageFile = new File(args);
		Tesseract tess4j = new Tesseract();
		
		// tess path location
		tess4j.setDatapath("C:\\Users\\nicho_dfsl9b0\\git\\OCRMaven\\tessdata");
		try {
			String result = tess4j.doOCR(imageFile);
			resultado = procuraEmail(result);

		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void main(String args) {

	}

	private static String procuraEmail(String result) {
		// TODO Auto-generated method stub
		int res = result.indexOf("@");
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
			return email;
		} else {
			return "Sem email";
		}

	}
}