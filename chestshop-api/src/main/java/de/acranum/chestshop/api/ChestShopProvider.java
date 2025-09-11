package de.acranum.chestshop.api;

public class ChestShopProvider {
    private static ChestShopAPI instance;

    public static ChestShopAPI get() {
        return instance;
    }
    public static void register(ChestShopAPI api) {
        if (instance == null) {
            instance = api;
        }
    }
}
