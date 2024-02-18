package JavaTCC.OCRMaven;

import java.io.File;

public class ArquivoBase {
    // File args, String type
    public File arquivo;
    public String tipoArquivo;

    public ArquivoBase(File arquivo, String tipoArquivo) {
        this.arquivo = arquivo;
        this.tipoArquivo = tipoArquivo;
    }

    // funções aplicadas a mais como dar o tamanho do arquivo
}
