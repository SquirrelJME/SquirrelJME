// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.IllegalSymbolException;

/**
 * This class acts as the interpreter engine.
 *
 * This engine only supports JavaME 8 and may not be fully capable of running
 * Java SE code (it does not support invokedynamic or reflection).
 *
 * @since 2016/03/01
 */
public abstract class JVMEngine
{
	/** The class manager. */
	protected final JVMClassPath classes =
		new JVMClassPath(this);
	
	/** The object manager. */
	protected final JVMObjects objects =
		new JVMObjects(this);
	
	/** The thread manager. */
	protected final JVMThreads threads =
		new JVMThreads(this);
	
	/** The computational machine. */
	protected final JVMComputeMachine computer =
		new JVMComputeMachine(this);
	
	/**
	 * Initializes the base engine.
	 *
	 * @since 2016/02/01
	 */
	public JVMEngine()
	{
	}
	
	/**
	 * Returns the engine classes.
	 *
	 * @return The engine classes.
	 * @since 2016/04/06
	 */
	public final JVMClassPath classes()
	{
		return classes;
	}
	
	/**
	 * Returns the compute machine used for logical operations.
	 *
	 * @return The computational machine for operation execution.
	 * @since 2016/04/08
	 */
	public final JVMComputeMachine computeMachine()
	{
		return computer;
	}
	
	/**
	 * Retruns the engine objects.
	 *
	 * @return The engine objects.
	 * @since 2016/04/06
	 */
	public final JVMObjects objects()
	{
		return objects;
	}
	
	/**
	 * Returns the engine threads.
	 *
	 * @return The engine threads.
	 * @since 2016/04/06
	 */
	public final JVMThreads threads()
	{
		return threads;
	}
}

