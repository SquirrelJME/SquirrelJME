/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// --------------------------------------------------------------------------*/

/**
 * This is the core header which is used by the SquirrelJME C output JIT
 * compiler.
 *
 * @since 2016/07/17
 */

/** Header guard. */
#ifndef SJME_hGNETMULTIPHASIGCSQUIRRELH
#define SJME_hGNETMULTIPHASIGCSQUIRRELH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXNETMULTIPHASIGCSQUIRRELH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

#include <stdlib.h>
#include <stdint.h>

/**
 * Boolean.
 *
 * @since 2016/07/22
 */
typedef uint8_t jboolean;
#define JFALSE UINT8_C(0)
#define JTRUE UINT8_C(1)

/**
 * Java byte.
 *
 * @since 2016/07/21
 */
typedef int8_t jbyte;
#define JBYTE_C(x) INT8_C(x)

/**
 * Java character.
 *
 * @since 2016/07/21
 */
typedef uint16_t jchar;
#define JCHAR_C(x) UINT16_C(x)

/**
 * 32-bit integer.
 *
 * @since 2016/07/21
 */
typedef int32_t jint;
#define JINT_C(x) INT32_C(x)

/**
 * 64-bt integer.
 *
 * @since 2016/07/22
 */
typedef int64_t jlong;
#define JLONG_C(x) INT64_C(x)

/**
 * Flags which are associated with classes.
 *
 * @since 2016/07/18
 */
typedef enum SJME_ClassFlag
{
	/** Public access. */
	SJME_CLASSFLAG_PUBLIC =
		1,
	
	/** Final. */
	SJME_CLASSFLAG_FINAL =
		2,
	
	/** Super. */
	SJME_CLASSFLAG_SUPER =
		4,
	
	/** Interface. */
	SJME_CLASSFLAG_INTERFACE =
		8,
	
	/** Abstract. */
	SJME_CLASSFLAG_ABSTRACT =
		16,
	
	/** Synthetic. */
	SJME_CLASSFLAG_SYNTHETIC =
		32,
	
	/** Annotation. */
	SJME_CLASSFLAG_ANNOTATION =
		64,
	
	/** Enumeration. */
	SJME_CLASSFLAG_ENUM =
		128,
	
	/** End. */
	NUM_SJME_CLASSFLAG
} SJME_ClassFlag;

/**
 * This enumeration is used to determine which kind of structure is being
 * declared so that its information can be parsed correctly.
 *
 * @since 2016/07/18
 */
typedef enum SJME_StructureType
{
	/** Not defined. */
	SJME_STRUCTURETYPE_UNDEFINED,
	
	/** A class. */
	SJME_STRUCTURETYPE_CLASS,
	
	/** A resource. */
	SJME_STRUCTURETYPE_RESOURCE,
	
	/** A namespace. */
	SJME_STRUCTURETYPE_NAMESPACE,
	
	/** A string. */
	SJME_STRUCTURETYPE_STRING,
	
	/** Virtual machine. */
	SJME_STRUCTURETYPE_VM,
	
	/** Contents of a namespace. */
	SJME_STRUCTURETYPE_NAMESPACECONTENTS,
	
	/** Namespaces. */
	SJME_STRUCTURETYPE_NAMESPACES,
	
	/** Bootstrap namespaces. */
	SJME_STRUCTURETYPE_BOOTSTRAPNAMESPACES,
	
	/** Implemented interfaces. */
	SJME_STRUCTURETYPE_INTERFACES,
	
	/** Fields of a class. */
	SJME_STRUCTURETYPE_FIELDS,
	
	/** Methods of a class. */
	SJME_STRUCTURETYPE_METHODS,
	
	/** A single field. */
	SJME_STRUCTURETYPE_FIELD,
	
	/** A single method. */
	SJME_STRUCTURETYPE_METHOD,
	
	/** End. */
	NUM_SJME_STRUCTURETYPE
} SJME_StructureType;

/**
 * A UTF-16 String.
 *
 * @since 2016/07/18
 */
typedef struct SJME_String
{
	/** The type of structure this is. */
	const SJME_StructureType structuretype;
	
	/** The string length. */
	const jint length;
	
	/** The UTF-16 characters. */
	const jchar chars[];
} SJME_String;

/**
 * This represents the interfaces which are implemented by a class.
 *
 * @since 2016/07/22
 */
typedef struct SJME_Interfaces
{
	/** The type of structure this is. */
	const SJME_StructureType structuretype;
	
	/** The number of implemented interfaces. */
	const jint count;
	
	/** The implemented interfaces. */
	const SJME_String interfaces[];
} SJME_Interfaces;

/**
 * A field.
 *
 * @since 2016/07/22
 */
typedef struct SJME_Field
{
	/** The type of structure this is. */
	const SJME_StructureType structuretype;
	
	/** Field flags. */
	const jint flags;
	
	/** The name of the field. */
	const SJME_String* const name;
	
	/** The type of the field. */
	const SJME_String* const type;
	
	/** Is there a constant value? */
	const jboolean hasconstant;
	
	/** The constant value of the field, if applicable. */
	const jlong constantvalue;
} SJME_Field;

/**
 * A method.
 *
 * @since 2016/07/22
 */
