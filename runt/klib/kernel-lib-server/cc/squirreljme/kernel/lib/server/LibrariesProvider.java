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

import cc.squirreljme.kernel.lib.client.InstallErrorCodes;
import cc.squirreljme.kernel.lib.client.LibrariesClient;
import cc.squirreljme.kernel.lib.client.LibrariesClientFactory;
import cc.squirreljme.kernel.lib.client.Library;
import cc.squirreljme.kernel.lib.client.LibraryInstallationReport;
import cc.squirreljme.kernel.lib.client.SuiteIdentifier;
import cc.squirreljme.kernel.lib.client.SuiteInfo;
import cc.squirreljme.kernel.lib.client.SuiteName;
import cc.squirreljme.kernel.lib.client.SuiteType;
import cc.squirreljme.kernel.lib.client.SuiteVendor;
import cc.squirreljme.kernel.lib.client.SuiteVersion;
import cc.squirreljme.kernel.service.ClientInstance;
import cc.squirreljme.kernel.service.ServerInstance;
import cc.squirreljme.kernel.service.ServicePacketStream;
import cc.squirreljme.kernel.service.ServiceProvider;
import cc.squirreljme.kernel.trust.client.TrustClient;
import cc.squirreljme.runtime.cldc.SystemCall;
import cc.squirreljme.runtime.cldc.SystemTask;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.blockreader.ZipEntryNotFoundException;

/**
 * This is the base class which manages the library of installed programs
 * on the server.
 *
 * The first library must always have an index of zero and be the library
 * which represents the system. The system manifest must be returned by it in
 * order for it to function.
 *
 * @since 2018/01/05
 */
