package org.teamvoided.reef.util

import com.mojang.datafixers.util.Pair

infix fun <A, B> A.of(that: B): Pair<A, B> = Pair.of(this, that)
