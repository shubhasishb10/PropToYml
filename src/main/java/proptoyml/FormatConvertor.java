package proptoyml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Shubhasish
 */
public class FormatConvertor {

    private final Map<String, Prop> ALL_PRESENT_KEYS = new HashMap<>();

    private final FileOperationDetails fileOperationDetails;

    protected FormatConvertor(FileOperationDetails fileOperationDetails) {
        this.fileOperationDetails = fileOperationDetails;
    }

    protected void convertPropToYml() {
        fileOperationDetails.writeYmlFile(convertToYmlFormat());
    }


    private List<Prop> convertToYmlFormat() {
        Set<Prop> propList = new HashSet<>();
        for (String line : fileOperationDetails.getPropertiesFileContent()) {
            propList.add(preparePropObject(line, 0, null));
        }
        return new ArrayList<>(propList);
    }

    private Prop preparePropObject(String inputData, int hierarchy, Prop parentNode) {
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
            if(ALL_PRESENT_KEYS.containsKey(nodeName) && isOnSameHierarchy(ALL_PRESENT_KEYS.get(nodeName), parentNode)) {
                prop = ALL_PRESENT_KEYS.get(nodeName);
            } else {
                prop = new Prop();
                ALL_PRESENT_KEYS.put(nodeName, prop);
            }
            prop.setParent(parentNode);
            prop.setHierarchy(hierarchy);
            prop.setName(nodeName);
            prop.setChild(preparePropObject(children, hierarchy+1, prop));
        } else {
            prop = new Prop();
            prop.setName(data);
            prop.setValue(value);
            prop.setHierarchy(hierarchy);
            prop.setParent(parentNode);
        }
        return prop;
    }

    private boolean isOnSameHierarchy(Prop currentProp, Prop parentProp) {

        if(parentProp == null)
            return true;
        if(currentProp.getParent() == null && parentProp.getParent() == null)
            return currentProp.getName().equals(parentProp.getName());
        if (parentProp.getParent() != null) {
            return isOnSameHierarchy(currentProp.getParent(), parentProp.getParent());
        }
        return parentProp.getName().equals(currentProp.getParent().getName());
    }
}
