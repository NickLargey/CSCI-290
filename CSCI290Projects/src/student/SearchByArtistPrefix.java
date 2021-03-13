/**
 * SearchByArtistPrefix.java 
 *****************************************************************************
 *                       revision history
 *****************************************************************************
 * 
 * 8/2015 Anne Applin - Added formatting and JavaDoc 
 * 2015 - Bob Boothe - starting code  
 *****************************************************************************
 * Search by Artist Prefix searches the artists in the song database 
 * for artists that begin with the input String
 */

package student;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;


public class SearchByArtistPrefix {
    // keep a local direct reference to the song array
    private final Song[] songs;  

    /**
     * constructor initializes the property. [Done]
     * @param sc a SongCollection object
     */
    public SearchByArtistPrefix(SongCollection sc) {
        songs = sc.getAllSongs();
    }

    /**
     * find all songs matching artist prefix uses binary search should operate
     * in time log n + k (# matches)
     * converts artistPrefix to lowercase and creates a Song object with 
     * artist prefix as the artist in order to have a Song to compare.
     * walks back to find the first "beginsWith" match, then walks forward
     * adding to the arrayList until it finds the last match.
     *
     * @param artistPrefix all or part of the artist's name
     * @return an array of songs by artists with substrings that match 
     *    the prefix
     */
    public Song[] search(String artistPrefix) {
        // we want to do this search case independent
        artistPrefix = artistPrefix.toLowerCase();
        // now create a Competitor with that as the owner name 
        Song key = new Song(artistPrefix, "none", "none");
        // print the competitor to make sure we created it right for debugging
        // System.out.println(key);
        // create an object of Comparator
        Comparator<Song> cmp = new Song.CmpArtist();
        ((CmpCnt)cmp).resetCmpCnt();
        int partLength = artistPrefix.length();
        // call the Arrays.binarySearch with the array, key, the Comparator
        int i = Arrays.binarySearch(songs, key, cmp);
        System.out.println(((CmpCnt)cmp).getCmpCnt() + " compares for search.");
        System.out.println("The result of the binary search " + i);
        // if it is actually a negative what we have is the index where it would
        // be if it existed. Since we are looking for a piece of a name it 
        // will not find one 
        if (i < 0) {
            i = -i - 1;
        }
        // System.out.println(i);
        // System.out.println(songs[i]);
        // System.out.println(songs[i].getArtist());
        // we don't know how many matches we will find so we will create an 
        // array list and convert it to an array to return it.
        ArrayList<Song> list = new ArrayList<>();
        if (i >= 0) {// it should be, but it never hurts to be careful
            // find the front - the first partial match.
            while (i >= 0 && 
                    songs[i].getArtist().length() >= partLength &&
                    songs[i].getArtist().substring(0, partLength).
                    compareToIgnoreCase(artistPrefix) == 0) {
                i--;
            }
            // we WILL go one too far.
            i++;
            // now fill the list by walking forward
            while (i < songs.length && 
                    songs[i].getArtist().length() >= partLength && 
                    songs[i].getArtist().substring(0, partLength).
                    compareToIgnoreCase(artistPrefix) == 0) {
                list.add(songs[i]);
                i++;
            }
            Song[] result = new Song[list.size()];
            result = list.toArray(result);
            return result;
        } else {
            return null;
        }
    }

    /**
     * testing method for this unit
     * @param args  command line arguments set in Project Properties - 
     * the first argument is the data file name and the second is the partial 
     * artist name, e.g. be which should return beatles, beach boys, beegees,
     * etc.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

        if (args.length > 1) {
            System.out.println("searching for: " + args[1]);
            Song[] byArtistResult = sbap.search(args[1]);

            System.out.println("Total songs = " + byArtistResult.length + 
                ", found artists: ");
            Stream.of(byArtistResult).limit(10).forEach(System.out::println);
        }
    }
}
