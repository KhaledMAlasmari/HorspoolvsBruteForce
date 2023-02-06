/*

    Khaled mohammed alasmari - 2035189
    Omar abdulbagi - 2037070
    Salman alhothly - 2044556

*/

package string.matching;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class StringMatching {

    private static HashMap<Character, Integer> shiftTable;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner inputFile = new Scanner(new File("input.txt"));
        Scanner input = new Scanner(System.in);
        FileWriter patternsFile = new FileWriter("patterns.txt");
        shiftTable = new HashMap<>();
        String[] patterns;

        //input prompts
        System.out.print("How many lines you want to read from the text file?: ");
        int numOfLines = input.nextInt();
        System.out.print("How many patterns to be generated?: ");
        int numOfPatterns = input.nextInt();
        System.out.print("What is the length of each pattern?: ");
        int lengthOfPattern = input.nextInt();

        //get lines of text from file and store them in string variable 
        String text = getText(inputFile, numOfLines);

        // gen patterns and store them in an array to use for algorithms 
        patterns = genPatterns(numOfLines, numOfPatterns, lengthOfPattern, text);

        // print out the patterns on file
        for (String pattern : patterns) {
            patternsFile.write(pattern + "\n");
        }
        patternsFile.close();

        //implement  algorithm and get average time
        algorithms(patterns, text, lengthOfPattern);
    }

    public static String getText(Scanner inputFile, int numOfLines) {
        StringBuilder reading = new StringBuilder();
        // read lines from text file 
        for (int i = 0; i < numOfLines; i++) {
            if (inputFile.hasNextLine()) {
                reading.append(inputFile.nextLine());
            }
        }
        return reading.toString().toLowerCase();
    }

    public static String[] genPatterns(int numOfLines, int numOfPatterns, int LengthOfPatterns, String text) {

        String[] patterns = new String[numOfPatterns];

        // create patterns from lines of text file
        Random rand = new Random();
        for (int i = 0; i < numOfPatterns; i++) {
            int randNum = rand.nextInt(text.length() - LengthOfPatterns);
            patterns[i] = text.substring(randNum, randNum + LengthOfPatterns);
        }
        System.out.printf("%d Patterns, each of length %d have been genrated in a file pattern.txt\n", numOfPatterns, LengthOfPatterns);
        return patterns;
    }

    public static void createShiftTable(String pattern, int lengthOfPattern, String text) {
        //initialize shift table chareters with length of pattern
        int textLength = text.length();
        for (int i = 0; i < textLength; i++) {
            char ch = text.charAt(i);
            if (!shiftTable.containsKey(ch)) {
                shiftTable.put(ch, lengthOfPattern);
            }
        }

        //implement algorithm
        for (int i = 0; i < lengthOfPattern - 1; i++) {
            shiftTable.put(pattern.charAt(i), lengthOfPattern - 1 - i);
        }
        System.out.println(shiftTable);
    }

    public static int horspool(String pattern, String text, int m) {
        int i = m - 1;
        for (; i < text.length();) {
            int k = 0;
            while ((k < m) && (pattern.charAt(m - 1 - k) == (text.charAt(i - k)))) {
                k++;
            }
            if (k == m) {
                return i - m + 1;
            } else {
                i = i + shiftTable.get(text.charAt(i));
            }
        }
        return -1;
    }

    public static int bruteForce(String pattern, String text, int lengthOfPattern) {
        for (int i = 0; i <= text.length() - lengthOfPattern; i++) {
            int j = 0;
            while ((j < lengthOfPattern) && (pattern.substring(j, j + 1).equals(text.substring(i + j, j + i + 1)))) {
                j++;
            }
            if (j == lengthOfPattern) {
                return i;
            }
        }
        return -1;
    }

    public static void algorithms(String[] patterns, String text, int lengthOfPattern) {
        ArrayList<Long> horspoolTime = new ArrayList<>();
        ArrayList<Long> bruteForceTime = new ArrayList<>();
        long startTime, endTime, timeTaken;

        for (String pattern : patterns) {
            shiftTable.clear();
            System.out.printf("The shift table for \"%s\" is:\n", pattern);
            startTime = System.nanoTime();
            createShiftTable(pattern, pattern.length(), text);
            horspool(pattern, text, lengthOfPattern);
            endTime = System.nanoTime();
            timeTaken = endTime - startTime;
            horspoolTime.add(timeTaken);
            
            startTime = System.nanoTime();
            bruteForce(pattern, text, lengthOfPattern);
            endTime = System.nanoTime();
            timeTaken = endTime - startTime;
            bruteForceTime.add(timeTaken);
            System.out.println("");
        }
        System.out.printf("average time of search in brute force approach: %,d ns \n", getaverage(bruteForceTime));
        System.out.printf("average time of search in horspool approach: %,d ns \n", getaverage(horspoolTime));

        if (getaverage(bruteForceTime) < getaverage(horspoolTime)) {
            System.out.println("for this instance Brute Force approach is better than Horspool");
        } else {
            System.out.println("for this instance Horspool approach is better than Brute Force");
        }
    }

    public static long getaverage(ArrayList<Long> time) {

        long sum = 0;
        for (int i = 0; i < time.size(); i++) {
            sum = time.get(i) + sum;
        }
        return sum / time.size();
    }
}
