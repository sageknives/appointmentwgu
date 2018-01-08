/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import appointment.models.Address;
import appointment.models.AddressInterface;
import appointment.models.City;
import appointment.models.CityInterface;
import appointment.models.Country;
import appointment.models.CountryInterface;
import appointment.models.Customer;
import appointment.models.CustomerInterface;
import static appointment.services.BaseService.conn;

/**
 *
 * @author sagegatzke
 */
public class CustomerService extends BaseService implements CustomerServiceInterface {

    @Override
    public CustomerInterface getCustomer(int customerId) {
        return _getCustomer(customerId);
    }

    private CustomerInterface _getCustomer(int customerId) {
        CustomerInterface dbCustomer;
        String validQuery = getSelectStatement()
                + "where cu.customerId ='" + customerId + "'";
        try {
            Statement validStatement = conn.createStatement();
            ResultSet result = validStatement.executeQuery(validQuery);
            if (result.first()) {
                dbCustomer = createCustomerFromResult(result);
            } else {
                dbCustomer = null;
            }

            validStatement.closeOnCompletion();

        } catch (SQLException ex) {
            dbCustomer = null;
            System.out.println(ex.toString());
        }
        return dbCustomer;
    }

    @Override
    public CustomerInterface[] getCustomers() {
        return _getCustomers();
    }

