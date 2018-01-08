/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.services;

import appointment.models.AddressInterface;
import java.util.UUID;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author sagegatzke
 */
public class AddressService extends BaseService implements AddressServiceInterface {

    @Override
    public AddressInterface addAddress(AddressInterface address) {
        return this._addAddress(address);
    }

    private AddressInterface _addAddress(AddressInterface address) {
        String validQuery = getInsertStatement(address);
        try {
            Statement insertStatement = conn.createStatement();
            insertStatement.execute(validQuery);

            insertStatement.closeOnCompletion();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return address;
    }

    @Override
    public AddressInterface updateAddress(AddressInterface address) {
        return address;
    }

    private AddressInterface _updateAddress(AddressInterface address) {
        String validQuery = getUpdateStatement(address);
        try {
            Statement validStatement = conn.createStatement();
            validStatement.execute(validQuery);

            validStatement.closeOnCompletion();
        } catch (SQLException ex) {
            System.out.println(ex.toString());

        }
        return address;
    }

    private String getInsertStatement(AddressInterface address) {
        int id = Math.abs(UUID.randomUUID().hashCode());
        address.setAddressId(id);
        return "INSERT INTO address "
                + "(`addressId`, "
                + "`address`, "
                + "`address2`, "
                + "`cityId`, "
                + "`postalCode`, "
                + "`phone`, "
                + "`createDate`, "
                + "`createdBy`, "
                + "`lastUpdate`, "
                + "`lastUpdateBy`) "
                + "VALUES ("
                + "'" + address.getAddressId() + "',"
                + "'" + address.getAddress() + "', "
                + "'" + address.getAddress2() + "',"
                + "'" + address.getCity().getCityId() + "', "
                + "'" + address.getPostalCode() + "',"
                + "'" + address.getPhone() + "',"
                + "'" + address.getCreatedDate() + "', "
                + "'" + address.getCreatedBy() + "', "
                + "'" + address.getLastUpdate() + "', "
                + "'" + address.getLastUpdatedBy() + "')";
    }

    private String getUpdateStatement(AddressInterface address) {
        return "UPDATE address "
                + "SET `address`='" + address.getAddress() + "', "
                + "`address2`='" + address.getAddress2() + "', "
                + "`cityId`='" + address.getCity().getCityId() + "', "
                + "`phone`='" + address.getPhone() + "', "
                + "`postalCode`='" + address.getPostalCode() + "', "
                + "`createDate`='" + address.getCreatedDate() + "', "
                + "`createdBy`='" + address.getCreatedBy() + "', "
                + "`lastUpdate`='" + address.getLastUpdate() + "', "
                + "`lastUpdateBy`='" + address.getLastUpdatedBy() + "' "
                + "WHERE `addressId`='" + address.getAddressId() + "'";
    }
}
