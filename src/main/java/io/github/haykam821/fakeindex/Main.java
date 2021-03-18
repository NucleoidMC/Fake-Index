package io.github.haykam821.fakeindex;

import io.github.haykam821.fakeindex.command.FakeIndexCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			FakeIndexCommand.register(dispatcher);
		});
	}
}