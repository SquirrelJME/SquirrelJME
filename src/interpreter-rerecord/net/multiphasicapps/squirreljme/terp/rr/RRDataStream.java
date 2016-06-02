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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
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
import java.util.Deque;
import java.util.LinkedList;
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
	/** The primary magic number. */
	public static final long MAGIC_NUMBER_A =
		0x537175697272656CL;
	
	/** The secondary magic number. */
	public static final long MAGIC_NUMBER_B =
		0x4A4D45214147504CL;
	
	/** The lock to use. */
	protected final Object lock;
	
	/** The owning interpreter. */
	protected final RRInterpreter interpreter;
	
	/** Packet queue which is used for reading and writing. */
	private final Deque<Reference<RRDataPacket>> _packetq =
		new LinkedList<>();
	
	/** The playback file. */
	private volatile DataInputStream _replay;
	
	/** The recording file. */
	private volatile DataOutputStream _record;
	
	/** The current re-record count (starts at -1 so next record is zero). */
	private volatile int _rerecordcount =
		-1;
	
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
	 * Creates a new data packet, the returned packet may be recycled from
	 * a previous call. To finish with a packet it can be closed and as such
	 * packets can be used with try-with-resources.
	 *
	 * @param __com The command to use.
	 * @param __l The length of the packet.
	 * @throws IllegalArgumentException If the length is negative or exceeds
	 * the packet field limit.
	 * @throws NullPointerException On null arguments.
	 */
	public final RRDataPacket createPacket(RRDataCommand __com, int __l)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__com == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BC02 Cannot request a data packet which exceeds
		// the length limitation or is negative. (The requested length)}
		if (__l < 0 || __l >= RRDataPacket.MAX_FIELD_LENGTH)
			throw new IllegalArgumentException(String.format("BC02 %d", __l));
		
		// Lock
		Deque<Reference<RRDataPacket>> packetq = this._packetq;
		synchronized (packetq)
		{
			// Check the queue
			RRDataPacket rv = null;
			while (!packetq.isEmpty())
			{
				// Is there something in the queue?
				Reference<RRDataPacket> ref = packetq.pollFirst();
				
				// Use it
				if (ref != null && null != (rv = ref.get()))
					break;
			}
			
			// Create a new one?
			if (rv == null)
				rv = new RRDataPacket(this);
			
			// Initialize the data
			rv.__clear(__com, __l);
			
			// Return it
			return rv;
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
	public final void streamInput(Path __p)
	{
		// The Interpreter
		RRInterpreter terp = this.interpreter;
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Starts recording to the specified output stream.
	 *
	 * @param __p The file to write a recorded session to.
	 * @since 2016/05/30
	 */
	public final void streamOutput(Path __p)
	{
		// The Interpreter
		RRInterpreter terp = this.interpreter;
		
		// Lock
		synchronized (this.lock)
		{
			// Write the magic number
			try (RRDataPacket pk = createPacket(RRDataCommand.MAGIC_NUMBER, 2))
			{
				// Record the magic number
				pk.set(0, MAGIC_NUMBER_A);
				pk.set(1, MAGIC_NUMBER_B);
				
				if (true)
					throw new Error("TODO");
			}
			
			// Set the Java instructions per second
			try (RRDataPacket pk = createPacket(RRDataCommand.SET_JIPS, 1))
			{
				pk.set(0, this._rerecordcount);
				
				if (true)
					throw new Error("TODO");
			}
		}
	}
	
	/**
	 * Closes the given packet and places it within the queue.
	 *
	 * @param __pk The packet to close.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/01
	 */
	final void __close(RRDataPacket __pk)
		throws NullPointerException
	{
		// Check
		if (__pk == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Deque<Reference<RRDataPacket>> packetq = this._packetq;
		synchronized (packetq)
		{
			packetq.offerLast(new WeakReference<>(__pk));
		}
	}
}

