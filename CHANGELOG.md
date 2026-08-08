# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

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
