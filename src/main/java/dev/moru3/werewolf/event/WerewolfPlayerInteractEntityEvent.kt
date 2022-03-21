package dev.moru3.werewolf.event

import dev.moru3.werewolf.PlayerData
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot

class WerewolfPlayerInteractEntityEvent(val playerData: PlayerData, who: Player, clickedEntity: Entity, hand: EquipmentSlot): PlayerInteractEntityEvent(who, clickedEntity, hand)