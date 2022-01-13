package me.name.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

public class Transmit {
	public static void audio(@NotNull Server server, @NotNull String cmd, TextChannel channel) {
		BotManager.playerManager.loadItemOrdered(server.player, cmd, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				if (channel != null) channel.sendMessage("Adding to queue " + track.getInfo().title).queue();
				play(server, track);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();
				if (firstTrack == null) firstTrack = playlist.getTracks().get(0);
				if (channel != null) {
					channel.sendMessage(
							"Adding to queue " +
									firstTrack.getInfo().title +
									" (first track of playlist " +
									playlist.getName() + ")")
							.queue();
				}
				play(server, firstTrack);
			}

			@Override
			public void noMatches() {
				if (channel != null) channel.sendMessage("Nothing found by " + cmd).queue();
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				if (channel != null) channel.sendMessage("Could not play: " + exception.getMessage()).queue();
			}
		});
	}

	private static void play(@NotNull Server server, AudioTrack track) {
		server.guild.getAudioManager().openAudioConnection(server.guild.getVoiceChannels().get(0));
		server.track_scheduler.queue(track);
	}
}
