/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.services;

import appointment.models.Country;
import appointment.models.CountryInterface;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author sagegatzke
 */
public class CountryService extends BaseService implements CountryServiceInterface {
    private final UUID provider = UUID.randomUUID();

    @Override
    public CountryInterface addCountry(CountryInterface country) {
        return this._addCountry(country);
    }
    
    private CountryInterface _addCountry(CountryInterface country){
        String validQuery = getInsertStatement(country);
        try {
            Statement insertStatement = conn.createStatement();
            insertStatement.execute(validQuery);

            insertStatement.closeOnCompletion();
        } catch (SQLException ex) {
            //dbAppointments = null;
            //ex.printStackTrace();
        }
        return country;
    }

    @Override
    public CountryInterface[] getCountries() {
        return _getCountries();
    }

    private CountryInterface[] _getCountries() {
        List<CountryInterface> countries = new ArrayList();
        String validQuery = getSelectStatement();
        try {
            Statement validStatement = conn.createStatement();
            ResultSet result = validStatement.executeQuery(validQuery);

            while (result.next()) {
                countries.add(createFromResult(result));
            }
            validStatement.closeOnCompletion();
        } catch (SQLException ex) {
            //dbAppointments = null;
            //System.out.print(ex.toString());
            //ex.printStackTrace();
        }
        CountryInterface[] dbCountries = new CountryInterface[countries.size()];
        dbCountries = countries.toArray(dbCountries);
        return dbCountries;
    }

    private String getSelectStatement() {
        return "Select "
            + "co.countryId as countryId,"
            + "co.country as country,"
            + "co.createDate as createDate,"
            + "co.createdBy as createdBy,"
            + "co.lastUpdate as lastUpdate,"
            + "co.lastUpdateBy as lastUpdateBy "
            + "FROM country as co ";
    }

    private String getInsertStatement(CountryInterface country){
        int id = Math.abs(UUID.randomUUID().hashCode());
        country.setCountryId(id);
        return "INSERT INTO country "
            +"(`countryId`, "
            +"`country`, "
            +"`createDate`, "
            +"`createdBy`, "
            +"`lastUpdate`, "
            +"`lastUpdateBy`) "
            + "VALUES ("
            + "'"+country.getCountryId()+"',"
            + "'"+country.getCountry()+"', "
            + "'"+country.getCreatedDate()+"', "
            + "'"+country.getCreatedBy()+"', "
            + "'"+country.getLastUpdate()+"', "
            + "'"+country.getLastUpdatedBy()+"')";
    }
    
    private CountryInterface createFromResult(ResultSet result) throws SQLException {
        return new Country(
            result.getInt("countryId"),
            result.getString("country"),
            result.getString("createdBy"),
            result.getTimestamp("createDate").toLocalDateTime(),
            result.getString("lastUpdateBy"),
            result.getTimestamp("lastUpdate").toLocalDateTime()
        );
    }
}
