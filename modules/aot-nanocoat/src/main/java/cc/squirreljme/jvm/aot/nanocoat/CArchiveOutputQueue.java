// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CFile;
import cc.squirreljme.c.CFileName;
import net.multiphasicapps.zip.queue.ArchiveOutputQueue;
import java.io.IOException;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * C archive output queue.
 *
 * @since 2023/12/04
 */
public class CArchiveOutputQueue
	extends ArchiveOutputQueue
{
	/**
	 * Initializes the archive output.
	 *
	 * @param __zip The Zip to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/04
	 */
	public CArchiveOutputQueue(ZipStreamWriter __zip)
		throws NullPointerException
	{
		super(__zip);
	}
	
	/**
	 * Builds a new C File for output.
	 *
	 * @param __name The name of the file.
	 * @return The resultant C File.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public CFile nextCFile(String __name)
		throws IOException, NullPointerException
	{
		// Setup new C file and make sure it has a standard header
		CFile result = Utils.cFile(this.nextEntry(__name));
		Utils.headerC(result);
		
		return result;
	}
	
	/**
	 * Builds a new C File for output.
	 *
	 * @param __name The name of the file.
	 * @return The resultant C File.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/13
	 */
	public CFile nextCFile(CFileName __name)
		throws IOException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		return this.nextCFile(__name.toString());
	}
}
