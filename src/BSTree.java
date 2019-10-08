import java.util.HashSet;
import java.util.LinkedList;

public class BSTree implements BSTOper {
    public Node rot = null;
    public Node root = new Node();
    public Node current = null;
    public int height = 0;
    public int blackH = 0;


    public static void main(String[] args) {
        BSTree tre = new BSTree();

        int[] values ={3,1,5,7,6,8,9,10};
        tre.addAll(values);
        tre.printOutTree();
        System.out.println(tre.size());


    }

    private class Node {
        Node left, right; // verdier i venstre subtre er < verdien i noden selv
        // verdier i høyre subtre er > verdien i noden selv
        int value; // konstruktører, programmer disse
        Node parent;
        boolean isBlack;

        Node( ) { }

        Node( int v ) {
            value = v;
        }
        Node(int v, Node parent){
            value = v;
            this.parent = parent;

        }
        Node(int v, Node parent, boolean isBlack){
            value = v;
            this.parent = parent;
            this.isBlack = isBlack;

        }

    }
    public boolean blackNode(Node n){
        return n == null || n.isBlack;
    }

    public void printOutTree(){
        System.out.println();
        boolean[] finished = new boolean[50];
        printOutTree(root.left, 0, finished);
        System.out.println();
    }

    private void printOutTree(Node n, int depth, boolean[] finished){
        finished[depth] = false;
        String beginner = "";
        for(int i = 0; i< depth; i++){
            if(i == depth - 1){
                beginner += "|---";
            }else{

                if(i < depth - 1 && finished[i]){
                    beginner+="    ";
                }else{
                    beginner += "|   ";
                }
            }
        }
        if(n == null){
            System.out.println(beginner);
            return;
        }
        String color = "";
        if (n.isBlack) color = "B";
        else color = "R";
        System.out.println(beginner+n.value+color);
        if(n.left == null && n.right == null){
            return;
        }
        printOutTree(n.right, depth + 1, finished);
        finished[depth] = true;
        printOutTree(n.left, depth +1, finished);
    }

    public void add( int value ){
        if (root.left == null){
            root.left = new Node(value, root, true);
            rot = root.left;

            return;
        } else {
            add(value, root.left);
        }
    }

    private void add(int value, Node n){
        if (value > n.value){
            if(n.right == null){
                n.right = new Node(value,n);
                restructure(n.right);
                return;
            } else{
                n = n.right;
            }
        } else if (value <= n.value){
            if(n.left == null){
                n.left = new Node(value,n);
                restructure(n.left);
                return;
            } else{
                n = n.left;
            }
        }
        add(value, n);
    }                               //HVIS TANTE ER RØD: color flip = rød forelder, svarte barn
                                    //HVIS TANTE ER SVART: rotasjon = svart forelder, røde barn

    public void restructure(Node ny){
        if (ny == rot){
            ny.isBlack = true;
            return;
        }

        Node p = ny.parent;
        Node g = ny.parent.parent;
        Node t;
        if (g.left == p) t = g.right;
        else t = g.left;

        if (blackNode(ny.parent)){                                     //1 hvis forelder er svart - ingen problem
            return;
        } else {

            //hvis rød tante - color flip
            if (!blackNode(t)) {
                System.out.println("Flipping colors- Aunt " + t.value + t.isBlack);
                t.isBlack = true;
                p.isBlack = true;
                ny.isBlack = false;

               restructure(g);                  //kjører rekursjon på bestefar for å sjekke fargestruktur lengre opp

            //hvis svart tante - rotasjon
            } else {
                System.out.println("Starting rotation - Aunt " + t + blackNode(t));
                if (g.left == t) {                                      //hvis tante er en venstre node
                    if (p.left == ny) {                                     //hvis ny også er en venstre node
                        doubleRotation(g, false);
                    } else {                                                //hvis ny er en høyre node
                        singleRotation(g, true);
                    }
                } else if (g.right == t) {
                    if (p.right == ny) {
                        doubleRotation(g, true);
                    } else {
                        singleRotation(g, false);
                    }

                }
                printOutTree();
                //må først sjekke totale svarte i stiene
                p.isBlack = true;                               //endrer farger på rot og barn hvis vi roterer
                if (p.left != null) p.left.isBlack = false;
                if (p.right != null) p.right.isBlack = false;
            }
        }
        printOutTree();
        return;

    }




    int minValue(Node n)
    {
        int minv = n.value;
        while (n.left != null)
        {
            minv = n.left.value;
            n = n.left;
        }
        return minv;
    }

