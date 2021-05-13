package io.github.haykam821.fakeindex.ui;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.pb4.polymer.item.VirtualItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.nucleoid.plasmid.shop.ShopEntry;
import xyz.nucleoid.plasmid.shop.ShopUi;

public class FakeIndexUi {
	private static final Text TITLE = new TranslatableText("text.fakeindex.title");

	private List<Item> content = FakeIndexUi.getContent();
	private int count;

	public FakeIndexUi(int count) {
		this.count = count;
	}

	private void giveItem(Item item, ServerPlayerEntity player) {
		player.inventory.offerOrDrop(player.getServerWorld(), new ItemStack(item, this.count));
	}

	private ShopUi getShop() {
		return ShopUi.create(TITLE, builder -> {
			for (Item item : this.content) {
				ShopEntry entry = ShopEntry.ofIcon(item);
				entry.onBuy(player -> this.giveItem(item, player));
				entry.noCost();

				builder.add(entry);
			}
		});
	}

	public void open(ServerPlayerEntity player) {
		player.openHandledScreen(this.getShop());
	}

	private static List<Item> getContent() {
		return Registry.ITEM.getEntries().stream().sorted((first, second) -> {
			Identifier firstId = first.getKey().getValue();
			Identifier secondId = second.getKey().getValue();

			return firstId.compareTo(secondId);
		}).flatMap(entry -> {
			if (entry.getValue() instanceof VirtualItem) {
				return Stream.of(entry.getValue());
			}
			return Stream.empty();
		}).collect(Collectors.toList());
	}
}
