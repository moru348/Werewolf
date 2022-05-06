package dev.moru3.werewolf.item

import dev.moru3.minepie.item.EasyItem
import dev.moru3.werewolf.Role
import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class MediumItem: AbstractShopItem(Role.MEDIUM) {
    override val item: ItemStack
        get() = EasyItem(Material.MUSIC_DISC_CHIRP,"霊媒", listOf("${ChatColor.GRAY}持っていると一個消費して死んだ人の霊媒を行えます。"))

    override val price: Int = 200
}