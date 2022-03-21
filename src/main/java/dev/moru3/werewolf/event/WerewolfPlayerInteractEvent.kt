package dev.moru3.werewolf.event

import dev.moru3.werewolf.PlayerData
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class WerewolfPlayerInteractEvent(val playerData: PlayerData,who: Player,action: Action,item: ItemStack?,clickedBlock: Block?,clickedFace: BlockFace,hand: EquipmentSlot?): PlayerInteractEvent(who, action, item, clickedBlock, clickedFace, hand) {

}