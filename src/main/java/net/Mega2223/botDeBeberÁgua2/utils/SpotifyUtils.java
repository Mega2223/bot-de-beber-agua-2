package net.Mega2223.botDeBeberÁgua2.utils;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.User;

import java.io.IOException;

import static net.Mega2223.botDeBeberÁgua2.principal.aguaBot.spotify;

public class SpotifyUtils {

    public static final String HIREARCHY_PREFIX = "| ";

    public static String getUserDetails(User user, String prefix) throws IOException, ParseException, SpotifyWebApiException {
        String retrieval = "\n" + prefix + "User: " + user.getDisplayName();
        retrieval += "\n" + prefix + "País: " + user.getCountry();
        retrieval += "\n" + prefix + "Playlists:";

        PlaylistSimplified[] playlists = spotify.getListOfUsersPlaylists(user.getId()).build().execute().getItems();

        for (int i = 0; i < playlists.length; i++) {
            PlaylistSimplified act = playlists[i];
            retrieval += "\n" + prefix + "Playlist: " + act.getName();
            retrieval += getPlaylistDetails(spotify.getPlaylist(act.getId()).build().execute(), prefix + prefix);
        }

        return retrieval;
    }

    public static String getPlaylistDetails(Playlist playlist, String prefix) {

        PlaylistTrack[] songs = playlist.getTracks().getItems();
        String retrieval = //"Playlist: " + playlist.getName() +
                "\n" + prefix + "Descrição: " + playlist.getDescription() +
                        "\n" + prefix + "ID: " + playlist.getId() +
                        "\n" + prefix + "Autor: " + playlist.getOwner().getDisplayName() +
                        "\n" + prefix + "Tamanho: " + songs.length +
                        "\n" + prefix + "Músicas: " +
                        "\n" + prefix + prefix;

        for (int i = 0; i < songs.length; i++) {
            PlaylistTrack act = songs[i];
            retrieval += "\n" + prefix + prefix + " Música: " + act.getTrack().getName();
            retrieval += getSongDetails(act, prefix + prefix + prefix) + "\n" + prefix + prefix;
        }
        return retrieval;
    }

    public static String getSongDetails(PlaylistTrack track, String prefix) {
        IPlaylistItem track1 = track.getTrack();
        String retrieval = "";
        //retrieval += "\n" + prefix + "Música: " + track1.getName();
        retrieval += "\n" + prefix + "Duração: " + track1.getDurationMs() + "ms";
        retrieval += "\n" + prefix + "URI: " + track1.getUri();
        retrieval += "\n" + prefix + "Data de adição: " + track.getAddedAt();

        return retrieval;
    }

}
