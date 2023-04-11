package me.opkarol;

import me.realized.tokenmanager.api.TokenManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TokenManagerExtension {
    private final TokenManager tokenManager;

    public TokenManagerExtension() {
        tokenManager = (TokenManager) Bukkit.getServer().getPluginManager().getPlugin("TokenManager");
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void addTokens(@NotNull UUID uuid, int amount) {
        tokenManager.addTokens(uuid.toString(), amount);
    }

    public void addTokens(@NotNull String uuid, int amount) {
        tokenManager.addTokens(uuid, amount);
    }
}
