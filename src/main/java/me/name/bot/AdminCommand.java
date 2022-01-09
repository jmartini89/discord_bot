package me.name.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public class AdminCommand {
	public static void admin_command(Guild guild, Message message, @NotNull String content) {
		Server server = Bot.serverMap.get(guild.getIdLong());
		if (server == null) return;

		if (content.startsWith("set")) {
			if (!setServer(server, message, content)) return;
			server.restart();
			message.addReaction("✅").queue();
			return;
		}

		if (content.matches("666")) {
			if (!server.hell) message.addReaction("\uD83D\uDD25").queue();
			else message.addReaction("\uD83D\uDCA7").queue();
			server.hell = !server.hell;
			return;
		}

		if (content.matches("on")) {
			if (server.active) message.addReaction("⁉").queue();
			else message.addReaction("✅").queue();
			server.start();
			return;
		}

		if (content.matches("off")) {
			if (server.active) message.addReaction("✅").queue();
			else message.addReaction("⁉").queue();
			server.stop();
			return;
		}

		if (content.matches("help")) {
			message.reply(
					"**vAlexa MAN:**\n"
					+ "`!on` start chaos with default values\n"
					+ "`!off` stop chaos\n"
					+ "`!set [delay] [chances]` restart chaos with custom values\n"
					+ "`delay`: a number in seconds, `chances`: a number between 0 and 1\n"
					+ "`default values: [300] [0.25]`\n"
					+ "`!666` **hell mode - use with caution!**")
					.queue();
		}
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
