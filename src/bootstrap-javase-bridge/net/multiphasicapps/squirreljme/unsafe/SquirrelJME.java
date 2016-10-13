// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.util.Map;
import java.util.NoSuchElementException;
import net.multiphasicapps.squirreljme.ipcmailbox.PostBox;
import net.multiphasicapps.squirreljme.ipcmailbox.PostBase;
import net.multiphasicapps.squirreljme.ipcmailbox.PostDestination;
import net.multiphasicapps.squirreljme.ipcmailbox.PostOffice;
import net.multiphasicapps.squirreljme.midletid.MidletSuiteID;
import net.multiphasicapps.squirreljme.midletid.MidletVersion;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This class contains implementations of unsafe SquirrelJME operations which
 * are not part of the bootstrap's external build dependencies. This in the
 * general case will only have socket related code.
 *
 * For documenatation for these methods, see the class using this name in
 * the {@code squirreljme-internal} project.
 *
 * @since 2016/10/11
 */
public final class SquirrelJME
{
	/** Post destination. */
	private static final Map<Integer, PostDestination> _POST_DESTS =
		new SortedTreeMap<>();
	
	/** Post mailboxes. */
	private static final Map<Integer, PostBox> _POST_BOXES =
		new SortedTreeMap<>();
	
	/** Post destination next id. */
	private static volatile int _nextpostdest =
		-1;
	
	/** Next mailbox id. */
	private static volatile int _nextboxid =
		1;
	
	/**
	 * Not used.
	 *
	 * @since 2016/10/11
	 */
	private SquirrelJME()
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * As duplicated.
	 *
	 * @return As duplicated.
	 * @since 2016/10/11
	 */
	public static boolean isKernel()
	{
		// Always runs on the kernel
		return true;
	}
	
	/**
	 * As duplicated.
	 *
	 * @return As duplicated.
	 * @since 2016/10/11
	 */
	public static boolean isSquirrelJME()
	{
		// Never is SquirrelJME
		return false;
	}
	
	/**
	 * As duplicated.
	 *
	 * @param __id As duplicated.
	 * @return As duplicated.
	 * @throws IllegalArgumentException As duplicated.
	 * @throws InterruptedException As duplicated.
	 * @since 2016/10/13
	 */
	public static int mailboxAccept(int __ld)
		throws IllegalArgumentException, InterruptedException
	{
		// Look in the destination map
		Map<Integer, PostDestination> postdests = SquirrelJME._POST_DESTS;
		PostDestination dest;
		synchronized (postdests)
		{
			// {@squirreljme.error DE0d The post destination is not valid.
			// (The identifier)}
			dest = postdests.get(__ld);
			if (dest == null)
				throw new IllegalArgumentException(String.format(
					"DE0d %s", __ld));
		}
		
		// Accept post office connect
		PostOffice po = dest.accept();
		
		// Add mailbox to the list of post boxes 
		return __addPostBox(po.server());
	}
	
	/**
	 * As duplicated.
	 *
	 * @param __mb As duplicated.
	 * @param __mo As duplicated.
	 * @param __ml As duplicated.
	 * @param __sb As duplicated.
	 * @param __so As duplicated.
	 * @param __sl As duplicated.
	 * @param __v As duplicated.
	 * @param __am As duplicated.
	 * @throws BindException As duplicated.
	 * @throws IllegalArgumentException As duplicated.
	 * @throws NullPointerException As duplicated.
	 * @since 2016/10/13
	 */
	public static int mailboxConnect(byte[] __mb, int __mo, int __ml,
		byte[] __sb, int __so, int __sl, int __v, boolean __am)
		throws ArrayIndexOutOfBoundsException, BindException,
			IllegalArgumentException, NullPointerException
	{
		// {@squirreljme.error DE0e Cannot connect to named MIDlets via
		// mailboxes in the bridged Java SE connection.}
		if (__mb != null)
			throw new BindException("DE0e");
		
		// Get server name and version
		MidletVersion ver = new MidletVersion(true, __v);
		String name;
		try
		{
			name = new String(__sb, __so, __sl, "utf-8");
		}
		
		// Should never happen
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("OOPS", e);
		}
		
