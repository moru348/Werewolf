package dev.moru3.werewolf.item

import dev.moru3.werewolf.PlayerData
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.event.WerewolfPlayerInteractEntityEvent
import dev.moru3.werewolf.event.WerewolfPlayerInteractEvent
import net.md_5.bungee.api.ChatColor

abstract class AbstractShopItem(vararg unique: Role = Role.values()): ShopItem {
    override val unique: List<Role> = unique.toList()

    override val displayName: String
        get() = item.itemMeta?.displayName?:"アイテム"

    override val description: List<String>
        get() = item.itemMeta?.lore?: listOf()

    override val showInShop: Boolean = true

    override fun onClick(event: WerewolfPlayerInteractEvent) {
        /** require override **/
    }
    override fun onClickEntity(event: WerewolfPlayerInteractEntityEvent) {
        /** require override **/
    }

    override fun buy(playerData: PlayerData): Boolean {
        val player = playerData.player?:return false
        return if(playerData.money >= price) {
            playerData.money -= price
            player.inventory.addItem(item)
            player.sendMessage("${ChatColor.YELLOW}[SHOP] ${displayName} を購入しました。")
            true
        } else {
            player.sendMessage("${ChatColor.YELLOW}[SHOP] 所持金が足りません。")
            false
        }
    }

    init { Items.add(this) }
}