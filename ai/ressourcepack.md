Here is a comprehensive documentation file (Ressourcepack.md) detailing the 1.21.4+ item model API changes. You can use this as a reference or as a context file for a future AI prompt.AI Prompting Guide: Java 1.21.4+ Resource PacksThis document outlines the fundamental changes to the item model system in Minecraft: Java Edition 1.21.4 (Resource Pack Version 46) and beyond. The old system of using overrides in models/item/*.json files is deprecated and replaced by a new system using "Top-level Item Info" files in items/*.json.Use this document as the single source of truth when generating or debugging resource packs for modern Minecraft.1. The Core Concept: Logic Moves from models/item/ to items/The single most important change is a separation of logic and presentation.Old Way (Pre-1.21.4): One file (assets/minecraft/models/item/stick.json) held both the model's appearance (parent, textures) AND all its selection logic (the overrides section based on custom_model_data, damage, etc.).New Way (1.21.4+): This is now split into multiple files:Top-level Item Info (assets/minecraft/items/stick.json): This new file contains only the logic. It decides which model to show. It uses dispatchers like range_dispatch or select to point to a model.Model Files (assets/minecraft/models/item/stick.json, assets/custom/models/item/my_stick.json, etc.): These files now only define appearance. They are simple model files (e.g., parent: "minecraft:item/generated") and should never contain an overrides section.2. pack.mcmeta is CriticalTo enable this new system, the pack.mcmeta file must use the correct pack_format. For 1.21.4, this is 46. If this is set to an older version, the game will ignore the items/ directory and look for the old overrides format, causing all new packs to fail.JSON{
"pack": {
"pack_format": 46,
"description": "A resource pack using the 1.21.4+ item model format."
}
}
3. New File Structure (Old vs. New)This example shows how to make a stick have a custom model (my_stick) when its CustomModelData is 1.Old Structure (Pack Format < 46)assets/
   └── minecraft/
   └── models/
   └── item/
   ├── stick.json  <-- This file contains logic (overrides)
   └── my_stick.json
   New Structure (Pack Format 46+)assets/
   ├── minecraft/
   │   └── items/
   │       └── stick.json      <-- 1. This new file holds the logic.
   │   └── models/
   │       └── item/
   │           └── stick.json  <-- 2. This is now just the default model.
   └── custom/
   └── models/
   └── item/
   └── my_stick.json <-- 3. This is the custom model.
4. The New custom_model_data ComponentCustomModelData is no longer a single number. It is a data component that can hold lists of floats, strings, flags (booleans), and colors.This means item logic can be based on a string (e.g., "naturecompass") or a number (e.g., 9001.0).Type/give CommandPaperMC Java PluginFloat/give @s stick[minecraft:custom_model_data={floats:[123.0]}]CustomModelData.customModelData().addFloat(123.0f).build()String/give @s stick[minecraft:custom_model_data={strings:["my_item"]}]CustomModelData.customModelData().addString("my_item").build()5. The New Logic Files (items/*.json)The file in assets/minecraft/items/*.json (or your namespace) defines the logic. The most important model types are range_dispatch (for numbers) and select (for strings).minecraft:range_dispatchThis replaces overrides for numeric properties like custom_model_data:floats, damage, or time.property: The numeric property to read.index: (For custom_model_data floats) Which float in the list to read (default is 0).entries: A list of models to choose from. The game picks the entry with the highest threshold that is less than or equal to the property's value.Example: Replaces CustomModelData:1 and CustomModelData:2.assets/minecraft/items/diamond_sword.jsonJSON{
   "model": {
   "type": "minecraft:range_dispatch",
   "property": "minecraft:custom_model_data",
   "index": 0,
   "entries": [
   {
   "threshold": 1.0,
   "model": {
   "type": "minecraft:model",
   "model": "custom:item/sword_1"
   }
   },
   {
   "threshold": 2.0,
   "model": {
   "type": "minecraft:model",
   "model": "custom:item/sword_2"
   }
   }
   ],
   "fallback": {
   "type": "minecraft:model",
   "model": "minecraft:item/diamond_sword"
   }
   }
   }
   minecraft:selectThis is new. It selects a model based on a string property.property: The string property to read (e.g., minecraft:custom_model_data).index: (For custom_model_data strings) Which string in the list to read (default is 0).cases: A list of when (string) and model (what to render) pairs.Example: Selects a model based on a custom_model_data:string.assets/minecraft/items/stick.jsonJSON{
   "model": {
   "type": "minecraft:select",
   "property": "minecraft:custom_model_data",
   "index": 0,
   "cases": [
   {
   "when": "magic_wand",
   "model": {
   "type": "minecraft:model",
   "model": "custom:item/magic_wand"
   }
   },
   {
   "when": "debug_stick",
   "model": {
   "type": "minecraft:model",
   "model": "custom:item/debug_stick"
   }
   }
   ],
   "fallback": {
   "type": "minecraft:model",
   "model": "minecraft:item/stick"
   }
   }
   }
6. Critical Syntax Change: propertyThis is a common error. When specifying a property (like compass or time), the syntax has changed.Old Way: The property was an object.JSON"property": {
   "type": "minecraft:compass",
   "target": "spawn"
   }
   New Way: The type string becomes the property string. Any parameters (like target) are inlined as siblings.JSON"property": "minecraft:compass",
   "target": "spawn",
7. Full Example: Animated Custom CompassThis example combines all concepts to create a custom compass (naturecompass) that also has a working animated needle. This requires nesting dispatchers.Step 1: The Plugin/CommandThe item is given with a float value (e.g., 9001.0)./give @s compass[minecraft:custom_model_data={floats:[9001.0]}]Step 2: The Logic File (assets/minecraft/items/compass.json)This file does two checks:Outer Check: Is the custom_model_data:float[0] equal to 9001.0?Inner Check: If yes, it passes control to another range_dispatch that checks the minecraft:compass property to select the correct animated frame.Fallback: If not 9001.0, it uses the default vanilla compass logic.JSON{
   "model": {
   "type": "minecraft:range_dispatch",
   "property": "minecraft:custom_model_data",
   "index": 0,
   "entries": [
   {
   "threshold": 9001.0,
   "model": {
   "type": "minecraft:range_dispatch",
   "property": "minecraft:compass",
   "target": "spawn",
   "entries": [
   { "threshold": 0.0, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_00" }},
   { "threshold": 0.03125, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_01" }},
   { "threshold": 0.0625, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_02" }},
   { "threshold": 0.09375, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_03" }},
   { "threshold": 0.125, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_04" }},
   { "threshold": 0.15625, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_05" }},
   { "threshold": 0.1875, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_06" }},
   { "threshold": 0.21875, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_07" }},
   { "threshold": 0.25, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_08" }},
   { "threshold": 0.28125, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_09" }},
   { "threshold": 0.3125, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_10" }},
   { "threshold": 0.34375, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_11" }},
   { "threshold": 0.375, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_12" }},
   { "threshold": 0.40625, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_13" }},
   { "threshold": 0.4375, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_14" }},
   { "threshold": 0.46875, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_15" }},
   { "threshold": 0.5, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_16" }},
   { "threshold": 0.53125, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_17" }},
   { "threshold": 0.5625, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_18" }},
   { "threshold": 0.59375, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_19" }},
   { "threshold": 0.625, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_20" }},
   { "threshold": 0.65625, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_21" }},
   { "threshold": 0.6875, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_22" }},
   { "threshold": 0.71875, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_23" }},
   { "threshold": 0.75, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_24" }},
   { "threshold": 0.78125, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_25" }},
   { "threshold": 0.8125, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_26" }},
   { "threshold": 0.84375, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_27" }},
   { "threshold": 0.875, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_28" }},
   { "threshold": 0.90625, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_29" }},
   { "threshold": 0.9375, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_30" }},
   { "threshold": 0.96875, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_31" }}
   ],
   "fallback": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_00" }
   }
   }
   ],
   "fallback": {
   "type": "minecraft:range_dispatch",
   "property": "minecraft:compass",
   "target": "spawn",
   "entries": [
   { "threshold": 0.0, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_00" }},
   { "threshold": 0.03125, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_01" }},
   { "threshold": 0.0625, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_02" }},
   { "threshold": 0.09375, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_03" }},
   { "threshold": 0.125, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_04" }},
   { "threshold": 0.15625, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_05" }},
   { "threshold": 0.1875, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_06" }},
   { "threshold": 0.21875, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_07" }},
   { "threshold": 0.25, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_08" }},
   { "threshold": 0.28125, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_09" }},
   { "threshold": 0.3125, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_10" }},
   { "threshold": 0.34375, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_11" }},
   { "threshold": 0.375, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_12" }},
   { "threshold": 0.40625, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_13" }},
   { "threshold": 0.4375, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_14" }},
   { "threshold": 0.46875, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_15" }},
   { "threshold": 0.5, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_16" }},
   { "threshold": 0.53125, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_17" }},
   { "threshold": 0.5625, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_18" }},
   { "threshold": 0.59375, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_19" }},
   { "threshold": 0.625, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_20" }},
   { "threshold": 0.65625, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_21" }},
   { "threshold": 0.6875, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_22" }},
   { "threshold": 0.71875, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_23" }},
   { "threshold": 0.75, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_24" }},
   { "threshold": 0.78125, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_25" }},
   { "threshold": 0.8125, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_26" }},
   { "threshold": 0.84375, "model": { "type": "minecraft:model", "model": "naturalcompass:item/naturecompass_27" }},
   { "threshold": 0.875, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_28" }},
   { "threshold": 0.90625, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_29" }},
   { "threshold": 0.9375, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_30" }},
   { "threshold": 0.96875, "model": { "type": "minecraft:model", "model": "minecraft:item/compass_31" }}
   ],
   "fallback": { "type": "minecraft:model", "model": "minecraft:item/compass" }
   }
   }
   }
   Step 3: The Model Files (assets/naturalcompass/models/item/)These files are simple and contain no logic. They just point to the textures.naturecompass_00.json:JSON{
   "parent": "minecraft:item/generated",
   "textures": {
   "layer0": "naturalcompass:item/naturecompass_00"
   }
   }
   naturecompass_01.json:JSON{
   "parent": "minecraft:item/generated",
   "textures": {
   "layer0": "naturalcompass:item/naturecompass_01"
   }
   }
   (...and so on for all 32 files.)8. AI Prompting ChecklistWhen I (an AI) am asked to create a resource pack:Ask for the Minecraft Version: Is it 1.21.4+ (Pack Format 46+)?Ask for the Item Base: What vanilla item is being modified (e.g., minecraft:stick)?Ask for the Logic: How will the custom models be triggered?If by a number (CustomModelData:1), I must use range_dispatch and custom_model_data:floats.If by a name ("magic_wand"), I must use select and custom_model_data:strings.If by durability, I must use range_dispatch and minecraft:damage.Action Plan (for me):I will create the pack.mcmeta with the correct pack_format.I will create the logic file in assets/minecraft/items/.I will create simple model files in assets/[namespace]/models/item/ for each custom texture.I will ensure all simple model files use the correct parent (e.g., "minecraft:item/generated" for 2D items).I will provide the correct /give command or PaperMC Java code to get the item.

MORE DOCUMENTATION : 

Paper 1.21.4+ and CustomModelAPI Documentation
Overview
Minecraft 1.21.4 introduced significant changes to customizing item models and how plugin APIs, including Paper, interact with custom items. If you are updating plugins or resource packs, you must now use new structures for model assignment and your code should rely on updated API methods for manipulating custom model data.

Changes for Custom Models in 1.21.4+
CustomModelData is now component-based: The minecraft:custom_model_data item component supports multiple field types, including floats, flags, strings, and colors, instead of just numeric values.​​

Models and overrides: The legacy overrides section for item model selection is replaced by a fully data-driven system. Now, models are referenced through data in items files (located in assets/namespace/items/) and not in traditional models/item Json hierarchy.​​

New item modeling system: You must now use selectors and cases for matching model data, and models can dynamically react to the string, boolean, or numeric properties you provide.​​

Example Data File (items/mace.json):
json
{
"model": {
"type": "minecraft:select",
"property": "minecraft:custom_model_data",
"cases": [
{
"when": "bat",
"model": {
"type": "minecraft:model",
"model": "item/bat"
}
}
],
"fallback": {
"type": "minecraft:model",
"model": "item/mace"
}
}
}
Use "property": "minecraft:custom_model_data" to match custom data provided via commands or plugin API.

Values like "bat" can be strings or numbers depending on your use-case.​​

API Usage in Paper 1.21.4+
In PaperMC plugins, interact with ItemMeta#getCustomModelData(), and now also check new fields for the richer component data.​

Set custom model data using .setCustomModelData(int data) or relevant component setters per new API documentation.

Updating item models by plugins should refer to the Paper developer documentation for updated methods. Legacy numeric-only custom model data will still work, but is deprecated for new features.​

You should now expect resource pack and model references to come from /assets/*/items/ rather than /models/item/.​​