		// Go through all destinations to look for a connection
		Map<Integer, PostDestination> postdests = SquirrelJME._POST_DESTS;
		synchronized (postdests)
		{
			// Go through all destinations and locate the given server
			for (PostDestination d : postdests.values())
				if (d.serverName().equals(name) &&
					ver.atLeast(d.serverVersion()))
					for (;;)
						try
						{
							// Connect and obtain a post office
							PostOffice po = d.connect();
					
							// Add mailbox to the list of post boxes 
							return __addPostBox(po.client());
						}
						catch (InterruptedException e)
						{
							// Ignore interruptions and retry
						}
			
			// {@squirreljme.error DE0f Could not find a connected to the
			// specified server. (The server name; The server version)}
			throw new BindException(String.format("DE0f %s %s",
				name, ver));
		}
	}
	
	/**
	 * As duplicated.
	 *
	 * @param __b As duplicated.
	 * @param __o As duplicated.
	 * @param __l As duplicated.
	 * @param __v As duplicated.
	 * @param __am As duplicated.
	 * @return As duplicated.
	 * @throws ArrayIndexOutOfBoundsException As duplicated.
	 * @throws BindException As described.
	 * @throws IllegalArgumentException As duplicated.
	 * @throws NullPointerException As duplicated.
	 * @since 2016/10/13
	 */
	public static int mailboxListen(byte[] __b, int __o, int __l, int __v,
		boolean __am)
		throws ArrayIndexOutOfBoundsException, BindException,
			IllegalArgumentException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Decode name and version
		MidletVersion ver = new MidletVersion(true, __v);
		String name;
		try
		{
			name = new String(__b, __o, __l, "utf-8");
		}
		
		// Should never happen
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("OOPS", e);
		}
		
		// Create destination
		PostDestination dest = new PostDestination(name, ver, __am);
		
		// Store the destinations
		Map<Integer, PostDestination> postdests = SquirrelJME._POST_DESTS;
		synchronized (postdests)
		{
			// Use linear ID progression
			int origpostdest = SquirrelJME._nextpostdest;
			for (int nextpostdest = (origpostdest - 1) | 0x8000_0000;;
				nextpostdest--)
			{
				Integer b = Integer.valueOf(nextpostdest);
				if (!postdests.containsKey(b))
				{
					// Store
					postdests.put(b, dest);
					
					// Next destination
					SquirrelJME._nextpostdest =
						((nextpostdest - 1) | 0x8000_0000);
					return nextpostdest;
				}
				
				// If there are no more mailbox listener descriptors, then
				// wait for some to be freed.
				if (origpostdest == nextpostdest)
					try
					{
						postdests.wait();
					}
					catch (InterruptedException e)
					{
					}
			}
		}
	}
	
	/**
	 * As duplicated.
	 *
	 * @param __fd As duplicated.
	 * @param __chan As duplicated.
	 * @param __b As duplicated.
	 * @param __o As duplicated.
	 * @param __l As duplicated.
	 * @param __wait As duplicated.
	 * @return As duplicated.
	 * @throws ArrayIndexOutOfBoundsException As duplicated.
	 * @throws IllegalArgumentException As duplicated.
	 * @throws InterruptedException As duplicated.
	 * @throws NoSuchElementException As duplicated.
	 * @throws NullPointerException As duplicated.
	 * @since 2016/10/13
	 */
	public static int mailboxReceive(int __fd, int[] __chan, byte[] __b,
		int __o, int __l, boolean __wait)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			InterruptedException, NoSuchElementException, NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * As duplicated.
	 *
	 * @param __fd As duplicated.
	 * @return As duplicated.
	 * @throws IllegalArgumentException As duplicated.
	 * @since 2016/10/13
	 */
	public static byte[] mailboxRemoteID(int __fd)
		throws IllegalArgumentException
	{
		// Use a precomposed string since it is the same regardless
		try
		{
			return "Steven Gawroriski;SquirrelJME;0.0.2".getBytes("utf-8");
		}
		
		// Should not occur
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("OOPS", e);
		}
	}
	
	/**
	 * As duplicated.
	 *
	 * @param __fd As duplicated.
	 * @param __chan As duplicated.
	 * @param __b As duplicated.
	 * @param __o As duplicated.
	 * @param __l As duplicated.
	 * @throws ArrayIndexOutOfBoundsException As duplicated.
	 * @throws IllegalArgumentException As duplicated.
	 * @throws NullPointerException As duplicated.
	 * @since 2016/10/13
	 */
	public static void mailboxSend(int __fd, int __chan, byte[] __b, int __o,
		int __l)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException
	{
		// Lock on boxes
		PostBox box;
		Map<Integer, PostBox> boxes = SquirrelJME._POST_BOXES;
		synchronized (boxes)
		{
			// {@squirreljme.error DE0g The specified descriptor is not a valid
			// post box. (The descriptor)}
			box = boxes.get(__fd);
			if (box == null)
				throw new IllegalArgumentException(String.format("DE0g %d",
					__fd));
		}
		
		// Send message from this box
		box.send(__chan, __b, __o, __l);
	}
	
	/**
	 * As duplicated.
	 *
	 * @return As duplicated.
	 * @since 2016/10/13
	 */
	public static int midletID()
	{
		// Always zero, for the kernel
		return 0;
	}
	
	/**
	 * Adds the specified post box to the set of post office boxes.
	 *
	 * @param __box The box to add.
	 * @return The descriptor for the given box.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	private static int __addPostBox(PostBox __box)
		throws NullPointerException
	{
		// Check
		if (__box == null)
			throw new NullPointerException("NARG");
		
		// Lock on boxes
		Map<Integer, PostBox> boxes = SquirrelJME._POST_BOXES;
		synchronized (boxes)
		{
			// Try to find a free box ID
			int origboxid = SquirrelJME._nextboxid;
			for (int nextboxid = (origboxid + 1) & 0x7FFF_FFFF;;
				nextboxid++)
			{
				Integer b = Integer.valueOf(nextboxid);
				if (!boxes.containsKey(b))
				{
					// Store
					boxes.put(b, __box);
					
					// Next destination
					SquirrelJME._nextboxid =
						((nextboxid + 7) | 0x7FFF_FFFF);
					return nextboxid;
				}
				
				// If there are no more box descriptors, wait for some
				if (origboxid == nextboxid)
					try
					{
						boxes.wait();
					}
					catch (InterruptedException e)
					{
					}
			}
		}
	}
}

