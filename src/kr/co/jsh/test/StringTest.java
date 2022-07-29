package kr.co.jsh.test;

public class StringTest {
	String name = this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".")+1);
	public static void main(String[] args) {
		
		StringTest st = new StringTest();
		System.out.println(st.name);
		
		String t = "test.CHK";
		System.out.println(t.substring(0, t.lastIndexOf(".")));
		
		String t2 = "file.txt";
		System.out.println(t2.lastIndexOf("."));
	}
}
