package encryption;

public class HuffmanTree {
	private static class Node{
		public Letter data;
		public Node left, right;
		public Node(Letter data){
			this.data = data;
		}
	}
	private boolean mapSet = false;
	private float avgLength = -1;
	private String[] map = new String[256]; //maps char to bit string
	private Node root; 
	private String inflattened = "", preflattened = "";
	public final static String SEPARATOR = ",", END = "END";

	public static HuffmanTree buildTree(String signature){
		if(signature.indexOf(HuffmanTree.END) == -1){
			throw new IllegalArgumentException("Bad tree signature");
		}
		String[] parts = signature.split(HuffmanTree.END);
		if(parts.length != 2){
			//last part is empty (2 total)
			throw new IllegalArgumentException("Bad tree signature");
		}
		String preorder = parts[0];
		String inorder = parts[1];
		return buildTree(preorder, inorder);
	}
	/*
	builds a tree from a flattened string
	since static, use a different generic variable

	This needs to be rethought since the trees contain duplicates of the 0 value. I need to separate nodes somehow.
	*/
	public static HuffmanTree buildTree(String pre, String in){
		HuffmanTree newTree = new HuffmanTree();
		String[] pre_arr = pre.split(HuffmanTree.SEPARATOR);
		String[] in_arr = in.split(HuffmanTree.SEPARATOR);
		Node n = buildTree(pre_arr,in_arr,0,in_arr.length-1,0);
		newTree.root = n;
		return newTree;
	}

	private static Node buildTree(String[] pre, String[] in, int lo, int hi, int preI){
		if(lo > hi || preI >= pre.length){
			//on empty tree, 
			return null;
		}
		String target = pre[preI];
		int id = Integer.parseInt(target);
		Node newNode = new Node(new Letter(id,0));
		int j;
		for(j = lo; j <= hi; j++){
			if(in[j].equals(target)){
				break;
			}
		}
		//System.out.println(j);
		newNode.left = buildTree(pre, in, lo, j-1, preI+1);
		newNode.right = buildTree(pre, in, j+1, hi, preI+(j-lo) + 1);
		return newNode;
	}

	public void addTree(HuffmanTree otherTree, Letter topNodeData){
		//make new node with added frequencies
		Node topNode = new Node (topNodeData);
		topNode.left = this.root;
		topNode.right = otherTree.root;
		this.root = topNode;
	}

	public void insert(Letter data){
		Node newNode = new Node(data);
		if(root == null){
			root = newNode;
			return;
		}
		Node prev = null, ptr = root;
		int c = 0;
		while(ptr != null){
			prev = ptr;
			c = data.compareTo(ptr.data);
			if(c < 0){
				ptr = ptr.left;
			}
			else{
				ptr = ptr.right;
			}
		}
		if(c < 0){
			prev.left = newNode;
		}
		else{
			prev.right = newNode;
		}
	}

	private void pInOrder(Node n){
		if(n == null){
			return;
		}
		else{
			pInOrder(n.left);
			inflattened += n.data.toString() + HuffmanTree.SEPARATOR;
			pInOrder(n.right);
		}
	}

	private void pPreOrder(Node n){
		if(n == null){
			return;
		}
		else{
			preflattened += n.data.toString() + HuffmanTree.SEPARATOR;
			pPreOrder(n.left);
			pPreOrder(n.right);
		}
	}

	public Letter getRootData(){
		return root.data;
	}

	//returns the average length
	public void getCodes(int totalLength){
		if(mapSet){
			return;
		}
		mapSet = true;
		getCodes(root, "", totalLength);
	}
	private void getCodes(Node n, String str, int totalLength){
		if(n == null){
			return;
		}
		getCodes(n.left, str + "0", totalLength);
		if(n.left == null && n.right == null){
			//leaf node
			map[n.data.id] = str;
			avgLength += str.length() * ((float)n.data.n) / totalLength;
		}
		getCodes(n.right, str + "1", totalLength);
	}

	public String encode(String input){
		//first get the map of the letters
		getCodes(input.length());
		String toReturn = "";
		for(int i = 0; i < input.length(); i++){
			toReturn += map[(int)input.charAt(i)];
		}
		return toReturn;
	}

	public String decode(String input){
		String decoded = "";
		Node ptr = root;
		for(int i = 0; i < input.length(); i++){
			char c = input.charAt(i);
			if(ptr.left == null && ptr.right == null){
				//leaf
				decoded += ptr.data.asChar();
				ptr = root;
			}
			if(c == '1'){
				ptr = ptr.right;
			} else if(c == '0'){
				ptr = ptr.left;
			}
			if(ptr == null){
				throw new IllegalArgumentException("Invalid code");
			}
		}
		decoded += ptr.data.asChar();
		return decoded;
	}
	//returns flattened tree
	public String toString(){
		preflattened = "";
		inflattened = "";
		pPreOrder(root);
		pInOrder(root);
		preflattened = preflattened.substring(0, preflattened.length() - 1);
		inflattened = inflattened.substring(0, inflattened.length() - 1);
		//since the trees will not have duplicate characters, searching for the last occurrence of the repeated characters should give the correct end
		return preflattened + HuffmanTree.END + inflattened + HuffmanTree.END;
	}
}