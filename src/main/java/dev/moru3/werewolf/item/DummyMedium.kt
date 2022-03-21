package dev.moru3.werewolf.item

import dev.moru3.minepie.item.Item
import dev.moru3.werewolf.Role
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class DummyMedium: AbstractShopItem(Role.MADMAN) {
    override val item: ItemStack
        get() = Item(Material.MUSIC_DISC_CHIRP,"ダミーアイテム（霊媒師）", listOf("${ChatColor.GRAY}ダミーアイテム。霊媒師になりすませる。"))

    override val price: Int = 200
}