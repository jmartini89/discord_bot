package me.name.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;

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

	double chances = 0.25;
	int delay = 300;
	boolean hell = false;

	public final AudioPlayer player;
	public final TrackScheduler track_scheduler;

	public Server(Guild guild, AudioPlayerManager manager) {
		this.guild = guild;

		chaos = () -> Roulette.roulette(this);
		spam_time = LocalTime.now();

		player = manager.createPlayer();
		track_scheduler = new TrackScheduler(player, this);
		player.addListener(track_scheduler);
		this.guild.getAudioManager().setSendingHandler(this.getSendHandler());
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
