// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support.dist;

import dev.shadowtail.palmos.PalmDatabaseBuilder;
import dev.shadowtail.palmos.PalmDatabaseType;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.javac.ZipCompilerOutput;

/**
 * Palm OS distribution.
 *
 * @since 2019/07/13
 */
public class PalmOSDist
	extends SummerCoatROM
{
	/** The size of a single fragment of the ROM. */
	public static final int ROM_FRAGMENT_SIZE =
		32768;
	
	/**
	 * Initializes the builder.
	 *
	 * @since 2019/07/13
	 */
	public PalmOSDist()
	{
		super("palmos");
	}
	
	/**
	 * Builds the ROM database.
	 *
	 * @param __out The stream to write to.
	 * @param __bp The build parameters.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	protected void buildRomDatabase(OutputStream __out, BuildParameters __bp)
		throws IOException, NullPointerException
	{
		if (__out == null || __bp == null)
			throw new NullPointerException("NARG");
		
		// Build the ROM first and get its bytes
		byte[] romdata = __bp.minimize();
		int romlen = romdata.length;
		
		// The ROM must be split into multiple fragments due to HotSync
		// limitation
		int numfrags = (romlen / ROM_FRAGMENT_SIZE) + 1;
		
		// Create builder for the database
		PalmDatabaseBuilder db = new PalmDatabaseBuilder(
			PalmDatabaseType.RESOURCE);
		
		// Set information on the database
		db.setName("SquirrelJME ROM");
		db.setType("mROM");
		db.setCreator("SjME");
		
		// Write ROM length
		try (DataOutputStream ent = new DataOutputStream(
			db.addEntry("RlEN", 0)))
		{
			ent.writeInt(romlen);
		}
		
		// Write fragment length
		try (DataOutputStream ent = new DataOutputStream(
			db.addEntry("FlEN", 0)))
		{
			ent.writeInt(ROM_FRAGMENT_SIZE);
		}
		
		// Write fragment count
		try (DataOutputStream ent = new DataOutputStream(
			db.addEntry("FnUM", 0)))
		{
			ent.writeInt(numfrags);
		}
		
		// Write every fragment to the database
		for (int i = 0, p = 0; i < numfrags; i++, p += ROM_FRAGMENT_SIZE)
		{
			// Write individual fragment
			try (DataOutputStream ent = new DataOutputStream(
				db.addEntry("FrOM", i)))
			{
				// Write the fragment data
				ent.write(romdata, p, Math.min(romlen - p, ROM_FRAGMENT_SIZE));
				
				// Make sure all fragments are the same size
				if (ent.size() < ROM_FRAGMENT_SIZE)
					ent.write(0);
			}
		}
		
		// Write the output
		db.write(__out);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/07/13
	 */
	@Override
	protected void generate(ZipCompilerOutput __zip, BuildParameters __bp)
		throws IOException, NullPointerException
	{
		if (__zip == null || __bp == null)
			throw new NullPointerException("NARG");
		
		// PalmOS (especially older versions) do not have a concept of a
		// file system so a ROM has to exist as a database
		try (OutputStream out = __zip.output("squirreljme-rom.prc"))
		{
			this.buildRomDatabase(out, __bp);
		}
		
		// Include the PRC that can run the actual ROM!
		todo.TODO.note("Include RatufaCoat squirreljme.prc for now!");
	}
}

