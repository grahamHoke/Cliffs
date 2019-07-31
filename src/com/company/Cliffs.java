package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Cliffs {

    public static void main(String[] args) {
        HashMap<String , JSONArray>  jsonFileObjects;
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("Welcome to CLI Fast Find Stuff (CLIFFS)");

        jsonFileObjects = parseFiles(args);

        JSONArray tickets = jsonFileObjects.get("tickets.json");
        Set<String> ticketFields = getFields(tickets);

        JSONArray users = jsonFileObjects.get("users.json");
        Set<String> userFields = getFields(users);

        JSONArray organizations = jsonFileObjects.get("organizations.json");
        Set<String> organizationFields = getFields(organizations);

        System.out.println("What records would you like to search? Enter a number 1-4 or any other value to quit.");
        System.out.println("1. Tickets");
        System.out.println("2. Users");
        System.out.println("3. Organizations");
        System.out.println("4. All");

        Set<String> curFields = new HashSet<>();
        JSONArray objectsToSearch= new JSONArray();

        input = scanner.nextLine();

        switch (input){
            case "1" :
                curFields = ticketFields;
                objectsToSearch = tickets;
                break;
            case "2" :
                curFields = userFields;
                objectsToSearch = users;
                break;
            case "3" :
                curFields = organizationFields;
                objectsToSearch = organizations;
                break;
            case "4" :
                curFields = new HashSet<>();
                curFields.addAll(ticketFields);
                curFields.addAll(userFields);
                curFields.addAll(organizationFields);

                objectsToSearch = new JSONArray();
                objectsToSearch.addAll(tickets);
                objectsToSearch.addAll(users);
                objectsToSearch.addAll(organizations);
                break;
            default:
                System.exit(0);
        }

        boolean validInput = false;
        while (!validInput) {
            System.out.println("Enter a field name from below or type \"all\" for universal search. You may also enter \"quit\" to exit");
            int fieldCount = 0;
            for (String field : curFields) {
                System.out.print(field);
                for ( int stringLength = field.length(); stringLength <= 16; stringLength ++){
                    System.out.print(" "); //pad length to 16 w/ spaces

                }
                System.out.print("\t");
                fieldCount++;
                if (fieldCount % 3 == 0) {
                    System.out.print("\n");
                }
            }
            System.out.print("\n");

            input = scanner.nextLine();
            if (input.equalsIgnoreCase("quit")){
                System.exit(0);
            }

            if (input.equals("all") || curFields.contains(input)){
                validInput = true;
            } else {
                System.out.println("Sorry, that field was not recognized.");
            }
        }
        String fieldToSearchInput = input;

        System.out.println("Please enter the term to search for:");
        input = scanner.nextLine();

        String searchTerm = input;

        Set<JSONObject> searchResults = new HashSet<>();

        if (!fieldToSearchInput.equals("all")){
            curFields = new HashSet<>();
            curFields.add(fieldToSearchInput);
        }

        for (String field:curFields){
            for (Object object: objectsToSearch){
                if (isMatch(object, searchTerm, field)){
                    searchResults.add((JSONObject) object);
                }
            }
        }

        printResults(searchResults);

    }

    private static void printResults(Set<JSONObject> searchResults) {
        String format = "%-40s%s%n";
        System.out.println("Found " + searchResults.size() + " results...");

        int resultCount = 0;
        for (JSONObject result : searchResults){
            resultCount++;
            System.out.println( "         ******** Result # " + resultCount + " ********");
            for (Object field: result.keySet()){
                if (result.get(field) != null) {
                    System.out.printf(format, field.toString(), result.get(field).toString());
                }
            }
        }
    }

    protected static boolean isMatch(Object object, String searchTerm, String key){
        JSONObject jsonObject = (JSONObject) object;
        Object fieldValue = jsonObject.get(key);
        if (fieldValue instanceof JSONArray){
            JSONArray arrayField = (JSONArray) fieldValue;
            return arrayField.contains(searchTerm);
        }else if (jsonObject.get(key) != null){
            return (jsonObject.get(key).toString().equals(searchTerm));
        } else {
            return (searchTerm.equals(""));
        }
    }

    protected static HashMap<String , JSONArray> parseFiles(String[] fileNames){
        BufferedReader br = null;
        JSONParser jsonParser = new JSONParser();
        HashMap<String , JSONArray> jsonFileObjects = new HashMap<>();

        if (fileNames.length == 0){
            System.out.println("Usage: enter filenames to read in as arguments");
            System.exit(0);
        }

        for (String filename : fileNames) {
            try {
                br = new BufferedReader(new FileReader(filename));

                String recordType = null;

                int lastPath = filename.lastIndexOf(File.separator);
                if (lastPath != -1){
                    recordType = filename.substring(lastPath + 1);
                }

                jsonFileObjects.put(recordType , (JSONArray) jsonParser.parse(br) );

                } catch (IOException | ParseException ioe){
                System.out.println("Error reading/parsing input file " + filename );
            }
            try {
                if (br != null){
                    br.close();
                }
            } catch (IOException ex) {
                System.out.println("Error reading/parsing closing file " + filename );
                ex.printStackTrace();
            }

        }

        return jsonFileObjects;
    }

    protected static Set<String> getFields(JSONArray array){
        Set<String> fields = new HashSet<>();
        for (Object object : array){
            JSONObject jsonObject = (JSONObject) object;
            for(Object key: jsonObject.keySet()){
                fields.add(key.toString());
            }
        }

        return fields;
    }
}
