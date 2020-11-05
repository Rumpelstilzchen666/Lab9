package Example;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestExceptions {
    public static String fileName = "";

    public static void main(String[] args) throws BadFileNameException {
        try (Scanner file = new Scanner(new File(fileName))) {
            if(file.hasNextLine()) {
                System.out.println(file.nextLine());
            }
            else {
                throw new IllegalArgumentException("Non readable file");
            }
        } catch(FileNotFoundException err) {
            if(!isCorrectFileName(fileName)) {
                throw new BadFileNameException("Bad filename: " + fileName,
                        err);
            }
        } catch(IllegalArgumentException err) {
            if(!containsExtension(fileName)) {
                throw new BadFileExtensionException(
                        "Filename does not contain extension: " + fileName,
                        err);
            }
        }
    }

    private static boolean isCorrectFileName(@NotNull String fileName) {
        return fileName.matches("[\\S &&[^/*?<>|]]+");
    }

    public static boolean containsExtension(@NotNull String fileName) {
        String allowed = "[\\S&&[^/*?<>|]]";
        return fileName.matches(
                "^[" + allowed + "&&[^\\\\:]][" + allowed + " ]*[.][" +
                        allowed + "&&[^\\\\:]]*$");
    }

    public static class BadFileNameException extends Exception {
        public BadFileNameException(String errorMessage, Throwable err) {
            super(errorMessage, err);
        }
    }

    public static class BadFileExtensionException extends RuntimeException {
        public BadFileExtensionException(String errorMessage, Throwable err) {
            super(errorMessage, err);
        }
    }
}
