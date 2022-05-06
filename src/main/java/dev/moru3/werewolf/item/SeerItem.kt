package dev.moru3.werewolf.item

import dev.moru3.minepie.Executor.Companion.runTaskLater
import dev.moru3.minepie.item.EasyItem
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.Werewolf
import dev.moru3.werewolf.event.WerewolfPlayerInteractEntityEvent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.UUID

class SeerItem: AbstractShopItem(Role.SEER) {
    override val item: ItemStack
        get() = EasyItem(Material.MUSIC_DISC_WAIT, "占い", listOf("${ChatColor.GRAY}プレイヤーを右クリックすることで人狼かどうかを占えます。"))

    override val price: Int = 400

    val players = mutableListOf<UUID>()

    override fun onClickEntity(event: WerewolfPlayerInteractEntityEvent) {
        val targetData = event.playerData.game.players[event.rightClicked.uniqueId]?:return
        if(players.contains(targetData.uniqueId)) { return }
        event.player.sendTitle("${ChatColor.AQUA}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.AQUA}${ChatColor.BOLD}占い中 ${ChatColor.AQUA}${ChatColor.BOLD}${ChatColor.MAGIC}~","${targetData.offlinePlayer.name}を占っています。",20,100,20)
        players.add(event.player.uniqueId)
        event.player.inventory.itemInMainHand.amount = 0
        Werewolf.INSTANCE.runTaskLater(100) {
            players.remove(event.player.uniqueId)
            event.player.sendMessage("${targetData.offlinePlayer.name}は${if(targetData.role==Role.WOLF) "人狼" else "村人"}です。")
            event.player.sendTitle("${ChatColor.GREEN}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.GREEN}${ChatColor.BOLD}結果 ${ChatColor.GREEN}${ChatColor.BOLD}${ChatColor.MAGIC}~","${targetData.offlinePlayer.name}は${if(targetData.role==Role.WOLF) "人狼" else "村人"}です。",20,100,20)
        }
    }
}