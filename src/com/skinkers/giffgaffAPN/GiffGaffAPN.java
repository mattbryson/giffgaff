package com.skinkers.giffgaffAPN;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;


public class GiffGaffAPN 
{
	private static final Uri APN_TABLE_URI = Uri.parse("content://telephony/carriers");
	private static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");  
	  
       
    
	private static final String APN_NAME = "giffgaff";
	private static final String APN = "giffgaff.com";
	private static final String APN_USER = "giffgaff";
	private static final String APN_PASSWORD = "password";
	
	
	
	private static final String APN_MCC = "234";
	private static final String APN_MNC = "10";

	//Testing for emulator
	//private static final String APN_MCC = "310";
	//private static final String APN_MNC = "260";
	
	
	private static final String APN_MMSC = "http://mmsc.mediamessaging.co.uk:8002";
	private static final String APN_MMSPROXY = "193.113.200.195";
	private static final String APN_MMSPORT = "8080";
     
    private static final String TAG = "GiffGaffAPN";
	
	/**
     * Creates the GiffGaff APN entry, and sets it as default.
     * Then notifies user of progress
     */
    public static boolean addGiffGaff(Context context)
    {
    	 int id = insertGiffGaffAPN(context);
    	
		if (id != -1)
		{
			setDefaultAPN(id, context);
			return true;
		}
		else
		{
			return false;
		}
    }
    
    
    public static boolean removeGiffGaff(Context context)
    {
    	
    	ContentResolver resolver = context.getContentResolver();
    	
    	int count = resolver.delete( APN_TABLE_URI , "apn == '"+APN+"' and current IS NOT NULL", null );
    	
    	if (count>0)	
    		return true;
    	else
    		return false;
     }
    
    /**
     * Deletes all Non GiffGaff APNS
     * And notifies user of progress
     */
    public static int removeOtherAPNs(Context context)
    {
    	 return deleteOtherAPNS(context);
    }
    
    
    
    
    
    /*
     * Insert a new APN entry into the system APN table
     * Require an apn name, and the apn address. More can be added.
     * Return an id (_id) that is automatically generated for the new apn entry.
     */
    private static int insertGiffGaffAPN(Context context) 
    {
        int id = -1;
        
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
       
        
        //Actual giff gaff details..
        values.put("mcc",APN_MCC);
        values.put("mnc",APN_MNC);
        values.put("numeric", APN_MCC + APN_MNC);
        values.put("name",APN_NAME);
        values.put("apn",APN);
        values.put("user",APN_USER);
        values.put("password",APN_PASSWORD);
        
        values.put("mmsc",APN_MMSC);
        values.put("mmsproxy",APN_MMSPROXY);
        values.put("mmsport",APN_MMSPORT);
       

        
        
        
        
        Cursor c = null;
        try
        {
            Uri newRow = resolver.insert(APN_TABLE_URI, values);
            if(newRow != null)
            {
                c = resolver.query(newRow, null, null, null, null);
                
                // Obtain the apn id
                int idindex = c.getColumnIndex("_id");
                c.moveToFirst();
                id = c.getShort(idindex);
                
            }
        }
        catch(SQLException e)
        {
            Log.d(TAG, e.getMessage());
        }

        if(c !=null ) 
            c.close();
        
        return id;
    }
    
    
    public static int getGiffGaffAPNID(Context context)
    {
    	ContentResolver resolver = context.getContentResolver();
    	int id = -1;
    	
    	try
        {
    		Cursor c = resolver.query(APN_TABLE_URI, new String[] {"_id"}, "apn == '"+APN+"' and current IS NOT NULL", null, null);
    		
            if(c != null && c.getCount() > 0)
            {
            	 int idindex = c.getColumnIndex("_id");
                 c.moveToFirst();
                 id = c.getShort(idindex);
                 
                c.close();
            }
        }
        catch (SQLException e)
        {
            Log.d(TAG, e.getMessage());
        }
    	
    	return id;
    	
    }
    
    
    public static boolean isGiffGaffDefault(Context context)
    {
    	String apn = getDefaultAPN(context);
    	if (apn.equals(APN))
    		return true;
    	else
    		return false;
    }
    
    private static String getDefaultAPN(Context context)
    {
    	ContentResolver resolver = context.getContentResolver();
    	String apn = "";
    	
    	try
    	{
    		Cursor c = resolver.query(PREFERRED_APN_URI, new String[] {"apn"}, null, null, null);
    		
    		if(c != null && c.getCount() > 0)
    		{
    			int apnIndex = c.getColumnIndex("apn");
    			c.moveToFirst();
    			apn = c.getString(apnIndex);
    			
    			c.close();
    		}
    	}
    	catch (SQLException e)
    	{
    		Log.d(TAG, e.getMessage());
    	}
    	
    	return apn;
    	
    }
    
    /*
     * Set an apn to be the default apn for web traffic
     * Require an input of the apn id to be set
     */
    public static boolean setDefaultAPN(int id, Context context)
    {
        boolean res = false;
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        
        //See /etc/apns-conf.xml. The TelephonyProvider uses this file to provide 
        //content://telephony/carriers/preferapn URI mapping
        values.put("apn_id", id); 
        try
        {
            resolver.update(PREFERRED_APN_URI, values, null, null);
            Cursor c = resolver.query(
                    PREFERRED_APN_URI, 
                    new String[]{"name","apn"}, 
                    "_id="+id, 
                    null, 
                    null);
            
            if(c != null)
            {
                res = true;
                c.close();
            }
        }
        catch (SQLException e)
        {
            Log.d(TAG, e.getMessage());
        }
        
        return res;
    }
    
    public static String getOtherAPNNames(Context context)
    {
    	ContentResolver resolver = context.getContentResolver();
    	String names = "";
    	
    	try
        {
    		Cursor c = resolver.query(APN_TABLE_URI, new String[] {"name"}, "apn != '"+APN+"' and current IS NOT NULL", null, null);
    		
            if(c != null && c.getCount() > 0)
            {
            	 c.moveToFirst(); 
            	
            	do
            	{
            		int index = c.getColumnIndex("name");
                    names += c.getString(index);
                    
                    if (!c.isLast())
                    	names+=",";	
                 }
            	 while( c.moveToNext() );
            	 
            	c.close();
            }
        }
        catch (SQLException e)
        {
            Log.d(TAG, e.getMessage());
        }
    	
    	return names;
    }
    
    /**
     * Deletes all ANP entries other than the giffgaff entry
     * @return
     */
    private static int deleteOtherAPNS(Context context)
    {
    	ContentResolver resolver = context.getContentResolver();
    	
    	int count = resolver.delete( APN_TABLE_URI , "apn <> '"+APN+"' and current IS NOT NULL", null );
    	
    	return count;
    }
	
}
