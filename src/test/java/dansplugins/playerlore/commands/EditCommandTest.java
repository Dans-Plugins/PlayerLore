package dansplugins.playerlore.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EditCommandTest {

    private EditCommand editCommand;
    private Player player;
    private PlayerInventory inventory;
    private ItemStack item;
    private ItemMeta itemMeta;

    @BeforeEach
    public void setUp() {
        editCommand = new EditCommand();
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
    public void execute_editsFirstLoreLineUsing1BasedIndex() {
        List<String> lore = new ArrayList<>(Arrays.asList("original line"));
        when(itemMeta.getLore()).thenReturn(lore);

        boolean result = editCommand.execute(player, new String[]{"1", "\"new line\""});

        assertTrue(result);
        verify(itemMeta).setLore(Arrays.asList(ChatColor.WHITE + "new line"));
    }

    @Test
    public void execute_rejectsIndexBelow1() {
        List<String> lore = new ArrayList<>(Arrays.asList("original line"));
        when(itemMeta.getLore()).thenReturn(lore);

        boolean result = editCommand.execute(player, new String[]{"0", "\"new line\""});

        assertFalse(result);
        verify(player).sendMessage(anyString());
    }

    @Test
    public void execute_rejectsIndexBeyondLoreSize() {
        List<String> lore = new ArrayList<>(Arrays.asList("original line"));
        when(itemMeta.getLore()).thenReturn(lore);

        boolean result = editCommand.execute(player, new String[]{"2", "\"new line\""});

        assertFalse(result);
    }

    @Test
    public void execute_rejectsNonNumericIndex() {
        boolean result = editCommand.execute(player, new String[]{"abc", "\"new line\""});

        assertFalse(result);
    }

    @Test
    public void execute_rejectsMissingArgs() {
        boolean result = editCommand.execute(player, new String[]{});

        assertFalse(result);
    }
}
