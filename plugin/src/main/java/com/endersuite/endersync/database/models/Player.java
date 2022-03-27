package com.endersuite.endersync.database.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 28.03.22
 */
@Table(name = "endersync_players")
public class Player {

    /**
     * Unique UUID of the minecraft player.
     */
    @Id
    private String uuid;

    @Column(name = "uuid", columnDefinition = "varchar(36)")
    public UUID getUuid() {
        return UUID.fromString(uuid);
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid.toString();
    }

    /**
     * Original player name.
     */
    @Getter @Setter
    public String name;

    /**
     * Player display name.
     */
    //@Getter @Setter
    public String displayName;

    /**
     * Last logon timestamp..
     */
    @Getter @Setter @Column(columnDefinition = "TIMESTAMP")
    public Timestamp lastLogon;

    /**
     * Whether a save/sync operation is currently in progress.
     * Should retry after X time if true.
     */
    @Getter @Setter @Column(columnDefinition = "TINYINT(1)")
    public boolean locked;


    public Player() {
        this.lastLogon = Timestamp.from(Instant.now());
        this.locked = false;
    }

    // can also have convenience constructor
    public Player(UUID uuid) {
        this();
        setUuid(uuid);
    }

}
