package dev.moru3.werewolf

import net.md_5.bungee.api.ChatColor

enum class Role(val displayName: String, val color: ChatColor = ChatColor.WHITE, val description: String, val importance: Int, val team: Team) {
    WOLF("人狼", ChatColor.DARK_RED, "市民を全滅させよう", 100, Team.WOLF),
    VILLAGER("村人", ChatColor.GOLD, "能力を持たない。最後まで生き残ろう", 110, Team.VILLAGE),
    SEER("占い師", ChatColor.LIGHT_PURPLE, "アイテムを使用してそのプレイヤーが人狼かどうかを占えます。", 90, Team.VILLAGE),
    DOCTOR("医者", ChatColor.AQUA, "アイテムを使用することで他人を回復できます。", 80, Team.VILLAGE),
    MEDIUM("霊媒師", ChatColor.DARK_PURPLE, "死体と話そう(仮)", 60, Team.VILLAGE),
    MADMAN("狂人", ChatColor.DARK_RED, "市民陣営として立ち回りながらも、人狼と協力しよう。", 70, Team.WOLF),

}

enum class Team(val displayName: String) {
    VILLAGE("村人"),
    WOLF("人狼")
}