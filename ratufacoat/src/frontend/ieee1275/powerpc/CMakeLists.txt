# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: IEEE1275 Open Firmware Interface

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# CMake really loves setting -fPIC on assembly code
add_library(SquirrelJMEIEEE1275PowerPCAsmCode OBJECT
	init.s)

# Specify our own runs for position independent code, since CMake does not
# set it correctly...
set_property(TARGET SquirrelJMEIEEE1275PowerPCAsmCode
	PROPERTY POSITION_INDEPENDENT_CODE OFF)
set_target_properties(SquirrelJMEIEEE1275PowerPCAsmCode
	PROPERTIES COMPILE_FLAGS "-K PIC")

# Use our function to export the object so we can use it without -fPIC
squirreljme_object_and_sources(SquirrelJMEIEEE1275PowerPCAsmCodeTarget
	SquirrelJMEIEEE1275PowerPCAsmCode)

# Include both core sets of libraries
get_property(SQUIRRELJME_IEEE1275_POWERPC_CORE
	GLOBAL PROPERTY SquirrelJMECoreTargetObjects)
get_property(SQUIRRELJME_IEEE1275_POWERPC_FRONTEND_IEEE1275
	GLOBAL PROPERTY SquirrelJMEIEEE1275LibTargetObjects)
get_property(SQUIRRELJME_IEEE1275_POWERPC_ASM_CODE
	GLOBAL PROPERTY SquirrelJMEIEEE1275PowerPCAsmCodeTargetObjects)

# Add executable here
add_executable(SquirrelJMEIEEE1275PowerPC
	boot.c
	${SQUIRRELJME_IEEE1275_POWERPC_CORE}
	${SQUIRRELJME_IEEE1275_POWERPC_FRONTEND_IEEE1275}
	${SQUIRRELJME_IEEE1275_POWERPC_ASM_CODE})

# Use a different name and directory for it, for ISO creation
set_target_properties(SquirrelJMEIEEE1275PowerPC PROPERTIES
	RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}/isoroot/ppc")
set_target_properties(SquirrelJMEIEEE1275PowerPC PROPERTIES
	OUTPUT_NAME "squirreljme.elf")

# Common C Directives
squirreljme_common_c(SquirrelJMEIEEE1275PowerPC)

# Position independent code always
squirreljme_enable_pic(SquirrelJMEIEEE1275PowerPC)

# Needs the assembly code
add_dependencies(SquirrelJMEIEEE1275PowerPC
	SquirrelJMEIEEE1275PowerPCAsmCode)

# We need to use the linker script
set_target_properties(SquirrelJMEIEEE1275PowerPC PROPERTIES
	LINK_DEPENDS "${CMAKE_CURRENT_SOURCE_DIR}/linker.ld")

# Options to use when linking
target_link_options(SquirrelJMEIEEE1275PowerPC PUBLIC
	-s											# Strip
	-static										# Static library
	-nostdlib									# No standard libraries
	-nostartfiles								# No startup files
	-nodefaultlibs								# No default libraries
	"-T${CMAKE_CURRENT_SOURCE_DIR}/linker.ld"	# Linker script for entry
	)

# ISO Target?
find_program(SQUIRRELJME_IEEE1275_POWERPC_GENISOIMAGE genisoimage)
if(SQUIRRELJME_IEEE1275_POWERPC_GENISOIMAGE)
	# What is the ISO called?
	set(SQUIRRELJME_IEEE1275_POWERPC_ISO
		"${PROJECT_BINARY_DIR}/squirreljme-ieee1275-powerpc.iso")

	# Generate ISO image
	add_custom_target(SquirrelJMEIEEE1275PowerPCIso
		COMMAND "${SQUIRRELJME_IEEE1275_POWERPC_GENISOIMAGE}"
			-hfs
			-mac-name
			-part
			-hfs-volid "SquirrelJME"
			-chrp-boot
			-hfs-bless "${CMAKE_CURRENT_SOURCE_DIR}/isoroot/ppc"
			-map "${CMAKE_CURRENT_SOURCE_DIR}/hfsmap"
			-J
			-r
			-V "SquirrelJME"
			-publisher "${CMAKE_CURRENT_SOURCE_DIR}/publish"
			-hfs-unlock
			#-copyright "${CMAKE_CURRENT_SOURCE_DIR}/copyrite"
			-pad
			-no-desktop
			-probe
			-v
			-v
			-o "${SQUIRRELJME_IEEE1275_POWERPC_ISO}"
			"${CMAKE_CURRENT_SOURCE_DIR}/isoroot"
			"${PROJECT_BINARY_DIR}/isoroot"
		DEPENDS SquirrelJMEIEEE1275PowerPC
		BYPRODUCTS "${SQUIRRELJME_IEEE1275_POWERPC_ISO}"
		SOURCES "${CMAKE_CURRENT_SOURCE_DIR}/isoroot/ppc/bootinfo.txt"
			"${CMAKE_CURRENT_SOURCE_DIR}/isoroot/ppc/ofboot.b"
		VERBATIM COMMAND_EXPAND_LISTS)

	# QEMU target? Requires the CD to be valid
	find_program(SQUIRRELJME_IEEE1275_POWERPC_QEMU qemu-system-ppc)
	if(SQUIRRELJME_IEEE1275_POWERPC_QEMU)
		# Running in QEMU
		add_custom_target(SquirrelJMEIEEE1275PowerPCQemu
			COMMAND "${SQUIRRELJME_IEEE1275_POWERPC_QEMU}"
				#-s -S
				-m 256
				-M mac99,via=pmu
				-cdrom "${SQUIRRELJME_IEEE1275_POWERPC_ISO}"
				-prom-env 'auto-boot?=true'
				-prom-env 'load-base=20000'
				#-prom-env 'boot-device=/pci@f2000000/mac-io@c/ata-3@21000/cdrom@0:,\\:tbxi'
				-boot d
			DEPENDS SquirrelJMEIEEE1275PowerPCIso
			COMMENT "Running via QEMU."
			VERBATIM COMMAND_EXPAND_LISTS)

		# Do not run this when "all" is done
		set_target_properties(SquirrelJMEIEEE1275PowerPCQemu PROPERTIES
			EXCLUDE_FROM_DEFAULT_BUILD TRUE)
		set_target_properties(SquirrelJMEIEEE1275PowerPCQemu PROPERTIES
			EXCLUDE_FROM_ALL TRUE)
	endif()
endif()

