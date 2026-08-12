package dansplugins.playerlore.commands;

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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddCommandTest {

    private AddCommand addCommand;
    private Player player;
    private PlayerInventory inventory;
    private ItemStack item;
    private ItemMeta itemMeta;

    @BeforeEach
    public void setUp() {
        addCommand = new AddCommand();
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
    public void execute_addsLoreLineWhenItemHasNoExistingLore() {
        when(itemMeta.getLore()).thenReturn(null);

        boolean result = addCommand.execute(player, new String[]{"\"new line\""});

        assertTrue(result);
        verify(itemMeta).setLore(Arrays.asList(ChatColor.WHITE + "new line"));
    }

    @Test
    public void execute_appendsLoreLineWhenItemHasExistingLore() {
        List<String> lore = new ArrayList<>(Arrays.asList("original line"));
        when(itemMeta.getLore()).thenReturn(lore);

        boolean result = addCommand.execute(player, new String[]{"\"new line\""});

        assertTrue(result);
        verify(itemMeta).setLore(Arrays.asList("original line", ChatColor.WHITE + "new line"));
    }

    @Test
    public void execute_rejectsMissingArgs() {
        boolean result = addCommand.execute(player, new String[]{});

        assertFalse(result);
        verify(player).sendMessage(ChatColor.RED + "Usage: /pl add \"line of lore\"");
    }

    @Test
    public void execute_rejectsArgsWithoutDoubleQuotes() {
        boolean result = addCommand.execute(player, new String[]{"unquoted", "line"});

        assertFalse(result);
        verify(player).sendMessage(ChatColor.RED + "Line of lore must be designated between double quotes.");
    }

    @Test
    public void execute_rejectsWhenNotHoldingAnItem() {
        when(item.getType()).thenReturn(Material.AIR);

        boolean result = addCommand.execute(player, new String[]{"\"new line\""});

        assertFalse(result);
    }

    @Test
    public void execute_rejectsNonPlayerSender() {
        CommandSender commandSender = mock(CommandSender.class);

        boolean result = addCommand.execute(commandSender, new String[]{"\"new line\""});

        assertFalse(result);
    }

    @Test
    public void execute_noArgs_sendsUsageMessage() {
        boolean result = addCommand.execute(player);

        assertFalse(result);
        verify(player).sendMessage(anyString());
    }
}
