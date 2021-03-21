package DataAccessLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataHandler {

    private List<ArrayList<String>> data;
    private String filePath;

    public DataHandler(String filePath) {
        this.data = new ArrayList<ArrayList<String>>();
        this.filePath = filePath;
        setData();
    }

    public List<ArrayList<String>> getData() {
        return data;
    }

    private void setData() {
        FileLineReader fileLineReader = new FileLineReader();
        List<String> allLines = fileLineReader.getLines(this.filePath);
        List<ArrayList<String>> temp = getFormattedLines(allLines);
        temp.remove(0);
        this.data = temp;
    }

    private List<ArrayList<String>> getFormattedLines(List<String> allLines) {

        List<ArrayList<String>> formattedLines = new ArrayList<ArrayList<String>>();

        for(int i=0; i<allLines.size(); i++){
            ArrayList<String> formattedLine = getSplitLine(allLines.get(i));
            formattedLines.add(formattedLine);
        }
        return formattedLines;
    }

    private ArrayList<String> getSplitLine(String line) {
        ArrayList<String> splitLine = new ArrayList<String>();

        String[] tokens = line.split(",", -1);
        Arrays.stream(tokens).map(String::trim).toArray(temp -> tokens);
        Collections.addAll(splitLine, tokens);

        return splitLine;
    }


}
