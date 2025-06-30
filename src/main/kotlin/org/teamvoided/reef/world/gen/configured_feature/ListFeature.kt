package org.teamvoided.reef.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import org.teamvoided.reef.world.gen.configured_feature.config.ListFeatureConfig

class ListFeature(codec: Codec<ListFeatureConfig>) :
    Feature<ListFeatureConfig>(codec) {
    override fun place(c: FeaturePlaceContext<ListFeatureConfig>): Boolean {
        val config = c.config()
        val random = c.random()
        val blockPos = c.origin()
        val world = c.level()

        var j = 0
        config.features.forEach {
            var i = 0
            for (l in 1..config.tries) {
                if (!it.value().place(world, c.chunkGenerator(), random, blockPos)) continue
                ++i
            }
            if (i > 0) ++j
        }

        return j > 0 && j == config.features.size
    }


}