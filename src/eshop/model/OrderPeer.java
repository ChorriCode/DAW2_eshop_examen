package eshop.model;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;

import eshop.beans.Customer;

public class OrderPeer {

  public static void insertOrder(Statement stmt, long orderId,
      Customer customer) throws SQLException {
	  LocalDate dt = LocalDate.now();
    String sql = "insert into orders (order_id, delivery_name,"
        + " delivery_address, cc_name, cc_number, cc_expiry, fecha_actual) values ('"
        + orderId + "','" + customer.getContactName() + "','"
        + customer.getDeliveryAddress() + "','"
        + customer.getCcName() + "','" + customer.getCcNumber() + "','"
        + customer.getCcExpiryDate() + "','"
        + dt + "')"
        ;
    stmt.executeUpdate(sql);
    }
  }
