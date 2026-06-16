// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchChoiceBracket;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;

/**
 * Handles choice deletion.
 *
 * @since 2025/04/18
 */
@KeepWhenCompacting
final class __ExecChoiceDelete__
	implements Runnable
{
	/** The API interface. */
	private final ScritchInterface _scritchApi;
	
	/** The choice to modify. */
	private final ScritchChoiceBracket _choice;
	
	/** The index to delete. */
	private final int _atIndex;
	
	/** If there was an exception. */
	@SquirrelJMEVendorApi
	volatile MLECallError _error;
	
	/** The cache to delete from. */
	@SquirrelJMEVendorApi
	private final ArrayList<CachedChoice> _cache;
	
	/**
	 * Initializes the task.
	 *
	 * @param __api The Scritch API.
	 * @param __widget The widget to delete from.
	 * @param __atIndex The index to delete.
	 * @param __cache The cache to modify for deletion.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/18
	 */
	@KeepWhenCompacting
	__ExecChoiceDelete__(ScritchInterface __api,
		ScritchChoiceBracket __widget, int __atIndex,
		ArrayList<CachedChoice> __cache)
		throws NullPointerException
	{
		if (__api == null || __widget == null || __cache == null)
			throw new NullPointerException("NARG");
		
		this._scritchApi = __api;
		this._choice = __widget;
		this._atIndex = __atIndex;
		this._cache = __cache;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/18
	 */
	@Override
	public void run()
	{
		try
		{
			// Delete single item
			int atIndex = this._atIndex;
			if (atIndex >= 0)
			{
				this._scritchApi.choice().choiceDelete(this._choice, atIndex);
				
				synchronized (this._cache)
				{
					this._cache.remove(atIndex);
				}
			}
			
			// Delete everything
			else
			{
				this._scritchApi.choice().choiceDeleteAll(this._choice);
				
				synchronized (this._cache)
				{
					this._cache.clear();
				}
			}
		}
		catch (MLECallError __e)
		{
			__e.printStackTrace(System.err);
			
			this._error = __e;
		}
	}
}
