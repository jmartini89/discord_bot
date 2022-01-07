package me.name.bot;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static net.dv8tion.jda.api.Permission.ADMINISTRATOR;
import static net.dv8tion.jda.api.entities.ChannelType.PRIVATE;

public class CustomListener extends ListenerAdapter {
	@Override
	public void onGuildReady(@NotNull GuildReadyEvent event) {
		Bot.serverList.add(new Server(event.getGuild()));
	}

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
			if (AdminCommand.admin_command(guild, message, content)) return;
		}

		if (content.contains("contatto")
				|| content.contains("random")
				|| content.contains("pinterest")
				|| content.contains("104")
				|| content.contains("199")) {
			if (guild.getIdLong() == 823664543628001400L) message.addReaction("♿").queue();
			else message.addReaction("\uD83C\uDF00").queue();
			Roulette.roulette(guild);
		}
	}
}
