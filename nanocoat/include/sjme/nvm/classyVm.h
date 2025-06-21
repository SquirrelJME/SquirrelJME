/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Classes as they appear to the runtime virtual machine.
 * 
 * @since 2024/09/08
 */

#ifndef SJME_C_CLASSYVM_H
#define SJME_C_CLASSYVM_H

#include "sjme/nvm/classy.h"
#include "sjme/nvm/rom.h"
#include "sjme/nvm/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CLASSYVM_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/
	
/** The class name length limit. */
#define SJME_NVM_CLASS_NAME_LIMIT 256
	
/**
 * A list of classes.
 * 
 * @since 2024/10/22
 */
SJME_LIST_DECLARE(sjme_jclass, 0);

/**
 * Base structure for the class loader.
 * 
 * @since 2024/09/08
 */
typedef struct sjme_nvm_vmClass_loaderBase sjme_nvm_vmClass_loaderBase;

/**
 * Virtual machine equivalent to Java's @c ClassLoader .
 * 
 * @since 2024/09/08
 */
typedef sjme_nvm_vmClass_loaderBase* sjme_nvm_vmClass_loader;

/**
 * The basic type of call for a method.
 * 
 * @since 2024/11/07
 */
typedef enum sjme_nvm_methodCallType
{
	/** Non-virtual, special, call. */
	SJME_NVM_CALL_NON_VIRTUAL,
	
	/** Virtual call. */
	SJME_NVM_CALL_VIRTUAL,
	
	/** Virtual super call. */
	SJME_NVM_CALL_SUPER,
	
	/** The number of call types. */
	SJME_NVM_NUM_METHOD_CALL_TYPE,
} sjme_nvm_methodCallType;

/** List of method binds. */
SJME_LIST_DECLARE(sjme_jmethodID, 0);

/** List of interface binds. */
SJME_LIST_DECLARE(sjme_jinterfaceID, 0);

struct sjme_jinterfaceIDBase
{
	/** Common virtual machine info. */
	sjme_nvm_commonBase common;

	/** The class this interface is. */
	sjme_jclass isInterface;

	/** The hash of the descriptor of the interface being implemented. */
	sjme_jint descriptorHash;

	/** The methods which are bound to this interface instance. */
	sjme_list_sjme_jmethodID* methods;
};
	
struct sjme_jmemberIDBase
{
	/** Common virtual machine info. */
	sjme_nvm_commonBase common;
	
	/** The class this member is in. */
	sjme_jclass inClass;

	/** The identifier hash of this member. */
	sjme_jint idHash;
	
	/** The name of this member. */
	sjme_nvm_stringPool_string name;
	
	/** The type of this member. */
	sjme_nvm_stringPool_string type;
};

struct sjme_jmethodIDBase
{
	/** Member information. */
	sjme_jmemberIDBase member;

	/** The method flags. */
	sjme_nvm_class_methodFlags flags;
	
	/** The info this is bound to, for virtual and non-virtual calls. */
	sjme_nvm_class_methodInfo info[SJME_NVM_NUM_METHOD_CALL_TYPE];
};

/**
 * Returns the direct pointer to the field data pointer.
 *
 * @param instance The object to access with.
 * @param field The field to access for.
 * @since 2025/06/21
 */
typedef sjme_jvalue* (*sjme_nvm_jfieldAccessFunc)(
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jfieldID field);

struct sjme_jfieldIDBase
{
	/** Member information. */
	sjme_jmemberIDBase member;

	/** The field flags. */
	sjme_nvm_class_fieldFlags flags;
	
	/** The field this is bound to. */
	sjme_nvm_class_fieldInfo info;

	/** The accessor for this field. */
	sjme_nvm_jfieldAccessFunc accessor;

	/** The direct offset to this field. */
	sjme_jint pointerOffset;
};

struct sjme_nvm_vmClass_loaderBase
{
	/** Common NanoCoat storage. */
	sjme_nvm_commonBase common;
	
	/** The state this loader is within. */
	sjme_nvm inState;
	
	/** Read/write lock. */
	sjme_thread_rwLock rwLock;
	
	/** The class path to use. */
	sjme_list_sjme_nvm_rom_library* classPath;
	
	/** Classes which have been loaded. */
	sjme_list_sjme_jclass* classes;

	/** String pool for classes which do not come from suites or libraries. */
	sjme_nvm_stringPool nullStrings;
};

/**
 * Checks and initializes the class if needed.
 * 
 * @param inClass The class to be initialized.
 * @param contextThread The thread this is working under.
 * @return Any resultant error, if any.
 * @since 2024/10/24
 */
sjme_errorCode sjme_nvm_vmClass_checkInit(
	sjme_attrOutNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread);

