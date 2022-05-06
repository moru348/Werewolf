package dev.moru3.werewolf.item

import dev.moru3.minepie.Executor.Companion.runTaskLater
import dev.moru3.minepie.item.EasyItem
import dev.moru3.werewolf.Role
import dev.moru3.werewolf.Werewolf
import dev.moru3.werewolf.event.WerewolfPlayerInteractEvent
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack

/**
 * 狂人専用のアイテム。
 * ランダムな人狼と自分(狂人)のアイテムを入れ替える。
 */
class SwapLose: AbstractShopItem(Role.MADMAN) {
    override val item: ItemStack
        get() = EasyItem(Material.WITHER_ROSE, "Swap Lose", listOf("${ChatColor.GRAY}ランダムな人狼の位置と自分の位置を入れ替える。"))

    override val price: Int = 220

    override fun onClick(event: WerewolfPlayerInteractEvent) {
        val wolfs = event.playerData.game.players.filter { it.value.role == Role.WOLF }
            .filter { it.value.isAlive }.keys.mapNotNull { Bukkit.getPlayer(it) }
        if(wolfs.isEmpty()) {
            event.player.sendMessage("人狼がいないため使用できない...")
        } else {
            event.item!!.amount--
            // 対象の人狼をランダムで選択
            val wolf = wolfs.random()

            // 人狼と使用者にそのことを通知
            event.player.sendTitle("${ChatColor.DARK_PURPLE}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.DARK_PURPLE}${ChatColor.BOLD}Swapping... ${ChatColor.DARK_PURPLE}${ChatColor.BOLD}${ChatColor.MAGIC}~","ランダムな人狼と自分の位置を入れ替えます。",20,60,20)
            wolf.sendTitle("${ChatColor.DARK_PURPLE}${ChatColor.BOLD}${ChatColor.MAGIC}~ ${ChatColor.DARK_PURPLE}${ChatColor.BOLD}Swapping... ${ChatColor.DARK_PURPLE}${ChatColor.BOLD}${ChatColor.MAGIC}~","狂人が位置を入れ替えるアイテムを使用しました。",20,60,20)

            // 1.5秒ほど待機
            Werewolf.INSTANCE.runTaskLater(30) {
                // サウンドの再生
                event.player.playSound(event.player, Sound.ENTITY_ENDERMAN_TELEPORT, 2F, 0F)
                wolf.playSound(wolf, Sound.ENTITY_ENDERMAN_TELEPORT, 2F, 0F)
                // 人狼と使用者の座標を記録
                val wolfLocation = wolf.location.clone()
                val location = event.player.location.clone()
                // テレポート(入れ替え)
                event.player.teleport(wolfLocation)
                wolf.teleport(location)
                // パーティクルの表示
                event.player.spawnParticle(Particle.SPELL_WITCH, 0.1, 0.0, 0.1, 30)
                wolf.spawnParticle(Particle.SPELL_WITCH, 0.1, 0.0, 0.1, 30)
            }
        }
    }
}