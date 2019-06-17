import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Basic_func {
	 static	String conn_db = "jdbc:sqlserver://192.168.35.98:1433;databaseName=DB01";
	 static	String conn_user = "sa";
	 static	String conn_user_pass = "P@ssw0rd";
	 static int theNext_choice = 0;
	 static String movie_choiced;

	public static void getAllList() {
		try (Connection connection = DriverManager.getConnection(conn_db, conn_user, conn_user_pass);) {
			// 以stmt_list--rs_list 獲得list中的資訊
			Statement stmt_list = connection.createStatement();
			ResultSet rs_list = stmt_list.executeQuery("select * from playlist");
			// 一筆筆抓取(1)的電影資訊(ptime/movie/room)
			System.out.println("[所有場次資訊如下]");
			while (rs_list.next()) {
				Timestamp ptime = rs_list.getTimestamp("ptime");
				String movie = rs_list.getString("movie");
				String room = rs_list.getString("roomid");
				System.out.println("電影: " + movie + "  廳別: " + room + "  時間: " + ptime);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	static Map<Integer, Timestamp> ptimeMap;
	
	public static void getTargetList() {
		List<String> movielist = new ArrayList<String>();
		try (Connection connection = DriverManager.getConnection(conn_db, conn_user, conn_user_pass);) {
			Scanner sc = new Scanner(System.in);
			Statement stmt_list = connection.createStatement();
			ResultSet rs_list = stmt_list.executeQuery("select DISTINCT movie from playlist");
			System.out.println("[目前上映的電影如下] ");
			int j = 1;
			while (rs_list.next()) {
				String movie = rs_list.getString("movie");
				System.out.println(j + ". " + movie);
				movielist.add(movie);
				j++;
			}

			boolean checkPoint = false;
			do {
				try {
					System.out.println("請輸入電影編號: ");
					System.out.print(">>> ");
					
					int movie_nmb = Integer.parseInt(sc.nextLine());
					movie_choiced = movielist.get(movie_nmb-1);					

					if (movielist.contains(movie_choiced)) {
						PreparedStatement ppstmt_room = connection
								.prepareStatement("select * from playlist where movie=?");
						ppstmt_room.setString(1, movie_choiced);
						ResultSet rs_movice = ppstmt_room.executeQuery();
						
						ptimeMap = new HashMap<Integer, Timestamp>();
//						ptimeMap=null;
						int i = 1;
						while (rs_movice.next()) {
							Timestamp ptime = rs_movice.getTimestamp("ptime");
							String movie = rs_movice.getString("movie");
							String room = rs_movice.getString("roomid");
							
//							需新增剩餘座位資訊;
							ptimeMap.put(i, ptime);
							System.out.println("電影: "+movie+"  廳別: "+room+"  時間: "+ptime);
							i++;
						}
						checkPoint = true;
//						sc.close();
					}

				} catch (Exception e) {
					System.out.println("輸入錯誤，請重新輸入");
				}
			} while (!checkPoint);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
