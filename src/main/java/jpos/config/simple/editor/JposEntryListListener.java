package jpos.config.simple.editor;

///////////////////////////////////////////////////////////////////////////////
//
// This software is provided "AS IS".  The JavaPOS working group (including
// each of the Corporate members, contributors and individuals)  MAKES NO
// REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED 
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
// NON-INFRINGEMENT. The JavaPOS working group shall not be liable for
// any damages suffered as a result of using, modifying or distributing this
// software or its derivatives. Permission to use, copy, modify, and distribute
// the software and its documentation for any purpose is hereby granted. 
//
// The JavaPOS Config/Loader (aka JCL) is now under the CPL license, which 
// is an OSS Apache-like license.  The complete license is located at:
//    http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
//
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

import java.util.*;

/**
 * Listener interface for the JposEntryList
 * @since 0.1 (Philly 99 meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
interface JposEntryListListener extends EventListener
{
    /**
     * Called when a new JposEntry is added to the JposEntryList
     * @param event the JposEntryListEvent object
     * @since 0.1 (Philly 99 meeting)
     */
    public void jposEntryAdded( JposEntryListEvent event );

    /**
     * Called when a JposEntry is removed to the JposEntryList
     * @param event the JposEntryListEvent object
     * @since 0.1 (Philly 99 meeting)
     */
    public void jposEntryRemoved( JposEntryListEvent event );
}
