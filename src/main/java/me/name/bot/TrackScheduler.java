package me.name.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
	private final Server server;
	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;

	public TrackScheduler(AudioPlayer player, Server server) {
		this.server = server;
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
	}

	public void queue(AudioTrack track) {
		if (!player.startTrack(track, true)) { queue.offer(track); }
	}

	public void nextTrack() {
		player.startTrack(queue.poll(), false);
	}

	@Override
	public void onPlayerPause(AudioPlayer player) {}

	@Override
	public void onPlayerResume(AudioPlayer player) {}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, @NotNull AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) nextTrack();
		server.guild.getAudioManager().closeAudioConnection();
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {}
}
