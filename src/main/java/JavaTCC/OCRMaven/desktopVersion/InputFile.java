package JavaTCC.OCRMaven.desktopVersion;

import JavaTCC.OCRMaven.ArquivoBase;
import JavaTCC.OCRMaven.SensitiveDataFinder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

import static JavaTCC.OCRMaven.ValidateDataFormat.isImage;
import static JavaTCC.OCRMaven.ValidateDataFormat.isPDF;

public class InputFile extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField textField1;
	JButton mybutton;

public InputFile() {
            JFileChooser choose = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Aceito PDF, PNG e Derivados",
                "jpg", "pdf","tif","jpeg", "png"
                );
            choose.setFileFilter(filter);
            int retorno = choose.showOpenDialog(null);
            if (retorno == JFileChooser.APPROVE_OPTION){
                String caminho = choose.getSelectedFile().getAbsolutePath();
                try {
                    String tipoArquivoBase = "";
                    if( isPDF(caminho) ) tipoArquivoBase = "PDF";
                    if( isImage(caminho) ) tipoArquivoBase = "Imagem/PDF Imagem";
                    File file = new File(caminho);
                    ArquivoBase arquivoBase = new ArquivoBase(file, tipoArquivoBase, "OCR", "");
                    SensitiveDataFinder le = new SensitiveDataFinder(arquivoBase);

                    JOptionPane.showMessageDialog(null, le.resultado);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
}

	public static void main(String args[]) {
		new InputFile();
	}
}
