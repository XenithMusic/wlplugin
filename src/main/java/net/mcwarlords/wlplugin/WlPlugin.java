package net.mcwarlords.wlplugin;

import org.bukkit.plugin.java.*;
import org.bukkit.scheduler.BukkitRunnable;

import net.mcwarlords.wlplugin.chat.ChatModule;
import net.mcwarlords.wlplugin.game.GameModule;
import net.mcwarlords.wlplugin.plot.PlotModule;

import java.util.*;

import org.bukkit.command.*;
import org.bukkit.event.*;

public class WlPlugin extends JavaPlugin {
  public static final String VERSION = "1.0.0";
  public static WlPlugin instance;
  public static Random rand;
  public static final char prefixCol    = '4';
  public static final char defaultCol   = '7';
  public static final char errorCol     = 'c';
  public static final char seperatorCol = '8';
  public static final int PLOT_SIZE     = 10000;

  ArrayList<Module> modules;

  @Override public void onEnable() {
    instance = this;
    getLogger().info("WlPlugin "+VERSION+" enabled");
    // create random
    rand = new Random();
    Data.onEnable();
    modules = new ArrayList<Module>();
    modules.add(new ChatModule());
    modules.add(new PlotModule());
    modules.add(new GameModule());
    for(Module m : modules)
      m.onEnable();
    // add autosave every 30 min
    new BukkitRunnable() {
      public void run() {
        Data.saveData();
      }
    }.runTaskTimer(this, 0, 36000);
  }

  @Override public void onDisable() {
    for(Module m : modules)
      m.onDisable();
    Data.onDisable();
    getLogger().info("WlPlugin "+VERSION+" disabled");
  }

  /** Add a listener to the server. */
  public static void addListener(Listener l) {
    instance.getServer().getPluginManager().registerEvents(l, instance);
  }

  /** Add a CommandExecutor to the server */
  public static void addCommand(String cmd, CommandExecutor exec) {
    instance.getCommand(cmd).setExecutor(exec);
  }

  /** Log to info. Equivalent to {@code WlPlugin.instance.getLogger().info(...)} */
  public static void info(String s) {
    instance.getLogger().info(s);
  }

  /** Log to warning. Equivalent to {@code WlPlugin.instance.getLogger().warning(...)} */
  public static void warning(String s) {
    instance.getLogger().warning(s);
  }

  /** Log to info and escape. Equivalent to {@code WlPlugin.info(Utils.escapeText(...))} */
  public static void infoe(String s) {
    instance.getLogger().info(Utils.escapeText(s));
  }

  /** Log to warning and escape. Equivalent to {@code WlPlugin.warning(Utils.escapeText(...))} */
  public static void warninge(String s) {
    instance.getLogger().warning(Utils.escapeText(s));
  }
}
