package dev.moru3.werewolf.item

import dev.moru3.minepie.Executor.Companion.runTaskLater
import dev.moru3.minepie.item.Item
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.Werewolf
import dev.moru3.werewolf.event.WerewolfPlayerInteractEvent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.Potion
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class LightningRod: AbstractShopItem(Role.WOLF) {
    override val item: ItemStack
        get() = Item(Material.LIGHTNING_ROD,"${ChatColor.DARK_RED}停電", listOf("${ChatColor.GRAY}人狼以外のプレイヤーに盲目をつける"))

    override val price: Int = 400

    override fun onClick(event: WerewolfPlayerInteractEvent) {
        event.player.inventory.itemInMainHand.amount--
        event.playerData.game.players.values.mapNotNull { it.player }.forEach {
            it.playSound(it, Sound.ENTITY_LIGHTNING_BOLT_THUNDER,1F,2F)
            it.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION,310,1,false,false,false))
            it.sendTitle("${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.YELLOW}${ChatColor.BOLD}LIGHTNING ${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.MAGIC}~","人狼が停電を使用しました。",0,1,30)
        }
        Werewolf.INSTANCE.runTaskLater(10) {
            event.playerData.game.players.values.filter { it.role != Role.WOLF }.mapNotNull { it.player }.forEach {
                it.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS,300,3,false,false,false))
            }
        }
    }
}