package dev.moru3.werewolf.item

import dev.moru3.minepie.item.EasyItem
import dev.moru3.werewolf.event.WerewolfPlayerInteractEvent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class InstantHealthPotion: AbstractShopItem() {
    override val item: ItemStack
        get() = EasyItem(Material.REDSTONE, "${ChatColor.GREEN}即時回復のポーション", listOf("${ChatColor.GRAY}粉タイプの即時回復の粉。体力が全回復する。"))

    override val price: Int = 600

    override fun onClick(event: WerewolfPlayerInteractEvent) {
        event.player.inventory.itemInMainHand.amount--
        event.player.sendTitle("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.RED}${ChatColor.BOLD}HEALING! ${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~", "体力を100%回復しました。", 0, 60, 20)
        event.player.health = event.player.healthScale
    }
}