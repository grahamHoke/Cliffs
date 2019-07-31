package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class CliffsTest extends Cliffs {

    //isMatch
    @Test
    public void isMatchWhenSearchingNumeric(){
        JSONObject object = new JSONObject();
        object.put("key", 5);

        Assert.assertTrue(Cliffs.isMatch(object,"5","key"));
    }

    @Test
    public void noMatchWhenSearchingNumeric(){
        JSONObject object = new JSONObject();
        object.put("key", 5);

        Assert.assertFalse(Cliffs.isMatch(object,"6","key"));

    }

    @Test
    public void isMatchWhenSearchingString(){
        JSONObject object = new JSONObject();
        object.put("key", "value");

        Assert.assertTrue(Cliffs.isMatch(object,"value","key"));
}
    @Test
    public void noMatchWhenSearchingString(){
        JSONObject object = new JSONObject();
        object.put("key", "value");

        Assert.assertFalse(Cliffs.isMatch(object,"schmalue","key"));
    }

    @Test
    public void isMatchWhenSearchingArray(){
        JSONObject object = new JSONObject();
        JSONArray names = new JSONArray();
        names.add("Bob");
        names.add("Casey");

        object.put("key", names);

        Assert.assertTrue(Cliffs.isMatch(object,"Bob","key"));
    }

    @Test
    public void noMatchWhenSearchingArray(){
        JSONObject object = new JSONObject();
        JSONArray names = new JSONArray();
        names.add("Bob");
        names.add("Casey");

        object.put("key", names);

        Assert.assertFalse(Cliffs.isMatch(object,"Dan","key"));
    }

    @Test
    public void isMatchWhenSearchingNullField(){
        JSONObject object = new JSONObject();

        Assert.assertTrue(Cliffs.isMatch(object,"","key"));
    }

    @Test
    public void noMatchWhenSearchingNullField(){
        JSONObject object = new JSONObject();
        object.put("key", "something");

        Assert.assertFalse(Cliffs.isMatch(object,"","key"));
    }

    @Test
    public void isMatchWhenSearchingEmptyField(){
        JSONObject object = new JSONObject();
        object.put("key", "");

        Assert.assertTrue(Cliffs.isMatch(object,"","key"));
    }

    //parseFiles
    @Test
    public void parseFilesNoErrorOnNormalFiles(){
        String filename1 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "com" +
                File.separator + "company" + File.separator + "organizations.json";

        String filename2 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "com" +
                File.separator + "company" + File.separator + "users.json";
        String[] filenames = new String[2];
        filenames[0] = filename1;
        filenames[1] = filename2;

        Assert.assertTrue(Cliffs.parseFiles(filenames).keySet().size() == 2);

    }

    @Test
    public void parseFilesParsingError(){
        String filename1 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "com" +
                File.separator + "company" + File.separator + "organizations.json";

        String filename2 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "com" +
                File.separator + "company" + File.separator + "Cliffs.java";
        String[] filenames = new String[2];
        filenames[0] = filename1;
        filenames[1] = filename2;
        Assert.assertTrue(Cliffs.parseFiles(filenames).keySet().size() == 1);

    }


    @Test
    public void parseFilesFileNotFoundError(){
        String filename1 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "com" +
            File.separator + "company" + File.separator + "notARealFile.json";

        String filename2 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "com" +
                File.separator + "company" + File.separator + "Cliffs.java";
        String[] filenames = new String[2];
        filenames[0] = filename1;
        filenames[1] = filename2;

        Assert.assertTrue(Cliffs.parseFiles(filenames).keySet().size() == 0);
    }

    //getFields

    @Test
    public void getFieldsNormalCase(){
        JSONArray jsonArray= new JSONArray();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        object1.put("key1","value");
        object2.put("key1","value");
        object2.put("key2","value");
        jsonArray.add(object1);
        jsonArray.add(object2);

        Assert.assertTrue((Cliffs.getFields(jsonArray)).contains("key1"));
        Assert.assertTrue((Cliffs.getFields(jsonArray)).contains("key2"));
    }


    @Test
    public void getFieldsEmptyArray() {
        JSONArray jsonArray= new JSONArray();
        Assert.assertFalse((Cliffs.getFields(jsonArray)).contains("key1"));


    }
}