// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

import cc.squirreljme.jvm.suite.SuiteIdentifier;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import net.multiphasicapps.io.Base64Encoder;

/**
 * This stores information on a record store.
 *
 * @since 2017/02/26
 */
@Api
public final class RecordStoreInfo
{
	/** The owner of this record. */
	private final SuiteIdentifier _owner;
	
	/** The name of this record. */
	private final String _name;
	
	/** Is this our own record? */
	private final boolean _isSelf;
	
	/** The base name for this record. */
	final String baseName;
	
	/** The meta info file name. */
	final String metaName;
	
	/**
	 * Initializes the record meta handler.
	 *
	 * @param __owner The owning suite name and vendor.
	 * @param __name The name of this record.
	 * @param __self Is this a record we own? 
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/16
	 */
	RecordStoreInfo(SuiteIdentifier __owner, String __name, boolean __self)
		throws NullPointerException
	{
		if (__owner == null || __name == null)
			throw new NullPointerException("NARG");
		
		this._owner = __owner;
		this._name = __name;
		this._isSelf = __self;
		
		// Determine the meta filename
		try
		{
			this.baseName = String.format("%08x%02d%s", __owner.hashCode(),
				__name.length(),
				Base64Encoder.encode(__name.getBytes()).toLowerCase());
			this.metaName = this.baseName + ".json";
		}
		catch (IOException __e)
		{
			throw new RuntimeException(__e);
		}
		
		// Debug
		Debugging.debugNote("RecordStore: %s %s -> %s",
			__owner, __name, this.baseName);
	}
	
	/**
	 * Gets the authorization mode of the associated {@link RecordStore}.
	 *
	 * @return The authorization mode, one of
	 * {@link RecordStore#AUTHMODE_PRIVATE} or
	 * {@link RecordStore#AUTHMODE_ANY}.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2017/02/26
	 */
	@Api
	public int getAuthMode()
		throws RecordStoreNotOpenException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the size of the record store including any overhead it may
	 * have.
	 *
	 * @return The number of bytes the record store consumes, not to exceed
	 * {@link Long#MAX_VALUE}.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2017/02/26
	 */
	@Api
	public long getSize()
		throws RecordStoreNotOpenException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the number of bytes which may be available for storage. This
	 * is only an estimate and the actual number may be higher or lower
	 * depending on overhead and storage requirements.
	 *
	 * @return The estimated number of bytes available for storage, not to
	 * exceed {@link Long#MAX_VALUE}.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2017/02/26
	 */
	@Api
	public long getSizeAvailable()
		throws RecordStoreNotOpenException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns {@code true} if the record store is encrypted.
	 *
	 * @return {@code true} if the record store is encrypted.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2017/02/26
	 */
	@Api
	public boolean isEncrypted()
		throws RecordStoreNotOpenException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns {@code true} if the record store can be written to by other
	 * suites. A value of {@code false} still specifies that the record may
	 * be written to by the current application.
	 *
	 * @return {@code true} if the record store can be written to by other
	 * suites.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2017/02/26
	 */
	@Api
	public boolean isWriteable()
		throws RecordStoreNotOpenException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Checks if this record store actually exists on the disk.
	 *
	 * @return If this actually exists.
	 * @since 2025/04/16
	 */
	boolean __exists()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the file that refers to the given record ID.
	 *
	 * @param __id The sub-record ID.
	 * @return The file associated with this ID, {@code -1} will create a
	 * new one.
	 * @throws IllegalArgumentException If the ID is not valid.
	 * @since 2025/04/16
	 */
	String __idFile(int __id)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Determines all the valid record IDs that exist.
	 *
	 * @return All the existing record IDs.
	 * @since 2025/04/16
	 */
	int[] __ids()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Is this writable by others?
	 *
	 * @return If this is writable by others.
	 * @since 2025/04/16
	 */
	boolean __isOtherWritable()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Is this record store writable by this application?
	 *
	 * @return If this can be written to.
	 * @since 2025/04/16
	 */
	@SuppressWarnings("ConstantValue")
	boolean __isSelfWritable()
	{
		return this._isSelf || this.__isOtherWritable();
	}
	
	/**
	 * Sets the access mode for this record store.
	 *
	 * @param __auth The authorization to use.
	 * @param __otherWrite If this can be written by others.
	 * @param __pass The password to use.
	 * @since 2025/04/16
	 */
	void __setAccess(int __auth, boolean __otherWrite, String __pass)
	{
		throw Debugging.todo();
	}
}

