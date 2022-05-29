package dev.moru3.werewolf

import dev.moru3.minepie.config.Config
import dev.moru3.werewolf.item.Items
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Werewolf : JavaPlugin() {
    val gameInstances = mutableListOf<Game>()
    val players = mutableMapOf<UUID, PlayerData>()
    lateinit var config: Config
    var isSingleton = true
    override fun onEnable() {
        INSTANCE = this
        config = Config(this, "config.yml")
        config.saveDefaultConfig()
        config.reloadConfig()
        Items.SWAP_LOSE
    }

    override fun onDisable() {
        gameInstances.forEach { it.end(Team.WOLF) }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) { return true }
        try {
            when(command.name) {
                "werewolf" -> {
                    when(args.getOrNull(0)) {
                        "start" -> {
                            Game(this).start(sender.location,args.toList().subList(1,args.size))
                        }
                        "reload" -> {
                            config.reloadConfig()
                        }
                        "test" -> {
                            sender.inventory.addItem(Items.STAN_BALL.item)
                        }
                        "money" -> {
                            gameInstances.getOrNull(0)?.players?.get(sender.uniqueId)?.money = args.getOrNull(1)?.toIntOrNull()?:0
                            gameInstances.forEach {
                                it.players.values.forEach { player ->
                                    player.player?.sendTitle("不正(${sender.name})","${args.getOrNull(1)?.toIntOrNull()?:0}円振り込まれました。", 0, 100, 10)
                                }
                            }
                        }
                    }
                }
                "medium" -> {
                    try {
                        val player = UUID.fromString(args.getOrNull(0))?:return true
                        sender.inventory.contents.forEach {
                            if(it==null) { return@forEach }
                            if(!it.hasItemMeta()) { return@forEach }
                            if(it.itemMeta?.displayName == (Items.MEDIUM_ITEM.item.itemMeta?.displayName ?: UUID.randomUUID().toString())) {
                                it.amount = 0
                                sender.sendMessage("${ChatColor.AQUA}${players[player]?.offlinePlayer?.name}は人狼${if(players[player]?.role==Role.WOLF) "です。" else "ではありません。"}")
                                return true
                            }
                        }
                    } catch (e: Exception) { e.printStackTrace();return true }
                }
            }
        } catch (e: Exception) {
            sender.sendMessage("${ChatColor.RED}${e.message}")
        }
        return super.onCommand(sender, command, label, args)
    }

    companion object {
        lateinit var INSTANCE: Werewolf
        var isCitizensAvailable = false
    }
}