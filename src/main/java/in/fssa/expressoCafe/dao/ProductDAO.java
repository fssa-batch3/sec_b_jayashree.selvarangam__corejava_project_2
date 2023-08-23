
package in.fssa.expressoCafe.dao;

import in.fssa.expressoCafe.exception.PersistanceException;

//import com.mysql.cj.jdbc.PreparedStatementWrapper;

import in.fssa.expressoCafe.interfaces.PriceDAOInterface;
import in.fssa.expressoCafe.model.Category;
import in.fssa.expressoCafe.model.Price;
import in.fssa.expressoCafe.model.Product;
import in.fssa.expressoCafe.model.SizeEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
//import in.fssa.expressoCafe.service.ProductEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.fssa.expressoCafe.util.ConnectionUtil;

public class ProductDAO {
	/**
	 * 
	 * @param product
	 * @return
	 * @throws PersistanceException
	 */
	public Product createProduct(Product product) throws PersistanceException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;
		int productId = -1;
		Timestamp startDate = null; // To store the start date

		try {
			connection = ConnectionUtil.getConnnetion();
			String insertProductQuery = "INSERT INTO product (name, description, category_id, created_date) VALUES (?, ?, ?, ?)";
			ps = connection.prepareStatement(insertProductQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, product.getName());
			ps.setString(2, product.getDescription());
			ps.setInt(3, product.getCategory().getCategoryId());

			// Set the current timestamp as the start date
			startDate = new Timestamp(System.currentTimeMillis());
			ps.setTimestamp(4, startDate);

			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {
					productId = generatedKeys.getInt(1);
					// Set the generated product ID and start date in the product object
					product.setProduct_id(productId);
					System.out.print(startDate);
					product.setCreatedDate(startDate);
				}
			}else {
				System.out.print("Product creation failed");
				throw new PersistanceException("Product creation failed");
			}
		} catch (SQLException e) {
			throw new PersistanceException("Product creation failed");
		} finally {
			ConnectionUtil.close(connection, ps, generatedKeys);
		}
		return product;
	}


	/**
	 * 
	 * @return
	 * @throws PersistanceException
	 */

	// this method get all product name
	public List<String> getAllProductName() throws PersistanceException {
	
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<String> productsName = new ArrayList<>();

		try {
			connection = ConnectionUtil.getConnnetion();
			String query = "SELECT product_id,name FROM product";
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				int productId = rs.getInt("product_id");
				String productName = rs.getString("name");
				productsName.add(productName);
			}
			System.out.print("All product names are recieved");
		} catch (SQLException e) {
			throw new PersistanceException("Cannot get all products Name");
		} catch (Exception e) {
			throw new PersistanceException("Cannot get all products Name");
		} finally {
			ConnectionUtil.close(connection, ps, rs);
		}
		return productsName;
	}

	/**
	 * 
	 * @param product_id
	 * @param productName
	 * @param productDescription
	 * @param cate_id
	 * @throws PersistanceException
	 */
	// add modified at
	public void updateProduct(int product_id, String productName, String productDescription, int cate_id)throws PersistanceException {
		
		Connection connection = null;
		PreparedStatement ps = null;
		
		// ResultSet generatedKeys = null;
		int productId = 0;
		
		try{
			connection = ConnectionUtil.getConnnetion();
			
			String insertProductQuery = "UPDATE product  SET name = ? , description = ? , category_id = ?   WHERE product_id = ? AND status = 1";
			ps = connection.prepareStatement(insertProductQuery);
			
			ps.setString(1, productName);
			ps.setString(2, productDescription);
			ps.setInt(3, cate_id);
			ps.setInt(4, product_id);
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.print("Product Updated");
			} else {
				System.out.print("Product Not Updated ");
				throw new Exception("Updating product failed");
			}
			
		}catch (SQLException e) {
			System.out.print(e.getMessage());
			throw new PersistanceException("Error while updating product");
		}catch (Exception e) {
			System.out.print(e.getMessage());
			throw new PersistanceException("Error while updating product");
		} finally {
			ConnectionUtil.close(connection, ps);
		}
	}

	/**
	 * 
	 * @param product_id
	 * @return
	 * @throws PersistanceException
	 */
	public boolean doesProductExist(int product_id) throws PersistanceException {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		boolean value = true;

		try {
			
			connection = ConnectionUtil.getConnnetion();
			String query = "SELECT * FROM product WHERE product_id = ? AND status = 1";
			ps = connection.prepareStatement(query);
			
			ps.setInt(1, product_id);
			rs = ps.executeQuery();

			if (!rs.next()) {
				System.out.print("product does not exist");
				throw new PersistanceException("product does not exist");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
			throw new PersistanceException("product does not exist");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
			throw new PersistanceException("product does not exist");
		} finally {
			ConnectionUtil.close(connection, ps, rs);
		}
		return value;
	}

	/**
	 * 
	 * @param product_id
	 * @throws PersistanceException
	 */
	// write testcases for isActive
	public void deleteProduct(int product_id) throws PersistanceException{
		
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = ConnectionUtil.getConnnetion();
			String deleteQuery = "UPDATE product SET status = 0 WHERE product_id = ? AND status = 1";
			ps = connection.prepareStatement(deleteQuery);
			
			ps.setInt(1, product_id);
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Product deleted successfully");
			}else {
				System.out.println("Product deletion failed");
				throw new Exception("Product deletion failed");
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new PersistanceException("Product deletion failed");
		}catch (Exception e) {
			e.printStackTrace();
			throw new PersistanceException("Product deletion failed");
		} finally {
			ConnectionUtil.close(connection, ps);
		}
	}

	/// this method is for checking whether the product is active or not
	/**
	 * 
	 * @param product_id
	 * @return
	 * @throws PersistanceException
	 */
	
	public boolean isActive(int product_id) throws PersistanceException {
		String query = "SELECT status FROM product WHERE product_id = ?";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean value = false;
		try {
			connection = ConnectionUtil.getConnnetion();
			ps = connection.prepareStatement(query);
			ps.setInt(1, product_id);
			rs = ps.executeQuery();
			if (rs.next()) {
				int status = rs.getInt("status");
				if (status == 1) {

					System.out.print("Product is active");
					value = true;
				} else {
					throw new PersistanceException("Product is inactive");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistanceException("Product is inactive");
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistanceException("Product is inactive");
		} finally {
			ConnectionUtil.close(connection, ps, rs);
		}
		return value;
	}
	
	public void changeStatus(int product_id , int status) throws PersistanceException {
		String query = "INSERT status VALUES (?) FROM product WHERE product_id = ? ";
		Connection connection = null;
		PreparedStatement ps = null;
		
		boolean value = false;
		try {
			connection = ConnectionUtil.getConnnetion();
			ps = connection.prepareStatement(query);
			ps.setInt(1, status);
			ps.setInt(2, product_id);
			int row =  ps.executeUpdate();
			if (row>0) {
		
					System.out.print("Product status is updated");
					
				} else {
					throw new PersistanceException("Product status updation is failed");
				}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistanceException("Product status updation is failed");
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistanceException("Product status updation is failed");
		} finally {
			ConnectionUtil.close(connection, ps);
		}
		
	
		}
		



	/**
	 * 
	 * @return
	 * @throws PersistanceException
	 */
	public List<Product> getAllProducts() throws PersistanceException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<Product> products = new ArrayList<>();

		try {
			con = ConnectionUtil.getConnnetion();
			String query = "SELECT * FROM product";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				Product product = new Product();
				Category category = new Category();
				product.setProduct_id(rs.getInt("product_id"));
				product.setName(rs.getString("name"));
				product.setDescription(rs.getString("description"));
				category.setCategoryId(rs.getInt("category_id"));
				product.setCategory(category);
				// product.setSize(SizeEnum.values()[rs.getInt("size_id") - 1]);
//		        
				products.add(product);

			}
		} catch (SQLException e) {
			throw new PersistanceException("Cannot get all products");
		} catch (Exception e) {
			throw new PersistanceException("Cannot get all products");
		} finally {
			ConnectionUtil.close(con, ps, rs);
		}

		return products;
	}

	/**
	 * 
	 * @param category_id
	 * @return
	 * @throws PersistanceException
	 */
	public List<Product> getAllproductswithCategoryId(int category_id) throws PersistanceException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<Product> products = new ArrayList<>();

		try {
			con = ConnectionUtil.getConnnetion();
			String query = "SELECT * FROM product where category_id = ? ";
			ps = con.prepareStatement(query);
			ps.setInt(1, category_id);
			rs = ps.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				Category category = new Category();

				product.setProduct_id(rs.getInt("product_id"));
				product.setName(rs.getString("name"));
				product.setDescription(rs.getString("description"));
				category.setCategoryId(rs.getInt("category_id"));
				product.setCategory(category);
				products.add(product);
			}
		} catch (SQLException e) {
			throw new PersistanceException("Cannot get all products");
		} catch (Exception e) {
			throw new PersistanceException("Cannot get all products");
		} finally {
			ConnectionUtil.close(con, ps, rs);
		}
		return products;
	}

	/**
	 * 
	 * @param productId
	 * @return
	 * @throws PersistanceException
	 */
	public Product findProductWithProductId(int productId) throws PersistanceException {
		Product product = new Product();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = ConnectionUtil.getConnnetion();
			String query = "SELECT * FROM product WHERE product_id = ?";
			ps = connection.prepareStatement(query);
			ps.setInt(1, productId);
			rs = ps.executeQuery();

			if (rs.next()) {
				product = new Product();
				Category category = new Category();

				product.setProduct_id(rs.getInt("product_id"));
				product.setName(rs.getString("name"));
				product.setDescription(rs.getString("description"));
				category.setCategoryId(rs.getInt("category_id"));
				product.setCategory(category);
			}
		} catch (SQLException e) {
			throw new PersistanceException("Cannot get  product detail");
		} catch (Exception e) {
			throw new PersistanceException("Cannot get  product detail");
		} finally {
			ConnectionUtil.close(connection, ps, rs);
		}
		return product;
	}

	
	
