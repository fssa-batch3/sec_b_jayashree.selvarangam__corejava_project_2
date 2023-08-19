package in.fssa.expressoCafe.service;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import in.fssa.expressoCafe.dao.PriceDAO;
import in.fssa.expressoCafe.dao.ProductDAO;
import in.fssa.expressoCafe.dao.SizeDAO;
import in.fssa.expressoCafe.exception.PersistanceException;
import in.fssa.expressoCafe.exception.ServiceException;
import in.fssa.expressoCafe.exception.ValidationException;
import in.fssa.expressoCafe.model.Price;
import in.fssa.expressoCafe.model.Product;
import in.fssa.expressoCafe.util.ConnectionUtil;
import in.fssa.expressoCafe.util.IntUtil;
import in.fssa.expressoCafe.validator.ProductValidator;
import in.fssa.expressoCafe.validator.SizeValidator;

public class PriceService {
/**
 * 
 * @param price
 * @throws ServiceException
 * @throws ValidationException
 */
	public void createPrice(Price price) throws ServiceException, ValidationException {
		try {

			IntUtil.priceCheck(price.getPrice(), "Price"); // Validate the price before creating

			PriceDAO priceDao = new PriceDAO();
			priceDao.createProductPrices(price);

			System.out.println("Price created successfully");
		} catch (PersistanceException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
/**
 * 
 * @param productId
 * @return
 * @throws ServiceException
 */
	public List<Price> getHistoryOfProuct(int productId) throws ServiceException , ValidationException{
		List<Price> priceList = null;
		try {
			ProductDAO prod = new ProductDAO();
			IntUtil.rejectIfInvalidInt(productId,"ProductId");
			prod.doesProductExist(productId);
			PriceDAO price = new PriceDAO();
			priceList = price.getAllPriceHistory(productId);
		}  catch (PersistanceException e) {
			throw new ServiceException(e.getMessage());
		}
		return priceList;
	}
/**
 * 
 * @param productId
 * @param sizeId
 * @return
 * @throws ServiceException
 */
	public List<Price> getHistoryOfProuctWithUniqueSize(int productId, int sizeId) throws ServiceException ,ValidationException {
		List<Price> priceList = null;
		try {
			ProductDAO prod = new ProductDAO();
			SizeDAO sizeDAO = new SizeDAO();
			IntUtil.rejectIfInvalidInt(productId,"ProductId");
			IntUtil.rejectIfInvalidInt(sizeId,"SizeId");
			// check product id already exists
			prod.doesProductExist(productId);

			// check size_id already exists
			sizeDAO.doesSizeIdExists(sizeId);
			PriceDAO price = new PriceDAO();

			priceList = price.getAllPriceHistoryWithSizeID(productId, sizeId);
		} catch (PersistanceException e) {
			throw new ServiceException(e.getMessage());
		}
		return priceList;
	}
/**
 * 
 * @param productId
 * @param size_id
 * @param price
 * @return
 * @throws ValidationException
 * @throws ServiceException
 */
	public Product updatePrice(int productId, int size_id, double price) throws ValidationException, ServiceException {
		Product product = new Product();
		try {
			ProductDAO ProductDAO= new ProductDAO();
			SizeDAO sizeDAO = new SizeDAO();
			

			PriceDAO price1 = new PriceDAO();
			ProductService productser = new ProductService();
			// form validation for product id and category id
			IntUtil.rejectIfInvalidInt(size_id, "SizeId");
			IntUtil.rejectIfInvalidInt(productId, "ProductId");
			IntUtil.priceCheck(price, "Price");
			// check product id already exists
			ProductDAO.doesProductExist(productId);
			// check size_id already exists
			sizeDAO.doesSizeIdExists(size_id);
			// check whether the price needed to be updated is same as its previous price
			int Storedprice = price1.findPriceByProductIdAndSizeId(productId, size_id);

			// check
			// validate whether the price is appropriate
			// I will do it later

			if (Storedprice != price) {
				int priceId = price1.checkIfPriceExistForProductWithUniqueSize(productId, size_id);
				LocalDateTime date = LocalDateTime.now();
				java.sql.Timestamp dateTime = java.sql.Timestamp.valueOf(date);
				price1.UpdatePrice(priceId, dateTime);
				price1.SetNewPrice(productId, size_id, price, dateTime);
				// get all prices for the product
			} 
			else {
				throw new ServiceException("Product price should not be same");
			}
		product = productser.findProductWithProductId(productId);
		}  catch (PersistanceException e) {
			throw new ServiceException(e.getMessage());
		}
		return product;
	}
}