typedef struct SJME_Method
{
	/** The type of structure this is. */
	const SJME_StructureType structuretype;
	
	/** Method flags. */
	const jint flags;
	
	/** The name of the method. */
	const SJME_String* const name;
	
	/** The type of the method. */
	const SJME_String* const type;
	
	/** The code for this method, the first argument is the VM pointer. */
	union
	{
		/** The call returns void. */
		const void (*returnsvoid)();
		
		/** The call returns integer. */
		const jint (*returnsjint)();
		
		/** The call returns long. */
		const jlong (*returnsjlong)();
	} code;
} SJME_Method;

/**
 * Fields of a class.
 *
 * @since 2016/07/22
 */
typedef struct SJME_Fields
{
	/** The type of structure this is. */
	const SJME_StructureType structuretype;
	
	/** The number of fields. */
	const jint count;
	
	/** The fields. */
	const SJME_Field* fields[];
} SJME_Fields;

/**
 * Methods of a class.
 *
 * @since 2016/07/22
 */
typedef struct SJME_Methods
{
	/** The type of structure this is. */
	const SJME_StructureType structuretype;
	
	/** The number of methods. */
	const jint count;
	
	/** The methods. */
	const SJME_Method* methods[];
} SJME_Methods;

/**
 * This represents a class which is available to SquirrelJME.
 *
 * @since 2016/07/18
 */
typedef struct SJME_Class
{
	/** The type of structure this is. */
	const SJME_StructureType structuretype;
	
	/** The name of the class. */
	const SJME_String* const name;
	
	/** Class flags. */
	const jint flags;
	
	/** The super class name. */
	const SJME_String* const superclass;
	
	/** Implemented interfaces. */
	const SJME_Interfaces* const interfaces;
	
	/** Fields. */
	const SJME_Fields* const fields;
	
	/** Methods. */
	const SJME_Methods* const methods;
	
	/** Should always be zero. */
	const jint zero;
} SJME_Class;

/**
 * This represents a resource which is available to a namespace.
 *
 * @since 2016/07/18
 */
typedef struct SJME_Resource
{
	/** The type of structure this is. */
	const SJME_StructureType structuretype;
	
	/** The name of the resource. */
	const SJME_String* const name;
	
	/** The resource data. */
	const jbyte* const data;
	
	/** The length of the resource. */
	const jint length;
} SJME_Resource;

/**
 * Contents a namespace may have.
 *
 * @since 2016/07/21
 */
typedef struct SJME_NamespaceContents
{
	/** The type of structure this is. */
	const SJME_StructureType structuretype;
	
	/** Contents in the namespace. */
	const void* const contents[];
} SJME_NamespaceContents;

/**
 * This represents a namespace which is available to the initialization and
 * linking system.
 *
 * @since 2016/07/18
 */
typedef struct SJME_Namespace
{
	/** The type of structure this is. */
	const SJME_StructureType structuretype;
	
	/** The name of the namespace. */
	const SJME_String* const name;
	
	/** Namespace contents. */
	const SJME_NamespaceContents* const contents;
	
} SJME_Namespace;

/**
 * Represents all available namespaces.
 *
 * @since 2016/07/21
 */
typedef struct SJME_Namespaces
{
	/** The type of structure this is. */
	const SJME_StructureType structuretype;
	
	/** Used namespaces. */
	const SJME_Namespace* const namespaces[];
} SJME_Namespaces;

/**
 * The current virtual machine instance and any control part of it.
 *
 * @since 2016/07/21
 */
typedef struct SJME_VM
{
	/** The type of structure this is. */
	SJME_StructureType structuretype;
	
	/** The number of namespaces in the VM. */
	jint namespacecount;
	
	/** The set of namespaces. */
	const SJME_Namespaces* namespaces;
} SJME_VM;

/**
 * Namespaces that make up the JVM classpath, this is {@code NULL} terminated.
 *
 * @since 2016/07/21
 */
extern const SJME_Namespaces initialNamespaces;

/**
 * Compares a C string to a VM string.
 *
 * @param __a The C string.
 * @param __b The VM string.
 * @return The comparison.
 * @since 2016/07/21
 */
jint SJME_compareCStringToSJMEString(const char* const __a,
	const SJME_String* const __b);

/**
 * Compares a VM string to a C string.
 *
 * @param __a The VM string.
 * @param __b The C string.
 * @return The comparison.
 * @since 2016/07/21
 */
jint SJME_compareSJMEStringToCString(const SJME_String* const __a,
	const char* const __b);

/**
 * Locates a class definition structure using the given binary name.
 *
 * @param __vm The virtual machine to locate in.
 * @param __s The name of the class to locate.
 * @return The pointer of the given class definition or {@code NULL} if not
 * found.
 * @since 2016/07/21
 */
const SJME_Class* SJME_locateClassDefC(SJME_VM* __vm, const char* const __s);

/**
 * Returns the next C string character from a multi-byte sequence, modified
 * UTF-8 form is used.
 *
 * @param __next The next character to obtain.
 * @return The character code or a negative value if the end of the string was
 * reached.
 * @since 2016/07/21
 */
jint SJME_nextCStringChar(const char** const __next);

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXNETMULTIPHASIGCSQUIRRELH
}
#undef SJME_cXNETMULTIPHASIGCSQUIRRELH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXNETMULTIPHASIGCSQUIRRELH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGNETMULTIPHASIGCSQUIRRELH */

