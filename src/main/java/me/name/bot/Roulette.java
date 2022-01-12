package me.name.bot;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Roulette {
	public static void roulette(Server server) {
		Random rand = new Random();

		List<VoiceChannel> voice_channels = server.guild.getVoiceChannels();
		if (voice_channels.isEmpty()) return;

		List<VoiceChannel> candidate_channels = new ArrayList<>();
		for (VoiceChannel voice_channel : voice_channels) {
			if (!voice_channel.getMembers().isEmpty()) candidate_channels.add(voice_channel);
		}
		if (candidate_channels.isEmpty()) return;

		VoiceChannel victim_channel = candidate_channels.get(rand.nextInt(candidate_channels.size()));
		List<Member> members = victim_channel.getMembers();
		Member victim = members.get(rand.nextInt(members.size()));

		if (server.hell) {
			// Admins can NOT de-mute members disconnected from voice channels
			// This method requires fallback implementation: de-mute routine/scheduler
			for (Member member : members) member.mute(false).complete();
			victim.mute(true).queue(act ->  victim.mute(false).queueAfter(5, TimeUnit.SECONDS));
			return;
		}
		server.guild.kickVoiceMember(victim).queue();
		victim.getUser().openPrivateChannel().queue(
				act -> act.sendMessage("Scusa cumpá...").queue());
//		server.guild.getAudioManager().openAudioConnection(victim_channel);
	}
}
