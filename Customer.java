
import java.awt.RenderingHints.Key;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Customer extends Basic_func {

	public static void exet() {
		System.out.println("我是Customer");
		Map<Integer, String> optionMap = new HashMap<Integer, String>();
		optionMap.put(1, "查詢上映的電影");
		optionMap.put(2, "查詢目標電影");
		optionMap.put(3, "離開");

		boolean checkPoint = false;
		int Cust_main_choice = 0;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("請輸入指定功能的對應數字");
			try {
				System.out.println("輸入  1 [" + optionMap.get(1) + "]\n輸入  2 [" + optionMap.get(2) + "]\n輸入  3 ["
						+ optionMap.get(3) + "]");
				System.out.println(">>>");
				Cust_main_choice = Integer.parseInt(sc.nextLine());
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
			getTargetList();
			theNext();
			break;
		case 2:
			getTargetList();
			theNext();
			break;
		default:
			break;
		}
	}

	public static void theNext() {
		int theNext_choice = 0;
		boolean checkPoint = false;
		Map<Integer, String> optionMap = new HashMap<Integer, String>();
		optionMap.put(1, "進行訂票");
		optionMap.put(2, "回主選單");
		optionMap.put(3, "離開");

		do {
			System.out.println("請輸入指定功能的對應數字");
			try {
				Scanner sc = new Scanner(System.in);
				System.out.println("輸入  1 [" + optionMap.get(1) + "]\n輸入  2 [" + optionMap.get(2) + "]\n輸入  3 ["
						+ optionMap.get(3) + "]");
				theNext_choice = Integer.parseInt(sc.nextLine());
				System.out.println(">>>");
				if (optionMap.keySet().contains(theNext_choice)) {
					checkPoint = true;
//					sc.close();
					switch (theNext_choice) {
					case 1:
						make_order();
						theNext();
						break;
					case 2:
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

	public static void make_order() {
		try (Connection connection = DriverManager.getConnection(conn_db, conn_user, conn_user_pass);) {
			Scanner sc = new Scanner(System.in);
			
//				System.out.println("請依下列格式輸入電影時刻 ");
//				System.out.println(">>>" + LocalDate.now() + " 13:00");
//				System.out.print(">>>");
				System.out.println("請輸入數字選取電影時刻 ");
				for (int i = 1; i <= ptimeMap.size(); i++) {
					Date time = ptimeMap.get(i); 
					System.out.println(i+"  ["+time+"]");
				}
				System.out.print(">>>");

			
				int movie_choice =  Integer.parseInt(sc.nextLine());
				Timestamp movie_time = ptimeMap.get(movie_choice);
				
//				Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(movie_time);
//				java.sql.Timestamp sqldate = new Timestamp(date.getTime());
			
				PreparedStatement ppstmt_room = connection
						.prepareStatement("select * from playlist where movie=? and ptime=?");
				ppstmt_room.setString(1, movie_choiced);
				ppstmt_room.setTimestamp(2, movie_time);
				ResultSet rs_movice = ppstmt_room.executeQuery();

				rs_movice.next();
				Timestamp ptime = rs_movice.getTimestamp("ptime");
				String movie = rs_movice.getString("movie");
				String room = rs_movice.getString("roomid");

				System.out.println("剩餘座位");
				PreparedStatement ppstmt_seat = connection
						.prepareStatement("select seat_num from seats where ptime=? and movie=? and sold=0");
				ppstmt_seat.setTimestamp(1, ptime);
				ppstmt_seat.setString(2, movie);
				ResultSet rs_seat = ppstmt_seat.executeQuery();

				Map<Integer, String> seatMap = new HashMap<Integer, String>();
				System.out.println("電影: " + movie + "  時間: " + ptime);
				int i =1;
				while (rs_seat.next()) {
					String seats = rs_seat.getString("seat_num");
					seatMap.put(i, seats);
					System.out.println("剩餘位置 " +i+"  "+ seats);
					i++;
				}

//				System.out.println("請依下列格式輸入所選位置 ");
//				System.out.println(">>> XX-XX");
//				System.out.print(">>> ");
				
				System.out.println("請輸入數字選取位置 ");
				int seat_choice = Integer.parseInt(sc.nextLine());
				String seat_num = seatMap.get(seat_choice);
				

				InetAddress address = InetAddress.getLocalHost();
				String ordid = address.getHostName();

				PreparedStatement ppstmt_update = connection.prepareStatement(
						"UPDATE seats SET sold = 1 , ordid = ? WHERE ptime=? and movie=? and seat_num=?");


				ppstmt_update.setString(1, ordid);
				ppstmt_update.setTimestamp(2, ptime);
				ppstmt_update.setString(3, movie);
				ppstmt_update.setString(4, seat_num);
				ppstmt_update.executeUpdate();
				ppstmt_update.clearParameters();
				
				
				PreparedStatement ppstmt_Record = connection
						.prepareStatement("insert into Ts_Record(ordtime,movie,ptime,roomid,seat_num,ordid) values (?,?,?,?,?,?)");

				DateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
				Date datenow = new Date();
				String ordtime_str = format.format(datenow);
				
				ppstmt_Record.setString(1, ordtime_str);
				ppstmt_Record.setString(2, movie);
				ppstmt_Record.setTimestamp(3, ptime);
				ppstmt_Record.setString(4, room);
				ppstmt_Record.setString(5, seat_num);
				ppstmt_Record.setString(6, ordid);				
				ppstmt_Record.executeUpdate();
				ppstmt_Record.clearParameters();
				connection.commit();
				
//				sc.close();

		} catch (Exception e) {
			System.out.println("輸入錯誤");
		}

	}

}
