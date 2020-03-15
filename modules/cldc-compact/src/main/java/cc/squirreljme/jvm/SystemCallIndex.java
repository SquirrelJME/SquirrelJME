// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This contains the index of system calls.
 *
 * @since 2019/05/23
 */
public interface SystemCallIndex
{
	/**
	 * Checks if the system call is supported.
	 *
	 * @squirreljme.syscallparam 1 The system call index to query.
	 * @squirreljme.syscallreturn Zero if not supported, otherwise a non-zero
	 * value.
	 */
	byte QUERY_INDEX =
		0;
	
	/**
	 * Gets the last error code.
	 *
	 * This value that stores the error state is thread-local and it may be
	 * stored with a precision of at least 16-bits.
	 *
	 * If the system call index is not valid then it is assumed to be
	 * {@link #QUERY_INDEX}.
	 *
	 * @squirreljme.syscallparam 1 The system call index to query.
	 * @squirreljme.syscallreturn The last error code, will be zero if the last
	 * command succeeded.
	 */
	byte ERROR_GET =
		1;
	
	/**
	 * Sets the last error code.
	 *
	 * This value that stores the error state is thread-local and it may be
	 * stored with a precision of at least 16-bits.
	 *
	 * If the system call index is not valid then it is assumed to be
	 * {@link #QUERY_INDEX}.
	 *
	 * @squirreljme.syscallparam 1 The system call index to query.
	 * @squirreljme.syscallparam 2 The value to set error register to.
	 * @squirreljme.syscallreturn The value which was previously in the
	 * register.
	 */
	byte ERROR_SET =
		2;
	
	/**
	 * Current wall clock milliseconds.
	 *
	 * @squirreljme.syscallreturn The current milliseconds time.
	 */
	byte TIME_MILLI_WALL =
		3;
	
	/**
	 * Returns the cross-IPC exception class which has been stored.
	 *
	 * This system call should have the same effect regardless of whether it
	 * is a supervisor thread or user thread, it does not get forwarded
	 * to the task system call handler.
	 *
	 * @squirreljme.syscallreturn The exception which has been stored.
	 */
	byte EXCEPTION_LOAD =
		4;
	
	/**
	 * Current monotonic clock nanoseconds (low).
	 *
	 * @squirreljme.syscallreturn The monotonic nanoseconds time.
	 */
	byte TIME_NANO_MONO =
		5;
	
	/**
	 * Stores the cross-IPC exception class for system call errors.
	 *
	 * This system call should have the same effect regardless of whether it
	 * is a supervisor thread or user thread, it does not get forwarded
	 * to the task system call handler.
	 *
	 * @squirreljme.syscallparam 1 The exception to store.
	 * @squirreljme.syscallreturn The old value that was stored in the
	 * register.
	 */
	byte EXCEPTION_STORE =
		6;
	
	/**
	 * VM Information: Free memory in bytes.
	 *
	 * @squirreljme.syscallreturn The free memory amount in bytes.
	 */
	byte VMI_MEM_FREE =
		7;
	
	/**
	 * VM Information: Used memory in bytes.
	 *
	 * @squirreljme.syscallreturn The used memory amount in bytes.
	 */
	byte VMI_MEM_USED =
		8;
	
	/**
	 * VM Information: Max memory in bytes.
	 *
	 * @squirreljme.syscallreturn The max memory amount in bytes.
	 */
	byte VMI_MEM_MAX =
		9;
	
	/**
	 * Suggests that the garbage collector should run, note that this may be
	 * a deferred operation and might not be immediate.
	 *
	 * @squirreljme.syscallreturn Generally zero although any other value could
	 * be returned.
	 */
	byte GARBAGE_COLLECT =
		10;
	
	/**
	 * Exits the VM with the given exit code.
	 *
	 * @squirreljme.syscallparam 1 The exit code to exit the process with.
	 * @squirreljme.syscallreturn This generally does not return, if it does
	 * then the error code will likely specify why this failed.
	 */
	byte EXIT =
		11;
	
	/**
	 * The API Level of the VM, this has been deprecated since the current
	 * SquirrelJME API specified in these system calls better handles various
	 * features.
	 *
	 * @squirreljme.syscallreturn The API level of the virtual machine.
	 */
	@Deprecated
	byte API_LEVEL =
		12;
	
	/**
	 * The pipe descriptor for stdin.
	 *
	 * @squirreljme.syscallreturn The pipe descriptor for standard input.
	 */
	byte PD_OF_STDIN =
		13;
	
