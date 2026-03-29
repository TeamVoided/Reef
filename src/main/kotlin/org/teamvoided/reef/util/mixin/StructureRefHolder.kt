package org.teamvoided.reef.util.mixin

import net.minecraft.resources.Identifier

@Suppress("FunctionName")
interface StructureRefHolder {

    fun reef_setStructureRef(id: Identifier)

    fun reef_getStructureRef(): Identifier?

}