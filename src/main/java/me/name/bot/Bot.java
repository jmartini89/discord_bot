package me.name.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot {
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	static List<Guild> serverList = new ArrayList<>();
	static double chances = 0.25;
	static int delay = 10;

	public static void main(String @NotNull [] args) throws Exception {
		JDABuilder.createDefault(args[0])
			.setMemberCachePolicy(MemberCachePolicy.VOICE)
			.enableIntents(GatewayIntent.GUILD_MEMBERS)
			.addEventListeners(new CustomListener())
			.setActivity(Activity.playing("samba"))
			.build()
			.awaitReady();
		scheduled_roulette();
	}

	private static void scheduled_roulette() {
		Runnable chaos = Roulette::roulette;
		scheduler.scheduleAtFixedRate(chaos, 0, delay, TimeUnit.MINUTES);
	}
}
