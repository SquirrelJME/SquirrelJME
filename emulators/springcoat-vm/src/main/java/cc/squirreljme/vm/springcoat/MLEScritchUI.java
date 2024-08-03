// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.NativeScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.extra.ScritchUnifiedWrapper;
import cc.squirreljme.vm.springcoat.callbacks.ScritchUnifiedProxy;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;

/**
 * SpringCoat layer for {@link NativeScritchInterface}.
 *
 * @since 2024/03/07
 */
public enum MLEScritchUI
	implements MLEFunction
{
	NATIVE_INTERFACE("nativeInterface:" +
		"()Lcc/squirreljme/jvm/mle/scritchui/ScritchInterface;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/03/07
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw new SpringMLECallError("Unsupported in SpringCoat");
		}
	},
	
	/** End. */
	;
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/07
	 */
	MLEScritchUI(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/07
	 */
	@Override
	public String key()
	{
		return this.key;
	}
}
