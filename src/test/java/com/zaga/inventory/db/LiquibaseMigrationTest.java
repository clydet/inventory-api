package com.zaga.inventory.db;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.quarkus.test.junit.QuarkusTestExtension;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
// @ExtendWith(QuarkusTestExtension.class)
public class LiquibaseMigrationTest {

    @Inject
    DataSource dataSource;

    @Test
    public void testLiquibaseMigrations() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            // Print connection URL and other properties for verification
            System.out.println("Database URL: " + connection.getMetaData().getURL());
            System.out.println("Database User: " + connection.getMetaData().getUserName());

            // List all schemas
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA")) {
                    System.out.println("Schemas:");
                    while (resultSet.next()) {
                        System.out.println(resultSet.getString(1));
                    }
                }
            }

            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA")) {
                    System.out.println("Schemas:");
                    while (resultSet.next()) {
                        System.out.println(resultSet.getString(1));
                    }
                }
            }

            // List all tables
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT TABLE_NAME, TABLE_SCHEMA FROM INFORMATION_SCHEMA.TABLES")) {
                    System.out.println("Tables:");
                    while (resultSet.next()) {
                        System.out.println(resultSet.getString(1) + " (Schema: " + resultSet.getString(2) + ")");
                    }
                }
            }

            // Check if the ITEM table was created
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'ITEM'")) {
                    assertThat(resultSet.next()).isTrue();
                    assertThat(resultSet.getInt(1)).isEqualTo(1);
                }

                // Check data integrity or specific records if necessary
                try (ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM ITEM")) {
                    assertThat(resultSet.next()).isTrue();
                    assertThat(resultSet.getInt(1)).isGreaterThan(0); // Adjust based on your data
                }
            }
        }
    }
}