/**
 * Checks and loads the class if needed.
 * 
 * @param inClass The class to be loaded.
 * @param contextThread The thread this is working under.
 * @return Any resultant error, if any.
 * @since 2024/10/24
 */
sjme_errorCode sjme_nvm_vmClass_checkLoad(
	sjme_attrOutNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread);

/**
 * Locates a field in the given class by name and type.
 * 
 * @param inClass The class to look within.
 * @param contextThread The context thread.
 * @param instanceType The type of field instance to locate.
 * @param required Is this a required lookup?
 * @param inName The name of the field to resolve.
 * @param inType The type of the field to resolve.
 * @param outID The resultant field.
 * @return Any resultant error, if any.
 * @since 2025/06/19
 */
sjme_errorCode sjme_nvm_vmClass_fieldIDByNameType(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_charSeq inName,
	sjme_attrInPositive sjme_charSeq inType,
	sjme_attrOutNotNull sjme_jfieldID* outID);
	
/**
 * Locates the source field in the given class chain for the given static
 * or instance field ID, which would be the source target field for the given
 * field slot.
 * 
 * @param inClass The class tree to look within. 
 * @param instanceType The type of instance this is.
 * @param fieldId The field identifier.
 * @param javaType The Java type used.
 * @param outInfo The output info.
 * @return Any resultant error.
 * @since 2024/11/03
 */
sjme_errorCode sjme_nvm_vmClass_fieldSourceByIndex(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS)
		sjme_javaTypeId javaType,
	sjme_attrInPositive sjme_jint fieldId,
	sjme_attrOutNotNull sjme_nvm_class_fieldInfo* outInfo);

/**
 * Loads the specified class by the given binary name.
 * 
 * @param inLoader The loader to use. 
 * @param outClass The resultant class.
 * @param contextThread The thread where any constructors can be called if
 * needed.
 * @param className The class to load.
 * @param doInit Initialize this class?
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_vmClass_loaderLoad(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_charSeq className,
	sjme_attrInValue sjme_jboolean doInit);

/**
 * Checks if the given class can be assigned to this one, the check is
 * in the same order as @code instanceof Object @endcode that
 * is @code b.getClass().isAssignableFrom(a.getClass()) ==
 * (a instanceof b) @endcode
 * and  @code (Class<B>)a @endcode does not throw @c ClassCastException.
 *
 * This will hide errors if the classes are not valid. 
 *
 * @param contextThread The context thread.
 * @param canAssignTo Can @c fromClass be assigned to this class?
 * @param fromClass The class to check if this can be assigned to.
 * @return If the class is assignable from the given class.
 * @since 2025/02/16
 */
sjme_jboolean sjme_nvm_vmClass_isAssignableFrom(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jclass canAssignTo,
	sjme_attrInNotNull sjme_jclass fromClass);
	
/**
 * Returns the is-classes for the given class.
 *
 * @param contextThread The context thread.
 * @param inClass The current class.
 * @param outIsClasses The resultant is-classes.
 * @return Any resultant error, if any.
 * @since 2025/04/02
 */
sjme_errorCode sjme_nvm_vmClass_isClasses(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrOutNotNull sjme_list_sjme_jclass** outIsClasses);

/**
 * Is the other class a super class of the base class?
 * 
 * @param thisClass The base class.
 * @param otherClass The other class to check.
 * @return If the other class is a super class.
 * @since 2025/06/19
 */
sjme_jboolean sjme_nvm_vmClass_isSuperClass(
	sjme_attrInNotNull sjme_jclass thisClass,
	sjme_attrInNotNull sjme_jclass otherClass);
	
/**
 * Generates an array class type of the specified component type.
 * 
 * @param inLoader The loader to use. 
 * @param outClass The resultant class.
 * @param contextThread The thread where any constructors can be called if
 * needed.
 * @param componentType The component type of the array.
 * @param dims The number of dimensions of an array for the component type. 
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_vmClass_loaderLoadArray(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jclass componentType,
	sjme_attrInPositiveNonZero sjme_jint dims);

/**
 * Generates an array class type of the specified component type.
 * 
 * @param inLoader The loader to use. 
 * @param outClass The resultant class.
 * @param contextThread The thread where any constructors can be called if
 * needed.
 * @param componentType The component type of the array.
 * @param dims The number of dimensions of an array for the component type. 
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_vmClass_loaderLoadArrayA(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_lpcstr componentType,
	sjme_attrInPositiveNonZero sjme_jint dims);

/**
 * Loads the specified class by the given field name.
 * 
 * @param inLoader The loader to use. 
 * @param outClass The resultant class.
 * @param contextThread The thread where any constructors can be called if
 * needed.
 * @param fieldName The field name to load. 
 * @param doInit Initialize this class?
 * @return Any resultant error, if any.
 * @since 2024/10/22
 */
