// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.interpreter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import net.multiphasicapps.squirreljme.os.interpreter.ContentType;

/**
 * This is used to write the string table as a content.
 *
 * @since 2016/07/23
 */
public class StringTableWriter
	extends InterpreterBaseOutput
{
	/** The string table. */
	protected final Map<String, Integer> table;
	
	/**
	 * Initializes the string table writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __data The string table data.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/23
	 */
	public StringTableWriter(InterpreterNamespaceWriter __nsw,
		Map<String, Integer> __table)
		throws NullPointerException
	{
		super(__nsw, "", ContentType.STRING_TABLE);
		
		// Check
		if (__table == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.table = __table;
	}
	
	/**
	 * Writes the string table.
	 *
	 * @throws IOException On write errors.
	 * @since 2016/07/23
	 */
	public void write()
		throws IOException
	{
		// Lock
		synchronized (this.lock)
		{
			// Go through the table
			Map<String, Integer> table = this.table;
			int n = table.size();
			
			// Write count
			DataOutputStream output = this.output;
			output.writeInt(n);
			
			// Write the contents
			for (String s : table.keySet())
			{
				// Write the hash code and the length
				output.writeInt(s.hashCode());
				int sn = s.length();
				output.writeInt(sn);
				
				// Write each character
				for (int i = 0; i < sn; i++)
					output.writeChar(s.charAt(i));
			}
		}
	}
}

