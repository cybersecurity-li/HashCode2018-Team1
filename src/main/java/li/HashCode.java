package li;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class HashCode {

    private int rows;
    private int columns;
    private int vehicles;
    private int ridesCount;
    private int bonus;
    private int steps;

    private Ride[] rides;
    private Car[] cars;

    private static final Map<String, String> fileSet;
    static {
        Map<String, String> files = ImmutableMap.of(
                "problem/a_example.in", "submissions/a_example.out",
                "problem/b_should_be_easy.in", "submissions/b_should_be_easy.out",
                "problem/c_no_hurry.in", "submissions/c_no_hurry.out",
                "problem/d_metropolis.in", "submissions/d_metropolis.out",
                "problem/e_high_bonus.in", "submissions/e_high_bonus.out"
                );
        fileSet = Collections.unmodifiableMap(files);
    }

    public static void main(String[] args) throws IOException {
        List<String> resultList;

        for(Map.Entry<String, String> files: fileSet.entrySet()) {
            HashCode hashCode = new HashCode();
            hashCode.readData(files.getKey());
            resultList = hashCode.simulation();
            hashCode.writeSubmission(resultList, files.getValue());
        }
    }

    private void readData(String inFile) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(inFile), StandardCharsets.ISO_8859_1);

        // Determine how many columns there are.
        List<String> parameterLine = Splitter.on(CharMatcher.breakingWhitespace()).splitToList(lines.remove(0));
        rows =  Integer.parseInt(parameterLine.get(0));
        columns =  Integer.parseInt(parameterLine.get(1));
        vehicles =  Integer.parseInt(parameterLine.get(2));
        ridesCount =  Integer.parseInt(parameterLine.get(3));
        bonus =  Integer.parseInt(parameterLine.get(4));
        steps =  Integer.parseInt(parameterLine.get(5));

        rides = new Ride[ridesCount];
        cars = new Car[vehicles];

        int rowKey = 0;
        for (String line: lines) {

            List<String> lineItems = Splitter.on(CharMatcher.breakingWhitespace()).splitToList(line);

            Ride ride = new Ride();
            ride.xStart = Integer.parseInt(lineItems.get(0));
            ride.yStart = Integer.parseInt(lineItems.get(1));
            ride.xEnd = Integer.parseInt(lineItems.get(2));
            ride.yEnd = Integer.parseInt(lineItems.get(3));
            ride.earliestStart = Integer.parseInt(lineItems.get(4));
            ride.latestFinish = Integer.parseInt(lineItems.get(5));

            rides[rowKey] = ride;
            rowKey++;
        }
    }

    private void writeSubmission(List<String> resultList, String outFile) throws IOException {
        Files.write(Paths.get(outFile), resultList);
    }

    private List<String> simulation() {
        List<String> result = new ArrayList<>();



        return result;
    }
}
