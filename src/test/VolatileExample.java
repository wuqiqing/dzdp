package test;

public class VolatileExample extends Thread {
	// 设置类静态变量,各线程访问这同一共享变量
	private static   boolean flag = false;
	public static boolean isFlag() {
		return flag;
	}
	public static void setFlag(boolean flag) {
		VolatileExample.flag = flag;
	}
	// 无限循环,等待flag变为true时才跳出循环
	public void run() {
		while (!flag) {};
	}

	public static void main(String[] args) throws Exception {
		//long t1 = System.currentTimeMillis();
		new VolatileExample().start();
		// sleep的目的是等待线程启动完毕,也就是说进入run的无限循环体了
		Thread.sleep(100);
		flag = true;
		//long t2 = System.currentTimeMillis();
		//System.out.println("-----this end----"+(t2-t1));
	}
}