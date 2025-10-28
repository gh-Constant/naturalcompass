# NaturalCompass Plugin Todo List

This file tracks the development progress of the NaturalCompass plugin.

## Phase 1 – Infrastructure
- [ ] Setup Maven/Gradle project structure.
- [ ] Create the main plugin class (`NaturalCompass.java`).
- [ ] Implement a command framework for `/naturalcompass` subcommands (e.g., reload, admin).
- [ ] Create a configuration manager to load and handle `config.yml`.
- [ ] Implement a reload mechanism for the configuration.
- [ ] Set up a permission system using Paper's API.

## Phase 2 – Core Item Logic
- [ ] Create a custom item manager for the NaturalCompass.
- [ ] Implement tier recognition using PersistentDataContainer to store the compass tier.
- [ ] Implement configurable crafting recipes for the Tier 1 compass.
- [ ] Implement the upgrade system for the compass (Tiers II-V).

## Phase 3 – GUIs
- [ ] Create a GUI Manager class to handle GUI creation and events.
- [ ] Implement the `BiomeSelectionGUI` with pagination.
- [ ] Implement the `BiomeExclusionGUI` for administrators.
- [ ] Implement a live update system for GUIs when settings change.

## Phase 4 – Biome Search Engine
- [ ] Implement an asynchronous biome scanning engine using Paper's async scheduler.
- [ ] Add configurable timeout logic to the search.
- [ ] Implement a caching system for biome search results.
- [ ] Update the compass needle to point towards the found biome.
- [ ] Implement user-friendly messages for search progress, success, and failure.

## Phase 5 – Resource Pack
- [ ] Design and create a custom model/texture for the NaturalCompass in Java Edition format.
- [ ] Convert the model to a Bedrock-compatible format.
- [ ] Package the resource pack using the Geyser pack format for cross-compatibility.
- [ ] Create custom icons for the GUI.
- [ ] Create the necessary pack metadata files (`pack.mcmeta`, etc.).

## Phase 6 – Polish & Finalization
- [ ] Add sounds and title messages for feedback.
- [ ] Implement fallback detection for Bedrock players without the resource pack.
- [ ] Create a configurable mapping for biome icons in the GUI.
- [ ] Add detailed debug logging controlled by the `detaillogging` config setting.
- [ ] Perform performance tuning and optimization.