/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/debug.h"
#include "sjme/joptarg.h"
#include "dispatch.h"

/**
 * Options for JavaDoc generation.
 *
 * @sice 2026/08/08
 */
typedef struct sjme_javadoc_options
{
	sjme_jint todo;
} sjme_javadoc_options;

/**
 * Parses JavaDoc command line arguments.
 * 
 * @note -1.1
 * @note -Jflag
 * @note -author
 * @note -bootclasspath classpathlist
 * @note -bottom text
 * @note -breakiterator
 * @note -charset name
 * @note -classpath classpathlist
 * @note -d directory
 * @note -docencoding name
 * @note -docfilesubdirs
 * @note -doclet class
 * @note -docletpath classpathlist
 * @note -doctitle title
 * @note -encoding
 * @note -exclude packagename1:packagename2:...
 * @note -excludedocfilessubdir name1:name2
 * @note -extdirs dirist
 * @note -footer footer
 * @note -group groupheading packagepattern:packagepattern
 * @note -header header
 * @note -help
 * @note -helpfile path/filename
 * @note -javafx
 * @note -keywords
 * @note -link extdocURL
 * @note -linkoffline extdocURL packagelistLoc
 * @note -linksource
 * @note -locale language_country_variant
 * @note -nocomment
 * @note -nodeprecated
 * @note -nodeprecatedlist
 * @note -nohelp
 * @note -noindex
 * @note -nonavbar
 * @note -noqualifier all | packagename1:packagename2...
 * @note -nosince
 * @note -notimestamp
 * @note -notree
 * @note -overview path/filename
 * @note -package
 * @note -private
 * @note -protected
 * @note -public
 * @note -quiet
 * @note -serialwarn
 * @note -source release
 * @note -sourcepath sourcepathlist
 * @note -sourcetab tablength
 * @note -splitindex
 * @note -stylesheet path/filename
 * @note -subpackages package1:package2:...
 * @note -tag tagname:Xaoptcmf:"taghead"
 * @note -taglet class
 * @note -tagletpath tagletpathlist
 * @note -title title
 * @note -top
 * @note -use
 * @note -verbose
 * @note -version
 * @note -windowtitle title
 * @param handler The handler.
 * @param state The current state.
 * @return Any resultant error, if any.
 * @since 2026/08/08
 */
static sjme_errorCode sjme_javadoc_parseArgs(
	sjme_attrInNotNull const sjme_joptarg_handler* handler,
	sjme_attrInNotNull sjme_joptarg_state* state)
{
	sjme_javadoc_options* options;
	
	if (handler == NULL || state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover existing options. */ 
	options = state->data;
	if (options == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_abcd_command_main_declare(javadoc)
{
	sjme_errorCode error;
	sjme_javadoc_options options;
	
	/* Parse command line options. */
	memset(&options, 0, sizeof(options));
	if (sjme_error_is(error = sjme_joptarg_parse(SJME_OPTARG_JAVA,
		nal, sjme_javadoc_parseArgs, &options, 0, argc, argv)))
		goto fail_parseArgs;
	
	sjme_todo("Impl?");
	return EXIT_FAILURE;
	
fail_parseArgs:
	sjme_emitB("Error: %d", (int)error);
	return EXIT_FAILURE;
}

sjme_abcd_command_help_declare(javadoc)
{
	sjme_todo("Impl?");
}
