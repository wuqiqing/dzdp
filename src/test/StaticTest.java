package test;

public class StaticTest {
	
	private static int flag = 1;

	public static int getFlag() {
		return flag;
	}

	public static void setFlag(int flag) {
		StaticTest.flag = flag;
	}

	public static void main(String[] args) {
           StaticTest test = new 	StaticTest();
           StaticTest test1 = new 	StaticTest();
           StaticTest test2 = new 	StaticTest();
           StaticTest test3 = new 	StaticTest();
           
           System.out.println(getFlag());
           test.setFlag(0);
           System.out.println(getFlag());
           test1.setFlag(1);
           System.out.println(getFlag());
           test2.setFlag(2);
           System.out.println(getFlag());
           test3.setFlag(3);
           
	}
}

