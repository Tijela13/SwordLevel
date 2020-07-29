package br.tijela.swordlevelv3.connector;

import org.bukkit.configuration.ConfigurationSection;

public class ConnectorPostgreSQL extends ConnectorBase{

    public ConnectorPostgreSQL(ConfigurationSection section){
        super(section);
    }

    @Override
    public void loadFromConfig(ConfigurationSection section){
        super.loadFromConfig(section);
        section = section.getConfigurationSection("PostgreSQL");
        url = "jdbc:postgresql://"+section.getString("Host")+
                (section.contains("Port") ? ":" + section.getString("Port") : "")+
                "/"+section.getString("Database");
        user = section.getString("User");
        password = section.getString("Password");
    }
}
