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

- `src/main/java/dansplugins/playerlore/` – Plugin source code
- `src/main/java/dansplugins/playerlore/commands/` – Command handlers (AddCommand, EditCommand, RemoveCommand, HelpCommand)
- `src/main/java/dansplugins/playerlore/services/` – ConfigService
- `src/main/resources/` – `plugin.yml`

## Coding Conventions

- Commands extend `AbstractPluginCommand` from the Ponder library.
- Config is managed through `ConfigService`.

## Contribution Workflow

- Branch from `main` for all changes.
- Open a pull request against `main`.
- Reference the related GitHub issue in every pull request description.
