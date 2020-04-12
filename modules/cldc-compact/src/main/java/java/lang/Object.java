// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.asm.ObjectAccess;

/**
 * This class is the root of all class trees in Java.
 *
 * @since 2016/02/08
 */
@ImplementationNote("The Java compiler does not allow any final fields to " +
	"exist in Object and if they are set via assignment no code will be " +
	"generated for them, so as such Object effectively has no fields.")
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
	 * @return The cloned object.
	 * @since 2016/02/08
	 */
	protected Object clone()
		throws CloneNotSupportedException
	{
		// If this is an array copy elements around
		Class<?> cl = this.getClass();
		if (cl.isArray())
		{
			// Need length of this array to recreate!
			int len = Assembly.arrayLength(this);
			
			// Allocate new array
			Object dest = ObjectAccess.arrayNew(cl, len);
			
			// Copy everything over
			System.arraycopy(this, 0, dest, 0, len);
			
			// This array was cloned
			return dest;
		}
		
		// {@squirreljme.error ZZ1d This object does not support being cloned.}
		throw new CloneNotSupportedException("ZZ1d");
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
	 * @param __o The object to check equality against.
	 * @return {@code true} if the two objects are equal.
	 * @since 2016/02/08
	 */
	public boolean equals(Object __o)
	{
		return this == __o;
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
		return ObjectAccess.classOf(this);
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
		return System.identityHashCode(this);
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
		// {@squirreljme.error ZZ1e This thread does not own the monitor for
		// this thread.}
		if (ObjectAccess.monitorNotify(this, false) < 0)
			throw new IllegalMonitorStateException("ZZ1e");
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
		// {@squirreljme.error ZZ1f This thread does not own the monitor for
		// this thread.}
		if (ObjectAccess.monitorNotify(this, true) < 0)
			throw new IllegalMonitorStateException("ZZ1f");
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
		return this.getClass().getName() + "@" +
			Integer.toString(this.hashCode(), 16);
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
		this.wait(0, 0);
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
		this.wait(__ms, 0);
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
	public final void wait(long __ms, int __ns)
		throws IllegalArgumentException, IllegalMonitorStateException,
			InterruptedException
	{
		// Call wait, but return status can have multiple kind of things
		// going on
		switch (ObjectAccess.monitorWait(this, __ms, __ns))
		{
				// {@squirreljme.error ZZ1g Cannot wait on monitor because
				// this thread does not own the monitor.}
			case ObjectAccess.MONITOR_NOT_OWNED:
				throw new IllegalMonitorStateException("ZZ1g");
			
				// Not interrupted
			case ObjectAccess.MONITOR_NOT_INTERRUPTED:
				return;
			
				// {@squirreljme.error ZZ1h Wait operation has been
				// interrupted.}
			case ObjectAccess.MONITOR_INTERRUPTED:
				Thread.currentThread()._interrupted = false;
				throw new InterruptedException("ZZ1h");
				
				// Should not happen
			default:
				throw new todo.OOPS();
		}
	}
}

