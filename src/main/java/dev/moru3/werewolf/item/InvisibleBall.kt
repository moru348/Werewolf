package dev.moru3.werewolf.item

import dev.moru3.minepie.item.Item
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.Werewolf
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class InvisibleBall: AbstractShopItem() {
    override val item: ItemStack
        get() = Item(Material.SLIME_BALL,"${ChatColor.WHITE}透明玉: 15.0秒", listOf("${ChatColor.GRAY}手に持ってる間だけ透明になります。"))

    override val price: Int = 200

    init {
        Bukkit.getScheduler().runTaskTimer(Werewolf.INSTANCE, Runnable {
            Werewolf.INSTANCE.gameInstances.forEach { game ->
                game.players.values.forEach s@{
                    val player = it.player?:return@s
                    val item = player.inventory.itemInMainHand
                    val displayName = item.itemMeta?.displayName?:return@s
                    if(displayName.contains("透明玉: ")) {
                        var time = ChatColor.stripColor(displayName.replace("透明玉: ","").replace("秒",""))?.toDoubleOrNull()?:return@s
                        time *= item.amount
                        item.amount = 1
                        time -= 1.0
                        if(time < 0.0) {
                            item.amount = 0
                            player.removePotionEffect(PotionEffectType.INVISIBILITY)
                            return@s
                        }
                        item.itemMeta = item.itemMeta?.also { it.setDisplayName("透明玉: ${time}秒") }
                        player.sendTitle("${ChatColor.GREEN}${ChatColor.BOLD}透明化中", "", 0,21,0)
                    }
                }
            }
        }, 0, 20)
    }
}