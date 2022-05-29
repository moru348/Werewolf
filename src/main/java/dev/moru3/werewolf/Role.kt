package dev.moru3.werewolf

import dev.moru3.minepie.item.EasyItem
import net.md_5.bungee.api.ChatColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

enum class Role(val displayName: String, val color: ChatColor = ChatColor.WHITE, val description: String, val importance: Int, val team: Team) {
    WOLF("人狼", ChatColor.DARK_RED, "市民を全滅させよう", 100, Team.WOLF),
    VILLAGER("村人", ChatColor.GOLD, "能力を持たない。最後まで生き残ろう", 110, Team.VILLAGE),
    SEER("占い師", ChatColor.LIGHT_PURPLE, "アイテムを使用してそのプレイヤーが人狼かどうかを占えます。", 90, Team.VILLAGE),
    DOCTOR("医者", ChatColor.AQUA, "アイテムを使用することで他人を回復できます。", 80, Team.VILLAGE),
    MEDIUM("霊媒師", ChatColor.DARK_PURPLE, "死体と話そう(仮)", 60, Team.VILLAGE),
    MADMAN("狂人", ChatColor.DARK_RED, "市民陣営として立ち回りながらも、人狼と協力しよう。", 70, Team.WOLF);

    fun getHelmet(): ItemStack {
        return EasyItem(Material.LEATHER_HELMET,"${this.color}${this.displayName}Coします。").also { item ->
            item.itemMeta = (item.itemMeta as LeatherArmorMeta).also { meta ->
                meta.setColor(Color.fromRGB(this.color.color.red,this.color.color.green,this.color.color.blue))
            }
        }
    }

}

enum class Team(val displayName: String,val color: ChatColor) {
    VILLAGE("村人", ChatColor.GOLD),
    WOLF("人狼", ChatColor.DARK_RED)
}