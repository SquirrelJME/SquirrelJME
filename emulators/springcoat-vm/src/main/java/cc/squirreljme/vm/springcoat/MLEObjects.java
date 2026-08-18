// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.brackets.AudioConnectionBracket;
import cc.squirreljme.jvm.mle.brackets.AudioStreamBracket;
import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.jvm.mle.brackets.MidiDeviceBracket;
import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
import cc.squirreljme.vm.OverlayVMClassLibrary;
import cc.squirreljme.vm.RawVMClassLibrary;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.springcoat.brackets.AudioConnectionObject;
import cc.squirreljme.vm.springcoat.brackets.AudioStreamObject;
import cc.squirreljme.vm.springcoat.brackets.BucketObject;
import cc.squirreljme.vm.springcoat.brackets.JarPackageObject;
import cc.squirreljme.vm.springcoat.brackets.MidiDeviceObject;
import cc.squirreljme.vm.springcoat.brackets.MidiPortObject;
import cc.squirreljme.vm.springcoat.brackets.NativeArchiveEntryObject;
import cc.squirreljme.vm.springcoat.brackets.NativeArchiveObject;
import cc.squirreljme.vm.springcoat.brackets.PipeObject;
import cc.squirreljme.vm.springcoat.brackets.TaskObject;
import cc.squirreljme.vm.springcoat.brackets.TracePointObject;
import cc.squirreljme.vm.springcoat.brackets.VMThreadObject;
import cc.squirreljme.vm.springcoat.callbacks.NativeImageLoadCallbackAdapter;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import javax.microedition.lcdui.Image;

/**
 * MLE Object wrappers.
 *
 * @since 2024/06/15
 */
public final class MLEObjects
{
	/**
	 * Not used.
	 * 
	 * @since 2024/06/15
	 */
	private MLEObjects()
	{
	}
	
	/**
	 * Adds an image to be passed to the image loading callback.
	 * 
	 * @param __callback The callback to send to.
	 * @param __frame The frame.
	 * @param __frameDelay The frame delay.
	 * @since 2022/06/28
	 */
	public static void addImage(NativeImageLoadCallbackAdapter __callback,
		Image __frame, int __frameDelay)
		throws NullPointerException
	{
		if (__callback == null || __frame == null)
			throw new NullPointerException("NARG");
		
		// Try to get a direct RGB buffer from the image, this is faster
		// than doing an RGB copy of it
		int[] buf;
		int off, len;
		if (__frame.squirreljmeIsDirect__())
		{
			buf = __frame.squirreljmeDirectRGBInt__();
			off = __frame.squirreljmeDirectOffset__();
			len = __frame.squirreljmeDirectScanLen__() * __frame.getHeight();
		}
		
		// Otherwise, we need to load the RGB data from the image
		else
		{
			buf = new int[__frame.getWidth() * __frame.getHeight()];
			off = 0;
			len = buf.length;
			
			// Read in all the data
			__frame.getRGB(buf, 0, __frame.getWidth(),
				0, 0, __frame.getWidth(), __frame.getHeight());
		}
		
		// Send to the callback
		__callback.addImage(buf, off, len, __frameDelay, __frame.hasAlpha());
	}
	
