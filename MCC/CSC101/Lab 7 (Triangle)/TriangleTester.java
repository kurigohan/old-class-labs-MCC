/* Program: TriangleTest.java
   Name: Andy Nguyen
	Date: 11/18/10
	Purpose: Test triangle class.
*/

class TriangleTester{
	public static void main(String[] args){
	  Triangle tri1=new Triangle(4.0,7.0,3.0);
	  
	  System.out.println("Triangle 1: "+tri1.getSideA()+" "+tri1.getSideB()
	  											  +" "+tri1.getSideC());
	  System.out.println("Perimeter: "+tri1.perimeter()+"\n");
	  System.out.println("Area: "+tri1.area()+"\n");
	  System.out.println("Shortest: "+tri1.shortestSide()+"\n");
	  System.out.println("Longest: "+tri1.longestSide()+"\n");
	  System.out.println("Type: "+tri1.typeOfTriangle()+"\n");
	
	
	}//end main
}//end class