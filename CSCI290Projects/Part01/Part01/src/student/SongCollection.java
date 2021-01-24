package student;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Stream;

/*
 * 
 * 8.2016 - Anne Applin - formatting and JavaDoc skeletons added   
 * 2015 - Prof. Bob Boothe - Starting code and main for testing  
 ************************************************************************
 * SongCollection.java 
 * Read the specified data file and build an array of songs.
 */
/**
 *
 * @author boothe
 */
public class SongCollection {

    private Song[] songs;

    /**
     * Note: in any other language, reading input inside a class is simply not
     * done!! No I/O inside classes because you would normally provide
     * precompiled classes and I/O is OS and Machine dependent and therefore not
     * portable. Java runs on a virtual machine that IS portable. So this is
     * permissable because we are programming in Java.
     *
     * @param filename The path and filename to the datafile that we are using
     * must be set in the Project Properties as an argument.
     */
    public SongCollection(String filename) throws FileNotFoundException {

        // use a try catch block
        // read in the song file and build the songs array
        // you must use a StringBuilder to read in the lyrics!
        // you must add the line feed at the end of each lyric line.
        try {
            FileReader f = new FileReader(filename);
            BufferedReader b = new BufferedReader(f);
            String artist;
            String title;
            while (b != null) {
                artist = b.readLine();
                System.out.println(artist);
                title = b.readLine();
            StringBuilder str = new StringBuilder();
            while (b.readLine() != "\"") {
                    str.append(b.readLine());
                    System.out.println(b.readLine());
                }
            String lyrics = str.toString();
            System.out.println(lyrics);
            
//            Scanner scan = new Scanner(f);
//            String artist;
//            String title;
//            while (scan.hasNextLine()) {
//                artist = scan.nextLine();
//                
//                title = scan.nextLine();
//            }
//            StringBuilder str = new StringBuilder();
//            while (scan.nextLine() != "\"") {
//                    str.append(scan.nextLine());
//                }
//            String lyrics = str.toString();
            songs.add(Song(artist, title, lyrics));  
            b.close();
            }
        } catch (Exception e) {
            System.err.println("Caught Exception: " + e.getMessage());
        }

        // sort the songs array using Array.sort (see the Java API)
    }

    /**
     * this is used as the data source for building other data structures
     *
     * @return the songs array
     */
    public Song[] getAllSongs() {
        return songs;
    }

    /**
     * unit testing method
     *
     * @param args
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);

        // todo: show song count 
        Stream.of(sc).limit(10).forEach(System.out::println);
    }
}
