// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import java.util.Iterator;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;
import net.multiphasicapps.collections.EmptyIterator;

/**
 * This represents an application suite.
 *
 * Created suites by default have their {@link SuiteStateFlag#AVAILABLE} and
 * {@link SuiteStateFlag#ENABLED} flags set.
 *
 * @since 2016/06/24
 */
public class Suite
{
	/** This is a suite that represents the system. */
	public static Suite SYSTEM_SUITE =
		new Suite(0);
	
	/** The state lock. */
	private final Object _lock =
		new Object();
	
	/** The suite ID. */
	private final int _id;
	
	/**
	 * Initializes the suite.
	 *
	 * @param __id The ID of the suite.
	 * @since 2016/06/24
	 */
	Suite(int __id)
	{
		this._id = __id;
	}
	
	/**
	 * Checks if two suites are equal to each other, they are equal when
	 * the vendor and name of the suite match.
	 *
	 * @param __o The object to compare against.
	 * @return {@code true} if this suite is equal to the other object.
	 * @since 2016/06/24
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Not another suite?
		if (!(__o instanceof Suite))
			return false;
		
		// Check
		Suite o = (Suite)__o;
		return Objects.equals(getName(), o.getName()) &&
			Objects.equals(getVendor(), o.getVendor());
	}
	
	/**
	 * Returns the list of attributes which are defined in the JAD or the
	 * manifest.
	 *
	 * @return The iterator of attributes. The system suite always returns
	 * an empty iteration.
	 * @since 2016/06/24
	 */
	public Iterator<String> getAttributes()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the value of an attribute.
	 *
	 * @param __a The name of the attribute to obtain a value for.
	 * @return The value of the given attribute or {@code null} if it was not
	 * found. The system suite always returns null.
	 * @since 2016/06/24
	 */
	public String getAttributeValue(String __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the library suites which this suite depends on.
	 *
	 * @return The iterator over the suite dependencies. The system suite
	 * always returns an empty iterator.
	 * @since 2016/06/24
	 */
	public Iterator<Suite> getDependencies()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This returns the URL which a previously installed suite was downloaded
	 * from.
	 *
	 * @return The URL where the suite was downloaded from. If this is the
	 * system suite, the suite was pre-installed, or was installed using the
	 * raw byte array then this will return null.
	 * @since 2016/06/24
	 */
	public String getDownloadUrl()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This returns the names of all classes which are specfied in the MIDlet
	 * attributes in the manifest. The sequence of classes should match the
	 * MIDlet order number.
	 *
	 * @return The names of classes that are specified in the MIDlet attributes
	 * in the manifest. The system suite always returns an empty iterator.
	 * @since 2016/06/24
	 */
	public Iterator<String> getMIDlets()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the name of this suite.
	 *
	 * @return The suite name. The system suite always returns null.
	 * @since 2016/06/24
	 */
	public String getName()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the type of suite that this is.
	 *
	 * @return The type of suite this is. The system suite always returns
	 * {@link SuiteType#SYSTEM}.
	 * @since 2016/06/24
	 */
	public SuiteType getSuiteType()
	{
		throw new todo.TODO();
		/*
		return SuiteType.INVALID;
		*/
	}
	
	/**
	 * Returns the vendor of this suite.
	 *
	 * @return The vendor of this suite. The system suite always returns null.
	 * @since 2016/06/24
	 */
	public String getVendor()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the version of this suite.
	 *
	 * @return The version of this suite. The system suite always returns null.
	 * @since 2016/06/24
	 */
	public String getVersion()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Calculates the hash code of the given suite, the values are derived from
	 * the name and the vendor.
	 *
	 * @return The hash code.
	 * @since 2016/06/24
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getVendor()) ^
			Objects.hashCode(getName());
	}
	
	/**
	 * Returns {@code true} if this suite is installed.
	 *
	 * @return {@code true} if this suite is installed. The system suite always
	 * returns {@code true}.
	 * @since 2016/06/24
	 */
	public boolean isInstalled()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Checks if the suite has the specified flag set.
	 *
	 * @param __f The flag to check.
	 * @return {@code true} if the flag is set.
	 * @since 2016/06/24
	 */
	public boolean isSuiteState(SuiteStateFlag __f)
	{
		// Null is never true
		if (__f == null)
			return false;
		
		throw new todo.TODO();
		/*
		// Lock
		synchronized (this._lock)
		{
			return 0 != (this._state & (1 << __f.ordinal()));
		}
		*/
	}
	
	/**
	 * Sets whether this suite is trusted or not.
	 *
	 * @return {@code true} if this suite is trusted. The system suite always
	 * returns {@code true}.
	 * @since 2016/06/24
	 */
	public boolean isTrusted()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the given flag to the suite.
	 *
	 * @param __f The flag to set.
	 * @param __v If the flag should be set or cleared.
	 * @throws IllegalArgumentException If an attempt was made to set the
	 * {@link SuiteStateFlag#SYSTEM} or {@link SuiteStateFlag#PREINSTALLED}
	 * flags.
	 * @throws IllegalStateException If the suite was removed or this is the
	 * system suite.
	 * @throws SecurityException If the {@code {@link SWMPermission}("client",
	 * "manageSuite")} or {@code {@link SWMPermission}("crossClient",
	 * "manageSuite")} permission is not permitted.
	 * @since 2016/06/24
	 */
	public void setSuiteStateFlag(SuiteStateFlag __f, boolean __v)
		throws IllegalArgumentException, IllegalStateException,
			SecurityException
	{
		// Ignore
		if (__f == null)
			return;
		
		throw new todo.TODO();
		/*
		// {@squirreljme.error DG02 The current suite has been removed.}
		if (!isInstalled())
			throw new IllegalStateException("DG02");
		
		// {@squirreljme.error DG02 The given state flag cannot be set.
		// (The state flag)}
		if (__f == SuiteStateFlag.SYSTEM || __f == SuiteStateFlag.PREINSTALLED)
			throw new IllegalArgumentException(String.format("DG02 %s", __f));
		
		// Lock
		synchronized (this._lock)
		{
			// {@squirreljme.error DG03 Cannot change flags of the system
			// suite.}
			if (0 != (this._state & (1 << SuiteStateFlag.SYSTEM.ordinal())))
				throw new IllegalStateException("DG03");
			
			// Get the required bit
			int bit = (1 << __f.SYSTEM.ordinal());
			
			// Set or clear?
			if (__v)
				this._state |= bit;
			else
				this._state &= bit;
		}
		*/
	}
}

