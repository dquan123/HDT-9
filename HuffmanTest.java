import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class HuffmanTest {

    private HuffmanCompressor compressor;
    private HuffmanDecompressor decompressor;

    @BeforeEach
    void setUp() {
        compressor   = new HuffmanCompressor();
        decompressor = new HuffmanDecompressor();
    }

    @Test
    void testCompressDecompressSimple(@TempDir Path tmp) throws IOException {
        // Preparamos un archivo con contenido sencillo
        Path orig = tmp.resolve("simple.txt");
        String content = "ABRACADABRA";
        Files.writeString(orig, content, StandardCharsets.UTF_8);

        // Rutas de salida
        Path huff = tmp.resolve("simple.huff");
        Path tree = tmp.resolve("simple.hufftree");
        Path dec  = tmp.resolve("simple_decoded.txt");

        // Ejecución
        compressor.compress(orig.toString(), huff.toString(), tree.toString());
        decompressor.decompress(huff.toString(), tree.toString(), dec.toString());

        // Verificaciones
        assertTrue(Files.exists(huff),  "El archivo .huff debe existir");
        assertTrue(Files.exists(tree), "El archivo .hufftree debe existir");

        String result = Files.readString(dec, StandardCharsets.UTF_8);
        assertEquals(content, result,
            "El texto descomprimido debe coincidir exactamente con el original");
    }

    @Test
    void testCompressDecompressEmpty(@TempDir Path tmp) throws IOException {
        // Archivo vacío
        Path orig = tmp.resolve("empty.txt");
        Files.writeString(orig, "", StandardCharsets.UTF_8);

        Path huff = tmp.resolve("empty.huff");
        Path tree = tmp.resolve("empty.hufftree");
        Path dec  = tmp.resolve("empty_decoded.txt");

        compressor.compress(orig.toString(), huff.toString(), tree.toString());
        decompressor.decompress(huff.toString(), tree.toString(), dec.toString());

        assertTrue(Files.exists(huff),  "El .huff se debe crear incluso si el texto está vacío");
        assertTrue(Files.exists(tree), "El .hufftree se debe crear incluso si el texto está vacío");

        String result = Files.readString(dec, StandardCharsets.UTF_8);
        assertEquals("", result,
            "Un archivo vacío debe descomprimirse a una cadena vacía");
    }

    @Test
    void testCompressDecompressUnicode(@TempDir Path tmp) throws IOException {
        // Contenido con caracteres Unicode
        Path orig = tmp.resolve("unicode.txt");
        String content = "áéíóú 😊";
        Files.writeString(orig, content, StandardCharsets.UTF_8);

        Path huff = tmp.resolve("unicode.huff");
        Path tree = tmp.resolve("unicode.hufftree");
        Path dec  = tmp.resolve("unicode_decoded.txt");

        compressor.compress(orig.toString(), huff.toString(), tree.toString());
        decompressor.decompress(huff.toString(), tree.toString(), dec.toString());

        assertTrue(Files.exists(huff),  "El .huff debe generarse para texto Unicode");
        assertTrue(Files.exists(tree), "El .hufftree debe generarse para texto Unicode");

        String result = Files.readString(dec, StandardCharsets.UTF_8);
        assertEquals(content, result,
            "La descompresión debe soportar correctamente caracteres Unicode");
    }
}
