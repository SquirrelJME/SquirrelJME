// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.HeadlessException;
import java.awt.Toolkit;

/**
 * SpringCoat support for {@link UIFormShelf}.
 *
 * @since 2020/06/30
 */
public enum MLEUIForm
	implements MLEFunction
{
	/** {@link UIFormShelf#metric(int)}. */
	METRIC("metric:(I)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/30
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			int metric = (int)__args[0];
			if (metric < 0 || metric >= UIMetricType.NUM_METRICS)
				throw new MLECallError("Invalid metric.");
			
			switch (metric)
			{
					// Simplest way to check if forms are supported is if
					// the display is headless or not
				case UIMetricType.UIFORMS_SUPPORTED:
					try
					{
						// Just try to read the screen size
						if (null == Toolkit.getDefaultToolkit()
							.getScreenSize())
							return 0;
						
						return 1;
					}
					catch (HeadlessException e)
					{
						return 0;
					}
				
				default:
					throw new RuntimeException("Unknown metric: " + metric);
			}
		}
	}, 
	
	/* End. */
	;
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/30
	 */
	MLEUIForm(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/30
	 */
	@Override
	public String key()
	{
		return this.key;
	}
}
