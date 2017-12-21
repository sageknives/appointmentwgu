/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.services;

import appointment.models.City;
import appointment.models.CityInterface;
import appointment.models.Country;
import appointment.models.CountryInterface;
import static appointment.services.BaseService.conn;
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
public class CityService extends BaseService implements CityServiceInterface {

    private final UUID provider = UUID.randomUUID();

    @Override
    public CityInterface addCity(CityInterface city) {
        return this._addCity(city);
    }

    private CityInterface _addCity(CityInterface city) {
        String validQuery = getInsertStatement(city);
        try {
            Statement insertStatement = conn.createStatement();
            insertStatement.execute(validQuery);

            insertStatement.closeOnCompletion();
        } catch (SQLException ex) {
            //dbAppointments = null;
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
        return city;
    }

    @Override
    public CityInterface[] getCities(int countryId) {
        return _getCities(countryId);
    }

    private CityInterface[] _getCities(int countryId) {
        List<CityInterface> cities = new ArrayList();
        String validQuery = getSelectStatement()
            + "WHERE `countryId`='"+ countryId +"'";
        try {
            Statement validStatement = conn.createStatement();
            ResultSet result = validStatement.executeQuery(validQuery);

            while (result.next()) {
                cities.add(createFromResult(result));
            }
            validStatement.closeOnCompletion();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            //dbAppointments = null;
            ex.printStackTrace();
        }
        CityInterface[] dbCities = new CityInterface[cities.size()];
        dbCities = cities.toArray(dbCities);
        return dbCities;
    }

    private String getSelectStatement() {
        return "Select "
            + "ci.cityId as cityId,"
            + "ci.city as city,"
            + "ci.countryId as countryId,"
            + "ci.createDate as createDate,"
            + "ci.createdBy as createdBy,"
            + "ci.lastUpdate as lastUpdate,"
            + "ci.lastUpdateBy as lastUpdateBy "
            + "FROM city as ci ";
    }

    private String getInsertStatement(CityInterface city) {
        int id = Math.abs(UUID.randomUUID().hashCode());
        city.setCityId(id);
        return "INSERT INTO city "
            + "(`cityId`, "
            + "`city`, "
            + "`countryId`, "
            + "`createDate`, "
            + "`createdBy`, "
            + "`lastUpdate`, "
            + "`lastUpdateBy`) "
            + "VALUES ("
            + "'" + city.getCityId() + "',"
            + "'" + city.getCity() + "', "
            + "'" + city.getCountry().getCountryId() + "',"
            + "'" + city.getCreatedDate() + "', "
            + "'" + city.getCreatedBy() + "', "
            + "'" + city.getLastUpdate() + "', "
            + "'" + city.getLastUpdatedBy() + "')";
    }

    private CityInterface createFromResult(ResultSet result) throws SQLException {
        CountryInterface country = new Country();
        country.setCountryId(result.getInt("countryId"));
        return new City(
            result.getInt("cityId"),
            result.getString("city"),
            country,
            result.getString("createdBy"),
            result.getTimestamp("createDate").toLocalDateTime(),
            result.getString("lastUpdateBy"),
            result.getTimestamp("lastUpdate").toLocalDateTime()
        );
    }
}
