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

/**
 * Small class to aggregate a JposEntry property
 * @since 0.1 (Philly 99 meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @author Manuel M Monserat
 * @author Kriselie D Rivera
 */
class JposEntryProp extends Object
{
    /** 
     * Default no-arg ctor 
     * @since 0.1 (Philly 99 meeting)
     */
    public JposEntryProp() {}

    /**
     * Two argument ctor
     * @param name the property's name
     * @param value the property's value
     * @since 0.1 (Philly 99 meeting)
     */
    public JposEntryProp( String name, Object value )
    {
        this.name = name;
        this.value = value;

        determineValueType( value );
    }

    //-----------------------------------------------------------------------
    // Private methods
    //

    /**
     * Determine the type of the value passed
     * @param value the value to find to type of
     * @since 0.1 (Philly 99 meeting)
     */ 
    private void determineValueType( Object value )
    {
        if( value instanceof String )
            valueType = "String";
        else
        if( value instanceof Integer )
            valueType = "Integer";
        else
        if( value instanceof Long )
            valueType = "Long";
        else
        if( value instanceof Character )
            valueType = "Character";
        else
        if( value instanceof Byte )
            valueType = "Byte";
        else
        if( value instanceof Boolean )
            valueType = "Boolean";
        else
        if( value instanceof Float )
            valueType = "Float";
        else
            valueType = "Object";
    }

    //-----------------------------------------------------------------------
    // Public methods
    //

    /**
     * @return the name of this property
     * @since 0.1 (Philly 99 meeting)
     */
    public String getName() { return name; }

    /**
     * @return the value of this property
     * @since 0.1 (Philly 99 meeting)
     */
    public Object getValue() { return value; }

    /**
     * Sets the name of this property
     * @param s the String name
     * @since 0.1 (Philly 99 meeting)
     */
    public void setName( String s )
        throws IllegalArgumentException
    { 
        if( s == null || s.equals( "" ) ) 
            throw new IllegalArgumentException(SETNAMEEXCEPTION_STRING);
        else
            name = s; 
    }

    /**
     * Sets the value of this property
     * @param obj the Object vlaue of this property
     * @since 0.1 (Philly 99 meeting)
     */
    public void setValue( Object obj ) 
    {
        value = obj;
        determineValueType( value );
    }

    /**
     * @return a String for the type of the value
     * @since 0.1 (Philly 99 meeting)
     */
    public String getValueType() { return valueType; }

    /**
     * @return a String representation of this property
     * @since 0.1 (Philly 99 meeting)
     */
    public String toString() { return name + ":" + valueType; }

    //-----------------------------------------------------------------------
    // Instance variables
    //

    private String name = "";
    private Object value = "";

    private String valueType = "";


    //-------------------------------------------------------------------------
    // I18N
    //

    public static final String SETNAMEEXCEPTION_STRING = JposEntryEditorMsg.SETNAMEEXCEPTION_STRING;

}