public abstract class LibrariesProvider
	extends ServiceProvider
{
	/** Thread safety lock. */
	protected final Object lock =
		new Object();
	
	/** The trust client which is needed to determine how to secure tasks. */
	private final TrustClient _trusts =
		SystemCall.<TrustClient>service(TrustClient.class);
	
	/** Libraries which are availble for usage. */
	private final Map<Integer, Library> _libraries =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the base library server.
	 *
	 * @since 2018/01/05
	 */
	public LibrariesProvider()
		throws NullPointerException
	{
		super(LibrariesClient.class, LibrariesClientFactory.class);
	}
	
	/**
	 * Registers the specified library to the library provider.
	 *
	 * @param __l The library to register.
	 * @throws IllegalStateException If the library was already registered
	 * with the given index.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/15
	 */
	protected final void regsiterLibrary(Library __l)
		throws IllegalStateException, NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		Integer idx = __l.index();
		
		// Lock
		Map<Integer, Library> libraries = this._libraries;
		synchronized (this.lock)
		{
			// {@squirreljme.error BC01 Registration of a library with a
			// duplicate index. (The index)}
			Library exists = libraries.get(idx);
			if (exists != null)
				throw new IllegalStateException(String.format("BC01 %d", idx));
			
			// Store it
			libraries.put(idx, __l);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/05
	 */
	@Override
	public final ServerInstance createInstance(SystemTask __task,
		ServicePacketStream __sps)
		throws NullPointerException
	{
		if (__task == null || __sps == null)
			throw new NullPointerException("NARG");
		
		return new LibrariesServer(__task, __sps, this);
	}
	
	/**
	 * Installs the specified JAR file.
	 *
	 * @param __b The data for the JAR file.
	 * @param __o The offset to the JAR data.
	 * @param __l The length of the JAR.
	 * @return The installation report.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/13
	 */
	public final LibraryInstallationReport install(byte[] __b, int __o,
		int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		// Need to process the ZIP
		Map<Integer, Library> libraries = this._libraries;
		try (ZipBlockReader zip = new ZipBlockReader(__b, __o, __l))
		{
			// Need to parse the info so the trust group and to see if it is
			// replacing another program
			SuiteInfo info;
			try (InputStream in = zip.open("META-INF/MANIFEST.MF"))
			{
				info = new SuiteInfo(new JavaManifest(in));
			}
			
			// {@squirreljme.error BC02 Cannot install applications which are
			// SquirrelJME APIs, they must be pre-installed by the system.}
			if (info.type() == SuiteType.SQUIRRELJME_API)
				throw new __PlainInstallError__(
					InstallErrorCodes.INVALID_JAR_TYPE, "BC02");
			
			// Lock because we need to go through each library to see if this
			// one is a duplicate.
			// Additionally, dependencies for this suite need to be handled.
			// Then after compilation, etc. it needs to be registered after
			// it is created
			synchronized (this.lock)
			{
				// First check to see if this library is a duplicate
				// {@squirreljme.error BC03 The library to be installed is
				// a duplicate.}
				if (this.__checkInstallDuplicate(info))
					throw new __PlainInstallError__(
						InstallErrorCodes.ALREADY_INSTALLED, "BC03");
				
				throw new todo.TODO();
			}
		}
		
		// Map the thrown exception to an error code
		catch (Exception t)
		{
			// Print stack trace so it is better known why it failed
			t.printStackTrace();
			
			// Throw it
			return __mapThrowable(t);
		}
	}
	
	/**
	 * Lists the libraries which currently exist.
	 *
	 * @param __mask The mask for the library type.
	 * @return The list of libraries.
	 * @since 2018/01/07
	 */
	public final Library[] list(int __mask)
	{
		List<Library> rv = new ArrayList<>();
		
		Map<Integer, Library> libraries = this._libraries;
		synchronized (this.lock)
		{
			// Only add libraries which match the mask
			for (Library l : libraries.values())
				if ((l.type() & __mask) != 0)
					rv.add(l);
		}
		
		return rv.<Library>toArray(new Library[rv.size()]);
	}
	
	/**
	 * Registers the specified library to the library list.
	 *
	 * @param __l The library to register.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/12
	 */
	protected final void registerLibrary(Library __l)
		throws NullPointerException
	{
		Map<Integer, Library> libraries = this._libraries;
		synchronized (this.lock)
		{
			// {@squirreljme.error BC04 The library with the specified index
			// has already been registered. (The library index)}
			Integer idx = __l.index();
			if (libraries.containsKey(idx))
				throw new IllegalArgumentException(String.format("BC04 %d",
					idx));
			
			// Store it
			libraries.put(idx, __l);
		}
	}
	
	/**
	 * Checks if the specified suite information to see if a library which
	 * is considered a duplicate exists.
	 *
	 * @param __myinfo The library information.
	 * @return {@code true} if it is a duplicate.
	 * @throws NullPointerException On null arguments.
	 * @throws __PlainInstallError__ If the library is duplicated.
	 * @since 2018/01/15
	 */
	private final boolean __checkInstallDuplicate(SuiteInfo __myinfo)
		throws NullPointerException, __PlainInstallError__
	{
		if (__myinfo == null)
			throw new NullPointerException("NARG");
		
		SuiteName myname = __myinfo.name();
		SuiteVendor myvendor = __myinfo.vendor();
		SuiteVersion myversion = __myinfo.version();
		
		// Lock
		Map<Integer, Library> libraries = this._libraries;
		synchronized (this.lock)
		{
			// Go through libraries and check to see if there are any
			// duplicates
			for (Library otherlib : libraries.values())
			{
				SuiteInfo otherinfo = otherlib.suiteInfo();
				
				SuiteName othername = otherinfo.name();
				SuiteVendor othervendor = otherinfo.vendor();
				SuiteVersion otherversion = otherinfo.version();
				
				if (myname.equals(othername) && myvendor.equals(othervendor))
				{
					// {@squirreljme.error BC05 Installing library which has
					// an older version.}
					int comp = myversion.compareTo(otherversion);
					if (comp < 0)
						throw new __PlainInstallError__(
							InstallErrorCodes.VERSION_MISMATCH, "BC05");
					
					// {@squirreljme.error BC06 Installing library which has
					// a newer version.}
					else if (comp > 0)
						throw new __PlainInstallError__(
							InstallErrorCodes.NEW_VERSION, "BC06");
					
					// {@squirreljme.error BC07 The library has already been
					// installed.}
					throw new __PlainInstallError__(
						InstallErrorCodes.ALREADY_INSTALLED, "BC07");
				}
			}
		}
		
		// Is okay
		return false;
	}
	
	/**
	 * Maps the given throwable to a report.
	 *
	 * @param __t The input throwable.
	 * @return The resulting throwable.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/14
	 */
	private static final LibraryInstallationReport __mapThrowable(
		Throwable __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Determine error code
		int code;
		if (__t instanceof __PlainInstallError__)
			code = ((__PlainInstallError__)__t).code();
		else if (__t instanceof IOException)
			code = InstallErrorCodes.IO_FILE_ERROR;
		else if (__t instanceof ZipEntryNotFoundException)
			code = InstallErrorCodes.CORRUPT_JAR;
		else
			code = InstallErrorCodes.OTHER_ERROR;
		
		// {@squirreljme.error BC08 No message specified in the throwable.}
		return new LibraryInstallationReport(code,
			Objects.toString(__t.getMessage(), "BC08"));
	}
}

