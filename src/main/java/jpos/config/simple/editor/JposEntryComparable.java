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

import jpos.util.Comparable;
import jpos.config.JposEntry;

/**
 * JposEntry implementation of the Comparable interface
 * <b>NOTE:</b> uses the JposENtry's logicalName property as the mean of comparison
 * @author Manuel M Monserrate
 * @version 1.3.0 (JDK 1.1.x)
 */
public class JposEntryComparable extends Object implements Comparable
{
    //-------------------------------------------------------------------------
    // Ctor
    //

    /**
     * Default 1-argument ctor
     * @param obj the element's Object
     */
    public JposEntryComparable( JposEntry entry ) { this.entry = entry; }

    //-------------------------------------------------------------------------
    // Public instance methods
    //

    /**
     * Compares two JposEntries 
     * @param other JposEntry to compare to
     */
    public int compareTo( Object other )
    { 
        String thisLogicalName = entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME ).toString();  
        
        String otherLogicalName = ( (JposEntryComparable)other ).getJposEntry().getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME ).toString();

        return thisLogicalName.compareTo( otherLogicalName );

    }
    /**
     * Indicates this JposEntry is "equal to" the other 
     * @param other JposEntry to compare to
     */
    public boolean equals( Object other ) 
    { 
        String thisLogicalName = entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME ).toString();  
        
        String otherLogicalName = ( (JposEntryComparable)other ).getJposEntry().getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME ).toString();

        return thisLogicalName.equals( otherLogicalName );

    }

    /** @return the JposEntry of this Comparable element */
    public JposEntry getJposEntry() { return entry; }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private JposEntry entry = null;
}

