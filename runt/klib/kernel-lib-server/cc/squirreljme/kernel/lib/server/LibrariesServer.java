// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.lib.server;

import cc.squirreljme.kernel.lib.client.LibrariesFunction;
import cc.squirreljme.kernel.lib.client.LibraryInstallationReport;
import cc.squirreljme.runtime.cldc.library.Library;
import cc.squirreljme.runtime.cldc.library.LibraryControlKey;
import cc.squirreljme.runtime.cldc.library.LibraryResourceScope;
import cc.squirreljme.runtime.cldc.library.LibraryType;
import cc.squirreljme.runtime.cldc.service.ServiceServer;
import cc.squirreljme.runtime.cldc.system.ByteArray;
import cc.squirreljme.runtime.cldc.system.CallCast;
import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.cldc.trust.SystemTrustGroup;
import java.util.Arrays;

/**
 * This provides access for a client to the given server.
 *
 * @since 2018/03/03
 */
public final class LibrariesServer
	implements ServiceServer
{
	/** The definition. */
	protected final LibrariesDefinition definition;
	
	/** The task. */
	protected final SystemTask task;
	
	/**
	 * Initializes the server for the given client.
	 *
	 * @param __ld The definition.
	 * @param __task The task to listen for.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/03
	 */
	public LibrariesServer(LibrariesDefinition __ld, SystemTask __task)
		throws NullPointerException
	{
		if (__ld == null || __task == null)
			throw new NullPointerException("NARG");
		
		this.definition = __ld;
		this.task = __task;
	}
	 
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Override
	public final Object serviceCall(Enum<?> __func, Object... __args)
		throws NullPointerException
	{
		if (__func == null || __args == null)
			throw new NullPointerException("NARG");
		
		switch ((LibrariesFunction)__func)
		{
			case LIST_PROGRAMS:
				return this.__list(
					CallCast.<LibraryType>asEnum(LibraryType.class,
						__args[0]));
			
			case INSTALL_PROGRAM:
				return this.__install(
					CallCast.asByteArray(__args[0]),
					CallCast.asInteger(__args[1]),
					CallCast.asInteger(__args[2]));
			
				// {@squirreljme.error BC09 Unknown function. (The function)}
			default:
				throw new IllegalArgumentException(
					String.format("BC09 %s", __func));
		}
	}
	
	/**
	 * Returns the access mode for the current task which specifies which
	 * libraries it may access.
	 *
	 * @param __act The action to check.
	 * @return The permitted access mode.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/11
	 */
	private final LibrariesAccessMode __accessMode(String __act)
		throws NullPointerException
	{
		if (__act == null)
			throw new NullPointerException("NARG");
		
		SystemTask task = this.task;
		
		// "crossClient" is the most important and it will for the most part
		// imply "client" because it would be rather pointless if a program
		// could modify other clients but not its own
		try
		{
			task.checkPermission("javax.microedition.swm.SWMPermission",
				"crossClient", __act);
			
			return LibrariesAccessMode.ANY;
		}
		
		// Cannot access other clients, try in our same trust group
		catch (SecurityException e)
		{
			// This can also fail
			try
			{
				task.checkPermission("javax.microedition.swm.SWMPermission",
					"client", __act);
				
				return LibrariesAccessMode.SAME_GROUP;
			}
			
			// No access at all
			catch (SecurityException f)
			{
				return LibrariesAccessMode.NONE;
			}
		}
	}
	
	/**
	 * Reads the JAR from the specified packet and then installs it.
	 *
	 * @param __b The JAR file data.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return The installation report.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/13
	 */
	private final int __install(ByteArray __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length())
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		// {@squirreljme.error BC0a The task is not permitted to install new
		// applications.}
		LibrariesAccessMode accessmode = this.__accessMode("installation");
		if (accessmode == LibrariesAccessMode.NONE)
			throw new SecurityException("BC0a");
		
		SystemTask task = this.task;
		
		// Defensive copy the data
		byte[] jardata = new byte[__l];
		__b.get(__o, jardata, 0, __l);
		
		// Run the installation step
		LibraryInstallationReport r = this.definition.
			install(jardata, 0, __l);
		
		Library lib = r.library();
		int ldx = (lib != null ? lib.index() : -1),
			error = r.error();
		String message = r.message();
		
		// Return failure
		if (ldx < 0)
			if (error > 0)
				return (-error) - 1;
			else
				return error;
		else
			return ldx;
	}
	
	/**
	 * Returns the list of libraries which are available.
	 *
	 * @param __t The type of libraries to list.
	 * @return The library list.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/06
	 */
	private final int[] __list(LibraryType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
			
		SystemTask task = this.task;
		SystemTrustGroup mygroup = task.trustGroup();
		
		// Need permission to do this
		LibrariesAccessMode accessmode = this.__accessMode("manageSuite");
		
		// Read the library set
		Library[] libs = this.definition.list(__t);
		
		// Map to indexes
		int n = libs.length;
		int[] rv = new int[n];
		int counted = 0;
		for (int i = 0, o = 0; i < n; i++)
		{
			Library lib = libs[i];
			SystemTrustGroup libgroup = LibrariesDefinition.__trusts().byIndex(
				Integer.valueOf(lib.controlGet(
				LibraryControlKey.TRUST_GROUP)));
			
			// If the library belongs to another group
			if (accessmode.isAccessible(mygroup, libgroup))
			{
				rv[o++] = libs[i].index();
				counted++;
			}
		}
		
		return Arrays.<Library>copyOf(rv, counted);
	}
}

