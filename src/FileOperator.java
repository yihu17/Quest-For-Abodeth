import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class FileOperator
{
    private File file;
    private boolean available = false;
    private Exception exception = null;

    public FileOperator(String path)
    {
        try {
            file = new File(path);
            available = true;
        } catch (Exception e) {
//            System.out.println("ERROR- Could not read file: " + path);
            e.printStackTrace();
            exception = e;
        }
    }

    /**
     * Reads entire file
     *
     * @return (ArrayList < String >) Contents of file. Element per row
     */
    public ArrayList<String> readToList()
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getCanonicalFile()));
            String line;
            ArrayList<String> contents = new ArrayList<>();
            while ((line = reader.readLine()) != null) { //read current row into line
                contents.add(line); //add line into ArrayList contents
            }
            return contents;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads row of file
     *
     * @param row (int) Row
     * @return (String[]) Returns array of elements in row
     */
    public String[] readLine(int row)
    {
        try {
            ArrayList<String> contents = readToList();
            return contents.get(row).split(",");
        } catch (Exception e) {
//            System.err.println("Error- Could not read line. Check parameters");
            return null;
        }
    }

    /**
     * Reads single element in file
     *
     * @param row (int) Row
     * @param col (int) Column
     * @return (String) Returns element in row
     */
    public String readElement(int row, int col)
    {
        try {
            return readLine(row)[col];
        } catch (Exception e) {
//            System.err.println("Error- Could not read element. Check parameters");
            return null;
        }
    }

    /**
     * Swaps specified element, by row and column, to provided String
     *
     * @param add (String) String to swap element with
     * @param row (int) Row
     * @param col (int) Column
     * @return (boolean) Returns true if insert was successful, false if not
     */
    public boolean insert(String add, int row, int col)
    {
        try {
            ArrayList<String> contents = readToList(); //ArrayList of Strings for each line
            String[] lineSplit = readLine(row); //String array of elements in specified line
            lineSplit[col] = add; //updates element
            String newL = String.join(",", lineSplit); //packs elements into one string, separated by commas
            contents.set(row, newL); //replaces new line in ArrayList of rows
            clearFile();
            FileWriter appendFileWriter = new FileWriter(file, true); //Writes each row to file
            for (int i = 0; i < contents.size(); i++) {
                appendFileWriter.append(contents.get(i));
                if (i != contents.size() - 1) {
                    appendFileWriter.append("\n");
                } //Don't want empty row at EOF
            }
            appendFileWriter.flush();
            appendFileWriter.close();
            return true;
        } catch (Exception e) {
//            System.out.println("Insert Error");
            return false;
        }
    }

    /**
     * Adds new line to file
     *
     * @param add (String) Line to add
     * @return (boolean) Returns true if writing new line was successful, false if not
     */
    public boolean writeNewLine(String add)
    {
        try {
            FileWriter newLineWriter = new FileWriter(file, true);
            newLineWriter.append("\n" + add);
            newLineWriter.flush();
            newLineWriter.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error- Could not write new line");
            return false;
        }
    }

    /**
     * Removes line in file
     *
     * @param row (int) Row to remove
     * @return (boolean) Returns true if removing line was successful, false if not
     */
    public boolean removeLine(int row)
    {
        try {
            ArrayList<String> contents = readToList();
            contents.remove(row);
            clearFile();
            FileWriter appendFileWriter = new FileWriter(file, true); //Writes each row to file
            for (int i = 0; i < contents.size(); i++) {
                appendFileWriter.append(contents.get(i));
                if (i != contents.size() - 1) {
                    appendFileWriter.append("\n");
                } //Don't want empty row at EOF
            }
            appendFileWriter.flush();
            appendFileWriter.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error- Could not remove line");
            return false;
        }
    }

    /**
     * Clears contents of file
     *
     * @return (boolean) Returns true if clearing file was successful, false if not
     */
    public boolean clearFile()
    {
        try {
            FileWriter clearFileWriter = new FileWriter(file, false);
            clearFileWriter.append("");
            clearFileWriter.flush();
            clearFileWriter.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error- Could not clear file");
            return false;
        }
    }

    /**
     * Prints file
     */
    public void printFile()
    {
        ArrayList<String> contents = readToList();
        for (int i = 0; i < contents.size(); i++) {
            System.out.println(contents.get(i));
        }
    }

    public boolean isAvailable()
    {
        return available;
    }

    public Exception getException()
    {
        return exception;
    }
}