package me.name.bot;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.Arrays;

public class CmdUser {
	public static void user_command(MessageReceivedEvent event, Server server, Message message, String content) {
		if (troll(event, server, message, content)) return;
		music(event, server, message, content);
	}

	// TROLL
	private static boolean troll(MessageReceivedEvent event, Server server, Message message, String content) {
		if (!trigger(content)) return false;
		if (spam(event, server, message)) return true;

		if (server.guild.getIdLong() == 823664543628001400L) message.addReaction("♿").queue();
		else message.addReaction("\uD83C\uDF00").queue();

		Roulette.roulette(server);
		return true;
	}

	private static boolean trigger(@NotNull String content) {
		String[] trigger_lst = {"troll", "contatto", "random", "pinterest", "104", "199", "10-4"};
		String[] msg = content.split(" ");
		for (int i = 0; i < msg.length; i++) msg[i] = msg[i].toLowerCase();
		return Arrays.stream(trigger_lst).anyMatch(element -> Arrays.asList(msg).contains(element));
	}

	private static boolean spam(MessageReceivedEvent event, @NotNull Server server, Message message) {
		if (!LocalTime.now().isAfter(server.spam_time.plusSeconds(server.spam_delay))) {
			event.getAuthor().openPrivateChannel().queue(
					act -> act.sendMessage("https://youtu.be/6lA3T78o9pY").queue());
			message.delete().queue();
			return true;
		}
		server.spam_time = LocalTime.now();
		return false;
	}

	// MUSIC
	private static void music(
			@NotNull MessageReceivedEvent event, Server server, Message message, @NotNull String content) {
		Member member = event.getMember();
		if (member == null) return;
		GuildVoiceState state = member.getVoiceState();
		if (state == null) return;

		String[] cases = {"play", "next", "stop"};
		String[] emotes = {"▶", "⏭", "\uD83D\uDED1"};
		String[] cmd = content.split(" ", 2);
		int candidate = Arrays.asList(cases).indexOf(cmd[0]);
		if (candidate == -1) return;
		if (!isConnected(state, message)) return;

		if (cmd[0].equals("play")) {
			if (cmd.length != 2) {
				message.addReaction("⁉").queue();
				return;
			}
			AudioLoad.load(server, cmd[1], member.getVoiceState().getChannel(), event.getTextChannel());
		}
		if (cmd[0].equals("next")) server.track_scheduler.nextTrack();
		if (cmd[0].equals("stop")) server.track_scheduler.stop();
		message.addReaction(emotes[candidate]).queue();
	}

	private static boolean isConnected(@NotNull GuildVoiceState state, Message message) {
		if (state.inAudioChannel()) return true;
		message.addReaction("❌").queue();
		return false;
	}
}
