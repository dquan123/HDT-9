public class HuffmanNode implements Comparable<HuffmanNode> {
    public final char character;      
    public final int frequency;
    public final HuffmanNode left, right;
    public final boolean isLeaf;

    // Hoja
    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = this.right = null;
        this.isLeaf = true;
    }

    // Nodo interno
    public HuffmanNode(HuffmanNode left, HuffmanNode right) {
        this.character = '\0';
        this.frequency = left.frequency + right.frequency;
        this.left = left;
        this.right = right;
        this.isLeaf = false;
    }

    @Override
    public int compareTo(HuffmanNode other) {
        return Integer.compare(this.frequency, other.frequency);
    }
}
