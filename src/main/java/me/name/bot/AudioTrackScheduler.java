package me.name.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AudioTrackScheduler extends AudioEventAdapter {
	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;

	public AudioTrackScheduler(AudioPlayer player) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
	}

	public void queue(AudioTrack track) throws Exception {
		if (!player.startTrack(track, true)) {
			if (!queue.offer(track)) throw new Exception("TrackScheduler's queue is full");
		}
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
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, @NotNull FriendlyException exception) {
		exception.printStackTrace();
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {}
}
