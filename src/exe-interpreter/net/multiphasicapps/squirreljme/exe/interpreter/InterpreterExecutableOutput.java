// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.interpreter;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.exe.ExecutableOutput;

/**
 * This is able to link the blobs built for the interpreter as a single binary
 * which can then be run on the interpreter.
 *
 * @since 2016/07/24
 */
public class InterpreterExecutableOutput
	implements ExecutableOutput
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** System properties. */
	protected final Map<String, String> properties =
		new HashMap<>();
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/24
	 */
	@Override
	public void addSystemProperty(String __k, String __v)
		throws NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Map<String, String> properties = this.properties;
		synchronized (lock)
		{
			properties.put(__k, __v);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/24
	 */
	@Override
	public void linkBinary(OutputStream __os, String[] __names,
		InputStream[] __blobs)
		throws IOException, NullPointerException
	{
		// Check
		if (__os == null || __names == null || __blobs == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Map<String, String> properties = this.properties;
		synchronized (lock)
		{
			// Write output here
			DataOutputStream dos = new DataOutputStream(__os);
			
			// Go through all namespaces and just write their data
			int n = __names.length;
			byte[] buf = new byte[128];
			int lastaddr = -1;
			for (int i = 0; i < n; i++)
			{
				// Start position
				int datastart = dos.size();
				
				// Write data
				for (InputStream is = __blobs[i];;)
				{
					// Read
					int rc = is.read(buf);
					
					// EOF?
					if (rc < 0)
						break;
					
					// Write
					dos.write(buf, 0, rc);
				}
				
				// End position
				int dataend = dos.size();
				int datasize = dataend - datastart;
				
				// Write the last entry address along with the start and the
				// size of the data
				dos.writeInt(lastaddr);
				dos.writeInt(datastart);
				dos.writeInt(datasize);
				
				// Write the namespace name
				dos.writeUTF(__names[i]);
				
				// Set last address to point
				lastaddr = dataend;
			}
			
			// End the file with the last address point (points to the first
			// namespace that has been blobbed)
			dos.writeInt(lastaddr);
		}
	}
}

