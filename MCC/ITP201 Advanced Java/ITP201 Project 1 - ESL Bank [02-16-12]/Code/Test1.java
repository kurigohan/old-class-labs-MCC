
public class Test1 {
	public static void main (String[] args){
		Customer[] c1 = new Customer[30];
		int count = 0;
		c1[0] = new Customer("Nguyen", 555, 10.00, "766");
		count++;
		c1[1] = new Customer("NguyenA", 515, 145.00, "736");
		count++;
		c1[count++] = new Customer("NguyenB", 550, 15.00, "726");
		
		//c1[0].nameSort(c1, count);
		
		for(int i=0;i<count;i++)
			System.out.println(c1[i]);
	}
}
