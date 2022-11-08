package com.mini.project.java;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.ResultSet;

public class UserRegTest {

	public static void Buynow(int userid, int productid) {
		try {
			Connection connection=CommonConnection.getConnection();
			PreparedStatement ps=connection.prepareStatement("insert into purchasehistory(userid,productid)values(?,?)");
			ps.setInt(1, userid);
			ps.setInt(2, productid);
			ps.executeUpdate();
			PreparedStatement prst=connection.prepareStatement("select quantity from products where ProductId = ?");
			prst.setInt(1, productid);
			ResultSet prstg=prst.executeQuery();
			if (prstg.next()) {
				int quan =  prstg.getInt(1) - 1;
				PreparedStatement us=connection.prepareStatement("update products set quantity = ? where productid = ?");
				us.setInt(1, quan);
				us.setInt(2, productid);
				us.executeUpdate();
			}
			System.out.println("1.To see purchase history");
			Scanner sc=new Scanner(System.in);
			int pr=sc.nextInt();
			if (pr == 1) {
				PreparedStatement ps1=connection.prepareStatement("select * from products p inner join purchasehistory c on p.productid = c.productid order by updatedat");
				ResultSet rs=ps1.executeQuery();
				System.out.println("Id\tPrice\t\tQuantity\tName\t\t\t\t\tDesc: ");
				int totalprice = 0;
				while (rs.next()) {
					System.out.println(rs.getInt(1) + "\t" + rs.getInt(3) + "\t\t" + rs.getInt(5) + "\t" + rs.getString(4) + "\t\t\t\t\t\t" + rs.getString(2));
					totalprice = totalprice + rs.getInt(3);
			     }
				System.out.println("Total order Price: " + totalprice);
			} else {
				System.out.println("1.Back to Home");
				int bck=sc.nextInt();
				if (bck == 1) {
					Listing(userid);
				} else {
					System.out.println("Wrong input redirected to listing");
					Listing(userid);
				}			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void Cart(int userid, int productid) {
		try {
			Connection connection=CommonConnection.getConnection();
			PreparedStatement ps=connection.prepareStatement("insert into cart(userid,productid)values(?,?)");
			ps.setInt(1, userid);
			ps.setInt(2, productid);
			ps.executeUpdate();
			System.out.println("1.To see products in cart");
			Scanner sc=new Scanner(System.in);
			int pr=sc.nextInt();
			if (pr == 1) {
				PreparedStatement ps1=connection.prepareStatement("select * from products p inner join cart c on p.productid = c.productid order by updatedat");
				ResultSet rs=ps1.executeQuery();	
				System.out.println("Id\tPrice\t\tQuantity\tName\t\t\t\t\tDesc: ");				
				int cartcount = 0;
				while (rs.next()) {
					System.out.println(rs.getInt(1) + "\t" + rs.getInt(3) + "\t\t" + rs.getInt(5) + "\t" + rs.getString(4) + "\t\t\t\t\t\t" + rs.getString(2));
					cartcount = cartcount + rs.getInt(3);
			     }
				System.out.println("Cart Value: " + cartcount);
				System.out.println("\nEnter product id from above products to buy now:");
				int prodid=sc.nextInt();
				if(prodid<=10) {
					Buynow(userid, prodid);					
				} else {
					System.out.println("Entered wrong productid");
					Cart(userid, productid);
				}
			}else {
				System.out.println("WRONG INPUT REDIRECT TO CART ");
				Cart(userid, productid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void Listing(int userid) {
		System.out.println("\nProduct listing page");
		System.out.println("Select Category");
		System.out.println("1.Mobile");
		Scanner sc=new Scanner(System.in);
		int mob=sc.nextInt();
		try {
			Connection connection=CommonConnection.getConnection();
			PreparedStatement ps=connection.prepareStatement("select * from products ORDER BY name");
			ResultSet rs=ps.executeQuery();	
			System.out.println("Id\tPrice\t\tQuantity\tName\t\t\tDesc");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "\t" + rs.getInt(3) + "\t\t" + rs.getInt(5) + "\t\t" + rs.getString(4) + "\t\t" + rs.getString(2));
		     }
			System.out.println("\n\n");
			System.out.println("1.Select Product for Add to Cart");
			System.out.println("2.Select Product to Buy now");
			System.out.println("3.Back");
			int prd=sc.nextInt();
			if (prd == 1) {
				System.out.println("Enter Product Id for Add to cart");
				int ad=sc.nextInt();
				System.out.println("Product Added to cart");
				Cart(userid , ad);
			} else if (prd == 2) {
				System.out.println("Enter Product Id To Buy now");
				int add=sc.nextInt();
				Buynow(userid , add);
			} else if (prd == 3) {
				Login();
			} else {
				System.out.println("Wrong input selection");
				Listing(userid);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	public static void Login() {
        System.out.println("Enter Login details");
		try {
			System.out.println("Enter Email:");
			Scanner sc=new Scanner(System.in);
			String email=sc.next();
			System.out.println("Enter Password:");
			String pass=sc.next();
			Connection connection=CommonConnection.getConnection();
			PreparedStatement ps=connection.prepareStatement("select * from user where emailID=? and password=?");
			ps.setString(1, email);
			ps.setString(2, pass);
			ResultSet rs=ps.executeQuery();
			if (rs.next()) {
	            System.out.print("Authentication Successfull:\n ");
	            Listing(rs.getInt(5));
			} else {
				System.out.println("Login failed, Enter valid credentils!!");
				Login();
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void insertUserData(String name,String password,String MobileNumber,String emailID ) {
		//Connection rs=null;
		Connection connection=null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniporject","root","root123");
			PreparedStatement ps=connection.prepareStatement("insert into user(name,password,MobileNumber,emailID)values(?,?,?,?)");
			ps.setString(1, name);
			ps.setString(2, password);
			ps.setString(3, MobileNumber);
			ps.setString(4, emailID);
			ps.executeUpdate();
			System.out.println("User is registere successfully");
			System.out.println("1. Login");
			Scanner sc=new Scanner(System.in);
			int l = sc.nextInt();
			if (l == 1) {
				Login();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void prdQuantity() {
		try {
			Connection connection= CommonConnection.getConnection();
			PreparedStatement ps = connection.prepareStatement("select ProductId, quantity from products");			
			ResultSet rs=ps.executeQuery();
			System.out.println("ProductId\tQuantity");
			while (rs.next()) {
				System.out.println(rs.getInt(1)+"\t\t"+rs.getInt(2));
			}
			System.out.println("1.Back");
			Scanner sc=new Scanner(System.in);
			int val = sc.nextInt();
			if (val == 1) {
				backfn();
			} else {
				homepage();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void purHistory() {
		try {
			Connection connection= CommonConnection.getConnection();
			PreparedStatement ps = connection.prepareStatement("select * from purchasehistory");			
			ResultSet rs=ps.executeQuery();
			System.out.println("orderid\tuserid\t\tproductid");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "\t" + rs.getInt(2) + "\t\t" + rs.getInt(3));				
			}
			System.out.println("1.Back");
			Scanner sc=new Scanner(System.in);
			int val = sc.nextInt();
			if (val == 1) {
				backfn();
			} else {
				homepage();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void regUser() {
		try {
			Connection connection= CommonConnection.getConnection();
			PreparedStatement ps = connection.prepareStatement("select * from user");			
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
				System.out.println("Name: " + rs.getString(1));
				System.out.println("Password: " + rs.getString(2));
				System.out.println("Mobile No: " + rs.getLong(3));
				System.out.println("EmailID: " + rs.getString(4));
				System.out.println("UserID: " + rs.getInt(5));
		     }
			System.out.println("1.Back");
			Scanner sc=new Scanner(System.in);
			int val = sc.nextInt();
			if (val == 1) {
				backfn();
			} else {
				homepage();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void backfn() {
		Scanner sc=new Scanner(System.in);
		System.out.println("Authentication Successfull Welcome to Admin Panel");
        System.out.println("1.Show Register users");
        System.out.println("2.Show users purchase history");
        System.out.println("3.Show Products quantity");
        int id= sc.nextInt();
        if (id == 1) {
        	regUser();
        } else if (id == 2) {
        	purHistory();
        } else if (id == 3) {
        	prdQuantity();
        } else {
        	adminlogin();
        }
	}
	
	public static void adminlogin() {
		System.out.println("Enter Admin Login details");
		try {
			System.out.println("Enter Username:");
			Scanner sc=new Scanner(System.in);
			String username=sc.next();
			System.out.println("Enter Password:");
			String pass=sc.next();
			Connection connection=CommonConnection.getConnection();
			PreparedStatement ps=connection.prepareStatement("select * from admin where username=? and password=?");
			ps.setString(1, username);
			ps.setString(2, pass);
			ResultSet rs=ps.executeQuery();
			if (rs.next()) {
				backfn();
			} else {
				System.out.println("Admin Login failed, Enter valid credentils!!");
				adminlogin();
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void homepage() {
		System.out.println("1: Register");
		System.out.println("2: Login");
		System.out.println("3: Admin Login");
		Scanner sc=new Scanner(System.in);
		int id = sc.nextInt();
		if (id == 1) {
			System.out.println("Enter Name");
			String name=sc.next();
			
			System.out.println("Enter Email id ");
			String emailID=sc.next();
			
			System.out.println("Enter Mobile Number ");
			String MobileNumber=sc.next();
			
			System.out.println("Enter Password ");
			String password=sc.next();

			sc.close();
			if (id == 1) {
				insertUserData(name, password, MobileNumber, emailID);
			}
		} else if (id == 2) {
			Login();
		} else if (id == 3) {
			adminlogin();
		} else {
			System.out.println("Wrong Choice Redirect to HOMEPAGE");
			homepage();
		}
	}
	public static void main(String[] args) throws SQLException{
		
		System.out.println("WELCOME TO E-COMMERCE");
		homepage();
		}
	}