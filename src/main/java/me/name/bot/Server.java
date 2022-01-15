package me.name.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
	Guild guild;

	Runnable chaos;
	ScheduledExecutorService exec_scheduler;
	LocalTime spam_time;
	boolean active = false;

	double chances = 0.666;
	long delay = 120;
	long spam_delay = 30;
	boolean hell = false;

	public final AudioPlayer player;
	public final AudioTrackScheduler track_scheduler;
	Runnable audio_activity;
	ScheduledExecutorService audio_scheduler;

	public Server(Guild guild, @NotNull AudioPlayerManager manager) {
		this.guild = guild;

		spam_time = LocalTime.now();
		chaos = () -> Roulette.roulette(this);

		player = manager.createPlayer();
		track_scheduler = new AudioTrackScheduler(player);
		player.addListener(track_scheduler);
		this.guild.getAudioManager().setSendingHandler(this.getSendHandler());
		player.setVolume(100);

		audio_activity = () -> tracker(this);
		audio_scheduler = Executors.newScheduledThreadPool(1);
		audio_scheduler.scheduleAtFixedRate(audio_activity, 0, 60, TimeUnit.SECONDS);
	}

	public static void tracker(@NotNull Server server) {
		if (!server.guild.getAudioManager().isConnected()) return;
		AudioChannel channel = server.guild.getAudioManager().getConnectedChannel();
		if (channel == null) return;
		if ((server.player.getPlayingTrack()) == null || channel.getMembers().size() == 1) {
			server.player.stopTrack();
			server.guild.getAudioManager().closeAudioConnection();
		}
	}

	public AudioPlayerSendHandler getSendHandler() { return new AudioPlayerSendHandler(player); }

	public void start() {
		if (active) return;
		active = true;
		exec_scheduler = Executors.newScheduledThreadPool(1);
		exec_scheduler.scheduleAtFixedRate(chaos, 0, this.delay, TimeUnit.SECONDS);
	}

	public void stop() {
		if (!active) return;
		active = false;
		exec_scheduler.shutdown();
	}

	public void restart () { stop(); start(); }
}
