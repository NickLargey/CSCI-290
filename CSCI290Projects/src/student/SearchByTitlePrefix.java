/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.util.Comparator;

/**
 *
 * @author Nick Largey
 */
public class SearchByTitlePrefix {
    
    private final Song[] songs;
    
    public SearchByTitlePrefix(SongCollection sc){
        songs = sc.getAllSongs();
        
        Comparator<Song> comp = new Song.CmpTitle();
        ((CmpCnt) comp).resetCmpCnt();
        RaggedArrayList<Song> raggedArraySongList = new RaggedArrayList<>(comp);
        
        for (Song song : songs) {
            raggedArraySongList.add(song);
        }
    }
    
    public void search(String prefix){
        
    }
}
