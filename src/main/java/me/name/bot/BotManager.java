package me.name.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static net.dv8tion.jda.api.entities.ChannelType.PRIVATE;

public class BotManager extends ListenerAdapter {
	static Map<Long, Server> serverMap;
	static AudioPlayerManager playerManager;

	public BotManager() {
		serverMap = new HashMap<>();

		playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}

	private void newGuild(@NotNull GenericGuildEvent event) {
		Server server = new Server(event.getGuild(), playerManager);
		serverMap.put(event.getGuild().getIdLong(), server);
	}

	private boolean direct_msg(MessageReceivedEvent event, ChannelType channel_type) {
		if (channel_type != PRIVATE) return false;
		event.getAuthor().openPrivateChannel().queue(
				act -> act.sendMessage("ué").queue());
		return true;
	}

	@Override
	public void onGuildReady(@NotNull GuildReadyEvent event)  {
		newGuild(event);
	}

	@Override
	public void onGuildJoin(@Nonnull GuildJoinEvent event) {
		newGuild(event);
	}

	@Override
	public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
		serverMap.get(event.getGuild().getIdLong()).stop();
		serverMap.get(event.getGuild().getIdLong()).player.destroy();
		serverMap.remove(event.getGuild().getIdLong());
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
		if (direct_msg(event, event.getChannelType())) return;

		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (!content.startsWith("!")) return;
		Server server = serverMap.get(event.getGuild().getIdLong());
		CmdAdmin.admin_command(event, server, message, content.substring(1));
		CmdUser.user_command(event, server, message, content.substring(1));
	}
}
