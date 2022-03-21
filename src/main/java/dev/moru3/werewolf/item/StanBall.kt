package dev.moru3.werewolf.item

import dev.moru3.minepie.Executor.Companion.runTaskLater
import dev.moru3.minepie.events.EventRegister.Companion.registerEvent
import dev.moru3.minepie.item.Item
import dev.moru3.werewolf.Werewolf
import dev.moru3.werewolf.event.WerewolfPlayerInteractEvent
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack
import java.util.UUID

class StanBall: AbstractShopItem() {
    override val item: ItemStack
        get() = Item(Material.SNOWBALL,"${ChatColor.GOLD}カラーボール", listOf("${ChatColor.GRAY}当たると4秒間スタンするボール"))

    override val price: Int = 400

    private val stanPlayers = mutableListOf<UUID>()

    init {
        Werewolf.INSTANCE.registerEvent<ProjectileLaunchEvent> {
            val player = this.entity.shooter
            if(player is Player) {
                if(player.inventory.itemInMainHand.itemMeta?.displayName == item.itemMeta?.displayName) {
                    this.entity.addScoreboardTag("morutan_stan")
                }
            }
        }
        Werewolf.INSTANCE.registerEvent<PlayerMoveEvent> {
            if(stanPlayers.contains(this.player.uniqueId)) {
                if(this.player.gameMode==GameMode.SPECTATOR||this.player.gameMode==GameMode.CREATIVE) {
                    stanPlayers.remove(this.player.uniqueId)
                } else {
                    this.isCancelled = true
                }
            }
        }
        Werewolf.INSTANCE.registerEvent<ProjectileHitEvent> {
            if(this.entity.scoreboardTags.contains("morutan_stan")) {
                this.entity.world.spawnParticle(Particle.EXPLOSION_LARGE,this.entity.location,30)
                this.entity.world.playSound(this.entity.location,Sound.ENTITY_GENERIC_EXPLODE,1F,1F)
                Bukkit.getOnlinePlayers().filter { it.location.distance(this.entity.location) < 3 }.forEach {
                    stanPlayers.add(it.uniqueId)
                    it.playSound(it, Sound.ITEM_TOTEM_USE, 1F,0.7F)
                    it.sendTitle("${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.DARK_RED}${ChatColor.BOLD}STAN! ${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.MAGIC}~", "数秒間動けなくなります", 0, 80, 20)
                    Werewolf.INSTANCE.runTaskLater(80) { stanPlayers.remove(it.uniqueId) }
                    Werewolf.INSTANCE.runTaskLater(81) { stanPlayers.remove(it.uniqueId) }
                    Werewolf.INSTANCE.runTaskLater(82) { stanPlayers.remove(it.uniqueId) }
                }
            }
        }
    }
}