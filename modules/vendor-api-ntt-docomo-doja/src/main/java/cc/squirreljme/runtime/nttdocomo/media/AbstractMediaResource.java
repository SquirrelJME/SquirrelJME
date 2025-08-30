// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import com.nttdocomo.io.ConnectionException;
import com.nttdocomo.ui.MediaResource;
import com.nttdocomo.ui.UIException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;

/**
 * Abstract common implementation for media resources.
 *
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
public abstract class AbstractMediaResource
	implements MediaResource
{
	/** Properties for the resource. */
	@SquirrelJMEVendorApi
	final Map<String, String> _properties =
		new HashMap<>();
	
	/** The connection to the source data. */
	@SquirrelJMEVendorApi
	final InputConnection _source; 
	
	/** Can this be redistributed? */
	@SquirrelJMEVendorApi
	volatile boolean _redistribute =
		true;
	
	/** The number of times this has been used. */
	@SquirrelJMEVendorApi
	volatile int _useCount;
	
	/**
	 * Initializes the base resource with the resource connection.
	 *
	 * @param __source The original input for data.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	protected AbstractMediaResource(InputConnection __source)
		throws NullPointerException
	{
		if (__source == null)
			throw new NullPointerException("NARG");
		
		this._source = __source;
	}
	
	/**
	 * This is called when the resource is becoming realized. 
	 *
	 * @param __in The input stream to read from.
	 * @param __copy The resource to copy from.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If the data could not be read.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	protected abstract void becomingRealized(InputStream __in,
		MediaResource __copy)
		throws NullPointerException, UIException;
	
	/**
	 * This is called when the resource is being unrealized and must be freed.
	 *
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	protected abstract void becomingUnrealized();
	
	/**
	 * Checks if the given key is valid for this resource.
	 *
	 * @param __key The key to check.
	 * @return If this is a valid key.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	protected abstract boolean validKey(String __key)
		throws NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public final void dispose()
		throws UIException
	{
		synchronized (this)
		{
			this.becomingUnrealized();
			this._useCount = 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public final String getProperty(String __key)
		throws IllegalArgumentException, NullPointerException, UIException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		if (__key.isEmpty())
			throw new IllegalArgumentException("BLNK");
		
		synchronized (this)
		{
			return this._properties.get(__key);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public final boolean isRedistributable()
		throws UIException
	{
		synchronized (this)
		{
			return this._redistribute;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public final void setProperty(String __key, String __value)
		throws IllegalArgumentException, NullPointerException, UIException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		if (__key.isEmpty())
			throw new IllegalArgumentException("BLNK");
		
		synchronized (this)
		{
			// Only valid keys get placed in the map
			if (this.validKey(__key))
				this._properties.put(__key, __value);
		}
	}
	
	/**
	 * {@inheritDoc} 
	 * @since 2025/05/05
	 */
	@Override
	public final boolean setRedistributable(boolean __canRedistribute)
		throws UIException
	{
		synchronized (this)
		{
			boolean was = this._redistribute;
			this._redistribute = __canRedistribute;
			return (was != __canRedistribute);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public final void unuse()
		throws UIException
	{
		synchronized (this)
		{
			int useCount = this._useCount;
			if (useCount <= 0)
				throw new UIException(UIException.ILLEGAL_STATE);
			
			// Destroy/unrealize
			this._useCount = (--useCount);
			if (useCount == 0)
				this.becomingUnrealized();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public final void use()
		throws ConnectionException, SecurityException, UIException
	{
		this.use(null, false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public final void use(MediaResource __replaceWith, boolean __onlyOnce)
		throws ConnectionException, SecurityException, UIException
	{
		synchronized (this)
		{
			// Count up usage?
			int useCount = this._useCount;
			if (useCount > 0)
			{
				this._useCount = useCount + 1;
				return;
			}
			
			// Use replacement data?
			InputConnection source = this._source;
			if (__replaceWith instanceof AbstractMediaResource)
				source = ((AbstractMediaResource)__replaceWith)._source;
			
			// Load from whatever the source was
			try (InputStream in = source.openInputStream())
			{
				this.becomingRealized(in, __replaceWith);
			}
			catch (IOException __e)
			{
				UIException toss = new UIException(
					UIException.UNSUPPORTED_FORMAT, __e.getMessage());
				
				toss.initCause(__e);
				
				throw toss;
			}
			
			// Initial count as it is now loaded
			this._useCount = 1;
		}
	}
}
