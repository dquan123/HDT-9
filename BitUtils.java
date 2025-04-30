import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitUtils {
    // Escribe cadena "01011…" como bits en out
    public static void writeBits(String bitString, OutputStream out) throws IOException {
        int bitCount = bitString.length();
        int fullBytes = bitCount / 8;
        int remBits   = bitCount % 8;
        byte b;
        int idx = 0;

        for (int i = 0; i < fullBytes; i++) {
            b = 0;
            for (int j = 0; j < 8; j++, idx++) {
                b <<= 1;
                if (bitString.charAt(idx) == '1') b |= 1;
            }
            out.write(b);
        }
        if (remBits > 0) {
            b = 0;
            for (int j = 0; j < remBits; j++, idx++) {
                b <<= 1;
                if (bitString.charAt(idx) == '1') b |= 1;
            }
            b <<= (8 - remBits);
            out.write(b);
        }
    }

    // Lee todos los bytes de in y devuelve "01011…"
    public static String readBits(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int next;
        while ((next = in.read()) != -1) {
            for (int i = 7; i >= 0; i--) {
                sb.append(((next >> i) & 1) == 1 ? '1' : '0');
            }
        }
        return sb.toString();
    }
}
