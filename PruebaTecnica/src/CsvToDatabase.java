import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import com.opencsv.CSVReader;

public class CsvToDatabase {
    public static void main(String[] args) {
        // JDBC URL de la base de datos, usuario y contraseña (reemplace los valores por defecto por sus valores para establecer conexion)
        String dbURL = "jdbc:sqlserver://your_server:your_port;databaseName=your_database;";
        String username = "your_sql_username";
        String password = "your_sql_password";

        // Se le informa al usuario que los parametros de conexion con SQL server no han sido cambiados
        if (dbURL.equals("jdbc:sqlserver://your_server:your_port;databaseName=your_database;")) {
            System.out.println("\nADVERTENCIA: Por favor cambie el URL de la base de datos que esta por defecto en el codigo como dbURL, de lo contrario no podra establecer conexion");
            return;
        }

        // Se le pregunta al usuario que ingrese la ubicacion del archivo con el Scanner de entrada de datos
        Scanner scanner = new Scanner(System.in);
        String csvFilePath;
        do {
            System.out.println("\nIngrese la ubicacion del archivo CSV que desea importar: ");
            csvFilePath = scanner.nextLine().trim(); //se ocupa trim para que no se ingresen espacios en blanco
        } while (csvFilePath.isEmpty());

        try (Connection connection = DriverManager.getConnection(dbURL, username, password)) {
            // Se crea una tabla en caso de que no exista
            createTableIfNotExists(connection);

            // Se lee el archivo CSV
            try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
                String[] nextLine;
                // Se prepara la insercion de datos con INSERT
                String insertQuery = "INSERT INTO YourTableName (Column1, Column2, Column3) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    while ((nextLine = reader.readNext()) != null) {
                        // Asumiendo que el archivo .csv tiene solamente tres columnas, en caso de tener mas necesita ajustarse
                        preparedStatement.setString(1, nextLine[0]);
                        preparedStatement.setString(2, nextLine[1]); //setString debe cambiarse por el tipo de dato a utilizar en la columna
                        preparedStatement.setString(3, nextLine[2]);

                        // Se ejecuta la insercion de datos
                        preparedStatement.executeUpdate();
                    }
                    System.out.println("Data from file " + csvFilePath + " loaded successfully!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTableIfNotExists(Connection connection) throws SQLException {
        // Se crea una tabla en caso de que no exista
        String createTableQuery = "CREATE TABLE IF NOT EXISTS YourTableName (Column1 VARCHAR(255), Column2 VARCHAR(255), Column3 VARCHAR(255))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery)) {
            preparedStatement.executeUpdate();
        }
    }
}
