package me.name.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Bot {
	static List<Server> serverList = new ArrayList<>();

	public static void main(String @NotNull [] args) throws Exception {
		JDABuilder.createDefault(args[0])
			.setMemberCachePolicy(MemberCachePolicy.VOICE)
			.enableIntents(GatewayIntent.GUILD_MEMBERS)
			.addEventListeners(new CustomListener())
			.setActivity(Activity.playing("samba"))
			.build()
			.awaitReady();
	}
}
