# Copilot Instructions

This repository follows the DPC (Dans Plugins Community) conventions defined at
https://github.com/Dans-Plugins/dpc-conventions. Read those conventions before
making any changes.

## Technology Stack

- Language: Java
- Build tool: Maven
- Target platform: Spigot / Paper (Minecraft plugin)
- API version: 1.13+

## Project Structure

- `src/main/java/dansplugins/playerlore/` – Plugin source code (`PlayerLore` is the plugin main class)
- `src/main/java/dansplugins/playerlore/commands/` – Command handlers (AddCommand, EditCommand, RemoveCommand, HelpCommand, DefaultCommand)
- `src/main/java/dansplugins/playerlore/services/` – ConfigService
- `src/main/java/dansplugins/playerlore/trace/` – TraceClient (usage reporting)
- `src/main/java/dansplugins/playerlore/utils/` – Logger
- `src/main/resources/` – `plugin.yml` and the default `config.yml`
- `src/test/java/dansplugins/playerlore/` – JUnit 5 tests, mirroring the main package layout

## Coding Conventions

- Commands extend `AbstractPluginCommand` from the Ponder library.
- Config is managed through `ConfigService`.

## Contribution Workflow

- Branch from `main` for all changes.
- Open a pull request against `main`.
- Reference the related GitHub issue in every pull request description.
