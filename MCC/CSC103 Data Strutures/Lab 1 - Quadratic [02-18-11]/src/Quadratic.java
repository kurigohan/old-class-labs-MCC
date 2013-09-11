public class Quadratic implements Cloneable {
	private double coefA, coefB, coefC;
	
	Quadratic () {
		coefA = 0;
		coefB = 0;
		coefC = 0;
	}
	
	Quadratic (double a, double b, double c) {
		coefA = a;
		coefB = b;
		coefC = c;
	}
	
	public double getCoefA () {
		return coefA;
	}
	
	public double getCoefB () {
		return coefB;
	}
	
	public double getCoefC () {
		return coefC;
	}
	
	public Quadratic scale (double scale) {
		return new Quadratic (coefA * scale, coefB * scale, coefC * scale);
	}
	
	public Quadratic sum (Quadratic q) {
		return new Quadratic (coefA + q.coefA, coefB + q.coefB, coefC + q.coefC);
	}
	
	public double evalExpression (double x){ //evaluate expression
		double eval = 0;
		eval = (coefA * Math.pow(x, 2) ) + (coefB * x) + coefC;
		return eval;
	}
	
	public int getRootNum () { //finds # real roots
		int numRoot = 0;
		
		//double discrim = 0; //discriminant
		
		/*discrim = (coefB * coefB) - (4 * coefA * coefC);
		if (discrim == 0) //1 real root
			numRoot = 1;
		if (discrim > 0) //2 real roots
			numRoot = 2;
		if(discrim > 0) //no real roots
			numRoot = 0;*/
		
		if(coefA==0 && coefB==0 && coefC==0)//if a,b,c are all zero, infinite roots
			numRoot = 3;
		if(coefA==0&&coefB==0&&coefC!=0)//if a,b are zero & c nonzero, no real roots
			numRoot = 0;
		if(coefA==0&&coefB!=0)//if a zero & b nonzero, 1 real root
			numRoot = 1;
		if(coefA!=0&&((coefB*coefB)<(4*coefA*coefC)))//if a nonzero & b^2<4ac, no real roots
			numRoot = 0;
		if(coefA!=0&&((coefB*coefB)==(4*coefA*coefC)))//if a nonzero & b^2=4ac, 1 real root
			numRoot = 1;
		if(coefA!=0&&((coefB*coefB)>(4*coefA*coefC)))//if a nonzero & b^2>4ac, 2 real roots
			numRoot = 2;
		
		return numRoot;
	}
	
	public double getRootOne () {
		double root1 = 0;
		if(getRootNum() == 3)
			return root1;
		if(coefA==0 && coefB!=0)//if a=0 & b nonzero, 1 root -c/b
			root1 = -coefC/coefB;
		if(coefA!=0&&((coefB*coefB)==(4*coefA*coefC)))//if a nonzero & b^2=4ac, 1 root -b/2a
			root1 = -coefB/(2*coefA);
		if(getRootNum() == 2)
			root1 = (-coefB - (Math.sqrt ( (coefB * coefB) - (4 * coefA * coefC) ) ) ) / (2 * coefA);
		return root1;
	}
	
	public double getRootTwo () {
		double root2 = 0;
		if(getRootNum() == 3)
			return root2;
		if(coefA==0 && coefB!=0)
			root2 = -coefC/coefB;
		if(coefA!=0&&((coefB*coefB)==(4*coefA*coefC)))
			root2 = -coefB/(2*coefA);
		if(getRootNum()==2)
			root2 = (-coefB + (Math.sqrt ( (coefB * coefB) - (4 * coefA * coefC) ) ) ) / (2 * coefA);
		return root2;
	}
	
	public boolean equals (Quadratic a) {
        return (a.coefA == coefA && a.coefB == coefB && a.coefC == coefC);
    }
	
    public Object clone () {
        return new Quadratic (coefA, coefB, coefC);
    }
}
	
