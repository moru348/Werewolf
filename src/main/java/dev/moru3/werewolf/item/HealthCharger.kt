package dev.moru3.werewolf.item

import dev.moru3.minepie.item.EasyItem
import dev.moru3.werewolf.Game
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.Werewolf
import dev.moru3.werewolf.event.WerewolfPlayerInteractEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class HealthCharger: AbstractShopItem(Role.DOCTOR) {
    override val item: ItemStack
        get() = EasyItem(Material.REDSTONE_ORE,"${ChatColor.RED}ヘルスチャージャー", listOf("${ChatColor.GRAY}置くことで周囲3ブロック以内にいるプレイヤーの体力を回復する。"))

    override val price: Int = 400

    val locations = mutableMapOf<Location,Game>()

    override fun onClick(event: WerewolfPlayerInteractEvent) {
        event.item!!.amount--
        val location = event.clickedBlock!!.location.clone().add(event.blockFace.modX.toDouble(),event.blockFace.modY.toDouble(),event.blockFace.modZ.toDouble())
        location.block.type = Material.REDSTONE_ORE
        locations[location] = event.playerData.game
    }

    init {
        Bukkit.getScheduler().runTaskTimer(Werewolf.INSTANCE, Runnable {
            locations.forEach { (location, game) ->
                game.players.values.mapNotNull { it.player }.filter { location.distance(it.location) < 3 }.forEach {
                    it.health = minOf(it.health+1,it.healthScale)
                    it.sendTitle("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.RED}${ChatColor.BOLD}Healing... ${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~","ヘルスチャージャーで体力を回復しています。",0,1,30)
                }
            }
        }, 0, 30)
    }
}