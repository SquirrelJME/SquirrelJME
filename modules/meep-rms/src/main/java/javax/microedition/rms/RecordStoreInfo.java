// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

import cc.squirreljme.jvm.launch.IModeApplication;
import cc.squirreljme.jvm.mle.BucketShelf;
import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.jvm.mle.constants.StandardBucketType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.suite.SuiteIdentifier;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.rms.RecordIteration;
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
	final String _baseName;
	
	/** The meta info file name. */
	final String _metaName;
	
	/** The lock for this record store. */
	private final Object _lock;
	
	/** The meta session. */
	private volatile RecordStoreSession _metaSession;
	
	/**
	 * Initializes the record meta handler.
	 *
	 * @param __owner The owning suite name and vendor.
	 * @param __name The name of this record.
	 * @param __self Is this a record we own?
	 * @param __lock The lock used for accessing record information.
	 * @throws NullPointerException On null arguments.
	 * @throws RecordStoreException If the bucket could not be opened.
	 * @since 2025/04/16
	 */
	RecordStoreInfo(SuiteIdentifier __owner, String __name, boolean __self,
		Object __lock)
		throws NullPointerException, RecordStoreException
	{
		if (__owner == null || __name == null || __lock == null)
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
		this._lock = __lock;
		
		// Is this a DoJa vendor record?
		String ownerVendor = __owner.vendor().toString();
		boolean isDoJa = IModeApplication.VENDOR.equals(ownerVendor) ||
			RecordStoreSession.OLD_DOJA_VENDOR.equals(ownerVendor);
		
		// Check to see if the record store exists under a different basename
		String otherBaseName = null;
		for (RecordIteration iteration : RecordStoreSession.locateAll())
		{
			// Is the other suite also DoJa?
			String otherVendor = iteration.owner.vendor().toString();
			boolean otherDoJa = IModeApplication.VENDOR.equals(otherVendor) ||
				RecordStoreSession.OLD_DOJA_VENDOR.equals(otherVendor);
			
			// Treat the older vendor the same so older records are not lost
			if ((__owner.equals(iteration.owner) || (isDoJa && otherDoJa &&
				__owner.name().equals(iteration.owner.name()))) &&
				__name.equals(iteration.name))
			{
				otherBaseName = iteration.baseName;
				break;
			}
		}
		
		// Determine the meta filename
		try
		{
			if (otherBaseName != null)
				this._baseName = otherBaseName;
			else
				this._baseName = String.format("%08x%02d%s",
					__owner.hashCode(), __name.length(),
					Base64Encoder.encode(__name.getBytes("utf-8"))
						.toLowerCase().replace('=', '_')
						.replace('/', '~'));
			this._metaName = this._baseName + ".rms";
		}
		catch (IOException __e)
		{
			throw RecordUtils.wrap(
				new RecordStoreException(__e.getMessage()), __e);
		}
		
		// Debug
		Debugging.debugNote("RecordStore: %s %s -> %s",
			__owner, __name, this._baseName);
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
		synchronized (this._lock)
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
		synchronized (this._lock)
		{
			try (RecordStoreSession session = this.__meta())
			{
				return session.totalSize();
			}
			catch (RecordStoreException __e)
			{
				throw RecordUtils.wrap(
					new RecordStoreNotOpenException(__e.getMessage()), __e);
			}
		}
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
		// Use any value here, since it is unknown
		return Integer.MAX_VALUE >>> 1;
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
		// No encryption is supported
		return false;
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
		synchronized (this._lock)
		{
			try (RecordStoreSession session = this.__meta())
			{
				return (session.getInteger(RecordStoreSession.OTHER_WRITE,
					1) != 0);
			}
			catch (RecordStoreException __e)
			{
				throw RecordUtils.wrap(
					new RecordStoreNotOpenException(__e.getMessage()), __e);
			}
		}
	}
	
	/**
	 * Is this record store writable by this application?
	 *
	 * @return If this can be written to.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2025/04/16
	 */
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
		synchronized (this._lock)
		{
			// Always keep the same meta session
			RecordStoreSession result = this._metaSession;
			if (result != null)
				return result;
			
			// Setup new session
			result = new RecordStoreSession(this._bucket, this._metaName,
				this._lock, this._owner, this._name, false);
			
			// Cache and use it
			this._metaSession = result;
			return result;
		}
	}
}

