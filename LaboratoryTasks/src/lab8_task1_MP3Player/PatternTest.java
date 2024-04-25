package lab8_task1_MP3Player;

import java.util.ArrayList;
import java.util.List;
class Song{
    String title;
    String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Song{");
        sb.append("title=").append(title);
        sb.append(", artist=").append(artist);
        sb.append('}');
        return sb.toString();
    }
}
class MP3Player{
    List<Song> songs;
    int currSong;
    int playing;

    public MP3Player(List<Song> songs) {
        this.songs = songs;
        this.currSong = 0;
        this.playing=0;
    }

    public void pressPlay() {
        if(playing==1){
            System.out.println("Song is already playing");
        }else{
            System.out.println(String.format("Song %s is playing",currSong));
            playing=1;
        }
    }

    public void printCurrentSong() {
        System.out.println(songs.get(currSong).toString());
    }

    public void pressStop() {
        if(playing==1){
            System.out.println(String.format("Song %s is paused",currSong));
            playing=0;
        }else if(playing==0){
            System.out.println("Songs are stopped");
            playing=-1;
            currSong=0;
        }else{
            System.out.println("Songs are already stopped");
        }
    }

    public void pressFWD() {
        if(currSong+1==songs.size()) currSong=0;
        else currSong++;
        if(playing!=-1)playing=0;
        System.out.println("Forward...");
    }

    public void pressREW() {
        if(currSong-1==-1){
            currSong=songs.size()-1;
        }else{
            currSong--;
        }
        if(playing!=-1) playing=0;
        System.out.println("Reward...");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MP3Player{");
        sb.append("currentSong = ").append(currSong);
        sb.append(", songList = ").append(songs);
        sb.append('}');
        return sb.toString();
    }
}
public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde