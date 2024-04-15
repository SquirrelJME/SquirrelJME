// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;
import org.jetbrains.annotations.Async;

/**
 * This is the base class that is under various widgets.
 *
 * @since 2020/10/17
 */
abstract class __CommonWidget__
	implements DisplayWidget
{
	/** The common display state. */
	private final __CommonState__ _commonState;
	
	/**
	 * Initializes the common base state.
	 * 
	 * @since 2023/01/14
	 */
	__CommonWidget__()
	{
		this._commonState = this.__stateInit(
			UIBackendFactory.getInstance(true));
	}
	
	/**
	 * Initializes the display state.
	 * 
	 * @param __backend The backend to use.
	 * @return The created state.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/01/14
	 */
	abstract __CommonState__ __stateInit(UIBackend __backend)
		throws NullPointerException;
	
	/**
	 * Returns the current backend used for this widget.
	 * 
	 * @return The used backend.
	 * @since 2023/01/14
	 */
	final UIBackend __backend()
	{
		return this._commonState._backend;
	}
	
	/**
	 * Is this item painted?
	 * 
	 * @return If this can be painted on.
	 * @since 2020/10/17
	 */
	@SerializedEvent
	@Async.Execute
	boolean __isPainted()
	{
		return false;
	}
	
	/**
	 * Paints and forwards Graphics.
	 * 
	 * @param __gfx Graphics to draw.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @param __special The special painting code, may be {@code 0} or any
	 * other value depending on what is being painted.
	 * @since 2020/09/21
	 */
	@SerializedEvent
	@Async.Execute
	void __paint(Graphics __gfx, int __sw, int __sh, int __special)
	{
	}
	
	/**
	 * Handles property changes.
	 * 
	 * @param __form The form this affects.
	 * @param __item The item this affects.
	 * @param __intProp The property that changed.
	 * @param __sub The sub-index.
	 * @param __old The old value.
	 * @param __new The new value.
	 * @return If the event was handled and we should stop.
	 * @since 2020/10/17
	 */
	@SerializedEvent
	@Async.Execute
	boolean __propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __sub, int __old, int __new)
	{
		// Fallback to sending to the item
		DisplayWidget item = StaticDisplayState.locate(__item);
		if (item instanceof __CommonWidget__)
			return ((__CommonWidget__)item).__propertyChange(__form, __item,
				__intProp, __sub, __old, __new);
		
		// Un-Handled
		return false;
	}
	
	/**
	 * Handles property changes.
	 * 
	 * @param __form The form this affects.
	 * @param __item The item this affects.
	 * @param __strProp The property that changed.
	 * @param __sub The sub-index.
	 * @param __old The old value.
	 * @param __new The new value.
	 * @return If the event was handled and we should stop.
	 * @since 2020/10/17
	 */
	@SerializedEvent
	@Async.Execute
	boolean __propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __strProp, int __sub, String __old, String __new)
	{
		// Fallback to sending to the item, only if the specified item is not
		// registered to us as it would have been handled alreayd
		DisplayWidget item = StaticDisplayState.locate(__item);
		if (item instanceof __CommonWidget__ && item != this)
			return ((__CommonWidget__)item).__propertyChange(__form, __item,
				__strProp, __sub, __old, __new);
		
		// Un-Handled
		return false;
	}
	
	/**
	 * Returns the state for the widget or displayable.
	 * 
	 * @param <S> The state type used.
	 * @param __type The state type used.
	 * @return The state.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/01/14
	 */
	final <S extends __CommonState__> S __state(Class<S> __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		return __type.cast(this._commonState);
	}
	
	/**
	 * Common widget state.
	 * 
	 * @since 2023/01/14
	 */
	abstract static class __CommonState__
	{
		/** The backend to use. */
		final UIBackend _backend;
		
		/**
		 * Initializes the common base state.
		 *
		 * @param __backend The backend to use.
		 * @param __self The current item, may be used or ignored.
		 * @throws NullPointerException On null arguments.
		 * @since 2023/01/14
		 */
		__CommonState__(UIBackend __backend, DisplayWidget __self)
			throws NullPointerException
		{
			if (__backend == null)
				throw new NullPointerException("NARG");
			
			this._backend = __backend;
		}
	}
}
