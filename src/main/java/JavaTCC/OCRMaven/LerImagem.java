package JavaTCC.OCRMaven;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;

public class LerImagem {

    public static void main(String[] args) throws IOException {
        File imageFile = new File("C:\\Users\\Nicholas\\Pictures\\images\\eurotext.tif");
        Tesseract tess4j = new Tesseract();
        tess4j.setDatapath("C:\\Users\\Nicholas\\Downloads\\Tess4J-3.4.8-src\\Tess4J\\tessdata");
        // tess4j.setLanguage("por");
        try {
            String result = tess4j.doOCR(imageFile);
            // System.out.println(result);
            System.out.println(procuraEmail(result));
            
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

	private static int procuraEmail(String result) {
		// TODO Auto-generated method stub
		int res = result.indexOf("@");
		return res;
	}
}