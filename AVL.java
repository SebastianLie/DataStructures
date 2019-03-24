class AVL {
  protected Node head;
  public AVL() {
    head = null;
  }
  public String search(String s) {
    Node res = search(head, s);
    return res == null ? "" : res.name;
  }
  protected Node search(Node N, String s) {
    while (N != null) {
      if (s.compareTo(N.name)==0) return N;
      else if (s.compareTo(N.name)>0) N = N.right;
      else N = N.left;
    }
    return N;
  }
  public void insert(String s) { head = insert(head, s); }
  protected Node insert(Node N, String s) {
    if (N == null){
      return new Node<String>(s);
    }
    if (N.name.compareTo(s)<0) {
      N.right = insert(N.right, s);
      N.right.parent = N;
    } else {
      N.left = insert(N.left, s);
      N.left.parent = N;
    }
    updateNode(N);
    return updateBal(N);
  }
  public void remove(String patientName) {head = remove(head,patientName);}
  protected Node remove(Node N, String patientName) {
    if (N == null){return N;}
    if (N.name.compareTo(patientName)==0)   {
      // N is leaf
      if (N.left == null && N.right == null)
      return null;
      // N has a right child
      else if (N.left == null && N.right != null) {
        N.right.parent = N.parent;
        N = N.right;
      }
      // N has a left child
      else if (N.left != null && N.right == null) {
        N.left.parent = N.parent;
        N = N.left;
      }
      //N has 2 children
      else {
        Node succ = succ(patientName);
        N.name = succ.name;
        N.right = remove(N.right, succ.name);
      }
    }else if (N.name.compareTo(patientName)>0){
      N.left = remove(N.left, patientName);
    }else{
      N.right = remove(N.right, patientName);
    }
    N = updateBal(N);
    updateNode(N);
    return N;
  }
  public Node updateBal(Node N) {
    if (Math.abs(height(N.left)-height(N.right))<2) return N;
    else if (height(N.left) < height(N.right)) {
      if (height(N.right.right) < height(N.right.left)) {
        N.right = rightRotate(N.right);
      }
      N = leftRotate(N);
    } else {
      if (height(N.left.left) < height(N.left.right)){
        N.left = leftRotate(N.left);
      }
      N = rightRotate(N);
    }
    return N;
  }

  public int rank(String s) { //fully correct now LOL
    //returns rank of node that is lexicographically closest,
    //for start greater than equal to
    //for end is always less than
    Node N = head;
    int ranknum = 0;
    // whenever encounter possible node that has name smaller than string
    // record rank of node, add to ranknum, then go right as number wanted is
    // one less than first node in interval, which is rank of node before it.
    // then travel on and try to find a node that is lesser,
    // but still more than s, and if lesser, just go left
    while (N != null) {
      if (s.compareTo(N.name) > 0) {
        ranknum += size(N.left)+1;
        N = N.right;
      }
      else if (s.compareTo(N.name) == 0) { //is not +1 here because interval is [START,END), ,
        return ranknum + size(N.left);  //i.e less than end, so rank always no need count curr node in if it is equal to intervalS
      }
      else {
        N = N.left;
      }
    }
    return ranknum; //aw yiss my rank method is correct yeet
  }
  public void adjustP(Node N,Node temp, Node temp2){
    temp.parent = N.parent; //update nodes parents
    N.parent = temp;
    if (temp2 != null) {
      temp2.parent = N;
      updateNode(temp2); //update bf
    }
    updateNode(N); //update bf
    updateNode(temp);
    if (head == N) head = temp; //if n is the head
  }
  public Node leftRotate(Node N) { //works!!
    Node temp = N.right;   //swap nodes
    Node temp2 = temp.left;
    temp.left = N;
    N.right = temp2;
    if (N.parent != null) { //change N.parent.child to temp to detach node
      if (N.parent.left == N) N.parent.left = temp;
      else N.parent.right = temp;
    }
    adjustP(N,temp,temp2);
    return temp;
  }
  public Node rightRotate(Node N) {
    Node temp = N.left; //swap nodes
    Node temp2 = temp.right;
    temp.right = N;
    N.left = temp2;
    if (N.parent != null) { //change N.parent.child to temp
      if (N.parent.left == N) N.parent.left = temp;
      else N.parent.right = temp;
    }
    adjustP(N,temp,temp2);
    return temp;
  }
  public Node succ(String s) {
    Node N = search(head,s);
    if (N.right == null && N.left == null) { //node is leaf
      Node next = N.parent;
      if (next == head) return null;
      //must go up, next right turn,ie N is left of parent,
      //is where succ is
      while (next.parent != null && N != next.left) {
        N = next;
        next = next.parent;
      }
      return next;
    }
    else { //else go down,find leftmost node
      Node next = N.right;   //of immediate right subtree
      while (next.left != null) { next = next.left;}
      return next;
    }
  }
  public int max(int a, int b) { return (a > b) ? a : b;}
  public int min(int a, int b) { return (a < b) ? a : b;}
  public int height(Node N) {
    if (N == null) return 0;
    return N.height;
  }
  public int size(Node N) {
    //gives size below
    if (N == null) return 0;
    else return N.size;
  }
  public void updateNode(Node N){
    N.height = max(height(N.left), height(N.right))+1;
    N.size = size(N.left) + size(N.right) + 1;
  }
}
class Node<F> { // for string, but can always be for other data types
  public Node parent, left, right;
  public F name;
  public int height, size;
  Node(F s) {
    name = s;
    parent = left = right = null;
    height = 0; size = 1;
  }
}
