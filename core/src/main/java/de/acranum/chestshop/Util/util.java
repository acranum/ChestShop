package de.acranum.chestshop.Util;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class util {
    public static int getMaxShops(Player p) {
        int max = -1;
        for (PermissionAttachmentInfo perminfo : p.getEffectivePermissions()) {
            String perm = perminfo.getPermission();
            if (perm.startsWith("chestshop.max.")) {
                try {
                    int value = Integer.parseInt(perm.substring("chestshop.max.".length()));
                    if (value > max) {
                        max = value;
                    }
                }catch (NumberFormatException e) { }
            }
        }
        return max;
    }
}
