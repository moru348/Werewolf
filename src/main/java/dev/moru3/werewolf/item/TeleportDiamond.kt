package dev.moru3.werewolf.item

import dev.moru3.minepie.Executor.Companion.runTaskLater
import dev.moru3.minepie.Executor.Companion.runTaskTimer
import dev.moru3.minepie.item.EasyItem
import dev.moru3.werewolf.Game
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.Werewolf
import dev.moru3.werewolf.event.WerewolfPlayerInteractEvent
import org.bukkit.*
import org.bukkit.inventory.ItemStack
import java.util.*

class TeleportDiamond: AbstractShopItem(Role.WOLF) {
    override val item: ItemStack
        get() = EasyItem(Material.DIAMOND,"${ChatColor.AQUA}テレポートダイヤモンド", listOf("${ChatColor.GRAY}一度クリックしたところへ数回でも移動できるダイヤモンド"))

    override val price: Int = 400

    private val locations = mutableMapOf<UUID,Pair<Location,Game>>()

    override fun onClick(event: WerewolfPlayerInteractEvent) {
        if(locations.containsKey(event.player.uniqueId)&&locations[event.player.uniqueId]?.second==event.playerData.game) {
            event.item!!.amount--
            var count = 100/5
            val task = Werewolf.INSTANCE.runTaskTimer(0,20) {
                count--
                event.player.sendMessage("${count}秒前")
            }
            Werewolf.INSTANCE.runTaskLater(100) {
                event.player.teleport(locations[event.player.uniqueId]?.first?:return@runTaskLater)
                task.cancel()
            }
        } else {
            event.player.sendMessage("セット")
            event.player.playSound(event.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1F,1F)
            locations[event.player.uniqueId] = event.player.location to event.playerData.game
        }
    }
}