// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.scritchui.ScritchEnvironmentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchLAFInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Interface for environment details.
 *
 * @since 2024/03/24
 */
@Deprecated
@SquirrelJMEVendorApi
public class FramebufferEnvironmentInterface
	extends FramebufferBaseInterface
	implements ScritchEnvironmentInterface
{
	/** The look and feel interface. */
	protected final FramebufferLAFInterface laf;
	
	/** Internal cache of screens. */
	private final Map<Integer, FramebufferScreenObject> _screens =
		new LinkedHashMap<>();
	
	/**
	 * Initializes this interface. 
	 *
	 * @param __self The framebuffer self interface.
	 * @param __core The core interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/24
	 */
	public FramebufferEnvironmentInterface(
		Reference<FramebufferScritchInterface> __self,
		ScritchInterface __core)
		throws NullPointerException
	{
		super(__self, __core);
		
		// Setup sub interfaces
		this.laf = new FramebufferLAFInterface(__self, __core);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public boolean isInhibitingSleep()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public ScritchLAFInterface lookAndFeel()
	{
		return this.laf;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public ScritchScreenBracket[] screens()
	{
		ScritchInterface coreApi = this.coreApi;
		ScritchScreenInterface screenApi = coreApi.screen();
		
		// These are the screens we layer on top of
		ScritchScreenBracket[] coreScreens =
			coreApi.environment().screens();
		
		// We need to determine which screens we already know about and
		// what we need to add
		List<FramebufferScreenObject> result = new ArrayList<>();
		Map<Integer, FramebufferScreenObject> screens = this._screens;
		
		// We want to keep screens with a given ID around
		for (ScritchScreenBracket coreScreen : coreScreens)
		{
			synchronized (this)
			{
				Integer coreId = screenApi.id(coreScreen);
				
				// Do we need to create a wrapper over this screen?
				FramebufferScreenObject wrapped = screens.get(coreId);
				if (wrapped == null)
				{
					// Setup wrapper
					wrapped = new FramebufferScreenObject(
						this.selfApi, this.coreApi,
						coreScreen, coreId);
					
					// Store for later
					screens.put(coreId, wrapped);
				}
				
				// Add it in
				result.add(wrapped);
			}
		}
		
		// Use whatever result we calculated
		return result.toArray(new FramebufferScreenObject[result.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public void setInhibitSleep(boolean __inhibit)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public int windowManagerType()
	{
		throw Debugging.todo();
	}
	
	@Override
	public @NotNull PencilFontBracket[] builtinFonts()
	{
		throw Debugging.todo();
	}
}
