<!-- ![ChestSHopBanner](https://github.com/user-attachments/assets/489a95e5-b71d-4eaf-ab03-a68539b7201f) -->

# 💸 ChestShop 💸 

**ChestShop** is a lightweight, high-performance plugin that allows players to create their own item shops using chests and signs. It integrates seamlessly with Vault and supports all major economy plugins.

## Help:
If you need help or you want to report a bug please open a ticket on my [discord](https://discord.com/invite/TdwCTqJ4sZ) server or an issue on my [Github](https://github.com/acranum/ChestShop/issues)

## Dependencies:
- [Vault](https://www.spigotmc.org/resources/vault.34315/)
- an economy plugin (e.g., [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/))

## Installation
**Step 1 – Installing Dependencies:**
<br>
Ensure that Vault and an economy plugin (e.g., EssentialsX) are installed on your server.

**Step 2 – Downloading ChestShop:**
<br>
Get the latest version of ChestShop from the [SpigotMC resource page](https://www.spigotmc.org/resources/chestshop.114417/).

**Step 3 – Installing ChestShop:**
<br>
Move the ChestShop.jar file into your server's plugins directory.

**Step 4 – Load ChestShop:**
<br>
Restart your server to load the plugin.

**Step 5 – Configure Permissions:**
<br>
Adjust [permissions](#permissions) as needed to control who can create and interact with shops.
Compatible with plugins such as (LuckPerms, GroupManager, PowerRanks)


## Create Shop:
1. Place a sign on the chest
2. Write in first Line `Shop` to create a shop or write `aShop` to create an admin shop
3. in the second line you can set the selling price (e.g. 100 = 100$). If you want your chest to buy items from other players, just write a `b` before or behind the price
4. in the fourth line, you can specify the desired quantity of items that you want to sell or buy for the set price
5. If you now click on `Done`, you can set the item for the chestshop by right clicking the sign with the item you want to sell in your hand
6. Your shop should have been created

![ChestShopExplain](https://github.com/user-attachments/assets/0e4ecf1d-c910-4c2c-9162-ee63b7a5917c)

⚠️When sneaking, a whole stack is bought (if available)

## Community Tutorials:
[⏩| ChestShop Plugin [1.4] | Plugin Review     (Youtube)](https://www.youtube.com/watch?v=Kjj7P3YZJTE)

## [Addons](https://github.com/acranum/ChestShop-addons/blob/main/README.md)

To keep the plugin as simple as possible, there are **[Add-ons](https://github.com/acranum/ChestShop-addons)**. These allow you to add new, more advanced features to enhance the gaming experience. To use them, simply drag the downloaded jar file into the add-ons folder under ChestShop/addons and reload the plugin by executing the command `/chestshop reload`.
 - 🪄 [ChestShop Holographic items](https://github.com/acranum/ChestShop-addons/releases/download/1.0.0/chestshop-holograms-1.0.jar)

## Plugin Commands

- **/chestshop info** – View information about the item in the shop.
  - `EXECUTE COMMAND WHILE LOOKING AT A SHOP SIGN!`
- **/chestshop search + [item]** – Search for shops selling a specific item.
- **/chestshop reload** - Reload addons (as OP only)
- **/chestshop cleanup** - Clean up the config by removing invalid shops (as OP only)

## Permissions

```bash
chestshop.shopinfo
chestshop.create
chestshop.search
chestshop.buy
chestshop.admincreate
chestshop.tp
chestshop.interact  (allow players to open other players chests)
chestshop.max.x     (maximum amount of shops per player)
```

## Thank You! 🙏
Thank you for choosing ChestShop! I hope this plugin helps you build a player driven economy on your server.