//	public 	Product createProduct1(Product product) throws PersistanceException {
//
//		
//		Connection connection = null;
//		PreparedStatement ps = null;
//		ResultSet generatedKeys = null;
//	//	Product productObj = null ; 
//		int productId = -1;
//	
//
//		try {
//			connection = ConnectionUtil.getConnnetion();
//			String insertProductQuery = "INSERT INTO product (name, description,category_id) VALUES (?, ?,?)";
//			ps = connection.prepareStatement(insertProductQuery, PreparedStatement.RETURN_GENERATED_KEYS);
//			ps.setString(1, product.getName());
//			ps.setString(2, product.getDescription());
//			ps.setInt(3, product.getCategory().getCategoryId());
//			
//			int rowsAffected = ps.executeUpdate();
//			if (rowsAffected > 0) {
//				generatedKeys = ps.getGeneratedKeys();
//				if (generatedKeys.next()) {
//					productId = generatedKeys.getInt(1);
//					
//				} else {
//					throw new PersistanceException("Creating product failed, no ID obtained.");
//				}
//
//			} else {
//				System.out.print("Product creation failed");
//				throw new PersistanceException("Product creation failed");
//			}
//		} catch (SQLException e) {
//			throw new PersistanceException("Error while creating product");
//		} finally {
//			ConnectionUtil.close(connection, ps, generatedKeys);
//		}
//		return product;
//	}

	

//

}
