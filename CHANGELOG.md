# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Added

- The plugin now reports usage events — `startup` on enable, `command` on each of its commands — to the author's trace server so it is known which plugins are in use. Events carry the plugin name, the event name, and the plugin version or command name; nothing about players or the server. Reporting runs off the main thread, never delays a tick, drops silently when the server is unreachable, and is turned off with `usage-reporting.enabled: false` in `config.yml`. A `config.yml` now ships in the jar carrying the `usage-reporting` block and the plugin's key, so reporting is active out of the box unless turned off — including on servers upgraded from a version before the block existed, whose `config.yml` is only rewritten on a version change: the plugin reads the bundled defaults for any key the file lacks
- A `Dev Release` workflow, which republishes a rolling `dev` prerelease of `main` on every non-documentation push. This is what Dan's Plugin Manager's experimental channel installs from: `/dpm get playerlore --experimental` reads `releases/tags/dev`, so without it there is nothing for that command to download. The prerelease is unreleased, unreviewed code and is marked as such.

### Removed

- Two config-option branches in `ConfigService.setConfigOption` that parsed options named `A` and `C` as an integer and a double. Neither is a real config option — `CONFIG.md` and `saveMissingConfigDefaultsIfNotPresent` both list only `version` and `debugMode` — and both branches were leftovers from the plugin template this project was scaffolded from.

### Fixed

- The `Dev Release` workflow now retries publishing the `dev` prerelease before giving up. The release and its tag have to be deleted and recreated for the tag to move to the new commit, and a transient API failure inside that window previously left the repository with no `dev` release at all until the workflow was re-run by hand. Each attempt now starts from a clean slate, and an exhausted retry fails loudly.
- The bare `/pl` command advertised a wiki URL under the plugin's former `dmccoystephenson` owner; it now points at `https://github.com/Dans-Plugins/PlayerLore/wiki`, matching every other link the project publishes.
- The bare `/pl` command is now listed by `/pl help`, in `COMMANDS.md`, and in `USER_GUIDE.md`. It has always printed the plugin version, credits and wiki link, but was named in none of the three, so the only way to discover it was to type it.

## [2.0.0-SNAPSHOT-8-8-2026] – 2026-08-08

### Changed
- PlayerLore is now developed AI-first. Day-to-day feature work, grooming, review and maintenance run through AI agents working directly against this repository, with the maintainers setting direction and approving what lands. The major version bump marks that change in how the project is built — it is not a break in behaviour, configuration or stored data, and existing installations can upgrade in place. Released as `2.0.0-SNAPSHOT-8-8-2026`: the AI-first line has not yet been verified in live operation, and the dated snapshot designation stays until it has.

### Fixed
- `/pl edit` and `/pl remove` now treat `lineIndex` as 1-based, matching `COMMANDS.md`/`USER_GUIDE.md`, instead of silently indexing into the lore list as 0-based
- `/pl edit` and `/pl remove` no longer throw an uncaught exception when given a non-numeric index or no index at all; they now send a player-facing error message
- `/pl add` no longer throws an uncaught exception when invoked with no arguments; it now sends the usage message, matching `/pl edit` and `/pl remove`
- The default (no-argument) command no longer tells players to type the non-existent `/lp help`; it now correctly says `/pl help`

## [1.1]

### Added
- `/pl add`, `/pl edit`, `/pl remove` commands for managing item lore
- `/pl help` command
- `debugMode` config option
