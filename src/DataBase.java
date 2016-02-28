import java.util.ArrayList;

/**
 * Created by Александр on 28.02.2016.
 */
public interface DataBase {
    void createNewDB();

    void deleteDB();

    void createTable();

    void deleteTable();

    void insertInTable(int id, String name, String surname, int age);

    ArrayList<Object[]> search(String surname);

    void updateTable(int id, String name, String surname, int age);

    void deleteFromTable(String surname);
}
