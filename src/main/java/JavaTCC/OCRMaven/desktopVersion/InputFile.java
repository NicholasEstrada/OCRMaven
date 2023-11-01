package JavaTCC.OCRMaven.desktopVersion;

import JavaTCC.OCRMaven.LerImagem;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;

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
                    LerImagem le = new LerImagem(caminho, "local");

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
