package student;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

/*
 * 1.24.2021 - Nick Largey and Keith Austin - Added SongCollection constructor
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
    public SongCollection(String filename) {

	// use a try catch block
        // read in the song file and build the songs array
        // you must use a StringBuilder to read in the lyrics!
        // you must add the line feed at the end of each lyric line.
        try {
            Scanner fileReader = new Scanner(new File(filename));
            ArrayList<Song> tempList = new ArrayList<>();
            if (!fileReader.hasNext()) {
                System.err.println("File is empty.");
                System.exit(1);
            }
            
            String artist;
            String title;
            String lyrics;
            
            while (fileReader.hasNext()) {
                artist = fileReader.nextLine().split("=")[1].split("\"")[1];
                title = fileReader.nextLine().split("=")[1].split("\"")[1];
                StringBuilder lyricsFeed = new StringBuilder();
                String nextLine = fileReader.nextLine();
                while (!nextLine.equals("\"")) {
                    lyricsFeed.append(nextLine + "\n");
                    nextLine = fileReader.nextLine();
                    
                }
                
                
                lyrics = lyricsFeed.toString().split("=")[1];
                lyrics = lyrics.replaceAll("\"", "");
                lyrics = lyrics.replaceAll("''", "\"");
                
                tempList.add(new Song(artist, title, lyrics));
            }
            
            songs = new Song[tempList.size()];
            for (int i = 0; i < tempList.size(); i++) {
                songs[i] = tempList.get(i);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file '" + filename + "'.");
            System.exit(1);
        }
        
        
        // sort the songs array using Array.sort (see the Java API)
        Arrays.sort(songs);
    }
 
    /**
     * this is used as the data source for building other data structures
     * @return the songs array
     */
    public Song[] getAllSongs() {
        return songs;
    }
 
    /**
     * unit testing method
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);

        // todo: show song count 
        System.out.println("Total songs = " + sc.getAllSongs().length + 
                ", first songs: ");
        Stream.of(sc.getAllSongs()).limit(10).forEach(System.out::println);
    }
}