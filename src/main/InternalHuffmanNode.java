package main;

public class InternalHuffmanNode implements HuffmanNode {

	private HuffmanNode _left = null;
	private HuffmanNode _right = null;
	private InternalHuffmanNode _par = null;
	private int _count = 0;
	
	public InternalHuffmanNode() {
		
	}
	
	public InternalHuffmanNode(int count) {
		_count = count;
	}
	
	public InternalHuffmanNode(InternalHuffmanNode par) {
		_par = par;
	}
	
	public void makeParent(InternalHuffmanNode par) {
		_par = par;
	}
	
	public int count() {
		return _count;
	}

	public boolean isLeaf() {
		return false;
	}

	public int symbol() {
		throw new RuntimeException("Internal nodes have no symbol.");
	}

	public int height() {
		int left_height = 0;
		int right_height = 0;
		HuffmanNode left = this;
		HuffmanNode right = this;
		if(!left.isLeaf()) {
			left = left.left();
			left_height = 1 + left.height();
		} else {
			left_height = 1;
		}
		if(!right.isLeaf()) {
			right = right.right();
			right_height = 1 + right.height();
		} else {
			right_height = 1;
		}
		return Math.max(right_height, left_height);
			
	}

	public boolean isFull() {
		boolean left = false;
		boolean right = false;
		if(left() != null && right() != null) {
			left = left().isLeaf();
			right = right().isLeaf();
			if(!(left && right)) {
				left = left().isFull();
				right = right().isFull();
			}
			
		} 
		return left && right;
	}

	public boolean insertSymbol(int length, int symbol) {
		if(length == 1) {
			if(_left == null) {
				_left = new LeafHuffmanNode(this, new SymbolWithCodeLength(symbol, getDepth()+1));
				return true;
			} else if(_right == null){
				_right = new LeafHuffmanNode(this, new SymbolWithCodeLength(symbol, getDepth()+1));
				return true;
			} else {
				return false;
			}
		} else {
			if(_left != null && !_left.isFull()) {
				return _left.insertSymbol(length-1,symbol);
			} else if(_right != null && !_right.isFull()) {
				return _right.insertSymbol(length-1,symbol);
			} else if(_left == null){
				_left = new InternalHuffmanNode(this);
				return _left.insertSymbol(length-1, symbol);
			} else if(_right == null) {
				_right = new InternalHuffmanNode(this);
				return _right.insertSymbol(length-1, symbol);
			} else {
				return false;
			}
			
		}
	}

	public HuffmanNode left() {
		return _left;
	}

	public HuffmanNode right() {
		return _right;
	}
	
	public InternalHuffmanNode par() {
		return _par;
	}
	
	public int getDepth() {
		if(par() == null) {
			return 0;
		} else {
			return 1+par().getDepth();
		}
	}
	
	public void makeLeft(HuffmanNode n) {
		_left = n;
	}
	
	public void makeRight(HuffmanNode n) {
		_right = n;
	}

	public void removeLeft() {
		_left = null;
	}

	public void removeRight() {
		_right = null;
		
	}

}
