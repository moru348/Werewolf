package dev.moru3.werewolf.item

import dev.moru3.minepie.Executor.Companion.runTaskLater
import dev.moru3.minepie.Executor.Companion.runTaskTimer
import dev.moru3.minepie.item.EasyItem
import dev.moru3.werewolf.Japanese
import dev.moru3.werewolf.Werewolf
import dev.moru3.werewolf.event.WerewolfPlayerInteractEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class GlowInk: AbstractShopItem() {
    override val item: ItemStack
        get() = EasyItem(Material.GLOW_INK_SAC,"${ChatColor.GOLD}ピカピカインク", listOf("${ChatColor.GRAY}全員が発光するインク"))

    override val price: Int = 400

    override fun onClick(event: WerewolfPlayerInteractEvent) {
        if(event.player.activePotionEffects.map { it.type }.contains(PotionEffectType.BLINDNESS)) {
            event.player.sendMessage(Japanese.cantUseWhileBlindness)
            event.player.playSound(event.player,Sound.BLOCK_AZALEA_BREAK,1F,1F)
        } else {
            event.item!!.amount--
            val task = Werewolf.INSTANCE.runTaskTimer(0,20) {
                event.playerData.game.players.keys.mapNotNull { Bukkit.getPlayer(it) }.forEach {
                    it.playSound(it,Sound.ENTITY_BEE_STING,2F,0F)
                    if(it.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        it.removePotionEffect(PotionEffectType.INVISIBILITY)
                    } else {
                        it.addPotionEffect(PotionEffect(PotionEffectType.GLOWING,100,1,false,false,false))
                    }
                    it.sendTitle("${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.YELLOW}${ChatColor.BOLD}Glowing... ${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.MAGIC}~","ピッカピカに体が光ってます。",0,100,20)
                }
            }
            Werewolf.INSTANCE.runTaskLater(20*10) {
                task.cancel()
            }
        }
    }
}