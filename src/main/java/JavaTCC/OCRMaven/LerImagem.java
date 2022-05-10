package JavaTCC.OCRMaven;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;

public class LerImagem {

    public static void main(String[] args) throws IOException {
        File imageFile = new File("C:\\Users\\Nicholas\\Pictures\\images\\eurotext.tif");
        Tesseract tess4j = new Tesseract();
        // tess path location 
        tess4j.setDatapath("C:\\Users\\Nicholas\\eclipse-workspace\\OCRMaven\\tessdata");
        // tess4j.setLanguage("por");
        try {
            String result = tess4j.doOCR(imageFile);
            // System.out.println(result);
            System.out.println(procuraEmail(result));
            
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

	private static String procuraEmail(String result) {
		// TODO Auto-generated method stub
		int res = result.indexOf("@");
		String[] ary = result.split("");
		String email = "";
		
		for(int i = (res-1);i>=(res-30);i--) {
			String ss = ary[i].toLowerCase();
			char c = ss.charAt(0);
			if((c >= 'a' && c <= 'z')||(c >= '0' && c <= '9')||(c == '-')||(c == '.')||(c == 'à')||(c == 'á')||(c == 'â')||(c == 'ã')||(c == 'é')||(c == 'ê')||(c == 'í')||(c == 'ó')||(c == 'ô')||(c == 'õ')||(c == 'ú')||(c == 'ü')||(c == 'ç')) {
				// System.out.println(c);
				email = c+email;
			}else{
				break;
			}
		}
		
		email = email+"@";
		
		for(int i = (res+1);i<=(res+26);i++) {
			String ss = ary[i].toLowerCase();
			char c = ss.charAt(0);
			if((c >= 'a' && c <= 'z')||(c >= '0' && c <= '9')||(c == '-')||(c == '.')||(c == 'à')||(c == 'á')||(c == 'â')||(c == 'ã')||(c == 'é')||(c == 'ê')||(c == 'í')||(c == 'ó')||(c == 'ô')||(c == 'õ')||(c == 'ú')||(c == 'ü')||(c == 'ç')) {
				// System.out.println(c);
				email = email+c;
			}else{
				break;
			}
		}
		
		// System.out.println(email);
		
		return email;
	}
}