package me.name.bot;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
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

		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (direct_msg(event, event.getChannelType())) return;

		Server server = Bot.serverMap.get(event.getGuild().getIdLong());
		AdminCommand.admin_command(event, server, message, content.substring(1));
		UserTroll.user_troll(event, server, message, content);
	}

	static private boolean direct_msg(MessageReceivedEvent event, ChannelType channel_type) {
		if (channel_type != PRIVATE) return false;
		event.getAuthor().openPrivateChannel().queue(
				act -> act.sendMessage("ué").queue());
		return true;
	}
}
