package JavaTCC.OCRMaven;

import java.util.Scanner;

import java.io.File;
import java.io.IOException;
/*import java.nio.file.Files;
import java.util.ArrayList;

import com.lowagie.text.List;*/

public class ListaPasta {
	
	public ListaPasta() throws IOException {
		@SuppressWarnings("resource")
		Scanner r = new Scanner(System.in);
		System.out.println("Digite o caminho: ");
		String diretorio = r.next();
		
		File file = new File(diretorio);
		File afile[] = file.listFiles();
		int i = 0;
		for (int j = afile.length; i < j; i++) {
			File arquivos = afile[i];
			
			if(arquivos.isDirectory() == true) {
				String diretorio1 = diretorio+"\\"+arquivos.getName();
				File file1 = new File(diretorio1);
				File afile1[] = file1.listFiles();
				int i1 = 0;
				for (int j1 = afile1.length; i1 < j1; i1++) {
					File arquivos1 = afile1[i1];
					System.out.println(arquivos1.getName());
				}
			}else {
				System.out.println(arquivos.getName());
			}
		}

	}

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		try {
			ListaPasta v = new ListaPasta();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