	/**
	 * Checks if this is a {@link NativeArchiveObject}.
	 * 
	 * @param __object The object to check.
	 * @return As one if this is one.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2024/03/05
	 */
	public static NativeArchiveObject archive(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof NativeArchiveObject))
			throw new SpringMLECallError("Not a NativeArchiveObject.");
		
		return (NativeArchiveObject)__object; 
	}
	
	/**
	 * Checks if this is a {@link NativeArchiveEntryObject}.
	 * 
	 * @param __object The object to check.
	 * @return As one if this is one.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2024/03/05
	 */
	public static NativeArchiveEntryObject archiveEntry(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof NativeArchiveEntryObject))
			throw new SpringMLECallError(
				"Not a NativeArchiveEntryObject.");
		
		return (NativeArchiveEntryObject)__object; 
	}
	
	/**
	 * Returns the object as an array.
	 *
	 * @param __object The object.
	 * @return The resultant array.
	 * @throws SpringMLECallError If this is not an array.
	 * @since 2025/06/22
	 */
	public static SpringArrayObject array(Object __object)
		throws SpringMLECallError
	{
		return MLEObjects.notNull(SpringArrayObject.class, __object);
	}
	
	/**
	 * Returns the audio connection object.
	 *
	 * @param __object The object to get from.
	 * @return The resultant audio connection.
	 * @throws SpringMLECallError If this is not the given object.
	 * @since 2025/06/07
	 */
	public static AudioConnectionBracket audioConnection(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof AudioConnectionObject))
			throw new SpringMLECallError(
				"Not a AudioConnectionObject.");
		
		return ((AudioConnectionObject)__object).connection;
	}
	
	/**
	 * Returns the audio stream object.
	 *
	 * @param __object The object to get from.
	 * @return The resultant audio stream.
	 * @throws SpringMLECallError If this is not the given object.
	 * @since 2025/06/07
	 */
	public static AudioStreamBracket audioStream(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof AudioStreamObject))
			throw new SpringMLECallError(
				"Not a AudioStreamObject.");
		
		return ((AudioStreamObject)__object).stream;
	}
	
	/**
	 * Returns the bucket object.
	 *
	 * @param __object The object to check.
	 * @return The resultant bucket.
	 * @throws SpringMLECallError If this is not a bucket.
	 * @since 2025/04/25
	 */
	public static BucketBracket bucket(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof BucketObject))
			throw new SpringMLECallError(
				"Not a BucketObject.");
		
		return ((BucketObject)__object).bucket;
	}
	
	/**
	 * Returns the direct byte array.
	 *
	 * @param __object The byte array to obtain.
	 * @return The resultant byte array.
	 * @throws SpringMLECallError If this is not a byte array.
	 * @since 2025/04/25
	 */
	public static byte[] byteArray(Object __object)
		throws SpringMLECallError
	{
		return MLEObjects.notNull(
			SpringArrayObjectByte.class, __object).array();
	}
	
	/**
	 * Returns the direct character array.
	 *
	 * @param __object The character array to obtain.
	 * @return The resultant character array.
	 * @throws SpringMLECallError If this is not a character array.
	 * @since 2025/04/25
	 */
	public static char[] charArray(Object __object)
		throws SpringMLECallError
	{
		return MLEObjects.notNull(
			SpringArrayObjectChar.class, __object).array();
	}
	
	/**
	 * Checks if this is a trace point object.
	 * 
	 * @param __object The object to check.
	 * @return As a trace point if this is one.
	 * @throws SpringMLECallError If this is not a trace point object.
	 * @since 2020/06/22
	 */
	public static TracePointObject debugTrace(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof TracePointObject))
			throw new SpringMLECallError("Not a trace point.");
		
		return (TracePointObject)__object; 
	}
	
	/**
	 * Checks if this is a {@link JarPackageObject}.
	 * 
	 * @param __object The object to check.
	 * @return As a jar if this is one.
	 * @throws SpringMLECallError If this is not a jar.
	 * @since 2020/06/22
	 */
	public static JarPackageObject jarPackage(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof JarPackageObject))
			throw new SpringMLECallError("Not a JarPackageObject.");
		
		return (JarPackageObject)__object; 
	}
	
	/**
	 * Obtains the raw library.
	 *
	 * @param __lib The library to get the raw library of.
	 * @return The resultant library or {@code null} if there is none.
	 * @throws SpringMLECallError On null arguments.
	 * @since 2024/03/05
	 */
	public static RawVMClassLibrary libraryRaw(VMClassLibrary __lib)
		throws SpringMLECallError
	{
		if (__lib == null)
			throw new SpringMLECallError("Null arguments.");
		
		// If an overlay, go deeper
		if (__lib instanceof OverlayVMClassLibrary)
			return libraryRaw(
				((OverlayVMClassLibrary)__lib).originalLibrary());
		
		// Is this a raw library?
		if (__lib instanceof RawVMClassLibrary)
			return (RawVMClassLibrary)__lib;
		return null;
	}
	
	/**
	 * Ensures that this is a {@link MidiDeviceObject} and returns the
	 * MIDI device.
	 * 
	 * @param __object The object to check.
	 * @return As a {@link MidiDeviceBracket}.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2022/04/22
	 */
	public static MidiDeviceBracket midiDevice(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof MidiDeviceObject))
			throw new SpringMLECallError("Not a MidiDeviceObject.");
		
		return ((MidiDeviceObject)__object).device; 
	}
	
	/**
	 * Ensures that this is a {@link MidiDeviceObject} and returns the
	 * MIDI port.
	 * 
	 * @param __object The object to check.
	 * @return As a {@link MidiPortBracket}.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2022/04/22
	 */
	public static MidiPortBracket midiPort(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof MidiPortObject))
			throw new SpringMLECallError("Not a MidiPortObject.");
		
		return ((MidiPortObject)__object).port; 
	}
	
	/**
	 * Checks if the object is not {@code null}.
	 * 
	 * @param __object The object to check.
	 * @return The object.
	 * @throws SpringMLECallError If is null.
	 * @since 2020/06/22
	 */
	public static SpringObject notNull(Object __object)
		throws SpringMLECallError
	{
		if (__object == null || SpringNullObject.NULL == __object)
			throw new SpringMLECallError("Null object.");
		
		return (SpringObject)__object;
	}
	
	/**
	 * Checks if the object is not {@code null} and is of the given type.
	 * 
	 * @param <T> The type of object this must be.
	 * @param __type The type of object this must be.
	 * @param __object The object to check.
	 * @return The object.
	 * @throws SpringMLECallError If is null.
	 * @since 2025/01/23
	 */
	public static <T extends SpringObject> T notNull(Class<T> __type,
		Object __object)
		throws SpringMLECallError
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		if (__object == null || SpringNullObject.NULL == __object)
			throw new SpringMLECallError("Null object.");
		
		try
		{
			return __type.cast(__object);
		}
		catch (ClassCastException __e)
		{
			throw new SpringMLECallError("Not a " + __type);
		}
	}
	
	/**
	 * Ensures that this is a {@link PipeObject}.
	 * 
	 * @param __object The object to check.
	 * @return As a {@link PipeObject}.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2022/03/19
	 */
	public static PipeObject pipe(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof PipeObject))
			throw new SpringMLECallError("Not a PipeObject.");
		
		return (PipeObject)__object; 
	}
	
	/**
	 * Checks if this is a simple object.
	 * 
	 * @param __object The object to check.
	 * @return The simple object.
	 * @throws SpringMLECallError If not a simple object.
	 * @since 2020/06/22
	 */
	public static SpringSimpleObject simple(Object __object)
	{
		if (!(__object instanceof SpringSimpleObject))
			throw new SpringMLECallError("Not a SpringSimpleObject.");
		
		return (SpringSimpleObject)__object; 
	}
	
	/**
	 * Checks if this is a simple object.
	 * 
	 * @param __object The object to check.
	 * @return The simple object or {@code null} if this is not.
	 * @throws SpringMLECallError If not a simple object.
	 * @since 2025/06/21
	 */
	public static SpringSimpleObject simpleOptional(Object __object)
	{
		// Null is acceptable.
		if (__object == null || __object == SpringNullObject.NULL)
			return null;
		
		return MLEObjects.simple(__object); 
	}
	
	/**
	 * Returns the directly represented string, or {@code null} if the
	 * reference is {@code null}.
	 *
	 * @param __object The string object.
	 * @return The resultant string or {@code null}.
	 * @since 2025/04/25
	 */
	public static String string(Object __object)
	{
		if (__object == null || __object == SpringNullObject.NULL)
			return null;
		
		return MLEObjects.notNull(
			SpringStringObject.class, __object).toString();
	}
	
	/**
	 * Ensures that this is a {@link TaskObject}.
	 * 
	 * @param __object The object to check.
	 * @return As a {@link TaskObject}.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2020/07/08
	 */
	public static TaskObject task(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof TaskObject))
			throw new SpringMLECallError("Not a TaskObject.");
		
		return (TaskObject)__object; 
	}
	
	/**
	 * Checks if this is a Java thread.
	 * 
	 * @param __thread The context thread.
	 * @param __object The object to check.
	 * @return The verified object.
	 * @throws SpringMLECallError If {@code __object} is {@code null} or is
	 * not an instance of {@link Throwable}.
	 * @since 2020/06/28
	 */
	public static SpringSimpleObject threadJava(SpringThreadWorker __thread,
		Object __object)
		throws SpringMLECallError
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		if (!(__object instanceof SpringSimpleObject))
			throw new SpringMLECallError("Not a Java Thread");
		
		SpringSimpleObject rv = (SpringSimpleObject)__object;
		if (!__thread.resolveClass("java/lang/Thread")
			.isAssignableFrom(rv.type()))
			throw new SpringMLECallError("Not instance of Thread.");
		
		return rv;
	}
	
	/**
	 * Ensures that this is a {@link VMThreadObject}.
	 * 
	 * @param __object The object to check.
	 * @return As a {@link VMThreadObject}.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2020/06/27
	 */
	public static VMThreadObject threadVm(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof VMThreadObject))
			throw new SpringMLECallError("Not a VMThreadObject.");
		
		return (VMThreadObject)__object; 
	}
	
	/**
	 * Casts this to a class type.
	 *
	 * @param __object The input object.
	 * @return The resultant class.
	 * @since 2025/07/05
	 */
	public static SpringClass type(Object __object)
	{
		if (!(__object instanceof SpringClass))
			throw new SpringMLECallError("Not a SpringClass.");
		
		return (SpringClass)__object; 
	}
}
