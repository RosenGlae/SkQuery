package com.w00tmast3r.skquery.util.region;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CuboidRegion implements Iterable<Block> {

	private final Location pos1, pos2;
	private final int maxX, maxY, maxZ;
	private int nextX, nextY, nextZ;

	public CuboidRegion(Location pos1, Location pos2) {
		this.nextX = min().getBlockX();
		this.nextY = min().getBlockY();
		this.nextZ = min().getBlockZ();
		this.maxX = max().getBlockX();
		this.maxY = max().getBlockY();
		this.maxZ = max().getBlockZ();
		this.pos1 = pos1;
		this.pos2 = pos2;
	}

	@Override
	public Iterator<Block> iterator() {
		return new Iterator<Block>() {
			@Override
			public boolean hasNext() {
				return nextX != Integer.MIN_VALUE;
			}

			@Override
			public Block next() {
				if (!hasNext())
					throw new NoSuchElementException();
				Block result = new Location(pos1.getWorld(), nextX, nextY, nextZ).getBlock();
				forwardOne();
				forward();
				return result;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			public void forwardOne() {
				if (++nextX <= maxX)
					return;
				nextX = min().getBlockX();

				if (++nextY <= maxY)
					return;
				nextY = min().getBlockY();

				if (++nextZ <= maxZ)
					return;
				nextX = Integer.MIN_VALUE;
			}

			public void forward() {
				while (hasNext() && !checkHas(new BlockVector(nextX, nextY, nextZ))) {
					forwardOne();
				}
			}
		};
	}

	public Vector min() {
		return new Vector(Math.min(pos1.getBlockX(), pos2.getBlockX()), Math.min(pos1.getBlockY(), pos2.getBlockY()), Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
	}

	public Vector max() {
		return new Vector(Math.max(pos1.getBlockX(), pos2.getBlockX()), Math.max(pos1.getBlockY(), pos2.getBlockY()), Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
	}

	public boolean checkHas(Vector pt) {
		double x = pt.getX();
		double y = pt.getY();
		double z = pt.getZ();
		Vector min = min();
		Vector max = max();
		return (x >= min.getBlockX()) && (x <= max.getBlockX()) && (y >= min.getBlockY()) && (y <= max.getBlockY()) && (z >= min.getBlockZ()) && (z <= max.getBlockZ());
	}

}
