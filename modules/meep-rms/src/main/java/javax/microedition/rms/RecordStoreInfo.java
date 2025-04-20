// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

import cc.squirreljme.jvm.mle.BucketShelf;
import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.jvm.mle.constants.StandardBucketType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.suite.SuiteIdentifier;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.rms.RecordSession;
import cc.squirreljme.runtime.rms.RecordStoreSession;
import cc.squirreljme.runtime.rms.RecordUtils;
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
	/** The bucket used to access the information. */
	final BucketBracket _bucket;
	
	/** The owner of this record. */
	final SuiteIdentifier _owner;
	
	/** The name of this record. */
	final String _name;
	
	/** Is this our own record? */
	final boolean _isSelf;
	
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
	 * @throws RecordStoreException If the bucket could not be opened.
	 * @since 2025/04/16
	 */
	RecordStoreInfo(SuiteIdentifier __owner, String __name, boolean __self)
		throws NullPointerException, RecordStoreException
	{
		if (__owner == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Load in the bucket
		try
		{
			this._bucket = BucketShelf.bucket(StandardBucketType.DATA_BUCKET);
		}
		catch (MLECallError __e)
		{
			throw RecordUtils.wrap(
				new RecordStoreException(__e.getMessage()), __e);
		}
		
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
			throw RecordUtils.wrap(
				new RecordStoreException(__e.getMessage()), __e);
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
		try (RecordStoreSession session = this.__meta())
		{
			int result = session.getInteger(
				RecordStoreSession.AUTHENTICATION,
				RecordStore.AUTHMODE_PRIVATE);
			
			/* Use a default if unspecified. */
			if (result != RecordStore.AUTHMODE_PRIVATE &&
				result != RecordStore.AUTHMODE_ANY)
				return RecordStore.AUTHMODE_PRIVATE;
			return result;
		}
		catch (RecordStoreException __e)
		{
			throw RecordUtils.wrap(
				new RecordStoreNotOpenException(__e.getMessage()), __e);
		}
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
		try (RecordStoreSession session = this.__meta())
		{
			return (session.getInteger(
				RecordStoreSession.OTHER_WRITE, 1) != 0);
		}
		catch (RecordStoreException __e)
		{
			throw RecordUtils.wrap(
				new RecordStoreNotOpenException(__e.getMessage()), __e);
		}
	}
	
	/**
	 * Checks if this record store actually exists on the disk.
	 *
	 * @return If this actually exists.
	 * @throws RecordStoreException If this could not determined.
	 * @since 2025/04/16
	 */
	boolean __exists()
		throws RecordStoreException
	{
		try
		{
			// The meta-file must exist
			return BucketShelf.exists(this._bucket, this.metaName);
		}
		catch (MLECallError __e)
		{
			throw RecordUtils.wrap(
				new RecordStoreException(__e.getMessage()), __e);
		}
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
	 * Is this record store writable by this application?
	 *
	 * @return If this can be written to.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2025/04/16
	 */
	@SuppressWarnings("ConstantValue")
	boolean __isSelfWritable()
		throws RecordStoreNotOpenException
	{
		return this._isSelf || this.isWriteable();
	}
	
	/**
	 * Opens a meta session.
	 *
	 * @return The opened meta session.
	 * @throws RecordStoreException If the session could not be opened 
	 * @since 2025/04/20
	 */
	RecordStoreSession __meta()
		throws RecordStoreException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the access mode for this record store.
	 *
	 * @param __auth The authorization to use.
	 * @param __otherWrite If this can be written by others.
	 * @param __pass The password to use.
	 * @throws RecordStoreException If the record could not be opened.
	 * @since 2025/04/16
	 */
	void __setAccess(int __auth, boolean __otherWrite, String __pass)
		throws RecordStoreException
	{
		try (RecordStoreSession session = this.__meta())
		{
			session.set(RecordStoreSession.AUTHENTICATION,
				__auth);
			session.set(RecordStoreSession.OTHER_WRITE,
				(__otherWrite ? 1 : 0));
			session.set(RecordStoreSession.PASSWORD,
				(__pass != null ? __pass : ""));
		}
	}
}

