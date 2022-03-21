package dev.moru3.werewolf

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.util.Vector

class Grave(location: Location, val playerData: PlayerData, val reason: String) {
    val location = location.clone()
    init {
        val block = this.location.block
        val blockData = block.blockData
        when(blockData) {
            is Directional -> {
                when(block.type) {
                    Material.LADDER, Material.VINE -> {
                        when(blockData.facing) {
                            BlockFace.NORTH -> {
                                this.location.z -= 1
                            }
                            BlockFace.EAST -> {
                                this.location.x += 1
                            }
                            BlockFace.SOUTH -> {
                                this.location.z += 1
                            }
                            BlockFace.WEST -> {
                                this.location.x -= 1
                            }
                            else -> {}
                        }
                    }
                    else -> { }
                }
            }
            else -> { }
        }
        this.location.block.type = Material.BEDROCK
        val signLocation = this.location.clone().add(0.0, 1.0, 0.0)
        val signBlock = signLocation.block
        signBlock.type = Material.OAK_SIGN
        val signBlockState = signBlock.state
        if(signBlockState is org.bukkit.block.Sign) {
            signBlockState.setLine(1,"${playerData.offlinePlayer.name}")
        }
        graves.add(this)
    }

    fun delete() {
        this.location.clone().add(Vector(0,1,0)).block.type = Material.AIR
        this.location.clone().block.type = Material.AIR
    }

    companion object {
        val graves = mutableListOf<Grave>()
    }
}