import java.util.Scanner;


public class ticket_main {


	
public static void main(String[] args) {
	try {
		System.out.println("歡迎進入票卷系統");
		Thread.sleep(500);		
		
	    //建立身分別，方便後續管理
		String[] identity_type= {"一般顧客", "系統人員"};				
		//建立接收系統
		Scanner sc = new Scanner(System.in);
		//設置嘗試次數限制
		int tring_times = 0;
		do {
			tring_times++;
			try {
				System.out.println("請依照號碼輸入您的身分 :  \n1."+identity_type[0]+"   \n2."+identity_type[1]);
				System.out.print(">>>");
				//接收輸入資訊
				int identity = Integer.parseInt(sc.nextLine());	
				//判斷輸入類型
				if (identity == 1) {
									Customer customer = new Customer();
//									sc.close();
									customer.exet();
									break;
				} else if (identity == 2) {
											Systemer systemer = new Systemer();
//											sc.close();
											systemer.exet();
											break;
				} else {
						throw new ArithmeticException();
				}
				} catch (Exception e) {
					System.out.println("輸入錯誤\n" + "您還有"+ (3-tring_times) +"次嘗試機會\n");
					Thread.sleep(500);	
			}
		} while (tring_times < 3);
		System.out.println("謝謝您的光臨，歡迎下次再來");


	} catch (Exception e) {
		// TODO: handle exception
	}
	
//
//	
//	
//	try (
//		Connection connection = DriverManager.getConnection("jdbc:sqlserver://192.168.35.25:1433;databaseName=DB01", "sa", "P@ssw0rd");
//		){		
//		System.out.println("開始");
//		
//		
//		
//		
//		
//		
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		
//	}
	
	}
}