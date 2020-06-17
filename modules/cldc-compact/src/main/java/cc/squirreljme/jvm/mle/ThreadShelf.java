// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.VMThreadBracket;

/**
 * This shelf handles everything regarding threading and otherwise.
 *
 * @see VMThreadBracket
 * @since 2020/06/17
 */
public final class ThreadShelf
{
	/**
	 * Returns the number of alive threads.
	 *
	 * @param __includeMain Include main threads?
	 * @param __includeDaemon Include daemon threads?
	 * @return The number of alive threads.
	 * @since 2020/06/17
	 */
	public static native int aliveThreadCount(boolean __includeMain,
		boolean __includeDaemon);
	
	/**
	 * Creates a virtual machine thread for the given Java thread.
	 *
	 * @param __javaThread The Java thread to create under.
	 * @return The virtual machine thread.
	 * @since 2020/06/17
	 */
	public static native VMThreadBracket createVMThread(Thread __javaThread);
	
	/**
	 * Returns the current Java thread.
	 *
	 * @return The current {@link Thread}.
	 * @since 2020/06/17
	 */
	public static native Thread currentJavaThread();
	
	/**
	 * Returns the exit code for the current process.
	 *
	 * @return The exit code for the current process.
	 * @since 2020/06/17
	 */
	public static native int currentExitCode();
	
	/**
	 * Returns whether the interrupt flag was raised and clears it.
	 *
	 * @param __javaThread The Java thread.
	 * @return If the thread was interrupted.
	 * @since 2020/06/17
	 */
	public static native boolean javaThreadClearInterrupt(Thread __javaThread);
	
	/**
	 * Marks the thread as being started.
	 *
	 * @param __javaThread The thread to mark started.
	 * @since 2020/06/17
	 */
	public static native void javaThreadFlagStarted(Thread __javaThread);
	
	/**
	 * Has this Java thread been started?
	 *
	 * @param __javaThread The Java thread.
	 * @return If this thread has been started.
	 * @since 2020/06/17
	 */
	public static native boolean javaThreadIsStarted(Thread __javaThread);
	
	/**
	 * Returns the runnable for the given Java thread.
	 *
	 * @param __javaThread The Java thread.
	 * @return The {@link Runnable} for the given thread.
	 * @since 2020/06/17
	 */
	public static native Runnable javaThreadRunnable(Thread __javaThread);
	
	/**
	 * Sets if the thread is alive or not.
	 *
	 * @param __javaThread The Java thread.
	 * @param __set If this is to be alive or not. If this is {@code true}
	 * then the active count goes up, otherwise it shall go down.
	 * @since 2020/06/17
	 */
	public static native void javaThreadSetAlive(Thread __javaThread,
		boolean __set);
	
	/**
	 * Runs the main entry point for the current process and gives it all of
	 * the arguments that were specified on program initialization.
	 *
	 * @since 2020/06/17
	 */
	public static native void runProcessMain();
	
	/**
	 * Sets the current process exit code.
	 *
	 * @param __code The exit code to use.
	 * @since 2020/06/17
	 */
	public static native void setCurrentExitCode(int __code);
	
	/**
	 * Sleeps the current thread for the given amount of time.
	 *
	 * If both times are zero, this means to yield.
	 *
	 * @param __ms The number of milliseconds.
	 * @param __ns The number of nanoseconds.
	 * @return {@code true} if the thread was interrupted.
	 * @since 2020/06/17
	 */
	public static native boolean sleep(int __ms, int __ns);
	
	/**
	 * Returns the Java thread for the VM thread.
	 *
	 * @param __thread The VM thread.
	 * @return The Java thread which belongs to this thread.
	 * @since 2020/06/17
	 */
	public static native Thread toJavaThread(VMThreadBracket __thread);
	
	/**
	 * Returns the virtual machine thread from the given Java thread.
	 *
	 * @param __thread The Java thread.
	 * @return The VM thread for this thread.
	 * @since 2020/06/17
	 */
	public static native VMThreadBracket toVMThread(Thread __thread);
	
	/**
	 * Returns the thread ID for the given thread.
	 *
	 * @param __vmThread The virtual machine thread.
	 * @return The thread ID.
	 * @since 2020/06/17
	 */
	public static native int vmThreadId(VMThreadBracket __vmThread);
	
	/**
	 * Performs a hardware interrupt on the thread.
	 *
	 * @param __vmThread The virtual machine thread.
	 * @since 2020/06/17
	 */
	public static native void vmThreadInterrupt(VMThreadBracket __vmThread);
	
	/**
	 * Checks if the given thread is a main thread.
	 *
	 * @param __vmThread The thread to check.
	 * @return {@code true} if the given thread is a main thread.
	 * @since 2020/06/17
	 */
	public static native boolean vmThreadIsMain(VMThreadBracket __vmThread);
	
	/**
	 * Sets the thread priority.
	 *
	 * @param __vmThread The virtual machine thread.
	 * @param __p The priority to set.
	 * @since 2020/06/17
	 */
	public static native void vmThreadSetPriority(VMThreadBracket __vmThread,
		int __p);
	
	/**
	 * Performs the actual start of the given thread.
	 *
	 * @param __vmThread The thread to start.
	 * @since 2020/06/17
	 */
	public static native boolean vmThreadStart(VMThreadBracket __vmThread);
	
	/**
	 * Waits for the state of threads to be updated, or just times out.
	 *
	 * A thread update is when another thread becomes alive, becomes dead,
	 * or is started.
	 *
	 * @param __ms The amount of time to wait for.
	 * @since 2020/06/17
	 */
	public static native void waitForUpdate(int __ms);
}
