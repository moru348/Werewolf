package dev.moru3.werewolf.item

import dev.moru3.minepie.item.EasyItem
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.Werewolf
import dev.moru3.werewolf.event.WerewolfPlayerInteractEvent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack

class SelfBomb: AbstractShopItem(Role.WOLF) {
    override val item: ItemStack
        get() = EasyItem(Material.TNT,"${ChatColor.DARK_RED}自爆", listOf("${ChatColor.GRAY}半径5mの範囲を巻き込んで自爆するやばい爆弾"))

    override val price: Int = 1000

    override fun onClick(event: WerewolfPlayerInteractEvent) {
        event.player.inventory.itemInMainHand.amount--
        event.player.world.spawnParticle(Particle.EXPLOSION_LARGE,event.player.location,100)
        event.player.world.playSound(event.player.location,Sound.ENTITY_GENERIC_EXPLODE,1F,1F)
        event.playerData.game.players.values.mapNotNull { it.player }.filter { player -> Werewolf.INSTANCE.gameInstances.any { it.players.containsKey(player.uniqueId) } }.filter { event.player.location.distance(it.location) < 5 }.forEach {
            it.health = 0.0
        }
    }
}