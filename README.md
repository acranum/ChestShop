![ChestSHopBanner](https://github.com/user-attachments/assets/206cbca6-8efb-4fe6-87da-17b750eb5509)

# 💸 ChestShop 💸 

**ChestShop** is a lightweight, high-performance plugin that allows players to create their own item shops using chests and signs. Designed for simplicity and speed, it integrates seamlessly with Vault and supports all major economy plugins. Whether you're running a survival server, a creative build, or a minigame hub, ChestShop provides a reliable and user-friendly marketplace solution.

## Help:
If you need help or you want to report a bug please open a ticket on my [discord](https://discord.com/invite/TdwCTqJ4sZ) server or an issue on my [Github](https://github.com/acranum/ChestShop/issues)

## Dependencies:
- [Vault](https://www.spigotmc.org/resources/vault.34315/)
- an economy plugin (e.g., [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/))

## Installation
#### Step 1 – Installing Dependencies:
Ensure that Vault and an economy plugin (e.g., EssentialsX) are installed on your server.

#### Step 2 – Downloading ChestShop:
Get the latest version of ChestShop from the [SpigotMC resource page](https://www.spigotmc.org/resources/chestshop.114417/).

#### Step 3 – Installing ChestShop:
Move the ChestShop.jar file into your server's plugins directory.

#### Step 4 – Testing ChestShop
Restart your server to load the plugin.

#### Step 5 – Configure Permissions
Adjust [permissions](#permissions) as needed to control who can create and interact with shops.
Compatible with plugins such as (LuckPerms, GroupManager, PowerRanks)


## Create Shop:
1. To set the sold item, you should first put your item in the first slot in the chest
Place a sign on a chest `or` write a ? in the 4th line to specify the item later. To do that yust click the sign with the Item
2. Write in first Line `[Shop]` to create a shop / Write `[aShop]` to create an admin shop
3. in the 2nd line you can set the selling price (e.g. 100 = 100$)
4. now you can click on `Done`, and your store sign should have been created

![ChestShopExplain](https://github.com/user-attachments/assets/0e4ecf1d-c910-4c2c-9162-ee63b7a5917c)

⚠️When sneaking, a whole stack is bought (if available)

## Addons <!--Add link to ADDON.md -->

To keep the plugin as simple as possible, there are **[Add-ons](https://github.com/acranum/ChestShop-addons)**. These allow you to add new, more advanced features to enhance the gaming experience. To use them, simply drag the downloaded jar file into the add-ons folder under ChestShop/addons and reload the plugin by executing the command `/chestshop reload`.
 - 🪄 [ChestShop Holographic items](https://github.com/acranum/ChestShop-addons/releases/download/1.0.0/chestshop-holograms-1.0.jar)

## Plugin Commands

- **/chestshop info** – View information about the item in the shop.
  - `EXECUTE COMMAND WHILE LOOKING AT A SHOP SIGN!`
- **/chestshop search + [item]** – Search for shops selling a specific item.

## Permissions

```bash
chestshop.shopinfo
chestshop.create
chestshop.search
chestshop.buy
chestshop.break
chestshop.admincreate
chestshop.tp
chestshop.interact
chestshop.max.x 
```

## Thank You! 🙏
Thank you for choosing ChestShop! I hope this plugin helps you build a thriving marketplace on your server. Your players’ buying and selling adventures are just a chest away!
