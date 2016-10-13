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
import java.util.Map;
import net.multiphasicapps.squirreljme.ipcmailbox.Mailbox;
import net.multiphasicapps.squirreljme.ipcmailbox.PostBase;
import net.multiphasicapps.squirreljme.ipcmailbox.PostDestination;
import net.multiphasicapps.squirreljme.ipcmailbox.PostOffice;
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
		synchronized (postdests)
		{
			// {@squirreljme.error DE0d The post destination is not valid.
			// (The identifier)}
			PostDestination dest = postdetsts.get(__ld);
			if (dest == null)
				throw new IllegalArgumentException(String.format(
					"DE0d %s", __ld));
			
			throw new Error("TODO");
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
	 * @throws IllegalArgumentException As duplicated.
	 * @throws NullPointerException As duplicated.
	 * @throws RuntimeException As duplicated.
	 * @since 2016/10/13
	 */
	public static int mailboxListen(byte[] __b, int __o, int __l, int __v,
		boolean __am)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException, RuntimeException
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
			for (int i = System.identityHashCode(dest) | 0x8000_0000;;
				i = (i - 1) | 0x8000_0000)
			{
				Integer b = Integer.valueOf(i);
				if (!postdests.containsKey(b))
				{
					postdests.put(b, dest);
					return i;
				}
			}
		}
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
}

