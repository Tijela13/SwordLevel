package br.tijela.swordlevelv3.connector;

import org.bukkit.configuration.ConfigurationSection;

public class ConnectorMySql extends ConnectorBase{

    public ConnectorMySql(ConfigurationSection section){
        super(section);
    }

    @Override
    public void loadFromConfig(ConfigurationSection section){
        super.loadFromConfig(section);
        section = section.getConfigurationSection("MySql");
        url = "jdbc:mysql://"+section.getString("Host")+
                (section.contains("Port") ? ":" + section.getString("Port") : "")+
                "/"+section.getString("Database");
        user = section.getString("User");
        password = section.getString("Password");
    }
}
