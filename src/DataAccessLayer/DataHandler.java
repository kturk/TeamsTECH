package DataAccessLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataHandler {

    private List<List<String>> data;
    private String filePath;

    public DataHandler(String filePath) {
        this.data = new ArrayList<List<String>>();
        this.filePath = filePath;
        setData();
    }

    public List<List<String>> getData() {
        return data;
    }

    private void setData() {
        FileLineReader fileLineReader = new FileLineReader();
        List<String> allLines = fileLineReader.getLines(this.filePath);
        List<List<String>> temp = getFormattedLines(allLines);
        this.data = temp;
    }

    private List<String> splitLine(String line) {
        List<String> splitedLine = new ArrayList<String>();

        String[] tokens = line.split(",");
        Collections.addAll(splitedLine, tokens);
        return splitedLine;
    }

    private List<List<String>> getFormattedLines(List<String> allLines) {

        List<List<String>> formattedLines = new ArrayList<List<String>>();

        for(int i=0; i<allLines.size(); i++){
            List<String> formattedLine = splitLine(allLines.get(i));
            formattedLines.add(formattedLine);
        }
        return formattedLines;
    }
}
