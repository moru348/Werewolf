package dev.moru3.werewolf

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Team

class PlayerData(val game: Game, val role: Role, player: Player) {
    private val scoreboardManager = Bukkit.getServer().scoreboardManager?:throw IllegalStateException("スコアボードの作成に失敗しました。")

    val uniqueId = player.uniqueId

    val scoreBoard = scoreboardManager.newScoreboard

    val objective = scoreBoard.registerNewObjective("werewolf", "dummy", "${ChatColor.RED}${ChatColor.BOLD}人狼${ChatColor.DARK_RED}${ChatColor.BOLD}PVP").also {
        it.displaySlot = DisplaySlot.SIDEBAR
    }

    val timeTeam: Team = scoreBoard.registerNewTeam("time").also {
        it.addEntry("${ChatColor.RED}残り時間: ")
    }

    val playerCounterTeam: Team = scoreBoard.registerNewTeam("playerCounter").also {
        it.addEntry("${ChatColor.RED}残り人数: ")
    }

    private val moneyTeam: Team = scoreBoard.registerNewTeam("money").also {
        it.addEntry("${ChatColor.YELLOW}所持金: ")
    }


    var money = 500
        set(value) {
            moneyTeam.suffix = "${value}円"
            field = value
        }


    val player: Player?
        get() = Bukkit.getPlayer(uniqueId)

    val offlinePlayer = Bukkit.getOfflinePlayer(uniqueId)

    var isAlive = true

    var co: Role? = null


    init {
        objective.getScore("${ChatColor.RED}残り時間: ").score = 9
        objective.getScore(" ").score = 8
        objective.getScore("${ChatColor.RED}残り人数: ").score = 7
        objective.getScore("  ").score = 6
        objective.getScore("${ChatColor.GRAY}あなたの役職: ${role.color}${role.displayName}").score = 5
        objective.getScore("${ChatColor.YELLOW}所持金: ").score = 4
        moneyTeam.suffix = "${money}円"
        playerCounterTeam.suffix = "${game.players.values.count { it.isAlive }}人"
        if(player.scoreboard.getTeam("hidetag")==null) {
            player.scoreboard.registerNewTeam("hidetag").also {
                it.addEntry(this.player?.name.toString())
                it.setOption(Team.Option.NAME_TAG_VISIBILITY,Team.OptionStatus.NEVER)
                it.setCanSeeFriendlyInvisibles(false)
            }
        } else {
            player.scoreboard.getTeam("hidetag")?.addEntry(this.player?.name.toString())
        }
    }
}