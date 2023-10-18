package org.example.liquebase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;

public class Liquebase {
    public static void main(String[] args) {
        try {
            // параметры из конфига
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/application.properties");
            properties.load(fis);

            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String liquibaseSchemaName = properties.getProperty("liquibaseSchemaName");

            // создание схемы, для перемещения служебных таблиц Liquebase
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                Statement statement = connection.createStatement();
                statement.execute("CREATE SCHEMA IF NOT EXISTS audit;");
            }

            Connection connection = DriverManager.getConnection(url, username, password);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName(liquibaseSchemaName); // Устанавливаем схему для Liquibase


            // миграции
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();

            System.out.println("Миграции успешно выполнены!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
