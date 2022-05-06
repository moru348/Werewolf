package dev.moru3.werewolf.item

import dev.moru3.minepie.item.EasyItem
import dev.moru3.werewolf.Role
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class DummySeer: AbstractShopItem(Role.MADMAN) {
    override val item: ItemStack
        get() = EasyItem(Material.MUSIC_DISC_WAIT,"ダミーアイテム（占い師）", listOf("${ChatColor.GRAY}偽物のダミーアイテム。占い師になりすませる。"))

    override val price: Int = 200
}