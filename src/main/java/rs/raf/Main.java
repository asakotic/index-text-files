package rs.raf;

import java.io.*;
import java.util.*;

public class Main {
    /*
    Please write a Java application that provides a service for indexing text files.
    The console interface should allow for (i) specifying the indexed files and directories and (ii) querying files containing a given word.
    The library should be extensible by the tokenization algorithm (simple splitting by words/support lexers/etc.).
    State persistence between running sessions is not needed, but the application should be able to serve consecutive requests.
    Providing some tests and usage examples is advised. Usage of external libraries is discouraged.
     */

    private static Set<File> allFiles = new HashSet<>();
    private static Map<String, List<File>> allIndexedWords = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        while(true){
            String option = "";
            System.out.println("------------------------------------");
            System.out.println("Option 1 - Choose directories/files");
            System.out.println("Option 2 - Find word in files");
            System.out.println("Option 3 - Clear all files");
            System.out.println("Option 4 - Exit\n");
            option = scanner.nextLine();
            switch (option) {
                case "1" -> {
                    System.out.println("You can input as many directories/files, but separated by ;");
                    String input = scanner.nextLine();
                    List<String> paths = Arrays.stream(input.split(";")).toList();
                    for (String file : paths) {
                        if (!readFile(file)) {
                            System.out.println(file + " path does not exist!");
                        }
                    }
                    System.out.println("All files: " + allFiles);
                }
                case "2" ->{
                    System.out.println("Please enter the word to search for:");
                    String word = scanner.nextLine();
                    findWord(word);
                }
                case "3" -> {
                    allFiles.clear();
                    allIndexedWords.clear();
                }
                case "4"-> {
                    return;
                }
                default -> System.out.println("This option does not exist!");
            }
        }
    }
    private static boolean readFile(String path) throws IOException {

        File file = new File(path);

        if (file.exists()) {
            if (file.isDirectory())
                searchDirectory(file);
            else if (file.isFile() && file.getName().endsWith(".txt"))
                allFiles.add(file);
        } else {
            return false;
        }
        return true;
    }

    private static void searchDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory())
                    searchDirectory(file);
                else if (file.isFile() && file.getName().endsWith(".txt")) {
                    allFiles.add(file);
                }
            }
        }
    }

    private static void findWord(String word) throws IOException {
        List<File> foundFileForWord = new ArrayList<>();
        Splitter splitter = new SpaceSplitter();
        if(allIndexedWords.containsKey(word))
            System.out.println(allIndexedWords.get(word));
        else {
            for (File tmp : allFiles){
                String line;
                BufferedReader bufferedReader = new BufferedReader(new FileReader(tmp));
                while ((line = bufferedReader.readLine()) != null) {
                    List<String> words = splitter.split(line);
                    if (words.contains(word.toLowerCase())) {
                        foundFileForWord.add(tmp);
                        break;
                    }
                }
                bufferedReader.close();
            }
            if(foundFileForWord.isEmpty())
                System.out.println("Word does not exist in this given files");
            else {
                System.out.println(foundFileForWord);
            }
            allIndexedWords.put(word,foundFileForWord);
        }
    }
}