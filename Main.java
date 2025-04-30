import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HuffmanCompressor compressor = new HuffmanCompressor();
        HuffmanDecompressor decompressor = new HuffmanDecompressor();

        // Rutas fijas de ejemplo
        String txt1   = "C:\\Users\\dquan\\OneDrive\\Documentos\\Diego Quan\\UVG\\Ciclo 3\\Algoritmos y Estructura de datos\\Hoja 9\\HDT-9\\ejemplo1.txt";
        String huff1  = "C:\\Users\\dquan\\OneDrive\\Documentos\\Diego Quan\\UVG\\Ciclo 3\\Algoritmos y Estructura de datos\\Hoja 9\\HDT-9\\ejemplo1.huff";
        String tree1  = "C:\\Users\\dquan\\OneDrive\\Documentos\\Diego Quan\\UVG\\Ciclo 3\\Algoritmos y Estructura de datos\\Hoja 9\\HDT-9\\ejemplo1.hufftree";
        String out1   = "C:\\Users\\dquan\\OneDrive\\Documentos\\Diego Quan\\UVG\\Ciclo 3\\Algoritmos y Estructura de datos\\Hoja 9\\HDT-9\\ejemplo1_decoded.txt";

        while (true) {
            System.out.println("==========================================");
            System.out.println("       COMPRESOR / DESCOMPRESOR     ");
            System.out.println("==========================================");
            System.out.println("1) Comprimir ejemplo1.txt");
            System.out.println("2) Descomprimir ejemplo1.huff");
            System.out.println("3) Salir");
            System.out.print("Seleccione una opcion [1-3]: ");

            String opcion = scanner.nextLine().trim();

            if (opcion.equals("1")) {
                try {
                    compressor.compress(txt1, huff1, tree1);
                    System.out.println("\n--- Compresión completada! ---");
                    System.out.println("Archivo comprimido:  " + Paths.get(huff1).toAbsolutePath());
                    System.out.println("Archivo hufftree:    " + Paths.get(tree1).toAbsolutePath());
                } catch (Exception e) {
                    System.err.println("Error durante la compresión:");
                    e.printStackTrace();
                }

            } else if (opcion.equals("2")) {
                try {
                    decompressor.decompress(huff1, tree1, out1);
                    System.out.println("\n--- Descompresión completada! ---");
                    System.out.println("Archivo restaurado:  " + Paths.get(out1).toAbsolutePath());
                } catch (Exception e) {
                    System.err.println("Error durante la descompresión:");
                    e.printStackTrace();
                }

            } else if (opcion.equals("3")) {
                break;
            } else {
                System.out.println("Opción no válida, intente nuevamente.");
            }

            System.out.println(); // Línea en blanco antes de repetir
        }

        scanner.close();
    }
}
