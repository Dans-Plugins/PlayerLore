# PlayerLore Configuration

The configuration file is located at `plugins/PlayerLore/config.yml`.

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `version` | String | *(plugin version)* | Plugin version. Do not edit manually. |
| `debugMode` | Boolean | `false` | Enables verbose debug logging to the console. |
| `usage-reporting.enabled` | Boolean | `true` | Whether the plugin reports usage events (see below). Set to `false` to turn it off. |
| `usage-reporting.endpoint` | String | `https://trace.danielstephenson.dev` | The trace server events are sent to. |
| `usage-reporting.key` | String | the plugin's key | Identifies this plugin to the trace server so reports are attributed to it. Not a secret: it ships in the default config and can only report as PlayerLore. Empty means reporting is off regardless of `enabled`. |

## Usage reporting

When the plugin is enabled, and each time one of its commands is used, a small event is sent to the
author's [trace](https://github.com/Stephenson-Software/trace#usage-reporting) server so it is known
which plugins are actually in use. An event carries the plugin's name, the event name (`startup` or
`command`), the plugin version, and for a `command` event the command name, plus a random server ID (the
`server-id` line in `plugins/trace/config.yml`, which identifies no person, account or IP address; deleting the
line gets a new one) — nothing about players or the world. Sending happens off the main thread, never delays a
tick, and is dropped silently if the
server cannot be reached. On every enable the plugin logs whether reporting is on or, if it is off,
why.

`plugins/trace/config.yml` can also carry a `tags:` block, and every tag in it is added to each event
sent by every plugin on the server that reports to trace. On a test or CI server, for example,
`ci: "true"` keeps its events out of real-installation figures:

```yaml
enabled: true
tags:
  ci: "true"
```

A tag the plugin sets itself (`name`, `version`) wins over a server-wide tag with the same name. The
block is read once, on enable, so a change takes effect after a restart. The file is created with the
example commented out, so no tags are added unless one is uncommented or written in.

To turn it off, in the order the plugin checks them:

- for the whole server process: set the environment variable `TRACE_USAGE_REPORTING=off` (or `false`,
  `0`, `no`) or `DO_NOT_TRACK=1` (or `true`, `yes`);
- for every plugin on the server that reports to trace: set `enabled: false` in
  `plugins/trace/config.yml`, which the plugin creates on the first start if it does not exist;
- for this plugin only: set `usage-reporting.enabled` to `false` in `plugins/PlayerLore/config.yml`.

The `usage-reporting` block is written to `config.yml` on a first run, when the plugin version
changes, and — if a `config.yml` from before the block existed lacks it — on the next enable, so the
switch is always present in the file on disk.
