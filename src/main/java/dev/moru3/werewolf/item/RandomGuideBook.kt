package dev.moru3.werewolf.item

import dev.moru3.minepie.Executor.Companion.runTaskLater
import dev.moru3.minepie.item.Item
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.Werewolf
import dev.moru3.werewolf.event.WerewolfPlayerInteractEvent
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack

class RandomGuideBook: AbstractShopItem(Role.MADMAN) {
    override val item: ItemStack
        get() = Item(Material.BOOK, "Random Guide Book", listOf("${ChatColor.GRAY}誰が人狼化を一匹だけ教えてくれる攻略本"))

    override val price: Int = 800

    override fun onClick(event: WerewolfPlayerInteractEvent) {
        val wolfs = event.playerData.game.players.filter { it.value.role == Role.WOLF }.keys.map { Bukkit.getPlayer(it) }.filterNotNull()
        if(wolfs.isEmpty()) {
            event.player.sendMessage("人狼がいないため使用できない...")
        } else {
            event.item!!.amount--
            event.player.playSound(event.player, Sound.BLOCK_PORTAL_TRIGGER, 0.3F, 2F)
            event.player.sendTitle("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.RED}${ChatColor.BOLD}Searching... ${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~","誰が人狼化を一匹だけ教えてくれます。",20,60,20)

            Werewolf.INSTANCE.runTaskLater(30) {
                val wolf = wolfs.random()
                event.player.sendMessage("${ChatColor.RED}[Random Guide Book] ${wolf.name}が人狼の中のの一人です。")
                event.player.sendTitle("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.RED}${ChatColor.BOLD}Result ${ChatColor.RED}${ChatColor.BOLD}${ChatColor.MAGIC}~","${wolf.name}が人狼の中のの一人です。",20,100,20)
            }
        }
    }
}