Minecraft 1.21.4+ New Features Relevant to Custom Models
New biome, blocks, and mobs: Pale Garden biome, Pale Oak trees, Moss variants, Eyeblossoms, and the hostile Creaking mob.​

Resource Pack Format: Now at version 46, model selection and item component assignments changed (see above).

Model Properties & Selectors: Use selectors for model variants based on custom data, context dimension, display context, etc.​

Supported Model Selectors
String property selection (using selectors and cases)

Boolean property (e.g. based on item damage, carried state, etc.)

Numeric property selection (replacing the old predicate/overrides system completely)

In-Game Usage Examples
Give Command for Custom Items
Syntax (Paper 1.21.4+):

shell
/give PlayerName minecraft:paper{custom_model_data:{strings:["bat"]}}
or for numeric:

shell
/give PlayerName minecraft:paper{custom_model_data:{floats:[1001]}}
These commands set the item model from your pack using the "bat" or 1001, referencing entries in your data-driven item definition file (items/paper.json).​

Plugin Development Best Practices
Always check for the new API methods in Paper when manipulating item models.

For maximum compatibility, still set the numeric custom_model_data but prefer the new object-based assignment in plugins targeting Paper 1.21.4+.

Ensure your plugin provides fallback or legacy support for servers and resource packs still using older systems.

Migration Checklist
Update resource packs to move item model definitions to assets/namespace/items/*.json files.

Refactor any plugin code using predicates and overrides to leverage new selectors and cases in the item data files.

Use the expanded custom_model_data object and new Paper API methods for any custom item interaction.

Confirm that your models are referenced correctly, especially for items like shields, tridents, bundles, which have new behavior in inventory rendering.