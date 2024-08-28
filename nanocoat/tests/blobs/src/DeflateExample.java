// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.nio.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

public class DeflateExample
{
	public static void main(String... __args)
		throws Throwable
	{
		/*
		byte[] seq =
			("Squirrels are really cute and adorable" +
			"they are also very energetic and curious." +
			"1234567890QWERTYUIOPASDFGHJKLZXCVBNM").getBytes("utf-8");*/
		byte[] seq =
			("squirrel squirrel squirrel squirrel squirrel squirrel " +
			"squirrel squirrel squirrel squirrel squirrel squirrel " +
			"squirrel squirrel squirrel squirrel squirrel squirrel ")
			.getBytes("utf-8");
		
		Map<Integer, byte[]> outputs = new LinkedHashMap<>();
		
		for (int level = 0; level <= 9; level++)
			for (int strategy : new int[]{0, Deflater.FILTERED,
				Deflater.HUFFMAN_ONLY})
			{
				Deflater d = new Deflater(level, true);
				
				d.setStrategy(strategy);
				
				d.setInput(seq);
				
				byte[] result = new byte[4096];
				int size = d.deflate(result, 0, result.length,
					Deflater.FULL_FLUSH);
				
				d.finish();
				
				size += d.deflate(result, size, result.length - size,
					Deflater.FULL_FLUSH);
				
				d.end();
				
				if (!outputs.containsKey(size))
				{
					outputs.put(size, result);
					System.err.printf("Defl (%d %d) %d -> %d%n",
						level, strategy, seq.length, size);
				}
			}
		
		for (Map.Entry<Integer, byte[]> output : outputs.entrySet())
		{
			int size = output.getKey();
			Files.write(Paths.get("defl" + size),
				Arrays.copyOf(output.getValue(), size));
		}
		//System.out.write(result, 0, size);
		//System.out.flush();
	}
}
