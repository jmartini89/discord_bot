package me.name.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import static net.dv8tion.jda.api.Permission.ADMINISTRATOR;

public class CmdAdmin {
	public static boolean admin_command(
			@NotNull MessageReceivedEvent event, Server server, Message message, @NotNull String content) {
		if (event.getMember() == null || !event.getMember().hasPermission(ADMINISTRATOR))
			return false;

		if (content.startsWith("set")) {
			if (!setServer(server, message, content)) return false;
			server.restart();
			message.addReaction("✅").queue();
			return true;
		}

		if (content.matches("666")) {
			if (!server.hell) message.addReaction("\uD83D\uDD25").queue();
			else message.addReaction("\uD83D\uDCA7").queue();
			server.hell = !server.hell;
			return true;
		}

		if (content.matches("on")) {
			if (server.active) message.addReaction("⁉").queue();
			else message.addReaction("✅").queue();
			server.start();
			return true;
		}

		if (content.matches("off")) {
			if (server.active) message.addReaction("✅").queue();
			else message.addReaction("⁉").queue();
			server.stop();
			return true;
		}

		if (content.matches("help")) {
			message.reply(
					"**vAlexa MAN:**\n"
					+ "`!on` start chaos with default values\n"
					+ "`!off` stop chaos\n"
					+ "`!set [delay] [chances]` restart chaos with custom values\n"
					+ "`delay`: a number in seconds, `chances`: a number between 0 and 1\n"
					+ "`default values: [" + server.delay + "] [" + server.chances + "]`\n"
					+ "`!666` **hell mode - use with caution!**")
					.queue();
			return true;
		}
		return false;
	}

	private static boolean setServer(Server server, Message message, @NotNull String content) {
		String[] tokens = content.split(" ");

		if (tokens.length != 3) {
			message.addReaction("⁉").queue();
			return false;
		}

		try {
			server.delay = Integer.parseInt(tokens[1]);
			server.chances = Double.parseDouble(tokens[2]);
			if (server.delay < 0) server.delay *= -1;
		} catch (Exception e) {
			message.addReaction("⁉").queue();
			return false;
		}

		return true;
	}
}
