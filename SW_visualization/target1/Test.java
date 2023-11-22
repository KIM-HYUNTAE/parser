package SW_visualization;

public class Test {
	public static void main(String[] args) {
		private int a;
		int b=20;
		a=10;
		boolean c=true;
		
		Test1 t = new Test1();
		t.calculator(10,3,"+");
		
		for (int i=0;i<10;i=i+1) {
			a=a+1;
		}
		if (a==20)
			c=true;
		else
			c=false;
		System.out.println(c);
	}
}
class Test1 implements Testing{
	int x=10;
	int y=20;
	int sum=x+y;
	
	public void calculator(int a, int b, String c) {
		if(c.equals("+")
			System.out.print(a+b);
		else if(c.equals("-"))
			System.out.print(a-b);
		else
			System.out.print("다시 입력해주세요");
	}
	private void print() {
		System.out.println("출력");
	}
	public void interface_test(int a) {
		System.out.print(a+"출력");
	}
}
public interface Testing{
	public void interface_test(int a);
}