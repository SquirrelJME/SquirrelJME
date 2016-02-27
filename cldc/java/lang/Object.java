// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This class is the root of all class trees in Java.
 *
 * @since 2016/02/08
 */
public class Object
{
	/**
	 * Clones the current copy creating a shallow copy of it if
	 * {@code Cloneable} is implemented, unless this method is overridden to
	 * perform class specific cloning.
	 *
	 * @throws CloneNotSupportedException If cloning is not supported by the
	 * current class, the default implementation always throws this if the
	 * {@link Cloneable} interface does not exist.
	 * @since 2016/02/08
	 */
	protected Object clone()
		throws CloneNotSupportedException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Checks for equality between this object and another. Equality should be
	 * reflexive (equal to self), symmetric (a is equal to b as b is equal to
	 * a), transitive (a is equal to b, and b is equal to c, then a must be
	 * equal to c), and consistent (if no modifications to the object were made
	 * then it should return the same value returned as the previous call).
	 *
	 * If this method is overriden, then also override {@code hashCode()}.
	 *
	 * @return {@code true} if the two objects are equal.
	 * @since 2016/02/08
	 */
	protected boolean equals(Object __o)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the class object which is associated with this object, this is
	 * the class of the current object.
	 *
	 * @return The current class object.
	 * @since 2016/02/08
	 */
	public final Class<?> getClass()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Calculates the hash code for this object.
	 *
	 * If two objects are equal than they must have the same hash code,
	 * otherwise undefined behavior will occur with their potential usage.
	 *
	 * @return The hash code of this object.
	 * @since 2016/02/08
	 */
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Notifies a single thread which is waiting on this monitor.
	 *
	 * @throws IllegalMonitorStateException If the current thread does not own
	 * the monitor for this object.
	 * @since 2016/02/08
	 */
	public final void notify()
		throws IllegalMonitorStateException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Notifies all threads which are waiting on this monitor.
	 *
	 * @throws IllegalMonitorStateException If the current thread does not own
	 * the monitor for this object.
	 * @since 2016/02/09
	 */
	public final void notifyAll()
		throws IllegalMonitorStateException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the string representation of this object.
	 *
	 * The default implementation is {@code classname@hashcode}.
	 *
	 * @return The string representation of this object.
	 * @since 2016/02/09
	 */
	public String toString()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Causes the current thread to wait on this object's monitor until
	 * {@link #notify()} or {@link #notifyAll()} has been call.
	 *
	 * @throws IllegalMonitorStateException If the current thread does not own
	 * the monitor for this object.
	 * @throws InterruptedException If the current thread was interrupted
	 * during the wait.
	 * @since 2016/02/09
	 */
	public final void wait()
		throws InterruptedException, IllegalMonitorStateException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Causes the current thread to wait on this object's monitor until
	 * {@link #notify()} or {@link #notifyAll()} has been call, however it will
	 * stop after the given time has elapsed.
	 *
	 * @param __ms The milliseconds to wait for, if this is {@code 0} then this
	 * will wait forever.
	 * @throws IllegalArgumentException If any input time is negative.
	 * @throws IllegalMonitorStateException If the current thread does not own
	 * the monitor for this object.
	 * @throws InterruptedException If the current thread was interrupted
	 * during the wait.
	 * @since 2016/02/09
	 */
	public final void wait(long __ms)
		throws IllegalArgumentException, IllegalMonitorStateException,
			InterruptedException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Causes the current thread to wait on this object's monitor until
	 * {@link #notify()} or {@link #notifyAll()} has been call, however it will
	 * stop after the given time has elapsed.
	 *
	 * If both {@code __ms} and {@code __ns} are zero, then the wait will be
	 * forever.
	 *
	 * @param __ms The milliseconds to wait for, if this is {@code 0} then this
	 * will wait forever.
	 * @param __ns The number of nanoseconds to additionally wait for.
	 * @throws IllegalArgumentException If any input time is negative or
	 * {@code __ns} is not in the range [0, 999999].
	 * @throws IllegalMonitorStateException If the current thread does not own
	 * the monitor for this object.
	 * @throws InterruptedException If the current thread was interrupted
	 * during the wait.
	 * @since 2016/02/09
	 */
	public final void wait(long __ms, long __ns)
		throws IllegalArgumentException, IllegalMonitorStateException,
			InterruptedException
	{
		throw new Error("TODO");
	}
}