	/**
	 * The pipe descriptor for stdout.
	 *
	 * @squirreljme.syscallreturn The pipe descriptor for standard output.
	 */
	byte PD_OF_STDOUT =
		14;
	
	/**
	 * The pipe descriptor for stderr.
	 *
	 * @squirreljme.syscallreturn The pipe descriptor for standard error.
	 */
	byte PD_OF_STDERR =
		15;
	
	/**
	 * Pipe descriptor: Write single byte.
	 *
	 * @squirreljme.syscallparam 1 The pipe descriptor.
	 * @squirreljme.syscallparam 2 The value of the byte to write, only the
	 * lowest 8-bits is used.
	 * @squirreljme.syscallreturn The number of bytes written to the output,
	 * if this returns a value lower than zero then it indicates an error.
	 */
	byte PD_WRITE_BYTE =
		16;
	
	/**
	 * Bulk sets the memory inside of a region, this follows the same pattern
	 * as C's {@code memset()} operation.
	 *
	 * @squirreljme.syscallparam 1 The address to set.
	 * @squirreljme.syscallparam 2 The value to set the region with.
	 * @squirreljme.syscallparam 3 The number of bytes to set.
	 * @squirreljme.syscallreturn The number of bytes actually written, if this
	 * is zero then it is likely the system call is not supported.
	 */
	byte MEM_SET =
		17;
	
	/**
	 * Bulk sets the memory inside of a region writing full integer values at
	 * a time which is generally faster, this follows the same pattern as C's
	 * {@code memset()} operation.
	 *
	 * @squirreljme.syscallparam 1 The address to set.
	 * @squirreljme.syscallparam 2 The value to set the region with.
	 * @squirreljme.syscallparam 3 The number of bytes to set, the lower 2-bits
	 * ({@code 0x3}) will be masked off so the length is always a multiple of
	 * four.
	 * @squirreljme.syscallreturn The number of bytes actually written,
	 * if this is zero then it is likely the system call is not supported.
	 */
	byte MEM_SET_INT =
		18;
	
	/**
	 * Get the height of the call stack.
	 *
	 * @squirreljme.syscallreturn The height of the call stack.
	 */
	byte CALL_STACK_HEIGHT =
		19;
	
	/**
	 * Gets the specified call stack item.
	 *
	 * @squirreljme.syscallparam 1 The number of frames from the top of the
	 * call stack to get the items for, zero will be the top-most item.
	 * @squirreljme.syscallparam 2 The item to obtain as specified in
	 * {@link CallStackItem}.
	 * @squirreljme.syscallreturn The value of the item, if it is undefined or
	 * not supported then zero will be returned.
	 */
	byte CALL_STACK_ITEM =
		20;
	
	/**
	 * Returns the string of the given pointer.
	 *
	 * @squirreljme.syscallparam 1 The pointer to the modified UTF encoded
	 * string.
	 * @squirreljme.syscallreturn An instance of {@link String}.
	 */
	byte LOAD_STRING =
		21;
	
	/**
	 * Fatal ToDo hit.
	 *
	 * @squirreljme.syscallparam 1 The code to use for the To Do.
	 * @squirreljme.syscallreturn This should not return unless it is not
	 * supported.
	 */
	byte FATAL_TODO =
		22;
	
	/**
	 * This is used to indicate that the supervisor booted correctly and that
	 * execution control is normal.
	 *
	 * @squirreljme.syscallreturn Generally zero as no value is intended to be
	 * returned.
	 */
	byte SUPERVISOR_BOOT_OKAY =
		23;
	
	/**
	 * Get, set, or change a property of the framebuffer, the properties which
	 * are defined are specified in {@link Framebuffer}.
	 *
	 * @squirreljme.syscallparam 1 The frame buffer property to select.
	 * @squirreljme.syscallparam ... Undefined, this depends on the property
	 * selected.
	 * @squirreljme.syscallreturn Whatever value the frame buffer property will
	 * return.
	 */
	byte FRAMEBUFFER =
		24;
	
	/**
	 * Returns the native byte order of the system the virtual machine is
	 * running on.
	 *
	 * @squirreljme.syscallreturn Non-zero if little endian, otherwise zero
	 * will be big endian.
	 */
	byte BYTE_ORDER_LITTLE =
		25;
	
