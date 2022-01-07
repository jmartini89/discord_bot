package me.name.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Roulette {
	public static void roulette(Server server) {
		if (Math.random() > server.chances) return;
		exec(server, server.guild);
	}

	public static void roulette(Guild guild) {
		if (Math.random() > 0.5) return;
		exec(null, guild);
	}

	private static void exec(Server server, @NotNull Guild guild) {
		Random rand = new Random();

		List<VoiceChannel> voice_channels = guild.getVoiceChannels();
		if (voice_channels.isEmpty()) return;

		List<Member> members = null;
		for (VoiceChannel voice_channel : voice_channels) {
			members = voice_channel.getMembers();
			if (!members.isEmpty())
				break;
		}

		if (members.isEmpty()) return;

		Member victim = members.get(rand.nextInt(members.size()));

		if (server != null && server.hell) {
			// Admins can NOT de-mute members disconnected from voice channels
			// This method requires fallback implementation: de-mute routine/scheduler
			for (Member member : members) member.mute(false).complete();
			victim.mute(true).queue(act ->  victim.mute(false).queueAfter(5, TimeUnit.SECONDS));
		}
		else {
			guild.kickVoiceMember(victim).queue();
			victim.getUser().openPrivateChannel().queue(
					act -> act.sendMessage("Scusa cumpá...").queue());
		}
	}
}
