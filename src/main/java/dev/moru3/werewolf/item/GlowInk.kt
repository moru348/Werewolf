package dev.moru3.werewolf.item

import dev.moru3.minepie.item.Item
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
        get() = Item(Material.GLOW_INK_SAC,"${ChatColor.GOLD}ピカピカインク", listOf("${ChatColor.GRAY}全員が発光するインク"))

    override val price: Int = 400

    override fun onClick(event: WerewolfPlayerInteractEvent) {
        event.item!!.amount--
        event.playerData.game.players.keys.mapNotNull { Bukkit.getPlayer(it) }.forEach {
            it.playSound(it,Sound.ENTITY_BEE_STING,2F,0F)
            it.addPotionEffect(PotionEffect(PotionEffectType.GLOWING,300,1,false,false,false))
            it.sendTitle("${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.YELLOW}${ChatColor.BOLD}Glowing... ${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.MAGIC}~","ピッカピカに体が光ってます。",0,100,20)
        }
    }
}