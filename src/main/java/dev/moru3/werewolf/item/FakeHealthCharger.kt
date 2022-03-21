package dev.moru3.werewolf.item

import dev.moru3.minepie.item.Item
import dev.moru3.werewolf.Game
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.Team
import dev.moru3.werewolf.Werewolf
import dev.moru3.werewolf.event.WerewolfPlayerInteractEvent
import org.bukkit.*
import org.bukkit.inventory.ItemStack

class FakeHealthCharger: AbstractShopItem(Role.WOLF) {
    override val item: ItemStack
        get() = Item(Material.REDSTONE_ORE,"${ChatColor.DARK_RED}ヘルスチャージャー", listOf("${ChatColor.GRAY}置くことで周囲3ブロック以内にいるプレイヤーにダメージを与える","${ChatColor.GRAY}人狼陣営の場合は受けるダメージが少ない。"))

    override val price: Int = 800

    val locations = mutableMapOf<Location,Game>()

    override fun onClick(event: WerewolfPlayerInteractEvent) {
        event.item!!.amount--
        if(event.clickedBlock==null) { return }
        val location = event.clickedBlock!!.location.clone().add(event.blockFace.modX.toDouble(),event.blockFace.modY.toDouble(),event.blockFace.modZ.toDouble())
        location.block.type = Material.REDSTONE_ORE
        locations[location] = event.playerData.game
    }

    init {
        Bukkit.getScheduler().runTaskTimer(Werewolf.INSTANCE, Runnable {
            locations.forEach { (location, game) ->
                game.players.values.filter { it.role.team==Team.VILLAGE }.mapNotNull { it.player }.filter { location.distance(it.location) < 3 }.forEach {
                    it.damage(minOf(8.0,it.health))
                    it.sendTitle("${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.DARK_RED}${ChatColor.BOLD}Damage!! ${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.MAGIC}~","偽物のヘルスチャージャーのようだ。",0,1,30)
                    it.playSound(it, Sound.ENTITY_PLAYER_ATTACK_CRIT,1F,1F)
                }
                game.players.values.filter { it.role.team==Team.WOLF }.mapNotNull { it.player }.filter { location.distance(it.location) < 3 }.forEach {
                    it.health -= minOf(1.0,it.healthScale)
                    it.sendTitle("${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.DARK_RED}${ChatColor.BOLD}Damage!! ${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.MAGIC}~","偽物のヘルスチャージャーのようだ。",0,1,30)
                    it.playSound(it, Sound.ENTITY_PLAYER_ATTACK_CRIT,1F,1F)
                }
            }
        }, 0, 30)
    }
}