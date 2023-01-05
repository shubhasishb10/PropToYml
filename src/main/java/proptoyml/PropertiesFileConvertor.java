package proptoyml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

/**
 * This class used for converting a application.properties file to application.properties file
 *
 * @author Shubhasish
 */
public class PropertiesFileConvertor {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesFileConvertor.class);

    private static String inputFile = "src/main/resources/application.properties";
    private static String outputFile = "src/main/resources/application.yml";

    public static void main(String[] args) {

        if(args.length > 0)
            inputFile = args[0];
        if(args.length > 1)
            outputFile = args[1];

        logger.info("Input file path: " + Paths.get(inputFile).toAbsolutePath());
        logger.info("Output file path: " + Paths.get(outputFile).toAbsolutePath());

        FileOperationDetails fileOperationDetails = new FileOperationDetails(inputFile, outputFile);
        new FormatConvertor(fileOperationDetails).convertPropToYml();
    }
}
