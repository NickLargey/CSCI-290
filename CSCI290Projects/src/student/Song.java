/*
 * 1.25.2021 - Nick Largey and Keith Austin - Added toString and compareTo methods
 * 8.2016 - Anne Applin - formatting and JavaDoc skeletons added   
 * 2015 - Prof. Bob Boothe - Starting code and main for testing  
 ************************************************************************
 * Song class to hold strings for a song's artist, title, and lyrics
 * Do not add any methods for part 1, just implement the ones that are 
 * here.
 */
package student;

import java.util.*;

/**
 *
 * @author boothe
 */
public class Song implements Comparable<Song> {

    // fields
    private String artist;
    private String title;
    private String lyrics;

    /**
     * Parameterized constructor
     *  
     * @param artist the author of the song
     * @param title the title of the song
     * @param lyrics the lyrics as a string with linefeeds embedded
     */
    public Song(String artist, String title, String lyrics) {
        this.artist = artist;
        this.title = title;
        this.lyrics = lyrics;

    }

    /**
     *
     * @return artist: artist name
     */
    public String getArtist() {
        return artist;
    }

    /**
     *
     * @return lyrics: lyrics to song object
     */
    public String getLyrics() {
        return lyrics;
    }

    /**
     *
     * @return title: song title
     */
    public String getTitle() {
        return title;
    }

    /**
     * returns name and title ONLY
     *
     * @return formatted name and title
     */
    @Override
    public String toString() {
        return String.format("%s, \"%s\"", artist, title);
        
    }

    /**
     * the default comparison of songs primary key: artist, secondary key: title
     * used for sorting and searching the song array if two songs have the same
     * artist and title they are considered the same
     *
     * @param song2
     * @return a negative number, positive number or 0 depending on whether this
     * song should be before, after or is the same. Used for a "natural" sorting
     * order. In this case first by author then by title so that the all of an
     * artist's songs are together, but in alpha order. Follow the given
     * example.
     */
    @Override
    public int compareTo(Song song2) {
        if (song2 == null) {
            return 1; // shouldn't be any null objects, but if there are
        }                          // put them at the end       
        int res = this.artist.compareToIgnoreCase(song2.artist);
        if (res == 0){
            res = this.title.compareToIgnoreCase(song2.title);            
        }
        return res;
    }

    public static class CmpArtist extends CmpCnt 
            implements Comparator<Song>{

        @Override
        public int compare(Song s1, Song s2) {
            cmpCnt++;
            return s1.getArtist().compareToIgnoreCase(s2.getArtist());
        }
        
    }
    
    public static class CmpTitle extends CmpCnt
            implements Comparator<Song> {
        @Override
        public int compare(Song s1, Song s2){
            cmpCnt++;
            return s1.getTitle().compareToIgnoreCase(s2.getTitle());
        }
    }
    /**
     * testing method to unit test this class
     *
     * @param args
     */
    public static void main(String[] args) {
        Song s1 = new Song("Professor B",
                "Small Steps",
                "Write your programs in small steps\n"
                + "small steps, small steps\n"
                + "Write your programs in small steps\n"
                + "Test and debug every step of the way.\n");

        Song s2 = new Song("Brian Dill",
                "Ode to Bobby B",
                "Professor Bobby B., can't you see,\n"
                + "sometimes your data structures mystify me,\n"
                + "the biggest algorithm pro since Donald Knuth,\n"
                + "here he is, he's Robert Boothe!\n");

        Song s3 = new Song("Professor B",
                "Debugger Love",
                "I didn't used to like her\n"
                + "I stuck with what I knew\n"
                + "She was waiting there to help me,\n"
                + "but I always thought print would do\n\n"
                + "Debugger love .........\n"
                + "Now I'm so in love with you\n");

        System.out.println("testing getArtist: " + s1.getArtist());
        System.out.println("testing getTitle: " + s1.getTitle());
        System.out.println("testing getLyrics:\n" + s1.getLyrics());

        System.out.println("testing toString:\n");
        System.out.println("Song 1: " + s1);
        System.out.println("Song 2: " + s2);
        System.out.println("Song 3: " + s3);

        System.out.println("testing compareTo:");
        System.out.println("Song1 vs Song2 = " + s1.compareTo(s2));
        System.out.println("Song2 vs Song1 = " + s2.compareTo(s1));
        System.out.println("Song1 vs Song3 = " + s1.compareTo(s3));
        System.out.println("Song3 vs Song1 = " + s3.compareTo(s1));
        System.out.println("Song1 vs Song1 = " + s1.compareTo(s1));
        
        System.out.println("Testing Compare:");
        CmpArtist ca = new CmpArtist();
        System.out.println("Song1 vs Song2 = " + ca.compare(s1, s2));
        System.out.println("Song2 vs Song1 = " + ca.compare(s2, s1));
        System.out.println("Song1 vs Song3 = " + ca.compare(s1, s3));
        System.out.println("Song3 vs Song1 = " + ca.compare(s3, s1));
        System.out.println("Song1 vs Song1 = " + ca.compare(s1, s1));
    }
}
