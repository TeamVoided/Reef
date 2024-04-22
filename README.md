# Reef

Library that extends vanilla worldgen by providing alternatives to unmodifiable features

## What is the goal of the mod?

The main goal of Reef is to take vanilla world gen and make them configurable. And the way we want to achieve this is by
not change vanilla, but adding to it. So let's say we want to be able to customize the Ice Spike feature, and the way Reef
approaches this by creative a new identical feature that is fully customizable. So there is no need for dirty mixins and
breaking compatibility with other mods. This is not possible in all cases of course, and in those cases we will take
the least intrusive option.

## What Does the mod do?

Reef does a lot of things! Well, only a few for now, but we have great plans.
Currently, the mod adds duplicates of vanilla features that can be properly configured. Custom Tags have been added to
enable icebergs or eroded badlands generation to occur in biomes. And a Custom StructurePool Projection (See warning
section about this.)

## Contents

- Custom Features:
  - Spike ("Ice Spike" Feature, but customizable)
  - Inverted Spike ("Spike" Feature, but is upside down)
  - Monster Room ("Dungeon" Feature, but customizable)
  - Structure Piece (Similar to "Fossil" Feature, but with no forced height offset)
  - Large Cave Pillar ("Large Dripstone", but customizable)
  - Feature List (Places all features in a list)
- Custom Tags:
  - `has_eroded_pillar` (Will apply the Eroded Badlands Surface Builder, has been tweaked to use OCEAN_FLOOR_WG)
  - `has_iceberg` (Will apply the Iceberg Surface Builder)
- StructurePool Projections:
  - `SEAFLOOR_MATCHING` (Adds seafloor matching projection. See warning section about this.)

## Future of the lib

While this lib is only in the early stages of development. We already have plans to Fully deprecate this mod. Since This
is planned to be just one module from our future mod `VoidLib`.

### Why?

Because it is nicer to have a single jar file for all the libs we use, kinda like Fapi or QSL

Then why not just release `VoidLib`?
Because Gradle multi-module projects are hard :(

## WARNING

As the mod is currently in very early stages, so there are some kinks we have not fixed.
As of the current version (0.1.2) here are the current warning:

#### StructurePool Projection `SEAFLOOR_MATCHING`

This projection exists but does not behave anything like expected.
Please don't use it for anything, but testing.

#### Custom Features

All the current features were made quickly and very messily for a world gen mod we were working on. They have not been
property cleaned up and optimized. So do expect them to change, both as features and their configs.
The future vision for this mod is that the features that are modified version of vanilla ones. Act exactly like vanilla
ones but have the ability to be configured (And optimized where possible).

## Wiki

As of the current update (0.1.2) there is no wiki.
This is just a note saying that Ender is working on one.
If there isn't one more than a month after the (0.1.2) release, go @ Ender on discord