	/**
	 * Returns the pointer to the option JAR data.
	 *
	 * @squirreljme.syscallparam 1 The option JAR slot to request.
	 * @squirreljme.syscallreturn A pointer to the data or zero if there is no
	 * option JAR defined in the requested slot.
	 */
	byte OPTION_JAR_DATA =
		26;
	
	/**
	 * Returns the size of the option JAR data.
	 *
	 * @squirreljme.syscallparam 1 The option JAR slot to request.
	 * @squirreljme.syscallreturn The size of the specified option JAR or zero
	 * if there is no
	 * option JAR defined in the requested slot.
	 */
	byte OPTION_JAR_SIZE =
		27;
	
	/**
	 * Loads the specified class.
	 *
	 * @squirreljme.syscallparam 1 The Modified UTF specifying the class name.
	 * @squirreljme.syscallreturn The pointer to the loaded class info, will be
	 * zero on failure.
	 */
	byte LOAD_CLASS_UTF =
		28;
	
	/**
	 * Loads the specified class.
	 *
	 * @squirreljme.syscallparam 1 A byte array encoded in UTF-8 which contains
	 * the class name.
	 * @squirreljme.syscallreturn The pointer to the loaded class info, will be
	 * zero on failure.
	 */
	byte LOAD_CLASS_BYTES =
		29;
	
	/**
	 * Sets the value of a supervisor property.
	 *
	 * Supervisor properties are local to a thread.
	 *
	 * Only the supervisor is allowed to set these properties.
	 *
	 * @squirreljme.syscallparam 1 The supervisor property to set.
	 * @squirreljme.syscallparam 2 The new value of the property.
	 * @squirreljme.syscallreturn A non-zero value if this was successful.
	 */
	byte SUPERVISOR_PROPERTY_SET =
		30;
	
	/**
	 * Gets the value of a supervisor property.
	 *
	 * Supervisor properties are local to a thread.
	 *
	 * @squirreljme.syscallparam 1 The supervisor property to get.
	 * @squirreljme.syscallreturn The value of that property, error should be
	 * checked to ensure that it did not fail.
	 */
	byte SUPERVISOR_PROPERTY_GET =
		31;
	
	/**
	 * Sets the task ID of the current thread frame.
	 *
	 * Only the supervisor is allowed to set tihus.
	 *
	 * @squirreljme.syscallparam 1 The task ID to set.
	 * @squirreljme.syscallreturn A non-zero value if this was successful.
	 */
	byte FRAME_TASK_ID_SET =
		32;
	
	/**
	 * Gets the value of a thread register.
	 *
	 * @squirreljme.syscallreturn The value of the task ID.
	 */
	byte FRAME_TASK_ID_GET =
		33;
	
	/**
	 * Perform a feedback operation.
	 *
	 * @squirreljme.syscallparam 1 The type of feedback to perform.
	 * @squirreljme.syscallparam 2 The duration of the feedback.
	 * @squirreljme.syscallreturn Non-zero on success.
	 */
	byte DEVICE_FEEDBACK =
		34;
	
	/**
	 * Sleep for the given number of nanoseconds.
	 *
	 * @squirreljme.syscallparam 1 The number of milliseconds to sleep for.
	 * @squirreljme.syscallparam 2 The number of nanoseconds to sleep for.
	 * @squirreljme.syscallreturn Returns zero unless sleep was interrupted.
	 */
	byte SLEEP =
		35;
	
	/**
	 * If the framebuffer is shared with the console, this tells the console
	 * printer to not send messages to the screen as it will corrupt the
	 * display on the screen.
	 */
	byte SQUELCH_FB_CONSOLE =
		36;
	
	/**
	 * Perform IPC call.
	 *
	 * @squirreljme.syscallparam 1 The task to call, {@code 0} is the
	 * supervisor.
	 * @squirreljme.syscallparam 2 The IPC identifier which specifies which
	 * service this was associated with.
	 * @squirreljme.syscallparam ... Any arguments to the call.
	 * @squirreljme.syscallreturn The value returned from the remote call.
	 */
	byte IPC_CALL =
		37;
	
	/**
	 * Returns flags used to change how debugging is used within SquirreLJME.
	 *
	 * @since 2020/03/15
	 */
	byte DEBUG_FLAGS =
		38;
	
	/**
	 * The number of system calls that are defined in this run-time.
	 *
	 * One must NEVER utilize this value in a system call as it will have
	 * unintended consequences of requesting future API values.
	 */
	byte NUM_SYSCALLS =
		39;
}

