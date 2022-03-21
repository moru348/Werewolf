package dev.moru3.werewolf.item

import dev.moru3.minepie.events.EventRegister.Companion.registerEvent
import dev.moru3.minepie.item.Item
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.Werewolf
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class WolfAxe: AbstractShopItem(Role.WOLF) {
    override val item: ItemStack
        get() = Item(Material.STONE_AXE,"${ChatColor.GRAY}すごいおの", listOf("${ChatColor.GRAY}2秒間手に持つと一撃でプレイヤーを殺害できる強いおの。5秒持たないと効果がない")).also { item ->
            item.itemMeta = item.itemMeta.also { meta ->
                meta?.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, AttributeModifier("attack_speed",(1.0/4)-4,AttributeModifier.Operation.ADD_NUMBER))
            }
        }

    override val price: Int = 300

    val players = mutableMapOf<Player,Int>()

    init {

        Werewolf.INSTANCE.registerEvent<EntityDamageByEntityEvent> {
            val damagerData = Werewolf.INSTANCE.players[this.damager.uniqueId]?:return@registerEvent
            val playerData = Werewolf.INSTANCE.players[this.entity.uniqueId]?:return@registerEvent
            if((players[damagerData.player ?: return@registerEvent]
                    ?: 0) > 2 && damagerData.player?.inventory?.itemInMainHand?.itemMeta?.displayName == (item.itemMeta?.displayName
                    ?: UUID.randomUUID().toString())
            ) {
                this.isCancelled = true
                damagerData.player!!.inventory.itemInMainHand.amount--
                damagerData.player?.sendTitle("${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.DARK_RED}${ChatColor.BOLD}一撃の斧 ${ChatColor.DARK_RED}${ChatColor.BOLD}${ChatColor.MAGIC}~","一撃でプレイヤーを倒します。",0,1,20)
                damagerData.player?.playSound(damager,Sound.ITEM_TOTEM_USE,0.4F,1F)
                playerData.player!!.health = 0.0
            }
        }

        Bukkit.getScheduler().runTaskTimer(Werewolf.INSTANCE, Runnable {
            Bukkit.getOnlinePlayers().forEach { player ->
                if (item.itemMeta?.displayName == (player.inventory.itemInMainHand.itemMeta?.displayName ?: UUID.randomUUID().toString())) {
                    players[player] = (players[player]?:0) + 1
                    player.sendTitle("${net.md_5.bungee.api.ChatColor.DARK_PURPLE}${net.md_5.bungee.api.ChatColor.BOLD}${net.md_5.bungee.api.ChatColor.MAGIC}~ ${net.md_5.bungee.api.ChatColor.DARK_PURPLE}${net.md_5.bungee.api.ChatColor.BOLD}斧 ${net.md_5.bungee.api.ChatColor.DARK_PURPLE}${net.md_5.bungee.api.ChatColor.BOLD}${net.md_5.bungee.api.ChatColor.MAGIC}~",if((players[player]?:0) > 2) "パワーをチャージしました。" else "チャージ中...",20,60,20)
                } else {
                    players[player] = 0
                }
            }
        },0,20)
    }
}