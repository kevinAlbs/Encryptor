package encryption;

public class Letter implements Comparable<Letter>{
	public int n; //num of occurrences
	public int id = -1;
	private static int UNIQUE_ID = -1;

	public boolean isMyLetter = true; //false if this is a non-leaf node in the tree
	public Letter(int id, int n){
		this.id = id;
		this.n = n;
	}
	public Letter(){
		//for inner uninitialized nodes needing a unique id
		this.id = UNIQUE_ID;
		UNIQUE_ID--;
		this.n = 0;
	}
	public int compareTo(Letter o){
		//Letter l = (Letter)o;
		return this.n - o.n;
	}
	public String toString(){
		return this.id + "";
	}
	public String asChar(){
		return (char)this.id + "";
	}
}