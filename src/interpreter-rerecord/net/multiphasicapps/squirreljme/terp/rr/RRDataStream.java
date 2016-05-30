// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp.rr;

import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.classpath.ClassPath;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;

/**
 * This handles the data stream which is used to read sessions which were
 * previously recorded and then to play them back.
 *
 * @since 2016/05/30
 */
public class RRDataStream
{
	/** This is the signature which is used in the header. */
	private final byte[] _HEADER_SIGNATURE =
		new byte[]{(byte)137, 13, 10, 26, 10, 0123, 0161, 0165, 0151, 0162,
			0162, 0145, 0154, 0112, 0115, 0105, 040, 0122, 0145, 0122, 0145,
			0143, 0157, 0162, 0144, 0151, 0156, 0147, 0040, 0111, 0156, 0164,
			0145, 0162, 0160, 0162, 0145, 0164, 0145, 0162, 040, 074, 0150,
			0164, 0164, 0160, 072, 057, 057, 0155, 0165, 0154, 0164, 0151,
			0160, 0150, 0141, 0163, 0151, 0143, 0141, 0160, 0160, 0163, 056,
			0156, 0145, 0164, 057, 076, 012};
	
	/** The lock to use. */
	protected final Object lock;
	
	/** The owning interpreter. */
	protected final RRInterpreter interpreter;
	
	/** The playback file. */
	private volatile DataInputStream _replay;
	
	/** The recording file. */
	private volatile DataOutputStream _record;
	
	/** The current re-record count. */
	private volatile int _rerecordcount;
	
	/**
	 * Initializes the data stream.
	 *
	 * @param __i The owning interpreter.
	 * @param __lk The interpreter lock.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/30
	 */
	public RRDataStream(RRInterpreter __i, Object __lk)
		throws NullPointerException
	{
		// Check
		if (__i == null || __lk == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.interpreter = __i;
		this.lock = __lk;
	}
	
	/**
	 * This adjusts the program to start on initial launch.
	 *
	 * @param __cp The {@link ClassPath} to adjust.
	 * @param __mm The The main method to adjust.
	 * @param __args The program arguments to adjust.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/30
	 */
	public void adjustProgramStart(ClassPath[] __cp, CIMethod[] __mm,
		String[][] __args)
		throws NullPointerException
	{
		// Check
		if (__cp == null || __mm == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Specifies that the given path should be used as input for a playback
	 * session.
	 *
	 * @param __p The source replay to run, if {@code null} then playback
	 * stops.
	 * @since 2016/05/30
	 */
	public void streamInput(Path __p)
	{
		// Lock
		synchronized (this.lock)
		{
			// Stop existing stream?
			if (__p == null)
			{
				// Stop reading from the given replay
				DataInputStream replay = this._replay;
				if (replay != null)
					try
					{
						replay.close();
					}
					catch (IOException e)
					{
						// Ignore
					}
				this._replay = null;
				
				// Do not continue
				return;
			}
			
			// Attempt opening the output
			FileChannel chan = null;
			try
			{
				// Open the channel
				chan = FileChannel.open(__p,
					StandardOpenOption.READ);
				
				// Setup input data
				DataInputStream dis = new DataInputStream(
					Channels.newInputStream(chan));
				
				// Read the signature to make sure it is correct
				byte[] want = _HEADER_SIGNATURE;
				byte[] curr = new byte[want.length];
				dis.read(curr);
				
				// {@squirreljme.error BC03 The signature of the previously
				// recorded session is not corrrect.}
				if (!Arrays.equals(want, curr))
					throw new IOException("BC03");
				
				// Set the re-record count as one above the previous value
				this._rerecordcount = dis.readInt() + 1;
				
				// Set
				this._replay = dis;
			}
			
			// {@squirreljme.error BC02 Could not open the input stream for
			// replaying back. (The playback path)}
			catch (IOException e)
			{
				// Close the channel if it is open
				if (chan != null)
					try
					{
						chan.close();
					}
					catch (Throwable t)
					{
						e.addSuppressed(t);
					}
				
				// Throw
				throw new RuntimeException(String.format("BC02 %s", __p), e);
			}
		}
	}
	
	/**
	 * Starts recording to the specified output stream.
	 *
	 * @param __p The file to write a recorded session to.
	 * @since 2016/05/30
	 */
	public void streamOutput(Path __p)
	{
		// Lock
		synchronized (this.lock)
		{
			// Stop existing stream?
			if (__p == null)
			{
				// Stop recording
				DataOutputStream record = this._record;
				if (record != null)
					try
					{
						record.close();
					}
					catch (IOException e)
					{
						// Ignore
					}
				this._record = null;
				
				// Do not continue
				return;
			}
			
			// Attempt opening the output
			FileChannel chan = null;
			try
			{
				// Open the channel
				chan = FileChannel.open(__p,
					StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
				
				// Setup output data
				DataOutputStream dos = new DataOutputStream(
					Channels.newOutputStream(chan));
				
				// Write the signature
				dos.write(_HEADER_SIGNATURE);
				
				// Write the current rerecord count
				dos.writeInt(this._rerecordcount);
				
				// Set
				this._record = dos;
			}
			
			// {@squirreljme.error BC04 Could not open the output path for
			// recording. (The output path)}
			catch (IOException e)
			{
				// Close the channel if it is open
				if (chan != null)
					try
					{
						chan.close();
					}
					catch (Throwable t)
					{
						e.addSuppressed(t);
					}
				
				// Throw
				throw new RuntimeException(String.format("BC04 %s", __p), e);
			}
		}	
	}
}

