package me.name.bot;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import java.time.LocalTime;

import static net.dv8tion.jda.api.Permission.ADMINISTRATOR;
import static net.dv8tion.jda.api.entities.ChannelType.PRIVATE;

public class CustomListener extends ListenerAdapter {
	@Override
	public void onGuildReady(@NotNull GuildReadyEvent event) {
		Bot.serverMap.put(event.getGuild().getIdLong(), new Server(event.getGuild()));
	}

	@Override
	public void onGuildJoin(@Nonnull GuildJoinEvent event) {
		Bot.serverMap.put(event.getGuild().getIdLong(), new Server(event.getGuild()));
	}

	@Override
	public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
		Bot.serverMap.remove(event.getGuild().getIdLong());
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
		Server server = Bot.serverMap.get(guild.getIdLong());

		if (member != null && member.hasPermission(ADMINISTRATOR) && content.startsWith("!"))
			AdminCommand.admin_command(guild, message, content.substring(1));

		user_chaos(event, server, guild, message, content);
	}

	static private void user_chaos(
			MessageReceivedEvent event, Server server, Guild guild, Message message, String content) {
		if (content.contains("contatto")
				|| content.contains("random")
				|| content.contains("pinterest")
				|| content.contains("104")
				|| content.contains("199")) {
			if (!LocalTime.now().isAfter(server.spam_time.plusSeconds(60L))) {
				event.getAuthor().openPrivateChannel().queue(
						act -> act.sendMessage("https://youtu.be/6lA3T78o9pY").queue());
				message.delete().queue();
				return;
			}
			if (guild.getIdLong() == 823664543628001400L) message.addReaction("♿").queue();
			else message.addReaction("\uD83C\uDF00").queue();
			server.spam_time = LocalTime.now();
			Roulette.roulette(Bot.serverMap.get(guild.getIdLong()), false);
		}
	}
}
