import java.util.*;

import com.JDBC.prog.JDBC_API;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class IdAlreadyExistException extends Exception {
    IdAlreadyExistException(String message) {
        super(message);
    }
}

class WrongChoiceException extends Exception {
    WrongChoiceException(String message) {
        super(message);
    }
}

public class AppForStudentDB {
    Logger log_obj = LogManager.getLogger();
    Scanner sc = new Scanner(System.in);
    JDBC_API mc = new JDBC_API();

    void displayOptions() {
        int choice;

        do {
            log_obj.info("1. Insert Data");
            log_obj.info("2. Display Data");
            log_obj.info("3. Update Data");
            log_obj.info("4. Delete Data");
            log_obj.info("5. Exit");
            System.out.println("--------------");
            Scanner sc = new Scanner(System.in);
            log_obj.info("Enter Choice in the form of an integer ");
            choice = 5;
            try {
                choice = sc.nextInt();

                if (choice > 5 | choice < 1) {
                    throw new WrongChoiceException("Enter an integer only from displayed choices. Try Again ");
                }
            } catch (WrongChoiceException e) {
                log_obj.error(e.getMessage());
                displayOptions();
                return;
            } catch (InputMismatchException e) {
                sc.next();
                log_obj.error(e);
                log_obj.info("Enter an integer only. Try Again ");
                displayOptions();
                return;
            }

            switch (choice) {
                case 1:
                    insertData();
                    break;
                case 2:
                    displayData();
                    break;
                case 3:
                    updateData();
                    break;
                case 4:
                    deleteData();
                    break;
            }
        } while (choice < 5 & choice > 0);
        sc.close();
        log_obj.info("Exited Choices");
    }

    void insertData() {
        log_obj.info("Enter ID");
        int id = 0;
        try {
            id = sc.nextInt();
        } catch (InputMismatchException e) {
            sc.next();
            log_obj.error(e);
            log_obj.info("Enter an integer only. Try Again ");
            insertData();
            return;
        }
        try {
            if (mc.checkForExceptions("select ID from Student", id))
                throw new IdAlreadyExistException("ID already exist. Try Again ");
        } catch (IdAlreadyExistException e) {
            log_obj.error(e.getMessage());
            insertData();
            return;
        }

        log_obj.info("Enter First Name ");
        String fName = sc.next();
        log_obj.info("Enter Last Name ");
        String lName = sc.next();
        log_obj.info("Enter Subject Stream ");
        String subStream = sc.next();
        log_obj.info("Enter City ");
        String city = sc.next();
        log_obj.info("Enter marks ");
        int marks = sc.nextInt();
        String sendQuery = "insert into Student values(" + id + ", '" + fName + "', '" + lName + "', '" + subStream
                + "', '" + city + "', " + marks + ")";
        mc.queryExe(sendQuery);
    }

    void displayData() {
        String sendQuery = "select * from Student";
        mc.queryExe(sendQuery);
    }

    void updateData() {
        log_obj.info("Enter ID to be updated ");
        int update_id = 0;
        try {
            update_id = sc.nextInt();
        } catch (InputMismatchException e) {
            sc.next();
            log_obj.error(e);
            log_obj.info("Enter an integer only. Try Again ");
            updateData();
            return;
        }
        try {
            if (!mc.checkForExceptions("select ID from Student", update_id))
                throw new IdAlreadyExistException("ID does not exist. Try Again ");
        } catch (IdAlreadyExistException e) {
            log_obj.error(e.getMessage());
            updateData();
            return;
        }
        log_obj.info("Enter updated City ");
        String city = sc.next();
        String sendQuery = "update Student set city ='" + city + "' where id =" + update_id;
        mc.queryExe(sendQuery);
    }

    void deleteData() {
        log_obj.info("Enter ID to be deleted ");
        int delete_id = 0;
        try {
            delete_id = sc.nextInt();
        } catch (InputMismatchException e) {
            sc.next();
            log_obj.error(e);
            log_obj.info("Enter an integer only. Try Again ");
            deleteData();
            return;
        }
        try {
            if (!mc.checkForExceptions("select ID from Student", delete_id))
                throw new IdAlreadyExistException("ID you want to delete does not exist. Try Again ");
        } catch (IdAlreadyExistException e) {
            log_obj.error(e.getMessage());
            deleteData();
            return;
        }
        String sendQuery = "delete from Student where id = " + delete_id;
        mc.queryExe(sendQuery);
    }

    public static void main(String[] args) {
        AppForStudentDB doIt = new AppForStudentDB();
        doIt.displayOptions();
    }
}
