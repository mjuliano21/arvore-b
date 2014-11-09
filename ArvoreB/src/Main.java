
public class Main {

	public static void main(String[] args) {
		ArvoreB tree = new ArvoreB(1);
		tree.insertKey(99);
		tree.insertKey(75);
		tree.insertKey(63);
		tree.insertKey(50);
		tree.insertKey(30);
		tree.insertKey(25);
		tree.insertKey(22);
		tree.insertKey(20);
		tree.insertKey(19);
		tree.insertKey(1);
		tree.insertKey(7);
		tree.insertKey(8);
		tree.insertKey(12);
		tree.insertKey(15);
		tree.insertKey(18);
		tree.insertKey(17);
		tree.insertKey(21);
		tree.insertKey(80);
		tree.insertKey(64);
		tree.insertKey(65);
		System.out.println(tree.printBTree());
		
		tree.removeKey(7);
		System.out.println(tree.printBTree());
	}

}
