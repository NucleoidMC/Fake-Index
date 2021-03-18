package io.github.haykam821.fakeindex.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.haykam821.fakeindex.ui.FakeIndexUi;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class FakeIndexCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager
			.literal("fakeindex")
			.requires(source -> {
				return source.hasPermissionLevel(2);
			})
			.executes(context -> FakeIndexCommand.openFakeIndex(context, 1))
			.then(CommandManager.argument("count", IntegerArgumentType.integer(1, 64))
				.executes(context -> FakeIndexCommand.openFakeIndex(context, IntegerArgumentType.getInteger(context, "count")))));
	}

	private static int openFakeIndex(CommandContext<ServerCommandSource> context, int count) throws CommandSyntaxException {
		FakeIndexUi ui = new FakeIndexUi(count);
		ui.open(context.getSource().getPlayer());

		return Command.SINGLE_SUCCESS;
	}
}
