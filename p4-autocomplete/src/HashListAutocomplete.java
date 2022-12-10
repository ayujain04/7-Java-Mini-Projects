import java.util.*;

public class HashListAutocomplete implements Autocompletor {
    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> map; 
    private int mySize;

    public HashListAutocomplete(String[] terms, double[] weights) {
        if (terms == null || weights == null) {
			throw new NullPointerException("One or more arguments null");
		}
		if (terms.length != weights.length) {
			throw new IllegalArgumentException("terms and weights are not the same length");
		}
		initialize(terms,weights);
    }

    @Override
    public List<Term> topMatches(String prefix, int k) {
        if(prefix.length()>MAX_PREFIX){
            prefix = prefix.substring(0,MAX_PREFIX);
        }
        if(map.containsKey(prefix)){
            List<Term> all = map.get(prefix);
            List<Term> list = all.subList(0, Math.min(k, all.size()));
            return list; 
            
        }
        else{
                return new ArrayList<>(); 
        }

    }

    @Override
    public void initialize(String[] terms, double[] weights) {
    map = new HashMap<>();        
        List<Term>myTerms = new ArrayList<>();
		
		for (int i = 0; i < terms.length; i++) {
			myTerms.add(new Term(terms[i], weights[i]));
		}
        for(Term t: myTerms){
           
            int count = 0; 
            while(count<=MAX_PREFIX && count<=t.getWord().length()){
                if(! map.containsKey(t.getWord().substring(0,count))){
                    map.put(t.getWord().substring(0, count), new ArrayList<Term>());
                }
                map.get(t.getWord().substring(0, count)).add(t);
                count++;
            }
        }
        for(String k: map.keySet()){
                Collections.sort(map.get(k), Comparator.comparing(Term::getWeight).reversed());
            }
        
    
    }

    @Override
    public int sizeInBytes() {
        if(mySize==0){
            for(String k: map.keySet()){
                mySize+= k.length()*BYTES_PER_CHAR;
            }
            for(Term t: map.get("")){
                mySize += BYTES_PER_DOUBLE + 
                BYTES_PER_CHAR*t.getWord().length();
            }
        }
     return mySize;
    }
    
}

