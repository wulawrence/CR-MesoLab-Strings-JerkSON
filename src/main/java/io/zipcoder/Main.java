package io.zipcoder;

import org.apache.commons.io.IOUtils;

import java.util.ArrayList;


public class Main {

    private static int errors = 0;
    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) throws Exception{
        String output = (new Main()).readRawDataToString();
        ItemParser itemParser = new ItemParser();
        ArrayList<String> itemsSeparated = itemParser.parseRawDataIntoStringArray(output);
        ArrayList<Item> groceryList = new ArrayList<Item>();
        for (String items: itemsSeparated){
            try {
                Item item = itemParser.parseStringIntoItem(items);

//                System.out.println(item);
                groceryList.add(item);
            } catch (ItemParseException iae){
                errors++;
            }
        }
//        System.out.println(itemParser.getErrorCount());
        itemParser.buildListToString();
    }
}
