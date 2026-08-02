package dansplugins.playerlore.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RemoveCommandTest {

    private RemoveCommand removeCommand;
    private Player player;
    private PlayerInventory inventory;
    private ItemStack item;
    private ItemMeta itemMeta;

    @BeforeEach
    public void setUp() {
        removeCommand = new RemoveCommand();
        player = mock(Player.class);
        inventory = mock(PlayerInventory.class);
        item = mock(ItemStack.class);
        itemMeta = mock(ItemMeta.class);

        when(player.getInventory()).thenReturn(inventory);
        when(inventory.getItemInMainHand()).thenReturn(item);
        when(item.getType()).thenReturn(Material.DIAMOND_SWORD);
        when(item.getItemMeta()).thenReturn(itemMeta);
    }

    @Test
    public void execute_removesFirstLoreLineUsing1BasedIndex() {
        List<String> lore = new ArrayList<>(Arrays.asList("first line", "second line"));
        when(itemMeta.getLore()).thenReturn(lore);

        boolean result = removeCommand.execute(player, new String[]{"1"});

        assertTrue(result);
        verify(itemMeta).setLore(Arrays.asList("second line"));
    }

    @Test
    public void execute_rejectsIndexBelow1() {
        List<String> lore = new ArrayList<>(Arrays.asList("first line"));
        when(itemMeta.getLore()).thenReturn(lore);

        boolean result = removeCommand.execute(player, new String[]{"0"});

        assertFalse(result);
    }

    @Test
    public void execute_rejectsIndexBeyondLoreSize() {
        List<String> lore = new ArrayList<>(Arrays.asList("first line"));
        when(itemMeta.getLore()).thenReturn(lore);

        boolean result = removeCommand.execute(player, new String[]{"2"});

        assertFalse(result);
    }

    @Test
    public void execute_rejectsNonNumericIndex() {
        boolean result = removeCommand.execute(player, new String[]{"abc"});

        assertFalse(result);
    }

    @Test
    public void execute_rejectsMissingArgs() {
        boolean result = removeCommand.execute(player, new String[]{});

        assertFalse(result);
    }
}
