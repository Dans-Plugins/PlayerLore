package dansplugins.playerlore.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
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
    public void execute_editsOnlyTheTargetedLine() {
        List<String> lore = new ArrayList<>(Arrays.asList("first line", "second line", "third line"));
        when(itemMeta.getLore()).thenReturn(lore);

        boolean result = editCommand.execute(player, new String[]{"2", "\"new line\""});

        assertTrue(result);
        verify(itemMeta).setLore(Arrays.asList("first line", ChatColor.WHITE + "new line", "third line"));
    }

    @Test
    public void execute_writesMetaBackToItemAndConfirms() {
        List<String> lore = new ArrayList<>(Arrays.asList("original line"));
        when(itemMeta.getLore()).thenReturn(lore);

        editCommand.execute(player, new String[]{"1", "\"new line\""});

        verify(item).setItemMeta(itemMeta);
        verify(player).sendMessage(ChatColor.GREEN + "Line edited.");
    }

    @Test
    public void execute_rejectsItemWithNoLore() {
        when(itemMeta.getLore()).thenReturn(null);

        boolean result = editCommand.execute(player, new String[]{"1", "\"new line\""});

        assertFalse(result);
        verify(player).sendMessage(ChatColor.RED + "There aren't that many lines of lore.");
        verify(item, never()).setItemMeta(any());
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
        verify(player).sendMessage(ChatColor.RED + "There aren't that many lines of lore.");
        verify(item, never()).setItemMeta(any());
    }

    @Test
    public void execute_rejectsNonNumericIndex() {
        boolean result = editCommand.execute(player, new String[]{"abc", "\"new line\""});

        assertFalse(result);
        verify(player).sendMessage(ChatColor.RED + "Line index must be a number.");
    }

    @Test
    public void execute_rejectsArgsWithoutDoubleQuotes() {
        boolean result = editCommand.execute(player, new String[]{"1", "unquoted"});

        assertFalse(result);
        verify(player).sendMessage(ChatColor.RED + "Line of lore must be designated between double quotes.");
    }

    @Test
    public void execute_rejectsMissingArgs() {
        boolean result = editCommand.execute(player, new String[]{});

        assertFalse(result);
        verify(player).sendMessage(ChatColor.RED + "Usage: /pl edit (lineIndex) \"new line of lore\"");
    }

    @Test
    public void execute_rejectsWhenNotHoldingAnItem() {
        when(item.getType()).thenReturn(Material.AIR);

        boolean result = editCommand.execute(player, new String[]{"1", "\"new line\""});

        assertFalse(result);
        verify(player).sendMessage(ChatColor.RED + "You aren't holding anything.");
    }

    @Test
    public void execute_rejectsItemWithoutMeta() {
        when(item.getItemMeta()).thenReturn(null);

        boolean result = editCommand.execute(player, new String[]{"1", "\"new line\""});

        assertFalse(result);
        verify(player).sendMessage(ChatColor.RED + "That item's meta information wasn't found.");
    }

    @Test
    public void execute_rejectsNonPlayerSender() {
        CommandSender commandSender = mock(CommandSender.class);

        boolean result = editCommand.execute(commandSender, new String[]{"1", "\"new line\""});

        assertFalse(result);
        verify(commandSender).sendMessage("This command can only be used by a player.");
    }

    @Test
    public void execute_noArgs_sendsUsageMessage() {
        boolean result = editCommand.execute(player);

        assertFalse(result);
        verify(player).sendMessage(ChatColor.RED + "Usage: /pl edit (lineIndex) \"new line of lore\"");
    }
}
