package net.camotoy.geyserhacks;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.inventory.InventoryType;
import org.geysermc.floodgate.api.FloodgateApi;

public final class SweepingEdgeFix implements Listener 
{
	private final Plugin plugin;
	
    public SweepingEdgeFix(Plugin plugin) {
        this.plugin = plugin;
    }

    /*
     * TBYT adds Sweeping Edge fix.
     * This adds unbreaking 1 to sweeping edge items if they only have the 1 enchant(sweeping edge)
     * If has sweeping edge and another enchant, or after applying unbreaking fix, wil update item name to sweeping edge and the enchantment level.
     */
    @EventHandler
    public void findEnchant(InventoryClickEvent event) 
    {
		Player player = (Player) event.getWhoClicked();
		//Checking for floodgate/geyser player.
        if (FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) 
        {
        	//Inventory becomes null for some reason after player clicks on item then drops it out their inventory.
			if(event.getClickedInventory()!=null)
			{
				if (event.getClickedInventory().getType() == InventoryType.PLAYER) 
				{
					ItemStack item = event.getCurrentItem();
					if(item!=null) //rare case this equals null
					{
						if (item.getType().equals(Material.DIAMOND_SWORD)
								|| item.getType().equals(Material.NETHERITE_SWORD) || item.getType().equals(Material.IRON_SWORD)
								|| item.getType().equals(Material.GOLDEN_SWORD) || item.getType().equals(Material.STONE_SWORD)
								|| item.getType().equals(Material.WOODEN_SWORD)) 
						{
							ItemMeta meta = item.getItemMeta();
							if (meta.hasEnchant(Enchantment.SWEEPING_EDGE)) 
							{
								int sweepingLevel = meta.getEnchantLevel(Enchantment.SWEEPING_EDGE);
								String displayName = item.getType().name();
								int charAt = displayName.indexOf("_");
								meta.setDisplayName("Sweeping Edge " + sweepingLevel);
								if(meta.getEnchants().size()==1)
								{
									meta.addEnchant(Enchantment.DURABILITY, 1, false);
									player.sendMessage("Sweeping Edge Fixed on "+displayName.substring(0,1).toUpperCase()+displayName.substring(1, charAt).toLowerCase()+" Sword.");
								}
								item.setItemMeta(meta);
								event.setCurrentItem(item);
							}
						}
						else if(item.getType().equals(Material.ENCHANTED_BOOK))
						{
							EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
							if (meta.hasStoredEnchant(Enchantment.SWEEPING_EDGE)) 
							{
								int sweepingLevel = meta.getStoredEnchantLevel(Enchantment.SWEEPING_EDGE);
								meta.setDisplayName("Sweeping Edge " + sweepingLevel);
								if(meta.getStoredEnchants().size()==1)
								{
									meta.addStoredEnchant(Enchantment.DURABILITY, 1, false);
									player.sendMessage("Sweeping Edge Fixed on Enchanted Book.");
								}
								item.setItemMeta(meta);
								event.setCurrentItem(item);
							}
						}
					}
				}
			}
        }
    }
}