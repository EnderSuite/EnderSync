<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![GPLv3 License][license-shield]][license-url]
![Discord](https://img.shields.io/discord/313303575558356993?label=Discord&logo=discord&style=flat-square)

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/EnderSuite/EnderSync.svg?style=flat-square
[contributors-url]: https://github.com/EnderSuite/EnderSync/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/EnderSuite/EnderSync?style=flat-square
[forks-url]: https://github.com/EnderSuite/EnderSync/network
[stars-shield]: https://img.shields.io/github/stars/EnderSuite/EnderSync?style=flat-square
[stars-url]: https://github.com/EnderSuite/EnderSync/stargazers
[issues-shield]: https://img.shields.io/github/issues/EnderSuite/EnderSync?style=flat-square
[issues-url]: https://github.com/EnderSuite/EnderSync/issues
[license-shield]: https://img.shields.io/github/license/EnderSuite/EnderSync?style=flat-square
[license-url]: https://github.com/EnderSuite/EnderSync/blob/master/LICENSE

<!-- PROJECT HEADER -->
<br />
<p align="center">
  <a href="https://github.com/EnderSuite/EnderSync">
    <img src="" alt="Project Logo" >
  </a>

<h2 align="center">EnderSync</h2>

  <p align="center">
    A powerful data synchronization plugin for Minecraft servers.
    <br>
    <a href="#"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/EnderSuite/EnderSync/issues">Report Bug</a>
    ·
    <a href="https://github.com/EnderSuite/EnderSync/issues">Request Feature</a>
    ·
    <a href="https://discord.gg/sgRMJrZcZE">Discord</a>
  </p>
</p>

<!-- TABLE OF CONTENTS -->
## Table of Contents

- [Table of Contents](#table-of-contents)
- [About The Project](#about-the-project)
    - [Features](#features)
- [Contributing](#contributing)
- [Contact](#contact)

<!-- ABOUT THE PROJECT -->
## About The Project

EnderSync is a powerful tool for synchronizing data between multiple Minecraft Bukkit servers.
The plugin ...

We designed the plugin to be **Powerful** but **Performant**, **Easy to use** with **Stability** in mind.

### Features

#### Core
These are the core features of the plugin.

*Note: Modules can be configured: TODO: TEMPLATING*

- [ ] Compatible with versions 1.8 - 1.16
- [x] Easy to install & use
- [ ] Localization (See xxx)
- [ ] Pleasing visuals & sounds
- [ ] Configurable error handling
  - [ ] Ignore (Silent)
  - [ ] Kick player
  - [ ] Inform player
- [ ] Safety first (e.g. Anti duplication guard)
- [ ] In-Game management commands
  - [ ] Inv/End/Armor view
- [ ] Modules
  - [x] Health
  - [ ] Food
  - [ ] Air
  - [ ] Experience
  - [ ] Flight
  - [x] GameMode
  - [ ] (Potion) Effects
  - [ ] Location
    - [ ] World
    - [ ] Bed spawn point
    - [ ] Player position
  - [ ] Inventory (incl. armor & enderchest)
    - [ ] Normal items
    - [ ] Items with custom metadata & NBT
    - [ ] Placeholder items for synchronization across different server versions
  - [ ] Economy
    - [ ] Vault
  

#### Data storage & Performance

- [ ] Database
  - [ ] Store data inside any SQL database
  - [ ] Migration from MySqlPlayerDatabase plugin
- [x] Networking
  - [x] Connect multiple servers (nodes) into a cluster
  - [x] Data pre-caching (Drastically decreases sync. latency)
  
#### Extensibility
EnderSync allows 3td. parties to develop `extensions` which can add functionality to the plugin like the synchronization 
of additional data. We also provide an easy-to-use API, which enabled even more features (specially together with clustering)

- [ ] Custom extensions
- [ ] API
  - [ ] Methods for Saving / Syncing modules

#### Remote Management
We provide the option to enable remote access through the cluster config. This will allow authenticated used to manage data 
like player inventories from a web interface at [URL](#). If you want to use this feature [Remote management docs](#)

- [ ] Remote management through web-interface
  - [ ] View any stored data (table view)
  - [ ] Custom view for core modules like player inventories (View & Manipulate inventories of online & offline players)


<!-- CONTRIBUTING -->
## Contributing

Feel free to contribute to this project if you find something that is missing or can be optimized.
If you do so, please follow the following steps:

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


<!-- CONTACT -->
## Contact

Chat on Discord: ![Discord](https://img.shields.io/discord/313303575558356993?label=Discord&logo=discord&style=flat-square)

endersuite@maximilian-heidenreich.de - *Will change to contact@endersuite.com in the future*

Project Link: [https://github.com/EnderSuite/EnderSync](https://github.com/EnderSuite/EnderSync)

Project Icon: todo
