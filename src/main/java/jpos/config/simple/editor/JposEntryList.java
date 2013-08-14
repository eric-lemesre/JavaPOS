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

import jpos.loader.*;
import jpos.config.*;
import jpos.util.Sorter;
import jpos.util.tracing.Tracer;
import jpos.util.tracing.TracerFactory;

/**
 * Simple bean list class for JposEntry objects.  Allows listeners to 
 * know when JposEntry objects are added and removed to the list.
 * @since 0.1 (Philly 99 meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
class JposEntryList extends Object
{
    //--------------------------------------------------------------------------
    // Ctor(s)
    // 
	
    /**
     * Default no-arg ctor
     * @since 0.1 (Philly 99 meeting)
     */
    public JposEntryList()  { loadedFromFile = false; }

    /**
     * 1-arg ctor
     * @param fileName the file name to load the JposEntry from
     * @exception java.lang.Exception if something goes wrong loading the 
     * entries
     * @since 0.1 (Philly 99 meeting)
     */
    public JposEntryList( String fileName ) throws Exception
    { 
        load( fileName );

        loadedFromFile = true;
    }

    //--------------------------------------------------------------------------
    // Public methods
    // 

    /**
     * Adds new listener for this JposEntryList
     * <p>
     * <b>NOTE:</b> the listener name should change to JposEntryListListener 
     * insteand of JposEntryTreeListener
     * </p>
     * @param l the listener instance
     * @since 0.1 (Philly 99 meeting)
     */
    public void addJposEntryListListener( JposEntryListListener l ) 
    { listeners.addElement( l ); }

    /**
     * Removes new listener for this JposEntryList
     * <p>
     * <b>NOTE:</b> the listener name should change to JposEntryListListener 
     * instead of JposEntryTreeListener
     * </p>
     * @param l the listener instance
     * @since 0.1 (Philly 99 meeting)
     */
    public void removeJposEntryListListener( JposEntryListListener l ) 
    { listeners.removeElement( l ); }

    /**
     * @return an Enumeration of JposEntry objects
     * @since 0.1 (Philly 99 meeting)
     */
    public Enumeration getEntries() { return entries.elements(); }

    /**
     * @return an Enumeration of JposEntry objects that were removed from 
     * the list
     * @since 0.1 (Philly 99 meeting)
     */
    public Enumeration getRemovedEntries() 
    { return removedEntriesVector.elements(); }

    /**
     * @return the current size of the list
     * @since 0.1 (Philly 99 meeting)
     */
    public int size() { return entries.size(); }

    /**
     * @return the serialization file name
     * @since 0.1 (Philly 99 meeting)
     */
    public String getEntriesFileName() { return entriesFileName; }
   
    /**
     * Sets the serialization file name
     * @since 0.1 (Philly 99 meeting)
     */
    public void setEntriesFileName( String fileName ) 
    { 
        entriesFileName = fileName;

        loadedFromFile = true;
    }

    /**
     * Loads the JposEntry from the serialized file specified
     * @param fileName the serialized file name
     * @exception java.lang.Exception if something goes wrong loading 
     * this ser file
     * @since 0.1 (Philly 99 meeting)
     */
    public void load( String fileName ) throws Exception
    {
		entries.clear();

        getRegPopulator().load( fileName );

		tracer.println( "Tried to load fileName: " + fileName );
		tracer.println( "Using RegPopulator: " + getRegPopulator() );

		if( getRegPopulator().getLastLoadException() != null )
		{
			tracer.println( "Exception while loading fileName: " + fileName + 
							" Exception.message=" + getRegPopulator().
							getLastLoadException() );
			
			entries = new Vector();
			throw getRegPopulator().getLastLoadException();
		}

        Enumeration jposEntries = getRegPopulator().getEntries();
		int i = 0;
	
        while( jposEntries.hasMoreElements() )
        {
            JposEntry jposEntry = (JposEntry)jposEntries.nextElement();
			++i;
            entries.addElement( jposEntry );
        }

        entriesFileName = fileName;
        loadedFromFile = true;
        
		tracer.println( "Loaded fileName: " + fileName + 
						" found " + i + " JposEntries" );
    }

    /**
     * Serializes the JposEntry objects in the entriesFileName
     * @exception java.lang.Exception if something goes wrong saving 
     * this ser file
     * @since 0.1 (Philly 99 meeting)
     */
    public void save() throws Exception
    {
        Enumeration entriesEnum = entries.elements();

        getRegPopulator().save( entriesEnum, getEntriesFileName() );
    }
    
    /**
     * Adds a new entry with logicalName in the list
     * @param logicalName the logicalName for this entry
     * @param entry the JposEntry to add
     * @since 0.1 (Philly 99 meeting)
     */
    public void add( String logicalName, JposEntry entry )
    {
        entries.addElement( entry );
            
        fireJposEntryListEventAdded( new JposEntryListEvent( this, entry ) );
    }

    /**
     * Removes the entry with logicalName from the list
     * @param entry the JposEntry to add
     * @since 0.1 (Philly 99 meeting)
     */
    public void remove( String logicalName )
    {
        Enumeration vectorEntries = entries.elements();

        while( vectorEntries.hasMoreElements() )
        {
            JposEntry entry = (JposEntry)vectorEntries.nextElement();

            if( entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME ).
            	toString().equals( logicalName ) )
            {
                removedEntriesVector.addElement( entry );
                
                if( entries.removeElement( entry ) )
                {
                    fireJposEntryListEventRemoved( new JposEntryListEvent( 
                    							   this, entry ) );
                    return;
                }
            }

        }
    }

    /**
     * Change/replace the entry with logicalName with this one
     * @param logicalName the logicalName for this entry
     * @param entry the JposEntry to replace
     * @since 0.1 (Philly 99 meeting)
     */
    public void change( String logicalName, JposEntry entry )
    {
        entries.removeElement( entry );
        entries.addElement( entry );
    }
    
    /**
     * @return true if the entries are loaded from a file or not
     * @since 0.1 (Philly 99 meeting)
     */
    public boolean isLoadedFromFile() { return loadedFromFile; }

    /**
     * @param logicalName of the JposEntry to be searched
     * @return true if the entry is in the list
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-9-2000)
     */
    public boolean hasJposEntry( String logicalName ) 
    {
        Enumeration vectorEntries = entries.elements();

        while( vectorEntries.hasMoreElements() )
        {
            JposEntry entry = (JposEntry)vectorEntries.nextElement();

            if( entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME ).
            							toString().equals( logicalName ) )
                return true;
        }

        return false;
    }

    /**
     * @return true if the list is empty
     * @since 1.3 (SF 2K meeting)
     * @author MMM (5-10-2000)
     */
     public boolean isEmpty() { return entries.isEmpty(); }

    /**
     * sorts the list of entries
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-1-2000)
     */
     public void sort()
     {
        Enumeration vectorEntries = entries.elements();

        Vector comparables = new Vector();

        while( vectorEntries.hasMoreElements() )
        {
            JposEntry entry = (JposEntry)vectorEntries.nextElement();

            JposEntryComparable jposEntryComparable = 
            					new JposEntryComparable( entry );

            comparables.addElement( jposEntryComparable );
        }

        entries.removeAllElements();

        Vector sortedEntriesVector = Sorter.insertionSort( comparables );

        Enumeration sortedEntries = sortedEntriesVector.elements();

        while( sortedEntries.hasMoreElements() )
        {
            JposEntryComparable jposEntryComparable = 
            					(JposEntryComparable)sortedEntries.nextElement();

            entries.addElement( jposEntryComparable.getJposEntry() );
        }
    }
    
    /** 
     * @return the JposRegPopulator used by this list 
     * @since 2.1.0
     */
    public JposRegPopulator getRegPopulator() { return regPopulator; }

	/**
	 * Sets the JposRegPopulator used by this list
	 * @param populator the JposRegpopulator
	 */
	public void setRegPopulator( JposRegPopulator populator )
	{
		tracer.println( "Setting RegPopulator to: " + populator );
		regPopulator = populator;
	}

    //--------------------------------------------------------------------------
    // Private methods
    //

    /**
     * Fire the JposEntryListEvent to each listener calling the 
     * jposEntryAdded method
     * @param e the event to fire
     * @since 0.1 (Philly 99 meeting)
     */
    private void fireJposEntryListEventAdded( JposEntryListEvent e )
    {
        Vector listenersClone = (Vector)listeners.clone();

        synchronized( listenersClone )
        {
            for( int i = 0; i < listenersClone.size(); ++i )
                ((JposEntryListListener)listenersClone.elementAt( i ) ).
                jposEntryAdded( e );
        }
    }

    /**
     * Fire the JposEntryListEvent to each listener calling the 
     * jposEntryRemoved method
     * @param e the event to fire
     * @since 0.1 (Philly 99 meeting)
     */
    private void fireJposEntryListEventRemoved( JposEntryListEvent e )
    {
        Vector listenersClone = (Vector)listeners.clone();

        synchronized( listenersClone )
        {
            for( int i = 0; i < listenersClone.size(); ++i )
                ((JposEntryListListener)listenersClone.elementAt( i ) ).
                jposEntryRemoved( e );
        }
    }

    //--------------------------------------------------------------------------
    // Instance variables
    //

    private boolean loadedFromFile = false;

    private String entriesFileName = "";
    private Vector entries = new Vector();
    private Vector removedEntriesVector = new Vector();
    private Vector listeners = new Vector();
    
    private JposRegPopulator regPopulator = JposServiceLoader.getManager().
    										 getRegPopulator();
    										 
	private Tracer tracer = TracerFactory.getInstance().
							 createTracer( "JposEntryList" );    										 
}