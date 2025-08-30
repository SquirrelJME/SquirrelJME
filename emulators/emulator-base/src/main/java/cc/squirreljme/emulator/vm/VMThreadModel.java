// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import cc.squirreljme.jvm.mle.constants.ThreadModelType;

/**
 * This contains the thread model that will be used.
 *
 * @since 2021/05/07
 */
public enum VMThreadModel
{
	/** Single cooperative thread. */
	SINGLE_THREAD_COOP(ThreadModelType.SINGLE_THREAD_COOP),
	
	/** Single preemptive cooperative thread. */
	SINGLE_THREAD_PREEMPT(ThreadModelType.SINGLE_THREAD_PREEMPT),
	
	/** Simultaneous Multi-Threading. */
	MULTI_THREAD(ThreadModelType.MULTI_THREAD),
	
	/* End. */
	;
	
	/** The default thread model. */
	public static final VMThreadModel DEFAULT =
		VMThreadModel.MULTI_THREAD;
	
	/** The model ID. */
	public final int model;
	
	/**
	 * The thread model to use.
	 * 
	 * @param __model The model ID.
	 * @since 2021/05/07
	 */
	VMThreadModel(int __model)
	{
		this.model = __model;
	}
	
	/**
	 * Returns the thread model from the given string.
	 * 
	 * @param __s The string to use.
	 * @return The thread model, if unknown the default will be used.
	 * @since 2021/05/07
	 */
	public static VMThreadModel of(String __s)
	{
		if (__s == null)
			return VMThreadModel.DEFAULT;
		
		switch (__s.toLowerCase())
		{
			case "single":
			case "coop":
				return VMThreadModel.SINGLE_THREAD_COOP;
				
			case "shared":
				return VMThreadModel.SINGLE_THREAD_PREEMPT;
			
			case "multi":
			case "smt":
				return VMThreadModel.MULTI_THREAD;
			
			default:
				return VMThreadModel.DEFAULT;
		}
	}
}
