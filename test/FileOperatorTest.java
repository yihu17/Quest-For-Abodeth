import main.java.questfortheabodeth.FileOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileOperatorTest
{
    private FileOperator fileOps;

    @BeforeEach
    void setUp()
    {

        this.fileOps = new FileOperator(getClass().getResource("map.csv").getPath());
        if (!this.fileOps.isAvailable()) {
            System.out.println("The file is not available for reading");
        }
    }

    @Test
    void testAReadToList()
    {
        ArrayList<String> listOfStrings = fileOps.readToList();
        assertEquals(listOfStrings.size(), 4);
        assertEquals(listOfStrings.get(0), "0,1,2,3");
    }

    @Test
    void testBReadLine()
    {
        String[] line;
        line = fileOps.readLine(0);

        assertEquals(line.length, 4);
        assertEquals(line[0], "0");

        line = fileOps.readLine(7);
        assertNull(line);
    }

    @Test
    void testCReadElement()
    {
        assertEquals(fileOps.readElement(0, 0), "0");
        assertEquals(fileOps.readElement(3, 3), "1");
        assertNull(fileOps.readElement(8, 8));
        assertNull(fileOps.readElement(8, 4));
        assertNull(fileOps.readElement(4, 8));
    }

    @Test
    void testDInsert()
    {
        assertTrue(this.fileOps.insert("8", 0, 0));
        assertFalse(this.fileOps.insert("8", 8, 8));
        assertEquals(this.fileOps.readElement(0, 0), "8");
        assertTrue(this.fileOps.insert("0", 0, 0));
    }

    @Test
    void testEWriteNewLine()
    {
        assertTrue(fileOps.writeNewLine("5,4,3,2"));
    }

    @Test
    void testFRemoveLine()
    {
        assertTrue(fileOps.removeLine(4));
        assertFalse(fileOps.removeLine(9));
    }
}