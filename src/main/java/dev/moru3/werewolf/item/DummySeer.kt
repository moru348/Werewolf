package dev.moru3.werewolf.item

import dev.moru3.minepie.item.Item
import dev.moru3.werewolf.Role
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class DummySeer: AbstractShopItem(Role.MADMAN) {
    override val item: ItemStack
        get() = Item(Material.MUSIC_DISC_WAIT,"ダミーアイテム（占い師）", listOf("${ChatColor.GRAY}偽物のダミーアイテム。占い師になりすませる。"))

    override val price: Int = 200
}