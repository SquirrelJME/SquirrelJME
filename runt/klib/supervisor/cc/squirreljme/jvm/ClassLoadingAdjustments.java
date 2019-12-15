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
 * Adjustments to class loading.
 *
 * @since 2019/12/15
 */
public final class ClassLoadingAdjustments
{
	/**
	 * Is this class deferred loaded?
	 *
	 * @param __cl The class to check.
	 * @return If the class is deferred loaded.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/15
	 */
	public static final boolean isDeferredLoad(String __cl)
		throws NullPointerException
	{
		switch (__cl)
		{
				// Primitive types
			case "boolean":
			case "byte":
			case "short":
			case "char":
			case "int":
			case "float":
			case "long":
			case "double":
			
				// Primitive array types
			case "[Z":
			case "[B":
			case "[S":
			case "[C":
			case "[I":
			case "[F":
			case "[J":
			case "[D":
				
				// Common Java objects
			case "java/io/IOException":
			case "java/io/PrintStream":
			case "java/lang/Appendable":
			case "java/lang/ArithmeticException":
			case "java/lang/ArrayIndexOutOfBoundsException":
			case "java/lang/AssertionError":
			case "java/lang/AutoCloseable":
			case "java/lang/Boolean":
			case "java/lang/Byte":
			case "java/lang/Character":
			case "java/lang/Class":
			case "java/lang/ClassCastException":
			case "java/lang/Cloneable":
			case "java/lang/Closeable":
			case "java/lang/Comparable":
			case "java/lang/Double":
			case "java/lang/Enum":
			case "java/lang/Error":
			case "java/lang/Float":
			case "java/lang/IllegalArgumentException":
			case "java/lang/IllegalMonitorStateException":
			case "java/lang/IllegalStateException":
			case "java/lang/IllegalThreadStateException":
			case "java/lang/IndexOutOfBoundsException":
			case "java/lang/Integer":
			case "java/lang/InterruptedException":
			case "java/lang/Long":
			case "java/lang/Math":
			case "java/lang/NegativeArraySizeException":
			case "java/lang/NoClassDefFoundError":
			case "java/lang/NoSuchElementException":
			case "java/lang/NullPointerException":
			case "java/lang/Number":
			case "java/lang/Object":
			case "java/lang/OutOfMemoryError":
			case "java/lang/Runtime":
			case "java/lang/RuntimeException":
			case "java/lang/Short":
			case "java/lang/String":
			case "java/lang/StringBuffer":
			case "java/lang/StringBuilder":
			case "java/lang/StringIndexOutOfBoundsException":
			case "java/lang/System":
			case "java/lang/Thread":
			case "java/lang/Throwable":
			case "java/lang/VirtualMachineError":
			case "java/lang/ref/Reference":
			case "java/lang/ref/ReferenceQueue":
			case "java/lang/ref/WeakReference":
			case "java/util/Deque":
			case "java/util/Formatter":
			case "java/util/Iterable":
			case "java/util/Iterator":
			case "java/util/List":
			case "java/util/Map":
			case "java/util/Objects":
			case "java/util/Queue":
			case "java/util/Random":
			case "java/util/Set":
			
				// SquirrelJME special classes
			case "cc/squirreljme/jvm/Assembly":
			case "cc/squirreljme/jvm/ClassInfo":
			case "cc/squirreljme/jvm/Constants":
			case "cc/squirreljme/jvm/JVMFunction":
			case "cc/squirreljme/jvm/SoftDouble":
			case "cc/squirreljme/jvm/SoftFloat":
			case "cc/squirreljme/jvm/SoftInteger":
			case "cc/squirreljme/jvm/SoftLong":
			case "cc/squirreljme/jvm/SystemCallError":
			case "cc/squirreljme/jvm/SystemCallIndex":
			
				// Common array classes
			case "[Lcc/squirreljme/jvm/ClassInfo;":
			case "[Ljava/lang/String;":
				return false;
				
			default:
				return true;
		}
	}
}

