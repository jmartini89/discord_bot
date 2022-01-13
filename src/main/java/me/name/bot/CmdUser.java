package me.name.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.Arrays;

public class CmdUser {
	public static void user_command(MessageReceivedEvent event, Server server, Message message, String content) {
		if (troll(event, server, message, content)) return;
//		music(event, server, content);
	}

	private static boolean troll(MessageReceivedEvent event, Server server, Message message, String content) {
		if (!trigger(content)) return false;
		if (spam(event, server, message)) return false;

		if (server.guild.getIdLong() == 823664543628001400L) message.addReaction("♿").queue();
		else message.addReaction("\uD83C\uDF00").queue();

		if (Math.random() > 0.5) return true;
//		Roulette.roulette(server);
		Transmit.audio(server, "zvOWew99EAk", null);
		return true;
	}

	private static boolean trigger(@NotNull String content) {
		String[] trigger_lst = {
				"troll",
				"kick",
				"contatto",
				"random",
				"pinterest",
				"104",
				"199",
				"10-4"};
		String[] msg = content.split(" ");
		for (int i = 0; i < msg.length; i++) msg[i] = msg[i].toLowerCase();
		return Arrays.stream(trigger_lst).anyMatch(element -> Arrays.asList(msg).contains(element));
	}

	private static boolean spam(MessageReceivedEvent event, @NotNull Server server, Message message) {
		if (!LocalTime.now().isAfter(server.spam_time.plusSeconds(60L))) {
			event.getAuthor().openPrivateChannel().queue(
					act -> act.sendMessage("https://youtu.be/6lA3T78o9pY").queue());
			message.delete().queue();
			return true;
		}
		server.spam_time = LocalTime.now();
		return false;
	}

	private static void music(MessageReceivedEvent event, Server server, @NotNull String content) {
		String[] cmd = content.split(" ", 2);
		if (cmd[0].equals("stop")) server.player.stopTrack();
		if (cmd[0].equals("play") && cmd.length == 2) Transmit.audio(server, cmd[1], event.getTextChannel());
	}
}
