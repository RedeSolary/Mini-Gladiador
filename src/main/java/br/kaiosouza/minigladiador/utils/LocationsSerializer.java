package br.kaiosouza.minigladiador.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationsSerializer {
	
	public static String serializeLocation(Location loc, boolean needYawAndPitch) {
		if(needYawAndPitch) {
			return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
		}else {
			return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ();
		}
	}

	public static Location deserializeLocation(String s) {
		if (s == null || s.trim().equals("")) {
			return null;
		}
		final String[] parts = s.split(";");
		if (parts.length == 6) {
			World w = Bukkit.getWorld(parts[0]);
			double x = Double.parseDouble(parts[1]);
			double y = Double.parseDouble(parts[2]);
			double z = Double.parseDouble(parts[3]);
			float yaw = Float.parseFloat(parts[4]);
			float pitch = Float.parseFloat(parts[5]);
			return new Location(w, x, y, z, yaw, pitch);
		}else if(parts.length == 4) {
			World w = Bukkit.getWorld(parts[0]);
			double x = Double.parseDouble(parts[1]);
			double y = Double.parseDouble(parts[2]);
			double z = Double.parseDouble(parts[3]);
			return new Location(w, x, y, z);
		}
		return null;
	}

}
