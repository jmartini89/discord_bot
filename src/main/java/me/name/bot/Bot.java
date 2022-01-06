package me.name.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MINUTES;

public class Bot {
	private static final String BOT_TOKEN = "OTI3MjA2Mjk4MTA4NDM2NTUw.YdG2KA.TvJkHHEniRlmrSf_GMrwJowLxJk";
	static Long defaultServerId = 823664543628001400L;

	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	static int delay = 15;

	public static void main(String[] args) throws Exception {
		JDA api = JDABuilder.createDefault(BOT_TOKEN)
				.setMemberCachePolicy(MemberCachePolicy.VOICE)
				.enableIntents(GatewayIntent.GUILD_MEMBERS)
				.addEventListeners(new CustomListener())
				.setActivity(Activity.playing("samba"))
				.build()
				.awaitReady();
		randoulette(api);
	}

	private static void randoulette(JDA api) {
		Runnable chaos = () -> Roulette.roulette(api, defaultServerId);
		scheduler.scheduleAtFixedRate(chaos, 0, delay, MINUTES);
	}
}
