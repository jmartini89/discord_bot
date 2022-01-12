package me.name.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Guild;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
	Guild guild;
	double chances = 0.25;
	int delay = 300;

	boolean active = false;
	boolean hell = false;
	Runnable chaos;
	ScheduledExecutorService scheduler;
	LocalTime spam_time;

	AudioPlayer player;

	public Server(Guild guild) {
		this.guild = guild;
		chaos = () -> Roulette.roulette(this);
		spam_time = LocalTime.now();
		player = BotManager.playerManager.createPlayer();
	}

	public void start() {
		if (active) return;
		active = true;
		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(chaos, 0, this.delay, TimeUnit.SECONDS);
	}

	public void stop() {
		if (!active) return;
		active = false;
		scheduler.shutdown();
	}

	public void restart () {
		stop();
		start();
	}
}
