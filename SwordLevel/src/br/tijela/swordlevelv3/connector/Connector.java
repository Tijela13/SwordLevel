package br.tijela.swordlevelv3.connector;

import java.sql.Connection;
import java.sql.PreparedStatement;

public interface Connector{

    void openConnection();

    void closeConnection();

    Connection getConnection();

    PreparedStatement getStatement(String query);

}
