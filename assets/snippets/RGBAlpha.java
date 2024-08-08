// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

/**
 * Generates RGB and Alpha table mappings.
 * 
 * @since 2024/07/11
 */
public final class RGBAlpha
{
	/** Sequence of values for ranges. */
	private static final int[] _SEQ =
		new int[]{0x00, 0x11, 0x33, 0x66, 0x80, 0x99, 0xCC, 0xEE, 0xFF};
	
	/**
	 * Main entry point.
	 *
	 * @param __args Ignored.
	 * @throws IOException On write errors.
	 * @since 2024/07/11
	 */
	public static void main(String... __args)
		throws IOException
	{
		int n = RGBAlpha._SEQ.length;
		
		// Generate base table of colors
		int[] rgb = new int[n * n * n];
		for (int ri = 0, at = 0; ri < n; ri++)
			for (int gi = 0; gi < n; gi++)
				for (int bi = 0; bi < n; bi++)
				{
					rgb[at++] = (RGBAlpha._SEQ[ri] << 16) |
						(RGBAlpha._SEQ[gi] << 8) |
						(RGBAlpha._SEQ[bi]);
				}
		
		// Dump base color set
		RGBAlpha.__dump(Paths.get("______.rgb"), rgb);
		
		int len = rgb.length;
		int dim = (int)Math.ceil(Math.sqrt(len));
		
		// Go through each channel value
		for (int fi = 0; fi < n; fi++)
			for (int ti = 0; ti < n; ti++)
			{
				int fromA = RGBAlpha._SEQ[fi];
				int toA = RGBAlpha._SEQ[ti];
				
				BufferedImage from = new BufferedImage(dim, dim,
					BufferedImage.TYPE_INT_ARGB);
				BufferedImage to = new BufferedImage(dim, dim,
					BufferedImage.TYPE_INT_ARGB);
				
				int[] fromB = ((DataBufferInt)from.getRaster().getDataBuffer())
					.getData();
				int[] toB = ((DataBufferInt)to.getRaster().getDataBuffer())
					.getData();
				
				int actA = fromA;/*(fromA & (~0b1111)) | (fromA >>> 4);*/
				int actB = toA;/*(toA & (~0b1111)) | (toA >>> 4);*/
				
				for (int i = 0, q = rgb.length; i < q; i++)
				{
					fromB[i] = (actA << 24) | rgb[i];
					toB[(rgb.length - 1) - i] = (actB << 24) | rgb[i];
				}
				
				// Blend into the target
				Graphics g = from.getGraphics();
				g.drawImage(to, 0, 0, null);
				
				ImageIO.write(from, "png",
					new File(String.format("_%02x_%02x.png", fromA, toA)));
				
				// Dump to file
				RGBAlpha.__dump(Paths.get(String.format("_%02x_%02x.rgb", 
					fromA, toA)), fromB);
			}
	}
	
	/**
	 * Dumps the colors. 
	 *
	 * @param __to Where to write to.
	 * @param __colors The colors to write.
	 * @throws IOException On write errors.
	 * @since 2024/07/11
	 */
	private static void __dump(Path __to, int... __colors)
		throws IOException
	{
		ByteBuffer buf = ByteBuffer.allocate(__colors.length * 4);
		buf.order(ByteOrder.BIG_ENDIAN);
		buf.asIntBuffer().put(IntBuffer.wrap(__colors));
		
		byte[] data;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 ZipOutputStream zip = new ZipOutputStream(baos))
		{
			zip.setLevel(9);
			zip.setMethod(ZipOutputStream.DEFLATED);
			
			zip.putNextEntry(new ZipEntry(__to.getFileName().toString()));
			
			zip.write(buf.array());
			
			zip.closeEntry();
			
			zip.finish();
			zip.flush();
			
			data = baos.toByteArray();
		}
		
		Files.write(__to, data);
	}
}
