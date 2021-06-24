# BossShopPro

BossShopPro is one of the most player-friendly and multifunctional GUI plugins ever! Say goodbye to all the annoying
command- or signshops!
It can not only be used to create shops but for every kind of menu. Here are just a few examples:

* Shop (Buy or sell items)
* Kits
* A menu that allows players to execute commands with a simple click
* Server selector (when having multiple servers connected)
* Warp menu

## Project page

More information about the project can be found here: [Link](https://www.spigotmc.org/resources/222/). The page includes
a download-link as well.

## Building BossShopPro

A few notes regarding building BSP: the current state of the code, unfortunately, requires you to manually add (the jars
of) a few other plugins to the project in order to be able to successfully build BSP. As BSP includes support for many
different plugins, such as Kingdoms and EpicSpawners, you need to add those to the project (at least those, which I was
not able to add directly via Maven). It requires some initial effort to download those plugins and add them to the
project.

One (in my opinion a dirty one) workaround might be to just remove the code of those plugins you do not care about
anyways, skipping the step where you have to download the jars. This has been done by a few people already. Without
having tested the code myself, here is a commit of a fork, where it was already attempted to remove that code:
https://github.com/Megumiovo/BossShopPro/commit/319e8c13dfab33b73c1856428e638d1bc1b63698

## API

Information regarding the BossShopPro API can be found here: [Link](https://www.spigotmc.org/wiki/bossshoppro-api/).
