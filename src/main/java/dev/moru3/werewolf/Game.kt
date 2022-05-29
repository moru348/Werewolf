package dev.moru3.werewolf

import dev.moru3.minepie.Executor.Companion.runTask
import dev.moru3.minepie.Executor.Companion.runTaskLater
import dev.moru3.minepie.customgui.inventory.CustomContentsSyncGui.Companion.createCustomContentsGui
import dev.moru3.minepie.item.EasyItem
import dev.moru3.werewolf.item.Items
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask
import java.security.SecureRandom
import java.util.*

class Game(private val main: Werewolf): Listener {

    init {
        main.gameInstances.add(this)
        Bukkit.getPluginManager().registerEvents(this, main)
    }

    private var isStarting = false

    private var time = -10

    var players = mutableMapOf<UUID, PlayerData>()

    private val waitingPlayers = mutableListOf<UUID>()

    private var bukkitTask: BukkitTask? = null

    fun start(location: Location, ignore: List<String> = listOf()) {
        // isSingletonがtrueかつ、別のゲームが実行中の場合は実行停止。
        if(main.isSingleton && main.gameInstances.any(Game::isStarting)) {
            throw IllegalStateException("ゲームの並列実行は有効ではありません。他のゲームが終了するまでお待ち下さい。")
        } else {
            isStarting = true
            val players = Bukkit.getOnlinePlayers().toMutableList()
            ignore.forEach { players.remove(Bukkit.getPlayer(it)) }
            waitingPlayers.addAll(players.map { it.uniqueId })
            players.forEach { it.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS,50,100,false,false,false)) }
            main.runTaskLater(20) {
                players.forEach { it.teleport(location) }
                main.runTaskLater(40) {
                    players.forEach {
                        it.playSound(it,Sound.AMBIENT_NETHER_WASTES_MOOD,1F,0F)
                        it.playSound(it,Sound.AMBIENT_NETHER_WASTES_MOOD,1F,0F)
                        it.playSound(it,Sound.AMBIENT_NETHER_WASTES_MOOD,1F,0F)
                        it.playSound(it,Sound.AMBIENT_NETHER_WASTES_MOOD,1F,0F)
                        it.playSound(it,Sound.AMBIENT_NETHER_WASTES_MOOD,1F,0F)
                        it.playSound(it,Sound.AMBIENT_NETHER_WASTES_MOOD,1F,0F)
                        it.gameMode = GameMode.SURVIVAL
                        it.sendTitle("${ChatColor.DARK_PURPLE}${ChatColor.BOLD}${ChatColor.ITALIC}人狼スタート", "15秒後に役職が発表されます", 0, 160, 40 )
                        it.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 20*15,1,false,false,false))
                        it.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*15,1,false,false,false))
                    }
                    main.runTaskLater(20*15) {
                        // configから役職の最大数を取得
                        val roleString2MaxValue = mutableMapOf<String, Int>()
                        main.config.config()?.getConfigurationSection("roles")?.getKeys(false)?.forEach {
                            roleString2MaxValue[it] = main.config.config()?.getInt("roles.${it}.max")?:throw IllegalStateException("")
                        }?:throw IllegalStateException("うんこ")
                        // サイズが少ない順に序列し、取得した役職をRole型に変換
                        val role2MaxValue = roleString2MaxValue.toList().sortedBy { (_,value) -> value }.toMap().mapKeys { Role.valueOf(it.key.uppercase()) }.toMutableMap()
                        val orderImportanceRoles = Role.values().toList().sortedBy { it.importance }
                        while(true) {
                            if(role2MaxValue[Role.WOLF]!! + role2MaxValue[Role.MADMAN]!! + role2MaxValue[Role.DOCTOR]!! + role2MaxValue[Role.MEDIUM]!! + role2MaxValue[Role.SEER]!! > players.size - 1) {
                                orderImportanceRoles.forEach {
                                    if((role2MaxValue[it] ?: 0) > 0) { role2MaxValue[it] = (role2MaxValue[it]?:1) - 1 }
                                }
                            } else {
                                break
                            }
                        }
                        role2MaxValue[Role.VILLAGER] = players.size - (role2MaxValue[Role.WOLF]!! + role2MaxValue[Role.MADMAN]!! + role2MaxValue[Role.DOCTOR]!! + role2MaxValue[Role.MEDIUM]!! + role2MaxValue[Role.SEER]!!)
                        role2MaxValue.forEach { (role, maxCount) ->
                            // 最大数分ループする
                            repeat(maxCount) {
                                val player = players.random()
                                waitingPlayers.remove(player.uniqueId)
                                players.remove(player)
                                player.playSound(player,Sound.AMBIENT_NETHER_WASTES_MOOD,1F,1F)
                                player.playSound(player,Sound.AMBIENT_NETHER_WASTES_MOOD,1F,1F)
                                player.playSound(player,Sound.AMBIENT_NETHER_WASTES_MOOD,1F,1F)
                                player.playSound(player,Sound.AMBIENT_NETHER_WASTES_MOOD,1F,1F)
                                player.playSound(player,Sound.AMBIENT_NETHER_WASTES_MOOD,1F,1F)
                                player.playSound(player,Sound.AMBIENT_NETHER_WASTES_MOOD,1F,1F)
                                this.players[player.uniqueId] = PlayerData(this, role, player)
                                player.scoreboard = this.players[player.uniqueId]?.scoreBoard!!
                                main.players[player.uniqueId] = this.players[player.uniqueId]!!
                                player.inventory.setItem(8,EasyItem(Material.AMETHYST_SHARD, Japanese.openShop, listOf("${ChatColor.GRAY}右クリックすることでショップを利用できます。")))
                                player.inventory.addItem(ItemStack(Material.BOW).also { itemStack -> itemStack.itemMeta = itemStack.itemMeta?.also { itemMeta ->
                                    itemMeta.addEnchant(Enchantment.ARROW_INFINITE,1,true)
                                    itemMeta.isUnbreakable = true
                                } })
                                player.inventory.addItem(ItemStack(Material.STONE_SWORD).also { itemStack -> itemStack.itemMeta = itemStack.itemMeta?.also { itemMeta ->
                                    itemMeta.isUnbreakable = true
                                } })
                                player.inventory.addItem()
                                player.inventory.addItem(ItemStack(Material.ARROW, 64))
                                player.sendTitle("${role.color}${ChatColor.BOLD}${role.displayName}", role.description, 20, 120, 20)
                                println("${player.name} : ${this.players[player.uniqueId]!!.role}")
                                when(this.players[player.uniqueId]!!.role) {
                                    Role.WOLF -> {
                                        player.inventory.addItem(Items.BOMB_BALL.item)
                                    }
                                    Role.VILLAGER -> {}
                                    Role.SEER -> { player.inventory.addItem(Items.SEER_ITEM.item) }
                                    Role.DOCTOR -> { player.inventory.addItem(Items.DOCTOR_SWORD.item) }
                                    Role.MEDIUM -> {
                                        player.inventory.addItem(Items.MEDIUM_ITEM.item)
                                        player.inventory.addItem(Items.MEDIUM_ITEM.item)
                                        player.inventory.addItem(Items.MEDIUM_ITEM.item)
                                    }
                                    Role.MADMAN -> { player.inventory.addItem(Items.SWAP_LOSE.item) }
                                }
                                Role.values().forEach {
                                    player.inventory.addItem(it.getHelmet())
                                }
                            }
                        }

                        Bukkit.broadcastMessage("${Role.WOLF.color}黒陣営(人狼、狂人など): ${this.players.values.count { it.role==Role.WOLF||it.role==Role.MADMAN }}人")
                        var c = 0
                        role2MaxValue.toMutableMap()
                            .also { it.remove(Role.WOLF) }
                            .also { it.remove(Role.MADMAN) }
                            .forEach { c+=it.value }
                        Bukkit.broadcastMessage("${ChatColor.AQUA}白陣営(村人、占い師など): ${c}人")
                        this.players.values.filter { it.role == Role.WOLF }.forEach { playerData ->
                            playerData.player?.sendMessage("${ChatColor.RED}[人狼] 人狼は: ${this.players.values.filter { it.role == Role.WOLF }.map { it.offlinePlayer.name }.joinToString(" ")} です。")
                        }
                        players.forEach {
                            it.sendMessage("${ChatColor.RED}[人狼] ゲームを開始しました。ゲーム時間は10分です。")
                        }
                        // 時間を10分に設定
                        time = main.config.config()?.getInt("game.time")?:600

                        bukkitTask = Bukkit.getScheduler().runTaskTimer(main, Runnable {
                            time--
                            this.players.forEach { (_, playerData) ->
                                if(time%30==0) { playerData.money += 100 }
                                playerData.timeTeam.suffix = sec2string(time)
                                playerData.playerCounterTeam.suffix = "${this.players.values.count { it.isAlive }}人"
                            }

                            Role.values().forEach {
                                this.players.values.forEach s@{ playerData ->
                                    val player = playerData.player?:return@s
                                    if(player.inventory.helmet?.itemMeta?.displayName == "${it.color}${it.displayName}Coします。"&&playerData.co!=it) {
                                        playerData.co = it
                                        player.setPlayerListName("${it.color}[${it.displayName}Co] ${player.name}")
                                        player.setDisplayName("${it.color}[${it.displayName}Co] ${player.name}")
                                        Bukkit.broadcastMessage("${it.color}${player.name}が${it.displayName}Coしました。")
                                        if(playerData.role == Role.MADMAN) {
                                            this.players.values.filter { it.role == Role.WOLF }.forEach { data ->
                                                data.player?.sendMessage("${ChatColor.RED}[人狼] 狂人が${it.displayName}をCoしました。")
                                            }
                                        }
                                    }
                                }
                            }
                            if(time==0) { end(Team.VILLAGE) }
                        }, 0, 20)
                    }
                }
            }
        }
    }

    fun end(winner: Team) {
        time = -10
        bukkitTask?.cancel()
        Bukkit.getOnlinePlayers().forEach { player ->
            Bukkit.getOnlinePlayers().forEach {
                it.showPlayer(Werewolf.INSTANCE, player)
            }
        }
        try { Items.HEALTH_CHARGER.locations.forEach { if(it.value == this) { it.key.block.type = Material.AIR;Items.HEALTH_CHARGER.locations.remove(it.key) } } } catch (_: Exception) {}
        try { Items.FAKE_HEALTH_CHARGER.locations.forEach { if(it.value == this) { it.key.block.type = Material.AIR;Items.HEALTH_CHARGER.locations.remove(it.key) } } } catch (_: Exception) {}
        // cadavers.forEach { it.npc.destroy() }
        this.players.values.forEach { playerData ->
            val player = playerData.player?:return@forEach
            player.sendTitle("${winner.color}${ChatColor.BOLD}${winner.displayName}陣営の勝利",if(playerData.role.team==winner) "勝利しました！" else "敗北しました。", 20, 200, 20)
            player.sendMessage("${ChatColor.RED}============ 結果 ============")
            player.sendMessage("${ChatColor.RED}勝者: ${ChatColor.YELLOW}${winner.displayName}陣営")
            player.sendMessage("${ChatColor.RED}${Role.WOLF.displayName}: ${this.players.values.filter { it.role==Role.WOLF }.map { it.offlinePlayer.name }.joinToString(", ")}")
            if(this.players.values.map { it.role }.contains(Role.MADMAN)) {
                player.sendMessage("${ChatColor.RED}${Role.MADMAN.displayName}: ${this.players.values.filter { it.role==Role.MADMAN }.map { it.offlinePlayer.name }.joinToString(", ")}")
            }
            player.sendMessage("${ChatColor.RED}${Role.VILLAGER.displayName}: ${this.players.values.filter { it.role==Role.VILLAGER }.map { it.offlinePlayer.name }.joinToString(", ")}")
            if(this.players.values.map { it.role }.contains(Role.DOCTOR)) {
                player.sendMessage("${ChatColor.RED}${Role.DOCTOR.displayName}: ${this.players.values.filter { it.role==Role.DOCTOR }.map { it.offlinePlayer.name }.joinToString(", ")}")
            }
            if(this.players.values.map { it.role }.contains(Role.SEER)) {
                player.sendMessage("${ChatColor.RED}${Role.SEER.displayName}: ${this.players.values.filter { it.role==Role.SEER }.map { it.offlinePlayer.name }.joinToString(", ")}")
            }
            if(this.players.values.map { it.role }.contains(Role.MEDIUM)) {
                player.sendMessage("${ChatColor.RED}${Role.MEDIUM.displayName}: ${this.players.values.filter { it.role==Role.MEDIUM }.map { it.offlinePlayer.name }.joinToString(", ")}")
            }
            player.sendMessage("${ChatColor.RED}============ 結果 ============")
            player.sendMessage("${winner.color}[人狼] ${winner.displayName}陣営の勝利です！")
            player.playSound(player,Sound.ENTITY_FIREWORK_ROCKET_BLAST,1F,1F)
            playerData.objective.unregister()
            player.closeInventory()
            player.inventory.clear()
            player.setDisplayName(player.name)
            player.setPlayerListName(player.name)
            player.gameMode = GameMode.ADVENTURE
            main.players.remove(player.uniqueId)
        }
        isStarting = false
        this.players.clear()
    }

    @EventHandler
    fun onProjectileHitEvent(event: ProjectileHitEvent) {
        if(event.entity.type==EntityType.ARROW&&event.hitEntity!=null&&event.hitEntity is Player&&players.containsKey(event.hitEntity?.uniqueId)) {
            (event.hitEntity as Player).damage(minOf((event.hitEntity as Player).healthScale/3, (event.hitEntity as Player).health))
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val playerData = players[event.entity.uniqueId]?:return
        when(event.cause) {
            EntityDamageEvent.DamageCause.FALL -> {
                event.isCancelled = true
            }
            else -> {}
        }
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val playerData = players[event.player.uniqueId]?:return
        if(!playerData.isAlive) {
            event.isCancelled = true
            players.values.filter { !it.isAlive }.forEach {
                it.player?.sendMessage("${ChatColor.GRAY}死亡者チャット(${event.player.name}): ${event.message}")
            }
        } else {
            if(playerData.co!=null) {
                event.isCancelled = true
                main.runTask { Bukkit.broadcastMessage("${playerData.co!!.color}[${playerData.co!!.displayName}Co]${event.player.name}: ${event.message}") }
            }
        }
    }

    val cadavers = mutableListOf<Cadaver>()

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity
        val playerData = players[player.uniqueId]?:return
        if(!playerData.isAlive) { return }
        event.drops.clear()
        event.droppedExp = 0
        playerData.isAlive = false
        Bukkit.getOnlinePlayers().forEach {
            it.hidePlayer(Werewolf.INSTANCE,player)
        }
        player.gameMode = GameMode.SPECTATOR
        player.inventory.clear()
        event.deathMessage = null

        // cadavers.add(Cadaver(player))

        event.entity.spigot().respawn()

        players.values.forEach { playerData1 ->
            playerData1.playerCounterTeam.suffix = "${players.values.count { it.isAlive }}人"
        }

        if(playerData.role == Role.WOLF) {
            this.players.values.filter { it.role == Role.MADMAN }.forEach {
                // it.player?.sendMessage("${ChatColor.RED}[人狼] 人狼が一人死亡しました。")
            }
        } else if(playerData.role == Role.MADMAN) {
            this.players.values.filter { it.role == Role.WOLF }.forEach {
                // it.player?.sendMessage("${ChatColor.RED}[人狼] 狂人が一人死亡しました。")
            }
        }

        this.players.values.filter { it.role == Role.MEDIUM }.forEach {
            val text = TextComponent("一人死亡しました。${ChatColor.RED}[${ChatColor.AQUA}クリックで冷媒する${ChatColor.RED}]")
            text.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT,Text("${ChatColor.AQUA}クリックで冷媒する"))
            text.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/medium ${playerData.offlinePlayer.uniqueId}")
            it.player?.spigot()?.sendMessage(text)
        }

        // Grave(location, playerData, reason)
        if(this.players.values.filter { it.role.team == Team.WOLF }.none { it.isAlive }) {
            end(Team.VILLAGE)
        } else if(this.players.values.filter { it.role.team == Team.VILLAGE }.none { it.isAlive }) {
            end(Team.WOLF)
        }
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        if(players.containsKey(event.player.uniqueId)) { event.isCancelled = true }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        players[event.whoClicked.uniqueId]?:return
        if(event.currentItem?.itemMeta?.displayName==Japanese.openShop) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onRegainHealth(event: EntityRegainHealthEvent) {
        if(players.containsKey(event.entity.uniqueId)) { event.isCancelled = true }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val playerData = players[event.player.uniqueId]?:return
        if(event.hand==EquipmentSlot.HAND&&event.player.inventory.itemInMainHand.itemMeta?.displayName==Japanese.openShop&&(event.action==Action.RIGHT_CLICK_AIR||event.action==Action.RIGHT_CLICK_BLOCK)) {
            event.isCancelled = true
            main.createCustomContentsGui(2,"${ChatColor.DARK_RED}${ChatColor.BOLD}ショップ", 0,0, 8,1) {
                Items.filter { it.unique.contains(playerData.role) }.filter { it.showInShop }.forEach { shopItem ->
                    addContents(shopItem.item.also { it.itemMeta = it.itemMeta?.also { itemMeta -> itemMeta.lore = itemMeta.lore?.also { lore -> lore.add("${ChatColor.YELLOW}値段: ${shopItem.price}円") } } }) { addAction(ClickType.LEFT) { shopItem.buy(playerData) } }
                }
            }.open(event.player)
        }
    }

    private fun sec2string(sec: Int): String {
        return "${maxOf(sec/60,0)}分${maxOf(sec%60,0)}秒"
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if(players.containsKey(event.player.uniqueId)) { event.isCancelled = true }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if(players.containsKey(event.player.uniqueId)) {
            if(Items.HEALTH_CHARGER.locations.contains(event.block.location)||Items.FAKE_HEALTH_CHARGER.locations.contains(event.block.location)) {
                return
            }
            event.isCancelled = true
        }
    }
}