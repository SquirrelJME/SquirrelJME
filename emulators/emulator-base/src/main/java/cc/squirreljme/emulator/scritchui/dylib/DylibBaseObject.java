// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Base class for all brackets that are wrapped by the ScritchUI Dylib.
 *
 * @since 2024/04/04
 */
public abstract class DylibBaseObject
{
	/** Pointer to the object. */
	protected final long componentP;
	
	/**
	 * Initializes the base object.
	 *
	 * @param __componentP The object pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/04
	 */
	public DylibBaseObject(long __componentP)
		throws NullPointerException
	{
		if (__componentP == 0)
			throw new NullPointerException("NARG");
		
		this.componentP = __componentP;
		
		// Bind object to this so native code can find the object again
		DylibBaseObject.__bind(__componentP, this);
	}
	
	/**
	 * Binds this to the Java object.
	 *
	 * @param __objectP The base wrapped native object.
	 * @param __javaObject The Java object to reference.
	 * @throws MLECallError On null arguments.
	 * @since 2024/04/06
	 */
	private static native void __bind(long __objectP, Object __javaObject)
		throws MLECallError;
}
