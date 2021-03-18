package io.github.haykam821.fakeindex.ui;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.nucleoid.plasmid.fake.FakeItem;
import xyz.nucleoid.plasmid.shop.ShopEntry;
import xyz.nucleoid.plasmid.shop.ShopUi;

public class FakeIndexUi {
	private static final Text TITLE = new TranslatableText("text.fakeindex.title");

	private List<FakeItem> content = FakeIndexUi.getContent();
	private int count;

	public FakeIndexUi(int count) {
		this.count = count;
	}

	private void giveItem(FakeItem item, ServerPlayerEntity player) {
		player.inventory.offerOrDrop(player.getServerWorld(), new ItemStack((Item) item, this.count));
	}

	private Text getIconName(FakeItem item, ItemStack stack) {
		return ((Item) item).getName(stack).shallowCopy().styled(style -> {
			return style.withItalic(false);
		});
	}

	private ItemStack getIcon(FakeItem item) {
		ItemStack stack = new ItemStack(item.asProxy(), this.count);
		stack.setCustomName(this.getIconName(item, stack));

		return stack;
	}

	private ShopUi getShop() {
		return ShopUi.create(TITLE, builder -> {
			for (FakeItem item : this.content) {
				ShopEntry entry = ShopEntry.ofIcon(this.getIcon(item));
				entry.onBuy(player -> this.giveItem(item, player));
				entry.noCost();

				builder.add(entry);
			}
		});
	}

	public void open(ServerPlayerEntity player) {
		player.openHandledScreen(this.getShop());
	}

	private static List<FakeItem> getContent() {
		return Registry.ITEM.getEntries().stream().sorted((first, second) -> {
			Identifier firstId = first.getKey().getValue();
			Identifier secondId = second.getKey().getValue();

			return firstId.compareTo(secondId);
		}).flatMap(entry -> {
			if (entry.getValue() instanceof FakeItem) {
				return Stream.of((FakeItem) entry.getValue());
			}
			return Stream.empty();
		}).collect(Collectors.toList());
	}
}
