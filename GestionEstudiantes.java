import java.sql.*;
import java.util.Scanner;

public class GestionAlumnos {

    public static void main(String[] args) {
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            String url = "jdbc:mysql://localhost:3306/escuela";
            String username = "juliancollant";
            String password = "pancuca1";

            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Conexión exitosa a la base de datos Escuela");

            boolean continuar = true;
            while (continuar) {
                System.out.println("Menú:");
                System.out.println("1. Mostrar alumnos");
                System.out.println("2. Agregar un alumno");
                System.out.println("3. Eliminar un alumno");
                System.out.println("4. Salir");
                System.out.println("Seleccione una opción:");

                int opcionMenu = scanner.nextInt();
                scanner.nextLine();

                switch (opcionMenu) {
                    case 1:
                        mostrarAlumnos(connection);
                        break;
                    case 2:
                        agregarAlumno(connection, scanner);
                        break;
                    case 3:
                        eliminarAlumno(connection, scanner);
                        break;
                    case 4:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción inválida");
                        break;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                scanner.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    public static void agregarAlumno(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Ingrese el nombre del alumno: ");
        String nombre = scanner.nextLine();

        System.out.println("Ingrese el número de legajo del alumno: ");
        String legajo = scanner.nextLine();

        System.out.println("Ingrese la carrera del alumno: ");
        String carrera = scanner.nextLine();

        int arancel = 0;

        switch (carrera.toLowerCase()) {
            case "programación":
                arancel = 500;
                break;
            case "abogacía":
                arancel = 700;
                break;
            case "ingeniería":
                arancel = 1000;
                break;
            default:
                System.out.println("Carrera no encontrada en el menú. Se asigna un arancel de 1000 por defecto.");
                arancel = 1000;
                break;
        }

        String insertAlumno = "INSERT INTO alumnos (nombre, legajo, carrera, arancel) VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(insertAlumno);
        preparedStatement.setString(1, nombre);
        preparedStatement.setString(2, legajo);
        preparedStatement.setString(3, carrera);
        preparedStatement.setInt(4, arancel);
        preparedStatement.executeUpdate();

        System.out.println("Alumno agregado a la base de datos");
        preparedStatement.close();
    }

    public static void eliminarAlumno(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Ingrese el número de legajo del alumno que desea eliminar: ");
        String legajo = scanner.nextLine();

        String deleteAlumno = "DELETE FROM alumnos WHERE legajo = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(deleteAlumno);
        preparedStatement.setString(1, legajo);
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Alumno eliminado exitosamente");
        } else {
            System.out.println("No se encontró ningún alumno con ese número de legajo");
        }

        preparedStatement.close();
    }

    public static void mostrarAlumnos(Connection connection) throws SQLException {
        String consulta = "SELECT * FROM alumnos";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(consulta);

        System.out.println("Listado de Alumnos:");
        while (resultSet.next()) {
            String nombre = resultSet.getString("nombre");
            String legajo = resultSet.getString("legajo");
            String carrera = resultSet.getString("carrera");
            int arancel = resultSet.getInt("arancel");

            System.out.println("Nombre: " + nombre + ", Legajo: " + legajo + ", Carrera: " + carrera + ", Arancel: " + arancel);
        }
    }
}
