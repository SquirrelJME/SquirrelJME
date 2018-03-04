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
import cc.squirreljme.runtime.cldc.library.Library;
import cc.squirreljme.runtime.cldc.library.LibraryResourceScope;
import cc.squirreljme.runtime.cldc.library.LibraryType;
import cc.squirreljme.runtime.cldc.service.ServiceServer;
import cc.squirreljme.runtime.cldc.system.CallCast;
import cc.squirreljme.runtime.cldc.task.SystemTask;

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
				return this.__install(__p);
			
			case LOAD_RESOURCE_BYTES:
				return this.__loadResourceBytes(__p);
			
				// {@squirreljme.error BC09 Unknown packet. (The packet)}
			default:
				throw new IllegalArgumentException(
					String.format("BC09 %s", __p));
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
			task.checkPermission(LibrariesServer._PERMISSION_CLASS,
				"crossClient", __act);
			
			return LibrariesAccessMode.ANY;
		}
		
		// Cannot access other clients, try in our same trust group
		catch (SecurityException e)
		{
			// This can also fail
			try
			{
				task.checkPermission(LibrariesServer._PERMISSION_CLASS,
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
	 * @param __p The packet containing the JAR to be installed.
	 * @return The installation report.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/13
	 */
	private final Packet __install(Packet __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BC0a The task is not permitted to install new
		// applications.}
		LibrariesAccessMode accessmode = this.__accessMode("installation");
		if (accessmode == LibrariesAccessMode.NONE)
			throw new SecurityException("BC0a");
		
		SystemTask task = this.task;
		
		// Read in the packet data, but close it so its data is not consumed
		// as it will be wasted data in memory
		int jarlen = __p.readInteger(0);
		byte[] jardata = new byte[jarlen];
		__p.readBytes(4, jardata, 0, jarlen);
		
		// But we need a response packet before it goes away
		Packet rv = __p.respond();
		__p.close();
		
		// Run the installation step
		LibraryInstallationReport r = this.provider.
			install(jardata, 0, jarlen);
		
		Library lib = r.library();
		int ldx = (lib != null ? lib.index() : -1),
			error = r.error();
		String message = r.message();
		
		// Failing?
		if (error != 0)
		{
			rv.writeInteger(0, -1);
			rv.writeInteger(4, error);
			rv.writeString(8, message);
		}
		
		// Success
		else
			rv.writeInteger(0, ldx);
		
		return rv;
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
		Library[] libs = this.provider.list(__t);
		
		// Map to indexes
		int n = libs.length;
		int[] rv = new int[n];
		int counted = 0;
		for (int i = 0, o = 0; i < n; i++)
		{
			Library lib = libs[i];
			SystemTrustGroup libgroup = lib.trustGroup();
			
			// If the library belongs to another group
			if (accessmode.isAccessible(mygroup, libgroup))
			{
				rv[o++] = libs[i].index();
				counted++;
			}
		}
		
		return Arrays.copyOfRange(rv, counted);
	}
	
	/**
	 * Loads the bytes for the resource data.
	 *
	 * @param __p The request.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/13
	 */
	private final Packet __loadResourceBytes(Packet __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		PacketReader r = __p.createReader();
		
		// {@squirreljme.error BC0a The task is not permitted to install new
		// applications.}
		LibrariesAccessMode accessmode = this.__accessMode("manageSuite");
		if (accessmode == LibrariesAccessMode.NONE)
			throw new SecurityException("BC0a");
		
		// Read the library wanting to be read, make sure it exists
		int dx = r.readInteger();
		Library lib = this.provider.byIndex(dx);
		if (lib == null)
		{
			Packet rv = __p.respond(1);
			rv.writeByte(0, Library.DATA_NONE);
			return rv;
		}
		
		// Read the scope and name
		LibraryResourceScope scope = LibraryResourceScope.valueOf(
			r.readString());
		String name = r.readString();
		
		// Load resource data
		byte[] data = lib.loadResourceRawData(scope, name);
		
		// Send it
		int n = data.length;
		Packet rv = __p.respond(data.length);
		
		rv.writeBytes(0, data, 0, n);
		
		return rv;
	}
}

