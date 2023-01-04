package tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class used for converting a application.properties file to application.properties file
 *
 * @author Shubhasish
 */
public class PropertiesFileConvertor {

    private final Map<String, Map<String, Prop>> ALL_PRESENT_KEYS = new HashMap<>();

    private static final PropertiesFileConvertor INSTANCE = new PropertiesFileConvertor();

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesFileConvertor.class);

    private static final String PROPERTIES_FILE_PATH = "src/main/resources/application.properties";
    private static final String YML_FILE_PATH = "src/main/resources/application.yml";

    public static void main(String[] args) {

        // Read all the lines from application.properties file
        Set<String> propContents = INSTANCE.getPropertiesFileContent(Paths.get(PROPERTIES_FILE_PATH));
        // Convert for .yml file
        List<Prop> convertedPropObjects = INSTANCE.convertToYmlFormat(propContents);



        // Create application.properties file and write to it
        System.out.println("hello");
        //INSTANCE.writeYmlFile(convertedPropObjects);

    }

    private Set<String> getPropertiesFileContent(Path path) {

        Set<String> allProperties = new HashSet<>();

        try (Stream<String> lines = Files.lines(path)) {
            allProperties = lines.collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Error getting the file content :: " + e);
        }

        return allProperties;
    }

    private List<Prop> convertToYmlFormat(Set<String> propContent) {
        Set<Prop> propList = new HashSet<>();
        for (String line : propContent) {
            propList.add(preparePropObject(line, 0, line));
        }
        return new ArrayList<>(propList);
    }

    private Prop preparePropObject(String inputData, int hierarchy, String currentContextPath) {
        //TODO Implement the logic for preparing the yml object structure
        var keyValue = inputData.split("=");
        if(keyValue.length != 2)
            throw new RuntimeException("Malformed property line");
        var data = keyValue[0];
        var value = keyValue[1];
        Prop prop;
        if (data.contains(".")) {
            var nodeName = data.substring(0, data.indexOf("."));
            var children = inputData.substring(data.indexOf(".")+1);
            if(ALL_PRESENT_KEYS.containsKey(currentContextPath) && ALL_PRESENT_KEYS.get(currentContextPath).containsKey(nodeName)) {
                prop = ALL_PRESENT_KEYS.get(currentContextPath).get(nodeName);
            } else {
                prop = new Prop();
                Map<String, Prop> innerMap = ALL_PRESENT_KEYS.get(currentContextPath) == null ? new HashMap<>() : ALL_PRESENT_KEYS.get(currentContextPath);
                innerMap.put(nodeName, prop);
                ALL_PRESENT_KEYS.put(currentContextPath, innerMap);
            }
            prop.setHierarchy(hierarchy);
            prop.setName(nodeName);
            prop.setChild(preparePropObject(children, hierarchy+1, currentContextPath));
        } else {
            prop = new Prop();
            prop.setName(data);
            prop.setValue(value);
            prop.setHierarchy(hierarchy);
        }
        return prop;
    }

    private boolean isOnSameHierarchy(Prop existingProp, Prop currentProp) {

        if(currentProp == null)
            return true;
        while(existingProp.getParent() != null && currentProp.getParent() != null) {
            return isOnSameHierarchy(existingProp.getParent(), currentProp.getParent());
        }
        return existingProp.getName().equals(currentProp.getName());
    }


    /*private void writeYmlFile(List<Prop> ymlContent) {

        try {




        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error writing in file");
        }
    }*/

    // Hold the properties data
    private static final class Prop {

        private int hierarchy;
        private String name;
        private String value;
        private Prop parent;
        private final Set<Prop> children = new HashSet<>();

        /*public Prop getChild() {
            return child;
        }*/

        public void setChild(Prop children) {
            this.children.add(children);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setHierarchy(int hierarchy) {
            this.hierarchy = hierarchy;
        }

        public Prop getParent() {
            return parent;
        }

        public void setParent(Prop parent) {
            this.parent = parent;
        }

        @Override
        public String toString() {
            return super.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Prop prop)) return false;
            return getName().equals(prop.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName());
        }
    }
}
