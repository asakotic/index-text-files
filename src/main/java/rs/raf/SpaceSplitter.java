package rs.raf;

import java.util.Arrays;
import java.util.List;

public class SpaceSplitter implements Splitter {
    @Override
    public List<String> split(String line){
        return Arrays.stream(line.toLowerCase().replaceAll("[^A-Za-z0-9]"," ").split("[ \n\t]")).toList();
    }
}