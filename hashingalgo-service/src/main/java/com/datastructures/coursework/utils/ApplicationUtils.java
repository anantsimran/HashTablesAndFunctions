package com.datastructures.coursework.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationUtils {

    public List<Integer> getList(){
        List<Integer> ints = new ArrayList<>();
        String fileName ="src"+  File.separator+ "main"+  File.separator+  "resources"+  File.separator+ "primes.list";
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            List<String> stringList = lines.collect(Collectors.toList());
            for(String s: stringList)
            {
                s.replaceAll("\\s", "");
             if (!s.isEmpty()){
                ints.add(new Integer(s));
             }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ints;
    }

}
