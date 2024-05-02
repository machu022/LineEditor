import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LineEditor {

  private static final String EXIT_COMMAND = "quit";
  private static final String SAVE_COMMAND = "save";
  private static final String LIST_COMMAND = "list";
  private static final String DELETE_COMMAND = "del";
  private static final String INSERT_COMMAND = "ins";
  private static Scanner reader;
  private static ArrayList<String> lines;
  private static String filename;

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: java LineEditor <filename>");
      return;
    }

    filename = args[0];
    reader = new Scanner(System.in);
    lines = new ArrayList<>();

    // Open file
    openFile();

    // Editing loop
    while (true) {
      displayLines();
      String command = getCommand();

      switch (command.toLowerCase()) {
        case EXIT_COMMAND:
          System.out.println("Exiting...");
          saveFile();
          return;
        case SAVE_COMMAND:
          saveFile();
          break;
        case LIST_COMMAND:
          listLines();
          break;
        default:
          if (command.startsWith(DELETE_COMMAND)) {
            deleteLine(command);
          } else if (command.startsWith(INSERT_COMMAND)) {
            insertLine(command);
          } else {
            System.out.println("Invalid command. Try 'list', 'del n', 'ins n', 'save', or 'quit'.");
          }
      }
    }
  }

  private static void openFile() {
    try (Scanner fileReader = new Scanner(new File(filename))) {
      while (fileReader.hasNextLine()) {
        lines.add(fileReader.nextLine());
      }
    } catch (FileNotFoundException e) {
      System.out.println("Error: File not found!");
    }
  }

  private static void displayLines() {
    System.out.println(">>");
  }

  private static String getCommand() {
    return reader.nextLine();
  }

  private static void listLines() {
    for (int i = 0; i < lines.size(); i++) {
      System.out.println((i + 1) + ": " + lines.get(i));
    }
  }

  private static void deleteLine(String command) {
    try {
      int lineNum = Integer.parseInt(command.substring(DELETE_COMMAND.length()).trim()) - 1;
      if (lineNum >= 0 && lineNum < lines.size()) {
        lines.remove(lineNum);
      } else {
        System.out.println("Error: Line number out of range!");
      }
    } catch (NumberFormatException e) {
      System.out.println("Error: Invalid line number!");
    }
  }

  private static void insertLine(String command) {
    try {
      int lineNum = Integer.parseInt(command.substring(INSERT_COMMAND.length()).trim()) - 1;
      if (lineNum < 0) {
        lineNum = lines.size(); // Insert at the end if negative
      }
      System.out.print("Insert line: ");
      String newLine = reader.nextLine();
      lines.add(lineNum, newLine + "\n"); // Add newline character
    } catch (NumberFormatException e) {
      System.out.println("Error: Invalid line number!");
    }
  }

  private static void saveFile() {
    try (FileWriter writer = new FileWriter(filename)) {
      for (String line : lines) {
        writer.write(line);
      }
      System.out.println("File '" + filename + "' saved!");
    } catch (IOException e) {
      System.out.println("Error: Could not save file!");
    }
  }
}
