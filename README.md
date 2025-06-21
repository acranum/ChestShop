![ChestSHopBanner](https://github.com/user-attachments/assets/206cbca6-8efb-4fe6-87da-17b750eb5509)

## Description:
ChestShop is a simple plugin to optimize your server! It offers the function that players can create their own stores to earn money. The plugin is compatible with Vault and the Vault API and offers many setting options.

## Dependencies:
- [Vault](https://www.spigotmc.org/resources/vault.34315/)
- an economy plugin like [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/) (almost every plugin with economy)

## Create Shop:
1. To set the sold item, you should first put your item in the first slot in the chest
Place a sign on a chest `or` write a ? in the 4th line to specify the item later. To do that yust click the sign with the Item
2. Write in first Line `[Shop]` to create a shop / Write `[aShop]` to create an admin shop
3. in the 2nd line you can set the selling price (e.g. 100 = 100$)
4. now you can click on `Done`, and your store sign should have been created

![ChestShopExplain](https://github.com/user-attachments/assets/0e4ecf1d-c910-4c2c-9162-ee63b7a5917c)

⚠️When sneaking, a whole stack is bought (if available)

## Commands

- /chestshop info --> you have to look at a shop sign
    - gives info about the item for sale
    - important if you want to buy modified items (e.g. enchantments)
- /chestshop search + [item] --> Searches for shops with a specified item
## Permissions

```bash
chestshop.shopinfo
chestshop.create
chestshop.search
chestshop.buy
chestshop.sell
chestshop.break
chestshop.admincreate
chestshop.tp
```
