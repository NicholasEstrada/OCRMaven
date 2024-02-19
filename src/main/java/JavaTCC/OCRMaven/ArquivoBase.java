package JavaTCC.OCRMaven;

import java.io.File;

public class ArquivoBase {
    // File args, String type
    public File arquivo;
    public String tipoArquivo;
    public String tipoProcessamento;
    public String pathLocation;

    public ArquivoBase(File arquivo, String tipoArquivo, String tipoProcessamento, String pathLocation) {
        this.arquivo = arquivo;
        this.tipoArquivo = tipoArquivo;
        this.tipoProcessamento = tipoProcessamento;
        this.pathLocation = pathLocation;
    }

    // fazer funções aplicadas a mais; como dar o tamanho do arquivo
}
