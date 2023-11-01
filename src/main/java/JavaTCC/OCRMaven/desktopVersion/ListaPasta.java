package JavaTCC.OCRMaven;

import java.io.File;
import java.io.IOException;

public class ListaPasta {
	public static String local;
	public static String leitura;

//	public ListaPasta(String diretorio) throws IOException {
//		/*
//		 * @SuppressWarnings("resource") Scanner r = new Scanner(System.in);
//		 * System.out.println("Digite o caminho: "); String diretorio = r.next();
//		 */
//
//		// diretorio = "C:\\Users\\nicho_dfsl9b0\\Imagens\\Pictures";
//
//		File file = new File(diretorio);
//		File afile[] = file.listFiles();
//		int i = 0;
//		for (int j = afile.length; i < j; i++) {
//			File arquivos = afile[i];
//			recursivo(arquivos, diretorio);
//		}
//	}

//	public static void recursivo(File arquivos, String diretorio) {
//		if (arquivos.isDirectory() == true) {
//			String diretorio1 = diretorio + "\\" + arquivos.getName();
//			File file1 = new File(diretorio1);
//			File afile1[] = file1.listFiles();
//			int i1 = 0;
//			for (int j1 = afile1.length; i1 < j1; i1++) {
//				File arquivos1 = afile1[i1];
//				if (arquivos1.isDirectory() == true) {
//					recursivo(arquivos1, diretorio1);
//				} else {
//					String o = diretorio1 + "\\" + arquivos1.getName();
//					@SuppressWarnings("unused")
//					LerImagem l;
//
//					try {
//						if (tipoAceito(arquivos1.getName()) == true) {
//							l = new LerImagem(o, "local");
//							local = o;
//							leitura = l.resultado;
//							System.out.println(leitura + " " + local);
//						} else {
//							local = o;
//							leitura = "Tipo não aceito";
//							System.out.println(leitura + " " + local);
//						}
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		} else {
//			String o = diretorio + "\\" + arquivos.getName();
//			@SuppressWarnings("unused")
//			LerImagem l;
//
//			try {
//				if (tipoAceito(arquivos.getName()) == true) {
//					l = new LerImagem(o, "local");
//					local = o;
//					leitura = l.resultado;
//					System.out.println(leitura + " " + local);
//
//				} else {
//					leitura = "Tipo não aceito";
//					System.out.println(leitura + " " + local);
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}

	private static boolean tipoAceito(String name) {
		// TODO Auto-generated method stub
		int tip = name.indexOf(".");
		String[] ary = name.split("");
		String tipo = "";

		for (int i = (tip + 1); i < name.length(); i++) {
			String ss = ary[i].toLowerCase(); // AQUIIII AAAAAA HELP
			char c = ss.charAt(0);
			tipo = tipo + c;
		}

		if (tipo.equals("pdf") || tipo.equals("tif") || tipo.equals("png") || tipo.equals("jpg")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// try {
		// ListaPasta v = new ListaPasta("test");
		// } catch (IOException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
}
