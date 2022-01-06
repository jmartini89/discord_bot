package me.name.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Roulette {
	public static void roulette(JDA api, long ServerId) {
		Random rand = new Random();

		List<Integer> init = Arrays.asList(1, 2, 3, 4);
		if ((init.get(rand.nextInt(init.size()))) > 1) return;

		Guild guild = api.getGuildById(ServerId);
		if (guild == null) return;

		List<VoiceChannel> voice_channels = guild.getVoiceChannels();
		if (voice_channels.isEmpty()) return;

		List<Member> members = voice_channels.get(0).getMembers();
		if (members.isEmpty()) return;

		Member victim = members.get(rand.nextInt(members.size()));

		guild.kickVoiceMember(victim).queue();

		victim.getUser().openPrivateChannel().queue( (act) -> {
			act.sendMessage("Scusa cumpá...").queue(); } );

//		for (Member member : members) member.mute(false).complete();
//		victim.mute(true).queue((a) ->  victim.mute(false).queueAfter(5, SECONDS));
	}
}
