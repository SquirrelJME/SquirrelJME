// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchChoiceInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchChoiceBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Choice;

/**
 * Sets the selected flags.
 *
 * @since 2024/07/28
 */
final class __ExecChoiceSelectedFlags__
	implements Runnable
{
	/** The API interface. */
	private final ScritchInterface _scritchApi;
	
	/** The choice to modify. */
	private final ScritchChoiceBracket _choice;
	
	/** The flags to set. */
	private final boolean[] _flags;
	
	/** The choice type. */
	private final int _type;
	
	/** If there was an exception. */
	volatile Throwable _error;
	
	/**
	 * Initializes the flag setter.
	 *
	 * @param __scritchApi The scritch API to call.
	 * @param __choice The choice to modify.
	 * @param __flags The flags to set.
	 * @param __type The choice type.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/28
	 */
	__ExecChoiceSelectedFlags__(ScritchInterface __scritchApi,
		ScritchChoiceBracket __choice, boolean[] __flags, int __type)
		throws NullPointerException
	{
		if (__scritchApi == null || __choice == null || __flags == null)
			throw new NullPointerException("NARG");
		
		this._scritchApi = __scritchApi;
		this._choice = __choice;
		this._flags = __flags;
		this._type = __type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/28
	 */
	@Override
	public void run()
	{
		ScritchChoiceInterface choiceApi = this._scritchApi.choice();
		ScritchChoiceBracket choice = this._choice;
		int type = this._type;
		boolean[] flags = this._flags;
		
		// Make sure we can actually set the flags
		int n = choiceApi.length(choice);
		if (flags.length < n)
		{
			/* {@squirreljme.error EB1o Passed flags too small for choice.} */
			this._error = new IllegalArgumentException("EB1o");
			return;
		}
		
		// Set everything
		boolean allowSet = true;
		boolean anyTrue = false;
		for (int i = 0; i < n; i++)
		{
			boolean flag = flags[i];
			boolean set = flag && allowSet;
			
			// Set if we had any true, for certain list types
			if (flag)
				anyTrue = true;
			
			// Set item
			choiceApi.setSelected(choice, i, set);
			
			// If this is implicit, then no longer allow being set
			if (type == Choice.IMPLICIT && set)
				allowSet = false;
		}
		
		// If everything is false, force the first item to be set
		if (!anyTrue && (type == Choice.EXCLUSIVE || type == Choice.IMPLICIT ||
			type == Choice.POPUP) && n > 0)
				choiceApi.setSelected(choice, 0, true);
	}
}
