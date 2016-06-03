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
	 * @param __i The owning interpreter, which is also locked on.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/30
	 */
	public RRDataStream(RRInterpreter __i)
		throws NullPointerException
	{
		// Check
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.interpreter = __i;
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
	 * Returns {@code true} if a replay is currently being replayed.
	 *
	 * @return {@code true} if a replay is being performed.
	 * @since 2016/06/03
	 */
	public final boolean isPlaying()
	{
		// Lock
		synchronized (this.interpreter)
		{
			return this._replay != null;
		}
	}
	
	/**
	 * Returns {@code true} if the stream is currently recording.
	 *
	 * @return {@code true} if the stream is recording.
	 * @since 2016/06/03
	 */
	public final boolean isRecording()
	{
		// Lock
		synchronized (this.interpreter)
		{
			return this._record != null;
		}
	}
	
	/**
	 * Records the given packet to the output recording stream.
	 *
	 * @param __pk The packet to record.
	 * @throws RRDataStreamException If there was an error writing the data.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/01
	 */
	public final void record(RRDataPacket __pk)
		throws RRDataStreamException, NullPointerException
	{
		// Check
		if (__pk == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.interpreter)
		{
			// {@squirreljme.error BC0b The stream is not in recording mode.}
			DataOutputStream dos = this._record;
			if (dos == null)
				throw new RRNotRecordingException("BC0b");
			
			// Could fail
			try
			{
				// Read the packet command and length
				RRDataCommand cmd = __pk.getCommand();
				int len = __pk.length();
				
				// Write the command code and the length
				dos.writeByte((byte)cmd.ordinal());
				dos.writeShort((short)len);
				
				// Go through all packet values and write them
				for (int i = 0; i < len; i++)
				{
					// Get the type of data stored here
					Object v = __pk.get(i);
					RRDataType dt = RRDataType.of(v);
					
					// {@squirreljme.error BC0g Could not obtain the data type
					// to be used for the given argument of a given class.
					// (The index; The class type)}
					if (dt == null)
						throw new RRDataStreamException(String.format("BC0g",
							i, v.getClass()));
					
					// Write the value code
					dos.writeByte((byte)dt.ordinal());
					
					// Write its actual value storage
					dt.write(dos, v);
				}
			}
			
			// {@squirreljme.error BC0a Failed to record the packet to the
			// output stream.}
			catch (IOException e)
			{
				throw new RRDataStreamException("BC0a", e);
			}
		}
	}
	
	/**
	 * Specifies that the given path should be used as input for a playback
	 * session.
	 *
	 * @param __p The source replay to run, if {@code null} then playback
	 * stops.
	 * @throws RRDataStreamException If the stream could not be opened for
	 * replaying.
	 * @since 2016/05/30
	 */
	public final void streamInput(Path __p)
		throws RRDataStreamException
	{
		// The Interpreter
		RRInterpreter terp = this.interpreter;
		
		// Lock
		synchronized (this.interpreter)
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
				
				// Set
				this._replay = dis;
			}
			
			// {@squirreljme.error BC0d Could not open the input stream for
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
				throw new RRDataStreamException(String.format("BC0d %s", __p),
					e);
			}
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * Starts recording to the specified output stream.
	 *
	 * @param __p The file to write a recorded session to.
	 * @throws RRDataStreamException If the stream could not be opened.
	 * @since 2016/05/30
	 */
	public final void streamOutput(Path __p)
		throws RRDataStreamException
	{
		// The Interpreter
		RRInterpreter terp = this.interpreter;
		
		// Lock
		synchronized (this.interpreter)
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
				
				// Set
				this._record = dos;
			}
			
			// {@squirreljme.error BC0c Could not open the output path for
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
				throw new RRDataStreamException(String.format("BC0c %s", __p),
					e);
			}
			
			// Write the magic number
			try (RRDataPacket pk = createPacket(RRDataCommand.MAGIC_NUMBER, 2))
			{
				// Record the magic number
				pk.set(0, MAGIC_NUMBER_A);
				pk.set(1, MAGIC_NUMBER_B);
				
				// Record it
				record(pk);
			}
			
			// Set the Java instructions per second
			try (RRDataPacket pk = createPacket(RRDataCommand.SET_JIPS, 1))
			{
				// Increase it by 1
				pk.set(0, this._rerecordcount + 1);
				
				// Record it
				record(pk);
			}
			
			// Write virtual machine configuration details
			try (RRDataPacket pk = createPacket(RRDataCommand.HOST_VM, 1))
			{
				// Add some notes
				pk.set(0, new String[]
					{
						"SquirrelJME",
						"http://multiphasicapps.net/",
						
						"microedition.platform",
						System.getProperty("microedition.platform"),
						
						"microedition.encoding",
						System.getProperty("microedition.encoding"),
						
						"microedition.configuration",
						System.getProperty("microedition.configuration"),
						
						"microedition.profiles",
						System.getProperty("microedition.profiles"),
						
						"java.version",
						System.getProperty("java.version"),
						
						"java.vendor",
						System.getProperty("java.vendor"),
						
						"java.vendor.url",
						System.getProperty("java.vendor.url"),
						
						"java.vm.name",
						System.getProperty("java.vm.name"),
						
						"java.vm.version",
						System.getProperty("java.vm.version"),
						
						"java.vm.vendor",
						System.getProperty("java.vm.vendor"),
						
						"os.name",
						System.getProperty("os.name"),
						
						"os.arch",
						System.getProperty("os.arch"),
						
						"os.version",
						System.getProperty("os.version"),
						
						"line.separator",
						System.getProperty("line.separator"),
						
						"path.separator",
						System.getProperty("path.separator"),
						
						"file.separator",
						System.getProperty("file.separator"),
					});
				
				// Record it
				record(pk);
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

