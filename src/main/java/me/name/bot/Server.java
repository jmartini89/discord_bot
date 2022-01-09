package me.name.bot;

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

	public Server(Guild guild) {
		this.guild = guild;
		this.chaos = () -> Roulette.roulette(this, true);
		spam_time = LocalTime.now();
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
