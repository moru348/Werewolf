package dev.moru3.werewolf.item

import dev.moru3.minepie.item.EasyItem
import dev.moru3.werewolf.Werewolf
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class InvisibleBall: AbstractShopItem() {
    override val item: ItemStack
        get() = EasyItem(Material.SLIME_BALL,"${ChatColor.WHITE}透明玉: 15.0秒", listOf("${ChatColor.GRAY}手に持ってる間だけ透明になります。"))

    override val price: Int = 800

    val invisiblePlayers = mutableSetOf<Player>()

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
                        invisiblePlayers.add(player)
                        if(time < 0.0) {
                            item.amount = 0
                            return@s
                        }
                        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW,30,2,true,false,false))
                        player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY,30,2,true,false,false))
                        player.inventory.helmet = ItemStack(Material.AIR)
                        item.itemMeta = item.itemMeta?.also { it.setDisplayName("透明玉: ${time}秒") }
                        player.sendTitle("${ChatColor.GREEN}${ChatColor.BOLD}透明化中", "", 0,21,0)

                    } else {
                        if(invisiblePlayers.contains(player)) {
                            player.removePotionEffect(PotionEffectType.INVISIBILITY)
                            player.removePotionEffect(PotionEffectType.SLOW)
                            player.inventory.helmet = it.co?.getHelmet()
                        }
                    }
                }
            }
        }, 0, 20)
    }
}