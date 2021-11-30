// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connection;
import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;

/**
 * Handles the scratch pad for i-mode/i-appli applications, this is a small
 * storage area within the application.
 *
 * @since 2021/11/30
 */
public class ScratchPadConnection
	implements InputConnection, OutputConnection
{
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	public DataInputStream openDataInputStream()
		throws IOException
	{
		return new DataInputStream(this.openInputStream());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	public DataOutputStream openDataOutputStream()
		throws IOException
	{
		return new DataOutputStream(this.openOutputStream());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	public OutputStream openOutputStream()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	public InputStream openInputStream()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * @param __part The resource to be opened.
	 * @return A connection to the i-appli storage area.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/11/30
	 */
	public static Connection open(String __part)
		throws NullPointerException
	{
		if (__part == null)
			throw new NullPointerException("NARG");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
