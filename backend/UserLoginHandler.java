package backend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import android.text.InputType
import android.widget.CheckBox
import android.widget.EditText

public class UserLoginHandler {
    private static final String USER_DATA_FILE = "backend/users.txt";

    public static boolean login(String username, String password) {
        try {
            if (!Files.exists(Paths.get(USER_DATA_FILE))) return false;
            String hashedPassword = hashPassword(password);
            return Files.lines(Paths.get(USER_DATA_FILE))
                    .anyMatch(line -> {
                        String[] parts = line.split(",");
                        return parts[0].equals(username) && parts[1].equals(hashedPassword);
                    });
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
}

