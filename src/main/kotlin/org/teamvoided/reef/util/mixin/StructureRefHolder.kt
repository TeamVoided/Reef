package org.teamvoided.reef.util.mixin

import net.minecraft.resources.ResourceLocation

@Suppress("FunctionName")
interface StructureRefHolder {
    fun reef_setStructureRef(id: ResourceLocation)
    fun reef_getStructureRef(): ResourceLocation?
}