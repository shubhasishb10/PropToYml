package proptoyml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Shubhasish
 */
public class FileOperationDetails {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileOperationDetails.class);

    private final String qualifiedInputFilePath;
    private final String qualifiedOutputFileName;

    // No instance allowed
    protected FileOperationDetails(String qualifiedInputFilePath, String qualifiedOutputFileName){
        this.qualifiedInputFilePath = qualifiedInputFilePath;
        this.qualifiedOutputFileName = qualifiedOutputFileName;
    }

    protected Set<String> getPropertiesFileContent() {

        Set<String> allProperties = new HashSet<>();

        try (Stream<String> lines = Files.lines(Paths.get(qualifiedInputFilePath))) {
            allProperties = lines.collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Error getting the file content :: " + e);
        }

        return allProperties;
    }

    protected void writeYmlFile(List<Prop> ymlContent) {

        try {

            Files.writeString(Paths.get(qualifiedOutputFileName), ymlContent.stream().map(Prop::toString).reduce("", String::concat), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error writing in file");
        }
    }
}
