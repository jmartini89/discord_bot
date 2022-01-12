package me.name.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.LocalTime;
import java.util.Arrays;

public class UserTroll {
	public static void user_troll(
		MessageReceivedEvent event, Server server, Message message, String content) {
		if (!trigger(content)) return;
		if (spam(event, server, message)) return;

		if (server.guild.getIdLong() == 823664543628001400L) message.addReaction("♿").queue();
		else message.addReaction("\uD83C\uDF00").queue();

		if (Math.random() > 0.5) return;
		Roulette.roulette(server);
	}

	static private boolean trigger(String content) {
		String[] trigger_lst = {
				"contatto",
				"random",
				"pinterest",
				"104",
				"199"};

		return Arrays.stream(trigger_lst).anyMatch(content::contains);
	}

	static private boolean spam(MessageReceivedEvent event, Server server, Message message) {
		if (!LocalTime.now().isAfter(server.spam_time.plusSeconds(60L))) {
			event.getAuthor().openPrivateChannel().queue(
					act -> act.sendMessage("https://youtu.be/6lA3T78o9pY").queue());
			message.delete().queue();
			return true;
		}
		server.spam_time = LocalTime.now();
		return false;
	}
}
