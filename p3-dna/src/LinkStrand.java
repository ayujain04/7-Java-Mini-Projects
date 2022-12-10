import java.util.spi.CurrencyNameProvider;

public class LinkStrand implements IDnaStrand {


private Node myFirst, myLast;
private long mySize;
private int myAppends;
private int myIndex; 
private Node myCurrent; 
private int myLocalIndex;




    public LinkStrand(){
		this("");
	}
    public LinkStrand(String s) {
		initialize(s);
        mySize = s.length(); 
	}

    private class Node{
        String info; 
        Node next; 
        Node(String s){
            info = s; 
        }
        Node(String s, Node node){
            info = s; 
            next = node; 
        }

        
    }


    @Override
    public long size() {
        return mySize;
    }

    @Override
    public void initialize(String source) {
        myFirst = new Node(source);
        myLast = myFirst; 
        mySize=source.length(); 
      
        myLast = myFirst;
  myAppends = 0;
 myIndex = 0; 
  myCurrent = myFirst; 
  myLocalIndex = 0;


    }


  
    @Override
    public IDnaStrand getInstance(String source) {
        // TODO Auto-generated method stub
        return new LinkStrand(source);
    }

    @Override
    public IDnaStrand append(String dna) {
        Node n = new Node(dna); 
        myLast.next = n; 
        myLast = n; 
        myAppends++; 
        mySize+=dna.length(); 
        return this;
    }

    /*
     * StringBuilder copy = new StringBuilder(myInfo);
		StringBuilderStrand ss = new StringBuilderStrand("replace");
		copy.reverse();
		ss.myInfo = copy;
		return ss;
     */

    private void revAppend(String s){
        Node p = new Node(s);
        mySize += s.length(); 
        p.next = myFirst; 
        myFirst = p;
    }
   
    @Override
    public IDnaStrand reverse() {
        Node current = myFirst; 
        LinkStrand lin = new LinkStrand(); 

        while(current!=null){
            StringBuilder s = new StringBuilder(current.info);
            lin.revAppend(s.reverse().toString());
            current = current.next; 
        }
        return lin; 
     
    }

    @Override
    public int getAppendCount() {
        return myAppends;
       
    }

    @Override
    public char charAt(int index) throws IndexOutOfBoundsException {
        
        if(index>=size() || index<0){
            throw new IndexOutOfBoundsException();
        }

      
        if(myIndex>=index){
                myIndex = 0; 
                myLocalIndex = 0; 
                myCurrent = myFirst; 
        }

       while(myIndex!=index){
       myLocalIndex++; 
       myIndex++; 
       if(myCurrent.next!=null && myCurrent.info.length()<=myLocalIndex){
            myLocalIndex =0; 
            myCurrent = myCurrent.next; 
       }
       }
       return myCurrent.info.charAt(myLocalIndex);
           
    }
    
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder(); 
       Node cur = myFirst; 
        while(cur != null){
            s.append(cur.info); 
            cur = cur.next; 
        }

        return s.toString();

    }
}
