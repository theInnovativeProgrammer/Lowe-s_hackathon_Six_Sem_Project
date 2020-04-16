import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Random;

public class Billing {
	
	private JFrame frame;
	int sum=0;
	String fixedOrderId = "";
	private JTextField billAmountField;
	/**
	 * Launch the application.
	 * @param a2 
	 * @param itemPrice 
	 * @param itemName 
	 * @param itemId 
	 */
	public static void main(String[] a2, String[] itemId, String[] itemName, int[] itemQty, int[] itemPrice) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Billing window = new Billing(a2,itemId,itemName,itemQty,itemPrice);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @param a2 
	 * @param itemPrice 
	 * @param itemName 
	 * @param itemId 
	 */
	public Billing(String[] a2, String[] itemId, String[] itemName, int[] itemQty, int[] itemPrice) {
		
		
		initialize(a2,itemId,itemName,itemQty,itemPrice);
		
		
	}

	private void storeOrder(String[] itemId, String[] itemName, int[] itemQty, int[] itemPrice) {
		// TODO Auto-generated method stub
		PreparedStatement prepStmt=null;
		String cId = "";
		
		
		System.out.println("=======================================================================================");
		System.out.println("=======================================================================================");
		System.out.println("=======================================================================================");
		System.out.println("=======================================================================================");
		
		System.out.println(itemId[0]);
		System.out.println(itemName[0]);
		System.out.println(itemQty[0]);
		System.out.println(itemPrice[0]);
		
		System.out.println(itemId[1]);
		System.out.println(itemName[1]);
		System.out.println(itemQty[1]);
		System.out.println(itemPrice[1]);
		
		System.out.println(itemId[2]);
		System.out.println(itemName[2]);
		System.out.println(itemQty[2]);
		System.out.println(itemPrice[2]);
		
		try{  
			Class.forName("org.sqlite.JDBC");  
			Connection con=DriverManager.getConnection( 
			"jdbc:sqlite:./Database/lowes.db");  
			Statement stmt=con.createStatement();  
			
			String select="select C_id from customer where Status=?";
			prepStmt = con.prepareStatement(select);
			prepStmt.setString(1,"active");
			ResultSet rs2=prepStmt.executeQuery();
			
			
		
//Retrived database values are put into the variables and displaying				
			while(rs2.next())
			{
				cId=rs2.getString("C_id").toString();
				
				System.out.println("C_ID: "+cId);
			}
			
			
//			generating unique order id
			
			String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
			Calendar gc = Calendar.getInstance();
						
			String orderId= gc.get(Calendar.YEAR)+months[gc.get(Calendar.MONTH)]+gc.get(Calendar.DATE)+gc.get(Calendar.HOUR)+gc.get(Calendar.MINUTE)+gc.get(Calendar.SECOND);
			fixedOrderId = orderId.toString();
//			Ended generation of order id
			
				for(int x=0;x< itemId.length;x++) {
					
					prepStmt = con.prepareStatement("Insert into orders(Order_no,Item_id,Item_name,Qty,Price,Customer_id) values(?,?,?,?,?,?)");
					prepStmt.setString(1, fixedOrderId);
					prepStmt.setString(2, itemId[x]);
					prepStmt.setString(3, itemName[x]);
					prepStmt.setInt(4, itemQty[x]);
					prepStmt.setInt(5, itemPrice[x]);
					prepStmt.setString(6, cId);
					
					prepStmt.executeUpdate();
					
//				This if condition is for: since we did aloocate size of itemId[15] as 15 so this loop runs for 15 times but we want it to run till all items are processed.
					if(itemId[x+1]==null) {
						
						con.close();
						break;
					}
					
					
//					prepStmt.setString(6, x);
				}
				
				
			
				
			
			  
			}catch(Exception e){ System.out.println(e);}
		
	}

	/**
	 * Initialize the contents of the frame.
	 * @param a2 
	 */
	private void initialize(String[] a2,String[] itemId, String[] itemName, int[] itemQty, int[] itemPrice) {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 534, 508);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextArea itemList = new JTextArea();
		itemList.setBounds(10, 11, 498, 284);
		frame.getContentPane().add(itemList);
		
		JLabel billAmountLabel = new JLabel("Bill Amount:");
		billAmountLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		billAmountLabel.setForeground(Color.RED);
		billAmountLabel.setBackground(Color.ORANGE);
		billAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
		billAmountLabel.setBounds(80, 323, 134, 38);
		frame.getContentPane().add(billAmountLabel);
		
		billAmountField = new JTextField();
		billAmountField.setBounds(244, 326, 216, 38);
		frame.getContentPane().add(billAmountField);
		billAmountField.setColumns(10);
		
//		converting integer to string
		for(int i=0;i<a2.length;i++) {
			
//			String str1 = Integer.toString(a2[i]);
			itemList.append(a2[i]+"\n");
			sum += itemPrice[i];
		}
		
		
		
		
//		System.out.println("a[0]:"+a[0]);
//		System.out.println("a[1]:"+a[1]);
		
		System.out.println("Total:"+sum);
		billAmountField.setText(Integer.toString(sum));
		
		JButton checkOutBtn = new JButton("Card Payment");
		checkOutBtn.setForeground(Color.BLACK);
		checkOutBtn.setFont(new Font("Tahoma", Font.BOLD, 18));
		checkOutBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				System.out.println(fixedOrderId);
				storeOrder(itemId,itemName,itemQty,itemPrice);
				
				Checkout c1= new Checkout("billCard",sum, fixedOrderId);
				c1.main("billCard",sum, fixedOrderId);
			}
		});
		checkOutBtn.setBounds(148, 406, 216, 38);
		frame.getContentPane().add(checkOutBtn);
		
		
		
		
	}
}
