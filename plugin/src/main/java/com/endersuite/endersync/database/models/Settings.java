package com.endersuite.endersync.database.models;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 28.03.22
 */
@Table(name = "endersync_settings")
public class Settings {

    // must have 0-arg constructor
    public Settings() {
    }

    // can also have convenience constructor
    public Settings(String firstname) {
        this.firstname = firstname;
    }

    // primary key, generated on the server side
    @Id
    @GeneratedValue
    public long id;

    // a public property without getter or setter
    public String firstname;

}
