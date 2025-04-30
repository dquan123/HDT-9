import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HuffmanDecompressor {

    private HuffmanNode readTree(DataInputStream dis) throws IOException {
        boolean isLeaf = dis.readBoolean();
        if (isLeaf) {
            char c = dis.readChar();
            return new HuffmanNode(c, 0);
        } else {
            HuffmanNode left  = readTree(dis);
            HuffmanNode right = readTree(dis);
            return new HuffmanNode(left, right);
        }
    }

    public void decompress(String inHuff, String inTree, String outTxt) throws IOException {
        // 1) Reconstruir árbol
        byte[] treeBytes = Files.readAllBytes(Paths.get(inTree));
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(treeBytes));
        HuffmanNode root = readTree(dis);

        // 2) Leer bits comprimidos
        try (InputStream huffIn = new BufferedInputStream(new FileInputStream(inHuff))) {
            String bits = BitUtils.readBits(huffIn);

            // 3) Decodificar
            BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(outTxt),
                StandardCharsets.UTF_8
            );
            HuffmanNode node = root;
            for (char bit : bits.toCharArray()) {
                node = (bit == '0') ? node.left : node.right;
                if (node.isLeaf) {
                    writer.write(node.character);
                    node = root;
                }
            }
            writer.close();
        }
    }
}