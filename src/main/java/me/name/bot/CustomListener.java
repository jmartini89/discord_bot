package me.name.bot;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static net.dv8tion.jda.api.Permission.ADMINISTRATOR;
import static net.dv8tion.jda.api.entities.ChannelType.PRIVATE;

public class CustomListener extends ListenerAdapter {
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		Member member = event.getMember();
		ChannelType channel_type = event.getChannelType();
		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (channel_type == PRIVATE) {
			event.getAuthor().openPrivateChannel().queue(
					act -> act.sendMessage("ué").queue());
			return;
		}

		Guild guild = event.getGuild();

		if (member != null && member.hasPermission(ADMINISTRATOR)) {
			if (chaos_command(guild, message, content)) return;
		}

		if (content.contains("kick")
				|| content.contains("random")
				|| content.contains("pinterest")
				|| content.contains("104")
				|| content.contains("199")) {
			if (guild.getIdLong() == 823664543628001400L) message.addReaction("♿").queue();
			else message.addReaction("\uD83C\uDF00").queue();
			Roulette.roulette(guild);
		}
	}

	private static boolean chaos_command(Guild guild, Message message, @NotNull String content) {
		if (content.matches("!ON!")) {
			if (Bot.serverList.contains(guild)) {
				message.addReaction("⁉").queue();
				return true;
			}
			Bot.serverList.add(guild);
			message.addReaction("✅").queue();
			return true;
		}
		if (content.matches("!OFF!")) {
			if (!Bot.serverList.contains(guild)) {
				message.addReaction("⁉").queue();
				return true;
			}
			Bot.serverList.remove(guild);
			message.addReaction("✅").queue();
			return true;
		}
		return false;
	}
}
