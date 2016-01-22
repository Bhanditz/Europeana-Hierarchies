package eu.europeana.hierarchies.service.utils;

import eu.europeana.hierarchy.InputNode;
import eu.europeana.hierarchy.ListValue;
import eu.europeana.hierarchy.StringValue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Util class to convert a Map of parameters to a node
 * Created by ymamakis on 1/22/16.
 */
public class InputNodeCreator {

    /**
     * Convert a map of values to a Neo4j request node
     * @param map The map to convert
     * @return The resulting node
     */
    public static InputNode createInputNodeFromMap(Map<String,Object> map){
        InputNode inputNode = new InputNode();
        Set<StringValue> stringValues = new HashSet<>();
        Set<ListValue> listValues = new HashSet<>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if (entry.getValue().getClass().isAssignableFrom(String.class)){
                StringValue val = new StringValue();
                val.setKey(entry.getKey());
                val.setValue((String)entry.getValue());
                stringValues.add(val);
            } else if(entry.getValue().getClass().isAssignableFrom(List.class)){
                ListValue val = new ListValue();
                val.setKey(entry.getKey());
                val.setValue((List<String>)entry.getValue());
                listValues.add(val);
            }
        }
        inputNode.setListValues(listValues);
        inputNode.setStringValues(stringValues);
        return (inputNode.getStringValues().size()>0&&inputNode.getListValues().size()>0)?null:inputNode;
    }

}
