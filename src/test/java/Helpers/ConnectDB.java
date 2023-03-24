package com.utility.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.properties.mapping.LoadProp;


public class ConnectDB {
	
	
	private ConnectDB() {
	  }
	/**
	 * Method to fetch item id from the DB using company item id
	 * @param companyItemId user specified company item id
	 * @return item id
	 * @throws SQLException exception
	 */
	public static int getItemIdfromDB(String companyItemId) throws SQLException{

		int itemId = 0;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(Global.CLASS_NAME);
			String userName = LoadProp.getProperty(Global.DB_USERNAME);
			String password = LoadProp.getProperty(Global.DB_PASS);
			String connectionUrl = LoadProp.getProperty(Global.DB_CONNECTION_URL);
			con = DriverManager.getConnection(connectionUrl, userName, password);
			String selectSQL = "select ItemId FROM Item where CompanyItemId = ?";
			ps = con.prepareStatement(selectSQL);
			ps.setString(1, companyItemId);
			rs = ps.executeQuery(); 
			while (rs.next()){
				itemId = rs.getInt("ItemId");
			}
			return itemId;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs !=null){
				rs.close();
			}

			if (ps !=null){
				ps.close();
			}
			if (con !=null){
				con.close();
			}

		}
		return itemId;
	}
}
