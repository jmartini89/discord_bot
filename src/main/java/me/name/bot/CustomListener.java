package me.name.bot;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static net.dv8tion.jda.api.entities.ChannelType.PRIVATE;

public class CustomListener extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
		ChannelType channel_type = event.getChannelType();
		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (channel_type == PRIVATE) {
			event.getAuthor().openPrivateChannel().queue( (act) -> {
				act.sendMessage("ué").queue(); } );
			return;
		}

		if (content.contains("kick")
				|| content.contains("random")
				|| content.contains("pinterest")
				|| content.contains("104")
				|| content.contains("199")) {
			message.addReaction("♿").queue();
			Roulette.roulette(event.getJDA(), event.getGuild().getIdLong());
		}
	}
}
