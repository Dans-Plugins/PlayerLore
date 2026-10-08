# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Changed

- `CONFIG.md` now says that `debugMode` currently has no effect, instead of describing debug logging the plugin never emits (tracked in #25). `COMMANDS.md` now says that the lore commands can only be used by a player.

## [2.1.0] – 2026-10-07

### Added

- Minecraft 26.3 is now a supported version, listed in `minecraft-versions.json` and the README.

### Changed

- The usage-reporting "Details" link (startup notice, `config.yml` and the docs) now points at https://danielstephenson.dev/usage-reporting, a public page; the previous link led to a private repository and returned 404 for everyone. The vendored trace client is now 0.6.1, which carries the same link in the `plugins/trace/config.yml` header it writes. Details: https://github.com/Stephenson-Software/trace-client-java/releases/tag/0.6.1.
- Every usage-reporting event now carries a random server ID, so servers can be counted rather than events. The first time reporting starts enabled, a `server-id` line is appended to `plugins/trace/config.yml`; it identifies no person, account or IP address, deleting the line gets a new one, and a disabled client never creates one. The vendored trace client is updated from 0.4.0 to 0.5.0, and the startup notice, `config.yml` comment, README and `CONFIG.md` mention the ID.
- Usage reporting now honours server-wide tags: a `tags:` block in `plugins/trace/config.yml` is added to every event sent by each plugin on the server that reports this way (the release gates write `ci: "true"` there, so test-server boots are left out of real-installation figures). Nothing changes for a server without a `tags:` block. The vendored trace client is updated from 0.2.0 to 0.3.0. `CONFIG.md` describes the block under `## Usage reporting`.
- Every usage-reporting event now carries the plugin version, so a `command` event can be tied to a release as well as a `startup` one; a `command` event still also carries the command's name. The vendored trace client is updated from 0.3.0 to 0.4.0, and the bundled `config.yml` comment and `CONFIG.md` say what each event carries.

## [2.0.0] – 2026-09-19

### Added

- The plugin now reports usage events — `startup` on enable, `command` on each of its commands — to the author's trace server so it is known which plugins are in use. Events carry the plugin name, the event name, and the plugin version or command name; nothing about players or the server. Reporting runs off the main thread, never delays a tick, and drops silently when the server is unreachable. A `config.yml` now ships in the jar carrying the `usage-reporting` block and the plugin's key, so reporting is active out of the box unless turned off; a server upgraded from a version before the block existed has it written into its `config.yml` on the next enable, so the switch is always on disk. The plugin logs on every enable whether reporting is on or off (and why). It is turned off with `usage-reporting.enabled: false` in this plugin's `config.yml`, for every plugin that reports to trace with `enabled: false` in `plugins/trace/config.yml` (created on the first start), or for the whole server process with the environment variable `TRACE_USAGE_REPORTING=off` or `DO_NOT_TRACK=1`
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
