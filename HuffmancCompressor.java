import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanCompressor {
    private Map<Character,String> codeMap;
    private HuffmanNode root;

    private Map<Character,Integer> buildFreqMap(String text) {
        Map<Character,Integer> freq = new HashMap<>();
        for (char c : text.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        return freq;
    }

    private void buildTree(Map<Character,Integer> freqMap) {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        freqMap.forEach((ch, f) -> pq.add(new HuffmanNode(ch, f)));
        while (pq.size() > 1) {
            HuffmanNode n1 = pq.poll();
            HuffmanNode n2 = pq.poll();
            pq.add(new HuffmanNode(n1, n2));
        }
        root = pq.poll();
    }

    private void buildCodeMap(HuffmanNode node, String prefix) {
        if (node.isLeaf) {
            codeMap.put(node.character, prefix.length()>0 ? prefix : "0");
            return;
        }
        buildCodeMap(node.left,  prefix + '0');
        buildCodeMap(node.right, prefix + '1');
    }

    public void compress(String inTxt, String outHuff, String outTree) throws IOException {
        // 1) Leer texto completo
        String text = new String(
            Files.readAllBytes(Paths.get(inTxt)),
            StandardCharsets.UTF_8
        );

        // 2) Árbol de Huffman
        Map<Character,Integer> freqMap = buildFreqMap(text);
        buildTree(freqMap);

        // 3) Generar códigos
        codeMap = new HashMap<>();
        buildCodeMap(root, "");

        // 4) Serializar árbol
        ByteArrayOutputStream treeBos = new ByteArrayOutputStream();
        DataOutputStream treeDos = new DataOutputStream(treeBos);
        serializeTree(root, treeDos);
        treeDos.close();
        Files.write(Paths.get(outTree), treeBos.toByteArray());

        // 5) Codificar texto y escribir bits
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(codeMap.get(c));
        }
        try (OutputStream huffOut = new BufferedOutputStream(new FileOutputStream(outHuff))) {
            BitUtils.writeBits(sb.toString(), huffOut);
        }
    }

    private void serializeTree(HuffmanNode node, DataOutputStream dos) throws IOException {
        if (node.isLeaf) {
            dos.writeBoolean(true);
            dos.writeChar(node.character);
        } else {
            dos.writeBoolean(false);
            serializeTree(node.left,  dos);
            serializeTree(node.right, dos);
        }
    }
}