// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchBaseBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Menu;
import org.jetbrains.annotations.Async;

/**
 * Base class for menu components that can be bound to a ScritchUI bracket.
 * 
 * Because {@link Menu}s and {@link Command}s can be added to multiple
 * different {@link Displayable}s this keeps track of this and maps each,
 * accordingly to a single ScritchUI bracket.
 *
 * @param <M> The MIDP menu type.
 * @param <S> The ScritchUI bracket type.
 * @since 2024/07/18
 */
@SquirrelJMEVendorApi
public abstract class MenuLayoutBindable<M, S extends ScritchBaseBracket>
{
	/**
	 * Returns the MIDP item for this bindable.
	 *
	 * @return The MIDP item for this item.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	public final M getMidp()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Gets the ScritchUI item for this bindable.
	 *
	 * @return The ScritchUI item for this item.
	 * @throws IllegalStateException If the ScritchUI reference is no longer
	 * valid.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	public final S getScritchUi()
		throws IllegalStateException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Performs recursive refreshing of this bindable.
	 *
	 * @throws IllegalStateException If this is called outside the event
	 * loop.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	@Async.Execute
	public abstract void refresh()
		throws IllegalStateException;
}
