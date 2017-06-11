package it.polito.tdp.flight.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flight.model.Airline;
import it.polito.tdp.flight.model.Airport;
import it.polito.tdp.flight.model.Misurazioni;
import it.polito.tdp.flight.model.Route;

public class FlightDAO {

	public List<Airline> getAllAirlines() {
		String sql = "SELECT * FROM airline";
		List<Airline> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Airline(res.getInt("Airline_ID"), res.getString("Name"), res.getString("Alias"),
						res.getString("IATA"), res.getString("ICAO"), res.getString("Callsign"),
						res.getString("Country"), res.getString("Active")));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<Route> getAllRoutes() {
		String sql = "SELECT * FROM route";
		List<Route> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Route(res.getString("Airline"), res.getInt("Airline_ID"), res.getString("Source_airport"),
						res.getInt("Source_airport_ID"), res.getString("Destination_airport"),
						res.getInt("Destination_airport_ID"), res.getString("Codeshare"), res.getInt("Stops"),
						res.getString("Equipment")));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<Airport> getAllAirports() {
		String sql = "SELECT * FROM airport";
		List<Airport> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Airport(res.getInt("Airport_ID"), res.getString("name"), res.getString("city"),
						res.getString("country"), res.getString("IATA_FAA"), res.getString("ICAO"),
						new LatLng(res.getDouble("Latitude"), res.getDouble("Longitude")), res.getFloat("timezone"),
						res.getString("dst"), res.getString("tz")));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<Misurazioni> getMisure(int dist, Map<Integer, Airport> mappa) {
		
		String sql="select a1.Airport_ID as source, a2.Airport_ID as dest "+
					"from route as r, airport as a1, airport as a2,airline as comp "+
					"where r.Source_airport_ID=a1.Airport_ID and r.Destination_airport_ID=a2.Airport_ID and a1.Airport_ID<>a2.Airport_ID and comp.Airline_ID=r.Airline_ID "+
					"group by source,dest";
		List<Misurazioni> lista = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if(mappa.containsKey(res.getInt("source")) && mappa.containsKey(res.getInt("dest"))){
					Airport a=mappa.get(res.getInt("source"));
					Airport b=mappa.get(res.getInt("dest"));
					double dis= LatLngTool.distance(a.getCoord(),b.getCoord(), LengthUnit.KILOMETER);
					if(dis<dist)
						lista.add(new Misurazioni(a,b,dis,dis/800.0));
				}
			}
			conn.close();
			return lista;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
