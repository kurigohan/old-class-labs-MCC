/* Program: Triangle.java
	Name: Andy Nguyen
	Date: 11/18/10
	Purpose: Triangle class to be used by
			   TriangleTester.
*/

class Triangle{
	double sideA, sideB, sideC, area, perim;
	
	Triangle(){//default constructer
     sideA=0;
	  sideB=0;
	  sideC=0;
	}
	
	Triangle(double a,double b,double c){
	  sideA=a;
	  sideB=b;
	  sideC=c;
	}
	
	public boolean isTriangle(){
	  if((sideA+sideB)>=sideC)
	    return true;
	  else
	    return false;
	}//end isTriangle
	
	public double getSideA(){
	  return sideA;
	}
	public double getSideB(){
	  return sideB;
	}
	public double getSideC(){
	  return sideC;
	}
	
	public double perimeter(){
	  perim=sideA+sideB+sideC;
	  return perim;
	}
	
	public double area(){
	  double s=(sideA+sideB+sideC)/2;
	  area=Math.sqrt((s*(s-sideA)*(s-sideB)*(s-sideC)));
	  return area;
	}
	
	public double shortestSide(){
	  double shortest=0;

	  if((sideA<=sideB)&&(sideA<=sideC))
	    shortest=sideA;
	  else if((sideB<=sideC)&&(sideB<=sideA))
	    shortest=sideB;
	  else if((sideC<=sideB)&&(sideC<=sideA))
	    shortest=sideC;
	  

	  return shortest;
	}
	
	public double longestSide(){
	  double longest=0;
	  
	  if((sideA>=sideB)&&(sideA>=sideC))
	    longest=sideA;
	  else if((sideB>=sideC)&&(sideB>=sideA))
	    longest=sideB;
	  else if((sideC>=sideB)&&(sideC>=sideA))
	    longest=sideC;
		 
		 
	  return longest;
	
	}
	
	public void scale(int r){
	  sideA*=r;
	  sideB*=r;
	  sideC*=r;
	}
	
	public String typeOfTriangle(){
	  String t="Unknown triangle";;
	  
	   if(((sideA*sideA)+(sideB+sideB)==(sideC*sideC))||
	  		((sideA*sideA)+(sideC+sideC)==(sideB*sideB))||
			((sideB*sideB)+(sideC+sideC)==(sideA*sideA)))
	    t="right";
		 
	  if((sideA==sideB)&&(sideA==sideC))
	    t="equilateral";
		 
	  if((sideA==sideB)||(sideB==sideC))
	  	  if(((sideA*sideA)+(sideB+sideB)==(sideC*sideC))||
	  		((sideA*sideA)+(sideC+sideC)==(sideB*sideB))||
			((sideB*sideB)+(sideC+sideC)==(sideA*sideA)))
		   t="right and isosceles";
	  else
	    t="isosceles";
	  
	  if((sideA!=sideB)&&(sideB!=sideC))
	    t="scalene";
	 
	  return t; 
	}
}//end class