    private CustomerInterface[] _getCustomers() {
        List<CustomerInterface> customers = new ArrayList<>();
        String validQuery = getSelectStatement();
        try {
            Statement validStatement = conn.createStatement();
            ResultSet result = validStatement.executeQuery(validQuery);

            while (result.next()) {
                customers.add(createCustomerFromResult(result));
            }
            validStatement.closeOnCompletion();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        CustomerInterface[] dbCustomers = new CustomerInterface[customers.size()];
        dbCustomers = customers.toArray(dbCustomers);
        return dbCustomers;
    }

    @Override
    public CustomerInterface addCustomer(CustomerInterface customer) {
        return _addCustomer(customer);
    }

    private CustomerInterface _addCustomer(CustomerInterface customer) {
        String validQuery = getInsertStatement(customer);
        try {
            Statement validStatement = conn.createStatement();
            validStatement.execute(validQuery);

            validStatement.closeOnCompletion();
        } catch (SQLException ex) {
            //dbAppointments = null;
            System.out.println(ex.toString());
            //ex.printStackTrace();
        }
        return customer;
    }

    @Override
    public CustomerInterface updateCustomer(CustomerInterface customer) {
        return _updateCustomer(customer);
    }

    private CustomerInterface _updateCustomer(CustomerInterface customer) {
        String validQuery = getUpdateStatement(customer);
        try {
            Statement validStatement = conn.createStatement();
            validStatement.execute(validQuery);

            validStatement.closeOnCompletion();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return customer;
    }

    private String getUpdateStatement(CustomerInterface customer) {
        return "UPDATE customer "
                + "SET `customerName`='" + customer.getCustomerName() + "', "
                + "`addressId`='" + customer.getAddress().getAddressId() + "', "
                + "`active`='" + customer.getActive() + "', "
                + "`createDate`='" + customer.getCreatedDate() + "', "
                + "`createdBy`='" + customer.getCreatedBy() + "', "
                + "`lastUpdate`='" + customer.getLastUpdate() + "', "
                + "`lastUpdateBy`='" + customer.getLastUpdatedBy() + "' "
                + "WHERE `customerId`='" + customer.getCustomerId() + "'";
    }

    private String getInsertStatement(CustomerInterface customer) {

        int id = Math.abs(UUID.randomUUID().hashCode());
        customer.setCustomerId(id);
        return "INSERT INTO customer "
                + "(`customerId`, "
                + "`customerName`, "
                + "`addressId`, "
                + "`active`, "
                + "`createDate`, "
                + "`createdBy`, "
                + "`lastUpdate`, "
                + "`lastUpdateBy`) "
                + "VALUES ("
                + "'" + customer.getCustomerId() + "',"
                + "'" + customer.getCustomerName() + "', "
                + "'" + customer.getAddress().getAddressId() + "', "
                + "'" + customer.getActive() + "', "
                + "'" + customer.getCreatedDate() + "', "
                + "'" + customer.getCreatedBy() + "', "
                + "'" + customer.getLastUpdate() + "', "
                + "'" + customer.getLastUpdatedBy() + "')";
    }

    private String getSelectStatement() {
        return "Select "
                + "cu.customerId as customerId,"
                + "cu.customerName as customerName,"
                + "cu.active  as active,"
                + "cu.createDate as customerCreateDate,"
                + "cu.createdBy as customerCreatedBy,"
                + "cu.lastUpdate as customerLastUpdate,"
                + "cu.lastUpdateBy as customerLastUpdateBy,"
                + "ad.addressId as addressId,"
                + "ad.address as address,"
                + "ad.address2 as address2,"
                + "ad.postalCode as postalCode,"
                + "ad.phone as phone,"
                + "ad.createDate as addressCreateDate,"
                + "ad.createdBy as addressCreatedBy,"
                + "ad.lastUpdate as addressLastUpdate,"
                + "ad.lastUpdateBy as addressLastUpdateBy,"
                + "ci.cityId as cityId,"
                + "ci.city as city,"
                + "ci.createDate as cityCreateDate,"
                + "ci.createdBy as cityCreatedBy,"
                + "ci.lastUpdate as cityLastUpdate,"
                + "ci.lastUpdateBy as cityLastUpdateBy,"
                + "co.countryId as countryId,"
                + "co.country as country,"
                + "co.createDate as countryCreateDate,"
                + "co.createdBy as countryCreatedBy,"
                + "co.lastUpdate as countryLastUpdate,"
                + "co.lastUpdateBy as countryLastUpdateBy "
                + "FROM customer as cu "
                + "join address as ad on cu.addressId = ad.addressId "
                + "join city as ci on ci.cityId = ad.cityId "
                + "join country as co on co.countryId = ci.countryId ";
    }

    private CustomerInterface createCustomerFromResult(ResultSet result) throws SQLException {
        CountryInterface dbCountry = new Country(
                result.getInt("countryId"),
                result.getString("country"),
                result.getString("countryCreatedBy"),
                result.getTimestamp("countryCreateDate").toLocalDateTime(),
                result.getString("countryLastUpdateBy"),
                result.getTimestamp("countryLastUpdate").toLocalDateTime()
        );
        CityInterface dbCity = new City(
                result.getInt("cityId"),
                result.getString("city"),
                dbCountry,
                result.getString("cityCreatedBy"),
                result.getTimestamp("cityCreateDate").toLocalDateTime(),
                result.getString("cityLastUpdateBy"),
                result.getTimestamp("cityLastUpdate").toLocalDateTime()
        );
        AddressInterface dbAddress = new Address(
                result.getInt("addressId"),
                result.getString("address"),
                result.getString("address2"),
                dbCity,
                result.getString("postalCode"),
                result.getString("phone"),
                result.getString("addressCreatedBy"),
                result.getTimestamp("addressCreateDate").toLocalDateTime(),
                result.getString("addressLastUpdateBy"),
                result.getTimestamp("addressLastUpdate").toLocalDateTime()
        );
        CustomerInterface dbCustomer = new Customer(
                result.getInt("customerId"),
                result.getString("customerName"),
                dbAddress,
                result.getInt("active"),
                result.getString("customerCreatedBy"),
                result.getTimestamp("customerCreateDate").toLocalDateTime(),
                result.getString("customerLastUpdateBy"),
                result.getTimestamp("customerLastUpdate").toLocalDateTime()
        );
        return dbCustomer;
    }
}
