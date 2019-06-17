import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Systemer extends Basic_func {
	
	public static void exet() {
		System.out.println("我是Systemer");
		Map<Integer,String> optionMap = new HashMap<Integer, String>();
		optionMap.put(1, "查詢上映的電影");
		optionMap.put(2, "查詢目標電影");
		optionMap.put(3, "新增電影場次");
		optionMap.put(4, "查看訂單紀錄");
		optionMap.put(5, "離開");

		
		boolean checkPoint = false;
		int Cust_main_choice = 0;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("請輸入指定功能的對應數字");
			try {
				System.out.println("輸入  1 ["+optionMap.get(1)+"]\n輸入  2 ["+optionMap.get(2)+"]\n輸入  3 ["+optionMap.get(3)+"]\n輸入  4 ["+optionMap.get(4)+"]\n輸入  5 ["+optionMap.get(5)+"]");
				Cust_main_choice = Integer.parseInt(sc.nextLine());
				System.out.println(">>>");
				if (optionMap.keySet().contains(Cust_main_choice)) {
					checkPoint = true;
//					sc.close();
				} else {
					throw new ArithmeticException();
				}
			} catch (Exception e) {
				System.out.println("輸入錯誤，請重新輸入");
			}
		} while (!checkPoint);

		switch (Cust_main_choice) {
		case 1:
			getAllList();
			theNext();
			break;
		case 2:
			getTargetList();
			theNext();
			break;
		case 3:
			System.out.println("施工中");
			theNext();
			break;
		case 4:
			check_record();
			theNext();
			break;
		default:
			break;
		}
	}


	public static void theNext() {
		int theNext_choice = 0;
		boolean checkPoint = false;
		Map<Integer,String> optionMap = new HashMap<Integer, String>();
		optionMap.put(1, "回主選單");
		optionMap.put(2, "離開");
		System.out.println(">>>");
		do {
			try {
				Scanner sc = new Scanner(System.in);
				System.out.println("輸入  1 ["+optionMap.get(1)+"]\n輸入  2 ["+optionMap.get(2)+"]");
				theNext_choice = Integer.parseInt(sc.nextLine());
				if (optionMap.keySet().contains(theNext_choice)) {
					checkPoint = true;
//					sc.close();
					switch (theNext_choice) {
					case 1:
						exet();
						break;
					default:
						break;
					}
				} else {
					throw new ArithmeticException();
				}
			} catch (Exception e) {
				System.out.println("輸入錯誤，請重新輸入");
			}
		} while (!checkPoint);
	}
	
	public static void check_record() {
		try (Connection connection = DriverManager.getConnection(conn_db, conn_user, conn_user_pass);) {

			Statement stmt_record = connection.createStatement();
			ResultSet rs_record = stmt_record.executeQuery("select * from Ts_Record");

			System.out.println("[所有資訊如下]");
			while (rs_record.next()) {			
				String ordtime = rs_record.getString("ordtime");
				String movie = rs_record.getString("movie");
				Timestamp ptime = rs_record.getTimestamp("ptime");
				String roomid = rs_record.getString("roomid");
				String seat_num = rs_record.getString("seat_num");
				String ordid = rs_record.getString("ordid");
				System.out.println("時間: " + ordtime + "  電影: " + movie + "  場次: " + ptime+ "  影廳: " + roomid+ "  座位: " + seat_num+ "  id: " + ordid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}


	