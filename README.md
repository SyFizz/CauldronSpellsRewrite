# SyMaintenance
![GitHub issues](https://img.shields.io/github/issues/SyFizz/CauldronSpellsRewrite?label=issues) ![GitHub All Releases](https://img.shields.io/github/downloads/SyFizz/CauldronSpellsRewrite/total?color=light%20green) ![GitHub release (latest by date)](https://img.shields.io/github/v/release/SyFizz/CauldronSpellsRewrite)

CauldronSpells is a Spigot plugin using cauldrons to add spells in the game.

## Installation

Download the latest release [here](https://github.com/SyFizz/SyMaintenance/releases) and put the jar files in your "plugins" folder.

NOTE : Please download the both plugins, because PlayEffect is a dependency of CauldronSpells.

## Usage

### Commands :
The main command of the plugin is `/cspell`

- `/cspell reload` Reloads the configuration file.

- `/cspell give <spellname> [player]` If a player is specified, this command gives a spell to him. Otherwise, you receive the spell. 
NOTE : The player must be online !

- `/cspell reset` resets the configuration to the default values.

- `/cspell help` Sends the help message to the sender.
### Permissions :

The permissions can be customized in the configuration file. If the `per_spell_permission` option is enabled, each spell need a different permission node per command.
By default, permissions nodes are :
- `/cspell help` : `cauldronspells.help`
- `/cspell reload` : `cauldronspells.reload`
- `/cspell reset` : `cauldronspells.reset`
- `/cspell give <spellname>` : `cauldronspells.give.self`
- `/cspell give <spellname> <player>` : `cauldronspells.give`
- Bypassing spells effects (like vanish effect) : `cauldronspells.bypass`

## Contribution
Pull requests are welcome, but if you want to do a major change, please open an issue first to discuss about what you want to modify.

Please do some tests before opening a pull request.

## License
[GNU GPLv3](https://choosealicense.com/licenses/gpl-3.0/)
