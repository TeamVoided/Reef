package org.teamvoided.reef.datagen.assets

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.HolderLookup
import net.minecraft.resources.Identifier
import java.util.concurrent.CompletableFuture

class EnLangProvider(var output: FabricDataOutput, p: CompletableFuture<HolderLookup.Provider>) :
    FabricLanguageProvider(output, p) {

    override fun generateTranslations(lookup: HolderLookup.Provider, gen: TranslationBuilder) {

    }

    private fun genLang(id: Identifier): String =
        id.path.split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }


    fun trySafe(fn: () -> Unit) {
        try {
            fn()
        } catch (e: Exception) {
            if (output.isStrictValidationEnabled) {
                LOGGER.warn("Exception found when lang gen: ", e)
            }
        }
    }

}