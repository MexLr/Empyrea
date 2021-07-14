package org.example.ataraxiawarmup.song;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class NBSPlayer {

    private Song song;

    public NBSPlayer(String songName) {
        this.song = NBSDecoder.parse(new File("D:\\Games\\Eidolon\\plugins\\Songs", songName + ".nbs"));
    }

    public Song getSong() {
        return song;
    }

    public void startSong(Player player) {
        CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
        if (customPlayer.getCurrentRadio() != null) {
            if (!customPlayer.getCurrentRadio().getSong().getPath().equals(this.song.getPath())) {
                customPlayer.getCurrentRadio().removePlayer(player);
            } else {
                return;
            }
        }
        RadioSongPlayer sp = new RadioSongPlayer(this.song);
        sp.setAutoDestroy(true);
        sp.addPlayer(player);
        sp.setPlaying(true, true);
        sp.setRepeatMode(RepeatMode.ONE);
        customPlayer.setCurrentRadio(sp);
    }
}
