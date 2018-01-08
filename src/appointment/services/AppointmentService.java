/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.services;

import appointment.models.Appointment;
import appointment.models.AppointmentInterface;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import java.util.HashMap;

/**
 *
 * @author sagegatzke
 */
public class AppointmentService extends BaseService implements AppointmentServiceInterface {

    private final UUID provider = UUID.randomUUID();

    @Override
    public AppointmentInterface getAppointment(int appointmentId) {
        return _getAppointment(appointmentId);
    }

    private AppointmentInterface _getAppointment(int appointmentId) {
        AppointmentInterface dbAppointment;
        //String validAppointmentQuery = "SELECT * FROM appointment as app WHERE app.appointmentId = '" + appointment.getAppointmentId() + "'";
        String validAppointmentQuery = getSelectStatement()
                + "where ap.appointmentId ='" + appointmentId + "'";
        try {
            Statement findAppointmentStatement = conn.createStatement();
            ResultSet result = findAppointmentStatement.executeQuery(validAppointmentQuery);
            if (result.first()) {
                dbAppointment = createAppointmentFromResult(result);
            } else {
                dbAppointment = null;
            }

            findAppointmentStatement.closeOnCompletion();

        } catch (SQLException ex) {
            dbAppointment = null;
//            ex.printStackTrace();
        }
        return dbAppointment;
    }

    @Override
    public AppointmentInterface[] getAppointments(String userName) {
        return _getAppointments(userName);
    }

    private AppointmentInterface[] _getAppointments(String userName) {
        List<AppointmentInterface> appointments = new ArrayList<AppointmentInterface>();
        String validAppointmentQuery = getSelectStatement();
        if (userName != null) {
            validAppointmentQuery += " WHERE ap.createdBy = '" + userName + "' or ap.lastUpdateBy = '" + userName + "'";
        }

        try {
            Statement findAppointmentStatement = conn.createStatement();
            ResultSet result = findAppointmentStatement.executeQuery(validAppointmentQuery);

            while (result.next()) {
                appointments.add(createAppointmentFromResult(result));
            }
            findAppointmentStatement.closeOnCompletion();
        } catch (SQLException ex) {
            //dbAppointments = null;
//            ex.printStackTrace();
        }
        //dbAppointments = (AppointmentInterface[]) appointments.toArray();
        AppointmentInterface[] dbAppointments = new AppointmentInterface[appointments.size()];
        dbAppointments = appointments.toArray(dbAppointments);
        return dbAppointments;
    }

    @Override
    public HashMap getTypeCountPerMonth() {
        HashMap typeMap;
        typeMap = new HashMap<String, Integer>();
        String validAppointmentQuery = getTypeCountByMonthSelectStatement();
        String[] months = {"0","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        try {
            Statement findAppointmentStatement = conn.createStatement();
            ResultSet result = findAppointmentStatement.executeQuery(validAppointmentQuery);

            while (result.next()) {
                
                typeMap.put(months[result.getInt("month")], result.getInt("num"));
            }
            findAppointmentStatement.closeOnCompletion();
        } catch (SQLException ex) {
            //dbAppointments = null;
            ex.printStackTrace();
        }
        return typeMap;
    }

    @Override
    public AppointmentInterface addAppointment(AppointmentInterface appointment) {
        return _addAppointment(appointment);
    }

    private AppointmentInterface _addAppointment(AppointmentInterface appointment) {
        String validAppointmentQuery = getInsertStatement(appointment);
        try {
            Statement insertAppointmentStatement = conn.createStatement();
            insertAppointmentStatement.execute(validAppointmentQuery);

            insertAppointmentStatement.closeOnCompletion();
        } catch (SQLException ex) {
            //dbAppointments = null;
            ex.printStackTrace();
        }
        return appointment;
    }

    @Override
    public AppointmentInterface updateAppointment(AppointmentInterface appointment) {
        return _updateAppointment(appointment);
    }

    private AppointmentInterface _updateAppointment(AppointmentInterface appointment) {
        String validAppointmentQuery = getUpdateStatement(appointment);
        try {
            Statement updateAppointmentStatement = conn.createStatement();
            updateAppointmentStatement.execute(validAppointmentQuery);

            updateAppointmentStatement.closeOnCompletion();
        } catch (SQLException ex) {
            //dbAppointments = null;
            ex.printStackTrace();
        }
        return appointment;
    }