    Node singleRotation(Node a, boolean left){  //velg besteforelder til noden du skal rotere
        if (a == null) {
            System.out.println("Rotation not possible");
            return null;
        }
        if (left) {

            Node p = a.parent;
            Node b = a.right;


            a.right = b.left;
            if (b.left != null) b.left.parent = a;

            b.left = a;
            a.parent = b;
            b.parent = p;
            if (p != null) {
                if (p.left == a) {
                    p.left = b;
                } else {
                    p.right = b;
                }
            }
            return b;


        } else {
            Node p = a.parent;
            Node b = a.left;

            a.left = b.right;
            if (b.right != null) b.right.parent = a;

            b.right = a;
            a.parent = b;
            b.parent = p;
            if (p != null) {
                if (p.left == a) {
                    p.left = b;
                } else {
                    p.right = b;
                }
            }
            return b;       //den nye parenten

        }
    }


    public Node doubleRotation(Node a, boolean leftRight){
        if (leftRight){
            a.left = singleRotation(a.left, true);
            return singleRotation(a, false);
        } else {
            a.right = singleRotation(a.right, false);

            return  singleRotation(a, true);
        }
    }


    public boolean remove(int value)
    {
        Node n = deleteRec(root.left, value);
        return n != null;
    }

    private Node deleteRec(Node n, int value)
    {
        if (n == null)  return n;

        if (value < n.value)
            n.left = deleteRec(n.left, value);      //endrer verdi på n.left til resultatet av rekursjonen
        else if (value > n.value)
            n.right = deleteRec(n.right, value);    //samme som n.left

        else
        {
            if (n.left == null)
                return n.right;
            else if (n.right == null)
                return n.left;

            n.value = minValue(n.right);

            n.right = deleteRec(n.right, n.value);
        }

        return n;
    }
    public int size(){
        if (root.left == null) return 0;
         return size(root.left);
    }

    public int size(Node n){
        int count = 1;
        if (n.left != null) {
            count += size(n.left);
        }
        if (n.right != null) {
           count += size(n.right);
        }
        return count;

    }
    public boolean existsInTree( int value ){
        if (root.left == null) return false;
        return existsInTree(value, root.left);
    }

    public boolean existsInTree(int value, Node n){
        if (n.value == value) {
            return true;
        }
        if (value > n.value) {
            if (n.right != null) return existsInTree(value, n.right);
        }
        if (value <= n.value) {
            if (n.left != null) return existsInTree(value, n.left);
        }
        return false;
    }
    public int findNearestSmallerThan( int value ){
        if (root.left == null) return -999;
        HashSet<Integer> nodes = new HashSet<>();
        nodes = findNearestSmallerThan(root.left, value, nodes);
        int returnNode = -999;
        for (int i:nodes){
            if (i > returnNode) returnNode = i;
        }
        return returnNode;
    }
    private HashSet<Integer> findNearestSmallerThan(Node n, int value, HashSet<Integer> nodes){

        if (n.value < value){
            nodes.add(n.value);
        }
        if (n.left != null) findNearestSmallerThan(n.left, value, nodes);
        if (n.right != null) findNearestSmallerThan(n.right, value,nodes);

        return nodes;
    }
    public void addAll( int[] integers ){
        for (int i:integers){
            add(i);
        }
    }
    public int[] sortedArray(){ // inorder
        if (root.left == null) return null;
        int[]nodesSorted = new int[size()];
        LinkedList<Integer> nodes = new LinkedList<>();
        nodes = sortedArray(root.left, nodes);
        for (int i = 0; i< nodes.size();i++){
            System.out.println(i);
            nodesSorted[i] = nodes.get(i);
        }
       return nodesSorted;
    }
    public LinkedList<Integer> sortedArray(Node n, LinkedList<Integer> nodes){ // inorder
        if (n.left != null) sortedArray(n.left, nodes);

        nodes.add(n.value);

        if (n.right != null) sortedArray(n.right, nodes);

        return nodes;
    }

    public int[] findInRange (int low, int high){
        if (root.left == null) return null;
        HashSet<Integer> nodes = new HashSet<>();
        nodes = findInRange(root.left, low, high, nodes);

        int[]nodesArr = new int[nodes.size()];
        int i = 0;
        for (int n:nodes){
            nodesArr[i] = n;
            i++;
        }
        return nodesArr;

    }
    public HashSet findInRange (Node n, int low, int high, HashSet<Integer> nodes){
        if (n.value<=high && n.value >= low){
            nodes.add(n.value);
        }
        if (n.left != null) findInRange(n.left, low, high, nodes);
        if (n.right != null) findInRange(n.right, low, high, nodes);

        return nodes;
    }

    public Node findParent( Node n ){
        System.out.println(n.parent.value);
        return n.parent;
    }
    public Node findGrandparent( Node n ){
        System.out.println(findParent(findParent(n)));
        return findParent(findParent(n));
    }
    private Node find( int value ){
        if (root.left == null) return null;
        return find(root.left, value);

    }
    private Node find(Node n, int value){
        if (n.value == value) {
            return n;
        }
        if (value > n.value) {
            if (n.right != null) return find(n.right, value);
        }
        if (value <= n.value) {
            if (n.left != null) return find(n.left, value);
        }
        return null;
    }
}