sjme_errorCode sjme_nvm_vmClass_loaderLoadF(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_charSeq fieldName,
	sjme_attrInValue sjme_jboolean doInit);

/**
 * Loads the specified class by the given field name.
 * 
 * @param inLoader The loader to use. 
 * @param outClass The resultant class.
 * @param contextThread The thread where any constructors can be called if
 * needed.
 * @param fieldName The field name to load. 
 * @param doInit Initialize this class?
 * @return Any resultant error, if any.
 * @since 2024/10/22
 */
sjme_errorCode sjme_nvm_vmClass_loaderLoadFU(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_lpcstr fieldName,
	sjme_attrInValue sjme_jboolean doInit);

/**
 * Generates a primitive class type.
 * 
 * @param inLoader The loader to use. 
 * @param outClass The resultant class.
 * @param contextThread The thread where any constructors can be called if
 * needed.
 * @param basicType The type of primitive type to create for.
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_vmClass_loaderLoadPrimitive(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NUM_BASIC_TYPE_IDS) sjme_basicTypeId basicType);

/**
 * Initializes a new class loader.
 * 
 * @param inState The input state.
 * @param outLoader The resultant class loader. 
 * @param classPath The classpath to use for the loader.
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_vmClass_loaderNew(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_nvm_vmClass_loader* outLoader,
	sjme_attrInNotNull sjme_list_sjme_nvm_rom_library* classPath);

/**
 * Looks up a method ID from an interface call.
 * 
 * @param contextThread The current context thread.
 * @param required Is this required to be found?
 * @param outID The resultant method ID.
 * @param forObject The object this is for.
 * @param forMember The interface this is invoking.
 * @return Any resultant error, if any.
 * @since 2025/04/01
 */
sjme_errorCode sjme_nvm_vmClass_methodIDByInterface(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrOutNotNull sjme_jmethodID* outID,
	sjme_attrInNotNull sjme_jobject forObject,
	sjme_attrInNotNull sjme_nvm_class_poolEntryMember* forMember);
	
/**
 * Locates a method by the given name and type.
 * 
 * @param inClass The class to look within.
 * @param contextThread The thread this request is under.
 * @param instanceType The instance type of the method.
 * @param required Is this method required?
 * @param inName The name of the method to find.
 * @param inType The type of the method to find.
 * @param outID The resultant method ID.
 * @return Any resultant error, if any.
 * @since 2024/11/13 
 */
sjme_errorCode sjme_nvm_vmClass_methodIDByNameType(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_charSeq inName,
	sjme_attrInPositive sjme_charSeq inType,
	sjme_attrOutNotNull sjme_jmethodID* outID);
	
/**
 * Locates a method by the given name and type.
 * 
 * @param inClass The class to look within.
 * @param contextThread The thread this request is under.
 * @param instanceType The instance type of the method.
 * @param required Is this method required?
 * @param inName The name of the method to find.
 * @param inType The type of the method to find.
 * @param outID The resultant method ID.
 * @return Any resultant error, if any.
 * @since 2025/03/16 
 */
sjme_errorCode sjme_nvm_vmClass_methodIDByNameTypeU(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_lpcstr inName,
	sjme_attrInPositive sjme_lpcstr inType,
	sjme_attrOutNotNull sjme_jmethodID* outID);

/**
 * Locates the source method in the given class chain for the given static
 * or instance method ID, which would be the source target method for the given
 * method slot. This does not take into consideration overridden methods
 * or otherwise.
 * 
 * @param inClass The class tree to look within. 
 * @param instanceType The type of instance this is.
 * @param required Is this method required?
 * @param methodId The method identifier.
 * @param outInfo The output info.
 * @return Any resultant error.
 * @since 2024/11/03
 */
sjme_errorCode sjme_nvm_vmClass_methodSourceByIndex(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_jint methodId,
	sjme_attrOutNotNull sjme_nvm_class_methodInfo* outInfo);

/** Member access flags. */
#define SJME_M_AF(of) \
	((of)->flags.member.access)

/** Member name. */
#define SJME_M_N(of) \
	((of)->member.name)

/** Member type. */
#define SJME_M_T(of) \
	((of)->member.type)
	
/** Get super class. */
#define SJME_C_SU(cl) \
	(sjme_atomic_sjme_jclass_get(&(cl)->superClass))
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CLASSYVM_H
}
		#undef SJME_CXX_SQUIRRELJME_CLASSYVM_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CLASSYVM_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CLASSYVM_H */