    private String getUpdateStatement(AppointmentInterface appointment) {
        return "UPDATE appointment "
                + "SET `title`='" + appointment.getTitle() + "', "
                + "`description`='" + appointment.getDescription() + "', "
                + "`location`='" + appointment.getLocation() + "', "
                + "`contact`='" + appointment.getContact() + "', "
                + "`url`='" + appointment.getUrl() + "', "
                + "`start`='" + appointment.getStart().toLocalDateTime() + "', "
                + "`end`='" + appointment.getEnd().toLocalDateTime() + "', "
                + "`createDate`='" + appointment.getCreatedDate() + "', "
                + "`createdBy`='" + appointment.getCreatedBy() + "', "
                + "`lastUpdate`='" + appointment.getLastUpdate() + "', "
                + "`lastUpdateBy`='" + appointment.getLastUpdatedBy() + "' "
                + "WHERE `appointmentId`='" + appointment.getAppointmentId() + "'";
    }

    private String getInsertStatement(AppointmentInterface appointment) {
        int id = Math.abs(provider.hashCode());
        appointment.setAppointmentId(id);
        return "INSERT INTO appointment "
                + "(`appointmentId`, "
                + "`customerId`, "
                + "`title`, "
                + "`description`, "
                + "`location`, "
                + "`contact`, "
                + "`url`, "
                + "`start`, "
                + "`end`, "
                + "`createDate`, "
                + "`createdBy`, "
                + "`lastUpdate`, "
                + "`lastUpdateBy`) "
                + "VALUES ("
                + "'" + appointment.getAppointmentId() + "',"
                + " " + appointment.getCustomer().getCustomerId() + ", "
                + "'" + appointment.getTitle() + "', "
                + "'" + appointment.getDescription() + "', "
                + "'" + appointment.getLocation() + "', "
                + "'" + appointment.getContact() + "', "
                + "'" + appointment.getUrl() + "', "
                + "'" + appointment.getStart().toLocalDateTime() + "', "
                + "'" + appointment.getEnd().toLocalDateTime() + "', "
                + "'" + appointment.getCreatedDate() + "', "
                + "'" + appointment.getCreatedBy() + "', "
                + "'" + appointment.getLastUpdate() + "', "
                + "'" + appointment.getLastUpdatedBy() + "')";
    }

    private String getSelectStatement() {
        return "Select "
                + "ap.appointmentId as appointmentId,"
                + "ap.title as title,"
                + "ap.description as description,"
                + "ap.location as location,"
                + "ap.contact as contact,"
                + "ap.url as url,"
                + "ap.start as start,"
                + "ap.end as end,"
                + "ap.createDate as appointmentCreateDate,"
                + "ap.createdBy as appointmentCreatedBy,"
                + "ap.lastUpdate as appointmentLastUpdate,"
                + "ap.lastUpdateBy as appointmentLastUpdateBy,"
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
                + "FROM appointment as ap "
                + "join customer as cu on ap.customerId = cu.customerId "
                + "join address as ad on cu.addressId = ad.addressId "
                + "join city as ci on ci.cityId = ad.cityId "
                + "join country as co on co.countryId = ci.countryId ";
    }

    private String getTypeCountByMonthSelectStatement() {
        return "SELECT "
                + "count(Distinct contact) AS num, "
                + "MONTH(start) as month "
                + "FROM appointment "
                + "GROUP BY MONTH(start)";
    }

    private AppointmentInterface createAppointmentFromResult(ResultSet result) throws SQLException {
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
        AppointmentInterface dbAppointment = new Appointment(
                result.getInt("appointmentId"),
                dbCustomer,
                result.getString("title"),
                result.getString("description"),
                result.getString("location"),
                result.getString("contact"),
                result.getString("url"),
                result.getTimestamp("start").toLocalDateTime().atZone(ZoneId.systemDefault()),
                result.getTimestamp("end").toLocalDateTime().atZone(ZoneId.systemDefault()),
                result.getString("appointmentCreatedBy"),
                result.getTimestamp("appointmentCreateDate").toLocalDateTime(),
                result.getString("appointmentLastUpdateBy"),
                result.getTimestamp("appointmentLastUpdate").toLocalDateTime()
        );
        return dbAppointment;
    }

}
