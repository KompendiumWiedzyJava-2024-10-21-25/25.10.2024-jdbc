package com.comarch.szkolenia.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class App {

    public static Connection connection;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        //Mateusz.bereda@comarch.com
        connect();
        Optional<Client> userBox = getUser(2);
        if(userBox.isPresent()) {
            Client client = userBox.get();
            client.setName("Karol");
            updateUser(client);
        } else {
            System.out.println("Nie ma takiego usera !!!");
        }

        //inne zmiany

        /*Client client = new Client(0, "Wiesiek", "Malinowski", 20, "123");
        insertUser(client);*/
        //System.out.println(getAll());
        //deleteUser(1);

        connection.close();

        //zmiany
    }

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        App.connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/some_db", "root", "");
    }

    public static Optional<Client> getUser(int id) throws SQLException {
        String sql = "SELECT * FROM client WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);

        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) {
            return Optional.of(
                    new Client(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getInt("age"),
                            rs.getString("pesel")
                    )
            );
        }

        return Optional.empty();
    }

    public static List<Client> getAll() throws SQLException {
        String sql = "SELECT * FROM client";
        List<Client> result = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()) {
            result.add(new Client(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getInt("age"),
                    rs.getString("pesel")
            ));
        }

        return result;
    }

    public static void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);

        preparedStatement.execute();
    }

    public static void insertUser(Client client) throws SQLException {
        String sql = "INSERT INTO client (name, surname, age, pesel) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, client.getName());
        preparedStatement.setString(2, client.getSurname());
        preparedStatement.setInt(3, client.getAge());
        preparedStatement.setString(4, client.getPesel());

        preparedStatement.execute();
    }

    public static void updateUser(Client client) throws SQLException {
        String sql = "UPDATE client SET name = ?, surname = ?, age = ?, pesel = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, client.getName());
        preparedStatement.setString(2, client.getSurname());
        preparedStatement.setInt(3, client.getAge());
        preparedStatement.setString(4, client.getPesel());
        preparedStatement.setInt(5, client.getId());

        preparedStatement.execute();
    }
}
