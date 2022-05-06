package dev.moru3.werewolf.item

import dev.moru3.minepie.events.EventRegister.Companion.registerEvent
import dev.moru3.minepie.item.EasyItem
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.Werewolf
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack

class BombBall: AbstractShopItem(Role.WOLF) {
    override val item: ItemStack
        get() = EasyItem(Material.SNOWBALL,"${ChatColor.GOLD}爆発玉", listOf("${ChatColor.GRAY}投げることで爆発し、周囲に大ダメージを与える。だいたい死ぬ。"))

    override val price: Int = 1000


    init {
        Werewolf.INSTANCE.registerEvent<ProjectileLaunchEvent> {
            val player = this.entity.shooter
            if(player is Player) {
                if(player.inventory.itemInMainHand.itemMeta?.displayName == item.itemMeta?.displayName) {
                    this.entity.addScoreboardTag("morutan_bomb")
                }
            }
        }
        Werewolf.INSTANCE.registerEvent<ProjectileHitEvent> {
            if(this.entity.scoreboardTags.contains("morutan_bomb")) {
                this.entity.world.spawnParticle(Particle.EXPLOSION_LARGE,this.entity.location,30)
                this.entity.world.playSound(this.entity.location,Sound.ENTITY_GENERIC_EXPLODE,1F,1F)
                Bukkit.getOnlinePlayers().filter { it.location.distance(this.entity.location) < 3 }.forEach {
                    it.damage(minOf(19.0,it.health))
                }
            }
        }
    }
}