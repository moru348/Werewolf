package dev.moru3.werewolf.item

import dev.moru3.minepie.events.EventRegister.Companion.registerEvent
import dev.moru3.werewolf.PlayerData
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.Werewolf
import dev.moru3.werewolf.event.WerewolfPlayerInteractEntityEvent
import dev.moru3.werewolf.event.WerewolfPlayerInteractEvent
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

interface ShopItem {

    // アイテムのアイテム??。
    val item: ItemStack

    val displayName: String

    val showInShop: Boolean

    val description: List<String>

    // このアイテムを使用、所持できる役職
    val unique: List<Role>

    // PlayerInteractEventのやつ
    fun onClick(event: WerewolfPlayerInteractEvent)

    // PlayerInteractEntityEventのやつ
    fun onClickEntity(event: WerewolfPlayerInteractEntityEvent)

    val price: Int

    fun buy(playerData: PlayerData): Boolean
}