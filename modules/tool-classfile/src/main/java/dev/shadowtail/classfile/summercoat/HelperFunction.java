// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.summercoat;

import cc.squirreljme.jvm.summercoat.LogicHandler;
import cc.squirreljme.jvm.summercoat.brackets.ClassInfoBracket;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Helper function for JVM calls, calls into {@link LogicHandler}.
 *
 * @since 2020/11/28
 */
public enum HelperFunction
{
	/** {@link LogicHandler#checkArrayStore(Object, Object)}. */
	CHECK_ARRAY_STORE("checkArrayStore",
		"(Ljava/lang/Object;Ljava/lang/Object;)Z"),
	
	/** {@link LogicHandler#classInfoGetProperty(ClassInfoBracket, int)}. */
	CLASS_INFO_GET_PROPERTY("classInfoGetProperty",
		"(Lcc/squirreljme/jvm/summercoat/brackets/ClassInfoBracket;I)I"),
	
	/** {@link LogicHandler#gcMemHandle(int)}.  */
	GC_MEM_HANDLE("gcMemHandle", "(I)V"),
	
	/** {@link LogicHandler#isClassInit(ClassInfoBracket)}.  */
	IS_CLASS_INIT("isClassInit", "(Lcc/squirreljme/jvm/" +
		"summercoat/brackets/ClassInfoBracket;)Z"),
	
	/** {@link LogicHandler#isArray(Object)}. */
	IS_ARRAY("isArray", "(Ljava/lang/Object;)Z"),
	
	/** {@link LogicHandler#isInstance(int, ClassInfoBracket)}.  */
	IS_INSTANCE("isInstance", "(I" +
		"Lcc/squirreljme/jvm/summercoat/brackets/ClassInfoBracket;)Z"),
	
	/** {@link LogicHandler#initClass(ClassInfoBracket)}.  */
	INIT_CLASS("initClass", "(Lcc/squirreljme/jvm/" +
		"summercoat/brackets/ClassInfoBracket;)V"),
	
	/** {@link LogicHandler#newArray(ClassInfoBracket, int)}. */
	NEW_ARRAY("newArray", "(Lcc/squirreljme/jvm/" +
		"summercoat/brackets/ClassInfoBracket;I)Ljava/lang/Object;"),
	
	/** {@link LogicHandler#newInstance(ClassInfoBracket)}. */
	NEW_INSTANCE("newInstance", "(Lcc/squirreljme/jvm/" +
		"summercoat/brackets/ClassInfoBracket;)Ljava/lang/Object;"),
	
	/** {@link LogicHandler#staticVmAttribute(int)}. */ 
	STATIC_VM_ATTRIBUTE("staticVmAttribute", "(I)I"),
	
	/** End. */
	;
	
	/** The helper class to use. */
	public static final ClassName HELPER_CLASS =
		new ClassName("cc/squirreljme/jvm/summercoat/LogicHandler");
	
	/** The member to reference. */
	public final MethodNameAndType member;
	
	/**
	 * Initializes the helper information.
	 * 
	 * @param __name The name of the member.
	 * @param __type The type of the member.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/28
	 */
	HelperFunction(String __name, String __type)
		throws NullPointerException
	{
		if (__name == null || __type == null)
			throw new NullPointerException("NARG");
		
		this.member = new MethodNameAndType(new MethodName(__name),
			new MethodDescriptor(__type));
	}
}
