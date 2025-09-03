package backend;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class UserSignupHandler {
    private static final String USER_DATA_FILE = "backend/users.txt";

    public static boolean signup(String username, String password) {
        try {
            String hashedPassword = hashPassword(password);
            String userRecord = username + "," + hashedPassword + "\n";
            FileWriter fw = new FileWriter(USER_DATA_FILE, true);
            fw.write(userRecord);
            fw.close();
            return true;
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    // For demonstration: check if username already exists
    public static boolean userExists(String username) {
        try {
            if (!Files.exists(Paths.get(USER_DATA_FILE))) return false;
            return Files.lines(Paths.get(USER_DATA_FILE))
                    .anyMatch(line -> line.split(",")[0].equals(username));
        } catch (IOException e) {
            return false;
        }
    }
}

