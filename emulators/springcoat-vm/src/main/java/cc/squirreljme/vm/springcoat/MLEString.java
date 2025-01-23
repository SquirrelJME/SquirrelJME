// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * String functions.
 *
 * @since 2025/01/22
 */
public enum MLEString
	implements MLEFunction
{
	INIT("stringInit:(Ljava/lang/String;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/01/22
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	}
	
	/* End. */
	;
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/01/22
	 */
	MLEString(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/01/22
	 */
	@Override
	public String key()
	{
		return this.key;
	}
}
