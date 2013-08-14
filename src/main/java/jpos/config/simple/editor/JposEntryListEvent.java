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

import java.util.*;

import jpos.config.JposEntry;

/**
 * The Event class for the JposEntryList class
 * @since 0.1 (Philly 99 meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
class JposEntryListEvent extends EventObject
{
    /**
     * 2-argument ctor
     * @param source this is event's source object
     * @param entry the JposEntry object generating this event
     */
    public JposEntryListEvent( Object source, JposEntry entry )
    {
        super( source );
        jposEntry = entry;
    }

    /**
     * @return the JposEntry of this event
     * @since 0.1 (Philly 99 meeting)
     */
    public JposEntry getJposEntry() { return jposEntry; }
    
    //-------------------------------------------------------------------------
    // Instance variables
    //

    private JposEntry jposEntry = null;
}
