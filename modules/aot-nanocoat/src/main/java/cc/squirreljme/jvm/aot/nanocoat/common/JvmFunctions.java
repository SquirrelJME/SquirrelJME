// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.common;

import cc.squirreljme.c.CFunctionType;
import cc.squirreljme.c.std.CFunctionProvider;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Virtual machine functions for NanoCoat.
 *
 * @since 2023/06/24
 */
public enum JvmFunctions
	implements CFunctionProvider
{
	/** Return from method. */
	NVM_RETURN_FROM_METHOD
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/24
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_returnFromMethod");
		}
	},
	
	/* End. */
	;
	
	/** Function cache. */
	private volatile Reference<CFunctionType> _function;
	
	/**
	 * Builds the given function.
	 * 
	 * @return The built function.
	 * @since 2023/06/24
	 */
	abstract CFunctionType __build();
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CFunctionType function()
	{
		Reference<CFunctionType> ref = this._function;
		CFunctionType rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = this.__build();
			this._function = new WeakReference<>(rv);
		}
		
		return rv;
	}
}
