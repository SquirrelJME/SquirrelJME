// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classwriter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.symbols.BinaryNameSymbol;
import net.multiphasicapps.squirreljme.java.ci.CIMethodFlag;
import net.multiphasicapps.squirreljme.java.ci.CIMethodFlags;
import net.multiphasicapps.squirreljme.java.ci.CIMethodID;

/**
 * This class is used to write standard Java class files.
 *
 * @since 2016/06/20
 */
public class OutputClass
{
	/** The class magic number. */
	public static final int MAGIC_NUMBER =
		0xCAFE_BABE;
	
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** The interfaces this class implements. */
	private final Set<BinaryNameSymbol> _interfacenames = 
		new LinkedHashSet<>();
	
	/** Methods in the class. */
	private final Map<CIMethodID, OutputMethod> _methods =
		new LinkedHashMap<>();
	
	/** The class version number. */
	private volatile OutputVersion _version;
	
	/** The name of the current class. */
	private volatile BinaryNameSymbol _thisname;
	
	/** The name of the super class. */
	private volatile BinaryNameSymbol _supername;
	
	/**
	 * Adds an interface that the class should implement.
	 *
	 * @param __bn The class that this implements.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/1
	 */
	public final void addInterface(BinaryNameSymbol __bn)
		throws NullPointerException
	{
		// Check
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._interfacenames.add(__bn);
		}
	}
	
	/**
	 * Adds a method to the class to be written.
	 *
	 * @param __id The name and type of the method.
	 * @return The newly created method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/21
	 */
	public final OutputMethod addMethod(CIMethodID __id)
		throws NullPointerException
	{
		// Check
		if (__id == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			OutputMethod rv;
			this._methods.put(__id, (rv = new OutputMethod(this, __id)));
			return rv;
		}
	}
	
	/**
	 * Sets the name of the super class that this extends.
	 *
	 * @param __sn The super class this extends.
	 * @since 2016/06/21
	 */
	public final void setSuperName(BinaryNameSymbol __sn)
	{
		// Lock
		synchronized (this.lock)
		{
			this._supername = __sn;
		}
	}
	
	/**
	 * Sets the name of the current class.
	 *
	 * @param __tn The name of this class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	public final void setThisName(BinaryNameSymbol __tn)
		throws NullPointerException
	{
		// Check
		if (__tn == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._thisname = __tn;
		}
	}
	
	/**
	 * Sets the version of the class.
	 *
	 * @param __ver The class version number.
	 * @throws IllegalArgumentException
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	public final void setVersion(OutputVersion __ver)
		throws NullPointerException
	{
		// Check
		if (__ver == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._version = __ver;
		}
	}
	
	/**
	 * Writes the class to the given output stream.
	 *
	 * @param __os The stream to write to.
	 * @throws IllegalStateException If required information is missing or
	 * is invalid.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	public final void write(OutputStream __os)
		throws IllegalStateException, IOException, NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Wrap as a data stream
		DataOutputStream dos = new DataOutputStream(__os);
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error CO01 No version was specified for the
			// output class.}
			OutputVersion version = this._version;
			if (version == null)
				throw new IllegalStateException("CO01");
			
			// {@squirreljme.error CO02 The name of the current class was not
			// specified.}
			BinaryNameSymbol thisname = this._thisname;
			if (thisname == null)
				throw new IllegalStateException("CO02");
			
			// Superclass is optional
			BinaryNameSymbol supername = this._supername;
			
			// Interfaces
			Set<BinaryNameSymbol> interfacenames = this._interfacenames;
			
			// Methods
			Map<CIMethodID, OutputMethod> methods = this._methods;
			
			if (true)
				throw new Error("TODO");
			
			// Write magic
			dos.writeInt(MAGIC_NUMBER);
			
			// Write the version
			int magic = version.version();
			dos.writeShort((short)(magic & 0xFFFF));
			dos.writeShort((short)((magic >>> 16) & 0xFFFF));
			
			if (true)
				throw new Error("TODO");
		}
	}
	
	/**
	 * Returns the global class locking object.
	 *
	 * @return The class lock object.
	 * @since 2016/06/21
	 */
	final Object __lock()
	{
		return this.lock;
	}
}

