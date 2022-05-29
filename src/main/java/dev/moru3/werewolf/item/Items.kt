package dev.moru3.werewolf.item

import dev.moru3.minepie.events.EventRegister.Companion.registerEvent
import dev.moru3.werewolf.Werewolf
import dev.moru3.werewolf.event.WerewolfPlayerInteractEntityEvent
import dev.moru3.werewolf.event.WerewolfPlayerInteractEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

object Items: MutableSet<ShopItem> {
    private val items = mutableSetOf<ShopItem>()
    override val size: Int
        get() = items.size

    override fun contains(element: ShopItem): Boolean = items.contains(element)

    override fun containsAll(elements: Collection<ShopItem>): Boolean = items.containsAll(elements)

    override fun isEmpty(): Boolean = items.isEmpty()

    override fun iterator(): MutableIterator<ShopItem> = items.iterator()

    override fun add(element: ShopItem): Boolean = items.add(element)

    override fun addAll(elements: Collection<ShopItem>): Boolean = items.addAll(elements)

    override fun clear() = throw IllegalArgumentException("can't clear items.")

    override fun remove(element: ShopItem): Boolean = throw IllegalArgumentException("can't remove items")

    override fun removeAll(elements: Collection<ShopItem>): Boolean = throw IllegalArgumentException("can't remove items")

    override fun retainAll(elements: Collection<ShopItem>): Boolean = throw IllegalArgumentException("can't retain items")

    val SWAP_LOSE = SwapLose()
    val RANDOM_GUIDE_BOOM = RandomGuideBook()
    val INVISIBLE_BALL = InvisibleBall()
    val INSTANT_HEALTH_POTION = InstantHealthPotion()
    val DUMMY_SEER = DummySeer()
    val DUMMY_MEDIUM = DummyMedium()
    val STAN_BALL = StanBall()
    val GLOW_INK = GlowInk()
    val SEER_ITEM = SeerItem()
    val DOCTOR_SWORD = DoctorSword()
    val MEDIUM_ITEM = MediumItem()
    val HEALTH_CHARGER = HealthCharger()
    val SELF_BOMB = SelfBomb()
    val FAKE_HEALTH_CHARGER = FakeHealthCharger()
    val LIGHTNING_ROD = LightningRod()
    val WOLF_AXE = WolfAxe()
    val BOMB_BALL = BombBall()
    val TELEPORT_DIAMOND = TeleportDiamond()


    init {
        Werewolf.INSTANCE.registerEvent<PlayerInteractEvent> {
            val playerData = Werewolf.INSTANCE.players[player.uniqueId]?:return@registerEvent
            items
                .filter { it.item.itemMeta?.displayName == item?.itemMeta?.displayName }
                .filter { it.unique.contains(playerData.role) }
                .forEach { it.onClick(WerewolfPlayerInteractEvent(playerData,player,action,item,clickedBlock,blockFace,hand)) }
        }
        Werewolf.INSTANCE.registerEvent<PlayerInteractEntityEvent> {
            val playerData = Werewolf.INSTANCE.players[player.uniqueId]?:return@registerEvent
            items
                .filter { it.item.itemMeta?.displayName == player.inventory.getItem(this.hand).itemMeta?.displayName }
                .filter { it.unique.contains(playerData.role) }
                .forEach { it.onClickEntity(WerewolfPlayerInteractEntityEvent(playerData,player,rightClicked, hand)) }
        }
    }
}