# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: IEEE1275 Open Firmware Interface for SPARC

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# CMake really loves setting -fPIC on assembly code
add_library(SquirrelJMEIEEE1275SparcAsmCode OBJECT
	init.s)

# Specify our own runs for position independent code, since CMake does not
# set it correctly...
set_property(TARGET SquirrelJMEIEEE1275SparcAsmCode
	PROPERTY POSITION_INDEPENDENT_CODE OFF)
set_target_properties(SquirrelJMEIEEE1275SparcAsmCode
	PROPERTIES COMPILE_FLAGS "-K PIC")

# Disable debug for target?
target_compile_options(SquirrelJMEIEEE1275SparcAsmCode PRIVATE
	"")

# Use our function to export the object so we can use it without -fPIC
squirreljme_object_and_sources(SquirrelJMEIEEE1275SparcAsmCodeTarget
	SquirrelJMEIEEE1275SparcAsmCode)

# Include both core sets of libraries
get_property(SQUIRRELJME_IEEE1275_SPARC_CORE
	GLOBAL PROPERTY SquirrelJMECoreTargetObjects)
get_property(SQUIRRELJME_IEEE1275_SPARC_FRONTEND_IEEE1275
	GLOBAL PROPERTY SquirrelJMEIEEE1275LibTargetObjects)
get_property(SQUIRRELJME_IEEE1275_SPARC_ASM_CODE
	GLOBAL PROPERTY SquirrelJMEIEEE1275SparcAsmCodeTargetObjects)

# Add executable here
add_executable(SquirrelJMEIEEE1275Sparc
	boot.c
	${SQUIRRELJME_IEEE1275_SPARC_CORE}
	${SQUIRRELJME_IEEE1275_SPARC_FRONTEND_IEEE1275}
	${SQUIRRELJME_IEEE1275_SPARC_ASM_CODE})

# Use a different name and directory for it, for ISO creation
set_target_properties(SquirrelJMEIEEE1275Sparc PROPERTIES
	RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}/isoroot")
set_target_properties(SquirrelJMEIEEE1275Sparc PROPERTIES
	OUTPUT_NAME "sjme.elf")

# This is written only in C!
set_target_properties(SquirrelJMEIEEE1275Sparc PROPERTIES
	LINKER_LANGUAGE C)

# Position independent code always
set_property(TARGET SquirrelJMEIEEE1275Sparc
	PROPERTY POSITION_INDEPENDENT_CODE ${SQUIRRELJME_FPIC_MODE})

# Needs the assembly code
add_dependencies(SquirrelJMEIEEE1275Sparc
	SquirrelJMEIEEE1275SparcAsmCode)

# Includes for target
target_include_directories(SquirrelJMEIEEE1275Sparc PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# We need to use the linker script
set_target_properties(SquirrelJMEIEEE1275Sparc PROPERTIES
	LINK_DEPENDS "${CMAKE_CURRENT_SOURCE_DIR}/linker.ld")

# Options to use when linking
target_link_options(SquirrelJMEIEEE1275Sparc PUBLIC
	-s											# Strip
	-static										# Static library
	#-nostdlib									# No standard libraries
	-nostartfiles								# No startup files
	#-nodefaultlibs								# No default libraries
	-Wl,-N										# -N to linker
	-lgcc										# Include GCC Library (math)
	"-T${CMAKE_CURRENT_SOURCE_DIR}/linker.ld"	# Linker script for entry
	)

# IEEE1275 Boot Image, needed to convert from ELF to a.out image
add_custom_target(SquirrelJMEIEEE1275SparcBoot
	COMMAND "sparc-elf-objcopy"
		-O a.out-sunos-big	# Output SunOS a.out format
		-R .got				# Remove GOT, it is only 0xC in size and pointless
		-R .comment			# There is no relative to this
		-S					# Make very tiny
		"${PROJECT_BINARY_DIR}/isoroot/sjme.elf"
		"${PROJECT_BINARY_DIR}/isoroot/sjme.a"
	DEPENDS SquirrelJMEIEEE1275Sparc
	BYPRODUCTS "${PROJECT_BINARY_DIR}/isoroot/sjme.a"
	COMMENT "Converting IEEE1275 Boot Binary."
	VERBATIM COMMAND_EXPAND_LISTS)

# ISO Target?
find_program(SQUIRRELJME_IEEE1275_SPARC_GENISOIMAGE genisoimage)
if(SQUIRRELJME_IEEE1275_SPARC_GENISOIMAGE)
	# What is the ISO called?
	set(SQUIRRELJME_IEEE1275_SPARC_ISO
		"${PROJECT_BINARY_DIR}/squirreljme-ieee1275-sparc.iso")

	# Generate ISO image
	add_custom_target(SquirrelJMEIEEE1275SparcIso
		COMMAND "${SQUIRRELJME_IEEE1275_SPARC_GENISOIMAGE}"
			-B "${PROJECT_BINARY_DIR}/isoroot/sjme.a"
			-V "SquirrelJME"
			-o "${SQUIRRELJME_IEEE1275_SPARC_ISO}"
			"${PROJECT_BINARY_DIR}/isoroot"
		DEPENDS SquirrelJMEIEEE1275Sparc
			SquirrelJMEIEEE1275SparcBoot
		BYPRODUCTS "${SQUIRRELJME_IEEE1275_SPARC_ISO}"
		VERBATIM COMMAND_EXPAND_LISTS)

	# QEMU target? Requires the CD to be valid
	find_program(SQUIRRELJME_IEEE1275_SPARC_QEMU qemu-system-sparc)
	if(SQUIRRELJME_IEEE1275_SPARC_QEMU)
		# Running in QEMU
		add_custom_target(SquirrelJMEIEEE1275SparcQemu
			COMMAND "${SQUIRRELJME_IEEE1275_SPARC_QEMU}"
				#-s -S
				#-m 256
				-cdrom "${SQUIRRELJME_IEEE1275_SPARC_ISO}"
				-prom-env "load-base=4000"
				-prom-env "boot-from=cdrom:\\sjme.elf"
				-prom-env "boot-device=cdrom:\\sjme.elf"
				-boot d
				-M SS-10
			DEPENDS SquirrelJMEIEEE1275SparcIso
			COMMENT "Running via QEMU."
			VERBATIM COMMAND_EXPAND_LISTS)

		# Do not run this when "all" is done
		set_target_properties(SquirrelJMEIEEE1275SparcQemu PROPERTIES
			EXCLUDE_FROM_DEFAULT_BUILD TRUE)
		set_target_properties(SquirrelJMEIEEE1275SparcQemu PROPERTIES
			EXCLUDE_FROM_ALL TRUE)
	endif()
endif()

