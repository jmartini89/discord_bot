package me.name.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bot {
	static String src = "./audio/";
	static File folder;
	static List<String> file_tracks = new ArrayList<>();

	public static void main(String @NotNull [] args) throws Exception {
		customTracks();
		JDABuilder.createDefault(args[0])
			.setMemberCachePolicy(MemberCachePolicy.VOICE)
			.enableIntents(GatewayIntent.GUILD_MEMBERS)
			.addEventListeners(new BotManager())
			.setActivity(Activity.playing("samba"))
			.build()
			.awaitReady();
	}

	private static void customTracks() {
		try {
			folder = new File(src);
			for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
				if (fileEntry.isFile() && fileEntry.getName().endsWith(".mp3"))
					file_tracks.add(src + fileEntry.getName());
			}
		}
		catch (Exception e) { System.out.println("ERROR: Custom audio folder"); }
		if (!file_tracks.isEmpty()) System.out.println("LOADED: Custom audio files");
	}
}
