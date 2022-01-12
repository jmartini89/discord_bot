package me.name.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Bot {
	static Map<Long, Server> serverMap = new HashMap<>();
	static AudioPlayerManager playerManager;

	public static void main(String @NotNull [] args) throws Exception {
		playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);

		JDABuilder.createDefault(args[0])
			.setMemberCachePolicy(MemberCachePolicy.VOICE)
			.enableIntents(GatewayIntent.GUILD_MEMBERS)
			.addEventListeners(new CustomListener())
			.setActivity(Activity.playing("samba"))
			.build()
			.awaitReady();

	}
}
