
public class Statistics{
	private int[] data;//array of integers
	private int count;//counts # of elements in array

	public Statistics(){
	  data=new int[50];
	  count=0;
	}
	
	public Statistics(int s){
	  data=new int[s];
	  count=0;
	}
	
	public boolean add(int num){
	  boolean b=false;
	  
	  
	  count++;
	  return b; 
	}
	
	public boolean add(int num1, num2){
	  boolean b=false;
	  int i = find(num2);
	  if(i!=-1){
	    data[i+1]=num1;
		 b=true;}
	  else if(count==data.length)
	    
	  
	
	}
	
	public int find(int num){
	  int index=-1;
	  for(int i=0;i<data.length;i++){
	    if(data[i]!=null)
	      if(data[i]==num)
		     index=i;
	  }
	  return index;
	}





}