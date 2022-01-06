package me.name.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

import static me.name.bot.Bot.serverList;

public class Roulette {
	public static void roulette() {
		if (serverList.isEmpty()) return;

		if (Math.random() > Bot.chances) return;

		for (Guild guild : serverList) {
			if (guild != null) exec(guild);
		}
	}

	public static void roulette(Guild guild) {
		if (Math.random() > Bot.chances) return;
		exec(guild);
	}

	private static void exec(@NotNull Guild guild) {
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

		guild.kickVoiceMember(victim).queue();

		victim.getUser().openPrivateChannel().queue(
				act -> act.sendMessage("Scusa cumpá...").queue());

		/* Admins can NOT de-mute members disconnected from voice channels
        ** This method requires fallback implementation: de-mute routine/scheduler
		** for (Member member : members) member.mute(false).complete();
		** victim.mute(true).queue(act ->  victim.mute(false).queueAfter(5, SECONDS));
		 */
	}
}
