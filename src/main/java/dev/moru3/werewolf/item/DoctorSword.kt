package dev.moru3.werewolf.item

import dev.moru3.minepie.Executor.Companion.runTaskLater
import dev.moru3.minepie.events.EventRegister.Companion.registerEvent
import dev.moru3.minepie.item.Item
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.Werewolf
import dev.moru3.werewolf.event.WerewolfPlayerInteractEntityEvent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class DoctorSword: AbstractShopItem(Role.DOCTOR) {
    override val item: ItemStack
        get() = Item(Material.IRON_SWORD, "回復", listOf("${ChatColor.GRAY}プレイヤーを殴ると回復します。"))

    override val showInShop: Boolean = false

    override val price: Int = 0

    override fun onClickEntity(event: WerewolfPlayerInteractEntityEvent) {
        if(!event.playerData.game.players.containsKey(event.rightClicked.uniqueId)) { return }
        event.player.sendTitle("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.RED}${ChatColor.BOLD}回復 ${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~","${event.rightClicked.name}を回復しています。",0,1,20)
        (event.rightClicked as Player).sendTitle("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.RED}${ChatColor.BOLD}回復 ${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~","医者に回復されました。",0,1,20)
        (event.rightClicked as Player).health = minOf((event.rightClicked as Player).health + 3, (event.rightClicked as Player).healthScale)
    }

    init {
        Werewolf.INSTANCE.registerEvent<EntityDamageByEntityEvent> {
            val damagerData = Werewolf.INSTANCE.players[this.damager.uniqueId]?:return@registerEvent
            val playerData = Werewolf.INSTANCE.players[this.entity.uniqueId]?:return@registerEvent
            if(damagerData.player?.inventory?.itemInMainHand?.itemMeta?.displayName == (item.itemMeta?.displayName ?: UUID.randomUUID().toString())) {
                this.isCancelled = true
                damagerData.player?.sendTitle("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.RED}${ChatColor.BOLD}回復 ${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~","${playerData.offlinePlayer.name}を回復しています。",0,1,20)
                playerData.player?.sendTitle("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.RED}${ChatColor.BOLD}回復 ${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~","医者に回復されました。",0,1,20)
                playerData.player!!.health = minOf(playerData.player!!.health+3,playerData.player!!.healthScale)
            }
        }
    }
}