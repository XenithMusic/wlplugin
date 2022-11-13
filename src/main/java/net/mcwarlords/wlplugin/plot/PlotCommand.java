package net.mcwarlords.wlplugin.plot;

import org.bukkit.command.*;
import org.bukkit.entity.*;

import net.mcwarlords.wlplugin.*;
import net.mcwarlords.wlplugin.util.*;

public class PlotCommand implements CommandExecutor {
  void sendHelpMessage(Player p) {
    p.sendMessage(Utils.escapeText("&_s======[ &_eWLPLOT &_s]======"));
    p.sendMessage(Utils.escapeText("&_p/wlplot h | help &_s- &_dDisplays this help text."));
    p.sendMessage(Utils.escapeText("&_p/wlplot c | claim &_s- &_dClaims a plot and teleports you to its center."));
    p.sendMessage(Utils.escapeText("&_p/wlplot w | wild &_s- &_dTeleports to a random location within one of your plots."));
  }

  static final String invalidArguments = Utils.escapeText("&_p* &_eInvalid arguments.");

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(!(sender instanceof Player)) {
      sender.sendMessage("wlplot can only be used by a player");
      return false;
    }
    Player p = (Player)sender;
    if(args.length == 0) {
      sendHelpMessage(p);
      return true;
    }
    switch(args[0]) {
      case "c":
      case "claim": {
        if(args.length != 1) {
          p.sendMessage(invalidArguments);
          return true;
        }
        PlayerData pd = Data.getPlayerData(p);
        if(pd.plots <= 0) {
          p.sendMessage(Utils.escapeText("&_p* &_eYou cannot claim any more plots."));
          return true;
        }
        pd.plots--;
        int id = Data.plotOwners.size();
        Data.plotOwners.add(Utils.getUUID(p));
        BoundingBox bb = Utils.plot(id);
        p.teleport(bb.center().toLocation());
        p.sendMessage(Utils.escapeText("&_p* &_dSuccessfully claimed plot. (Plot ID &_e#"+Integer.toString(id)+"&_d)"));
        p.sendMessage(Utils.escapeText("&_p* &_dYou have &_e"+Integer.toString(pd.plots)+"&_d plots remaining."));
        break;
      }
      case "w":
      case "wild": {
        int[] plots = Utils.plotsOwnedBy(p);
        if(plots.length == 0) {
          p.sendMessage(Utils.escapeText("&_p* &_eYou do not own any plots!"));
          return true;
        }
        p.teleport(Utils.plot(plots[Utils.randInt(0, plots.length)]).randomWithin().toLocation());
        break;
      }
      case "h":
      case "help":
        sendHelpMessage(p);
        break;
      default:
        p.sendMessage(Utils.escapeText("&_p* &_eInvalid subcommand "+args[0]+"."));
    }
    return true;
  }
}
