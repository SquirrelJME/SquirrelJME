# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Increased verbosity

# Defines a verbose function
function(squirreljme_verbose_option define helpText)
	# Define the option, always default off
	# There is the preprocessor option, but also the standard option
	option(SJME_CONFIG_DEBUG_${define} "${helpText}"
		FALSE)
	option(SQUIRRELJME_DEBUG_${define} "${helpText}"
		FALSE)

	# If enabled, set define
	if(SJME_CONFIG_DEBUG_${define} OR SQUIRRELJME_DEBUG_${define})
		add_compile_definitions(SJME_CONFIG_DEBUG_${define}=1)
	endif()
endfunction()

# Grouped options
squirreljme_verbose_option(VERBOSE
	"Debug: General verbose options.")
squirreljme_verbose_option(STABLE
	"Debug: Verbosity for APIs considered to be stable.")

# Define all of the possible verbose functions
squirreljme_verbose_option(BYTECODES
	"Debug: Bytecode instructions.")
squirreljme_verbose_option(ENTRY
	"Debug: Entry and exit from methods.")
squirreljme_verbose_option(FIELD
	"Debug: Field get and set operations.")
squirreljme_verbose_option(GC
	"Debug: Garbage collection.")
squirreljme_verbose_option(MLE
	"Debug: Middle layer emulation calls.")
squirreljme_verbose_option(NO_REAL_GC
	"Debug: Do not perform any real garbage collection.")
squirreljme_verbose_option(TREAD
	"Debug: Stack and local treads.")
squirreljme_verbose_option(CIRCLEBUF
	"Debug: Circle buffers.")
squirreljme_verbose_option(CLOSEABLE
	"Debug: Closeable objects.")
squirreljme_verbose_option(ZIP
	"Debug: Zip reading and writing.")
