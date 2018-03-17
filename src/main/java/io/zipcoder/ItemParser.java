package io.zipcoder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {

    public int getErrorCount() {
        return errorCount;
    }

    private int errorCount = 0;
    private HashMap<String, HashMap<Double, Integer>> outputList = new HashMap<String, HashMap<Double, Integer>>();

    public ArrayList<String> parseRawDataIntoStringArray(String rawData){
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawData);
        return response;
    }
    public Item parseStringIntoItem(String rawItem) throws ItemParseException{
//        ArrayList<String> singleRawItem = parseRawDataIntoStringArray(rawItem);
        ArrayList<String> valuePair;
        ArrayList<String> valueOfKeys = new ArrayList<String>();
        if(findName(rawItem) && findPrice(rawItem) && findType(rawItem) && findExpiration(rawItem)) {

                ArrayList<String> keyValuePair = findKeyValuePairsInRawItemData(rawItem);
                String valueOfKey;
                for (int i = 0; i < keyValuePair.size(); i++) {
                        valuePair = splitStringWithRegexPattern(":", keyValuePair.get(i));
                        valueOfKey = valuePair.get(1).toLowerCase();

                        valueOfKeys.add(valueOfKey);

                    }
                Item item = new Item(valueOfKeys.get(0),
                        Double.parseDouble(valueOfKeys.get(1)),
                        valueOfKeys.get(2),
                        valueOfKeys.get(3));
                buildList(item);
                return item;
            }
        throwException();
        return null;
    }
    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem){
        String stringPattern = "[;|^|%|@|*|!]";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawItem);
        return response;
    }
    private ArrayList<String> splitStringWithRegexPattern(String stringPattern, String inputString){
        return new ArrayList<String>(Arrays.asList(inputString.split(stringPattern)));
    }
    private boolean findName(String rawItem){
        String regexName = "(N|n)(A|a)(M|m)(E|e):\\w+";
        Pattern name = Pattern.compile(regexName);
        Matcher match = name.matcher(rawItem);
        return match.find();
    }
    private boolean findPrice(String rawItem){
        String regexPrice = "(P|p)(R|r)(I|i)(C|c)(E|e):\\d+";
        Pattern price = Pattern.compile(regexPrice);
        Matcher match = price.matcher(rawItem);
        return match.find();
    }
    private boolean findType(String rawItem){
        String regexType = "(T|t)(Y|y)(P|p)(E|e):\\w+";
        Pattern type = Pattern.compile(regexType);
        Matcher match = type.matcher(rawItem);
        return match.find();
    }
    private boolean findExpiration(String rawItem){
        String regexExp = "(E|e)........(N|n):\\d.......\\d";
        Pattern expire = Pattern.compile(regexExp);
        Matcher match = expire.matcher(rawItem);
        return match.find();
    }
    private void throwException() throws ItemParseException {
        //thanks eric b.
        errorCount++;
        throw new ItemParseException();
    }

    public void buildList(Item item){
        if (item.getName().equalsIgnoreCase("")){
            return;
        }
        createMap(item);
    }

    private void createMap(Item item) {
        String name = item.getName();
        Double price = item.getPrice();

        if (!outputList.containsKey(name)){
            outputList.put(name, new HashMap<Double, Integer>());
            outputList.get(name).put(price, 1);
        } else if (!outputList.get(name).containsKey(price)){
            outputList.get(name).put(price, 1);
            }
            else {
            Integer seen = outputList.get(name).get(price);
            outputList.get(name).put(price, seen + 1);
            }
    }

    private int seen(HashMap<Double, Integer> outputList){
        int seen = 0;
        for (Integer num : outputList.values()){
            seen += num;
        }
        return seen;
    }
    public void buildListToString(){
//        System.out.println(outputList.toString());
        for (String name: outputList.keySet()){
            System.out.printf("\nName: %7s\t \t seen: %d times\n", name, seen(outputList.get(name)));
            System.out.println("============= \t \t =============");
            for (Map.Entry<Double, Integer> entry : outputList.get(name).entrySet()){
                System.out.printf("Price: %6s\t \t seen: %d times\n", entry.getKey(), entry.getValue());
                System.out.println("-------------\t\t -------------");
            }

        }
        System.out.printf("Errors: \t \t \t seen: %d times", getErrorCount());
    }
    public static void main(String[] args) throws ItemParseException {
        ItemParser test = new ItemParser();
//        ArrayList test0 = test.parseRawDataIntoStringArray("naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##naME:BreaD;price:1.23;type:Food;expiration:1/02/2016##NAMe:BrEAD;price:1.23;type:Food;expiration:2/25/2016##naMe:MiLK;price:3.23;type:Food^expiration:1/11/2016##naMe:Cookies;price:2.25;type:Food%expiration:1/25/2016##naMe:CoOkieS;price:2.25;type:Food*expiration:1/25/2016##naMe:COokIes;price:2.25;type:Food;expiration:3/22/2016##naMe:COOkieS;price:2.25;type:Food;expiration:1/25/2016##NAME:MilK;price:3.23;type:Food;expiration:1/17/2016##naMe:MilK;price:1.23;type:Food!expiration:4/25/2016##naMe:apPles;price:0.25;type:Food;expiration:1/23/2016##naMe:apPles;price:0.23;type:Food;expiration:5/02/2016##NAMe:BrEAD;price:1.23;type:Food;expiration:1/25/2016##naMe:;price:3.23;type:Food;expiration:1/04/2016##naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##naME:BreaD;price:1.23;type:Food@expiration:1/02/2016##NAMe:BrEAD;price:1.23;type:Food@expiration:2/25/2016##naMe:MiLK;priCe:;type:Food;expiration:1/11/2016##naMe:Cookies;price:2.25;type:Food;expiration:1/25/2016##naMe:Co0kieS;pRice:2.25;type:Food;expiration:1/25/2016##naMe:COokIes;price:2.25;type:Food;expiration:3/22/2016##naMe:COOkieS;Price:2.25;type:Food;expiration:1/25/2016##NAME:MilK;price:3.23;type:Food;expiration:1/17/2016##naMe:MilK;priCe:;type:Food;expiration:4/25/2016##naMe:apPles;prIce:0.25;type:Food;expiration:1/23/2016##naMe:apPles;pRice:0.23;type:Food;expiration:5/02/2016##NAMe:BrEAD;price:1.23;type:Food;expiration:1/25/2016##naMe:;price:3.23;type:Food^expiration:1/04/2016##");
//        ArrayList test1 = test.findKeyValuePairsInRawItemData("NAMe:;price:;type:@expiration:");
//        ArrayList test2 = test.findKeyValuePairsInRawItemData("naME:BreaD;price:1.23;type:Food@expiration:1/02/2016");
        Item itemTest = test.parseStringIntoItem("naMe:COokIes;price:2.25;type:Food;expiration:3/22/2016");
//        Item itemTest2 = test.parseStringIntoItem("naMe:;price:3.23;type:Food;expiration:1/04/2016");
        Item itemTest3 = test.parseStringIntoItem("naMe:Co0kieS;pRice:2.25;type:Food;expiration:1/25/2016");
        Item itemTest4 = test.parseStringIntoItem("naMe:Milk;price:3.23;type:Food;expiration:1/25/2016");
        Item itemTest5 = test.parseStringIntoItem("naMe:MilK;price:1.23;type:Food!expiration:4/25/2016");
//        System.out.println(test1);
//        System.out.println(test2);
//        System.out.println(itemTest);
//        System.out.println(itemTest2);
        System.out.println(itemTest3);
        test.buildListToString();

    }

}
