package dataaccesslayer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIOManager {


    public static List<String> getLines(String filePath) {
        List<String> temp = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                temp.add(line);
                line = reader.readLine();
            }
            reader.close();
        }

        catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return temp;
    }


    public static void setLines(String filePath, List<String> lines) {
        try {
            FileWriter myWriter = new FileWriter(filePath);
            for(int i=0; i<lines.size(); i++){
                myWriter.write(lines.get(i));
                myWriter.write(System.getProperty( "line.separator" ));
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }


}
