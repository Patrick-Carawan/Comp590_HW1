package main;
import java.lang.Exception;

public class LeafHuffmanNode implements HuffmanNode {
	
	private InternalHuffmanNode _parent = null;
	private SymbolWithCodeLength _pair;
	private int _count = -1;
	private int _value = -1;
	
	public  LeafHuffmanNode(InternalHuffmanNode parent, SymbolWithCodeLength pair) {
		_parent = parent;
		_pair = pair;
	}
	
	public LeafHuffmanNode(int count, int value) {
		_count = count;
		_value = value;
		_pair = new SymbolWithCodeLength(value, -1);
	}
	
	public int count() {
		return _count;
	}
	

	public boolean isLeaf() {
		return true;
	}

	public int symbol() {
		//return Integer.parseInt(Integer.toBinaryString(_pair.value()));
		return _pair.value();
	}

	public int height() {
		return 0;
	}

	public boolean isFull() {
		return true;
	}

	public boolean insertSymbol(int length, int symbol) {
		throw new RuntimeException("Cannot insert below a leaf.");
	}

	public HuffmanNode left(){
		throw new RuntimeException("Leaves have no children.");
	}

	public HuffmanNode right() {
		throw new RuntimeException("Leaves have no children.");
	}
	
	public void makeParent(InternalHuffmanNode par) {
		_parent = par;
	}

	public void removeLeft() {
		throw new RuntimeException("Leaves have no children.");
		
	}

	public void removeRight() {
		throw new RuntimeException("Leaves have no children.");
	}

}
