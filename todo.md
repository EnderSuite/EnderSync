
# db migrations
https://www.red-gate.com/simple-talk/databases/sql-server/database-administration-sql-server/using-migration-scripts-in-database-deployments/


# file structure

```
plugins/
    EnderSync/
        config.json
        lang-X.yml
        features.json
        lib/            - Libraries dep. on config: e.g. Grid Networking
        modules/        - Module configs
            health.json
            ...
```


# db structure

```
db. endersuite
    t. endersync_settings
    t. endersync_player
        id (uuid, index)
        name (str)
        displayName (str)
        locked (bool) -> Whether a save / sync operation is takin place -> Retry if true after 1 sec
    t. endersync_mod_x
        id (uuid, index) -> player uuid
        ... mod data
```