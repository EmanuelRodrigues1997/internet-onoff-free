package InternetOnOff.vv.android.development.com;



	



	import java.util.ArrayList;
	import java.util.Collections;
	import java.util.List;

	import android.app.Activity;
	import android.os.Bundle;
	import android.widget.Toast;
	import android.net.Uri;
	import android.content.ContentResolver;
	import android.content.ContentValues;
	import android.database.Cursor;
	import android.graphics.Color;
	import android.content.Context;
	import android.util.Log;
	import android.view.View.OnClickListener;
	import android.widget.TextView;
	import android.widget.ToggleButton;
	import android.widget.CheckBox;
	import android.widget.ImageButton;
	import android.view.View;
	import android.net.ConnectivityManager;
	import android.net.NetworkInfo;
	import java.util.StringTokenizer;
	import android.os.Handler;
	import android.app.ProgressDialog;
	import android.os.Message;
	import android.media.MediaPlayer;



	public class InternetOnOff extends Activity {
	 
	    
	    
	    Uri contentUri = Uri.parse("content://telephony/carriers/");
	    Cursor cursor = null;
	    String TAG = "vasek";
	    String check = ""; 
	    boolean mms = true;
	    private static final String ID = "_id";
	    private static final String APN = "apn";
	    private static final String NAME = "name";
	    private static final String TYPE = "type";
	    private static final String PREFIX = "disabled";
	    private static final String SUFFIX = "disabled";
	    private int mMmsTarget = STATE_ON;
	    public static final int STATE_OFF = 0;
	    public static final int STATE_ON = 1;
	    private boolean mDisableAll = false;
	    private static final String DB_LIKE_SUFFIX = "%" + SUFFIX;
	    private static final String DB_LIKE_TYPE_SUFFIX = "%" +SUFFIX + "%";
	    private static final String DB_LIKE_PREFIX = PREFIX + "%";
	    private static final String DB_LIKE_TYPE_PREFIX = "%" +PREFIX + "%";
	    private Handler myHandler = new Handler();
	    ProgressDialog dialog;
	    




	   

	    
	    
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	 
	 //        final ToggleButton toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
	         final CheckBox checkbox1 = (CheckBox) findViewById(R.id.checkBox1);
	         final ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
	         TextView StavInternetu = (TextView) findViewById(R.id.StavInternetu);
	         
	         checkbox1.setChecked(true);
	         
	 //        toggleButton1.setChecked(isOnline()); 
	         
	         // MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
	         
	         if (isOnline()) {
	        	 imageButton1.setImageResource(R.drawable.buttongreen200);
	        	 StavInternetu.setText(R.string.InternetOn);
	        	 StavInternetu.setTextColor(Color.GREEN);
	         } 
	         else {
	        	 imageButton1.setImageResource(R.drawable.buttonred200);
	        	 StavInternetu.setText(R.string.InternetOff);
	        	 StavInternetu.setTextColor(Color.RED);
	         };
	         

	        if ( checkbox1.isChecked() == true ) {
	        	mMmsTarget = STATE_ON;
	        }
	        else {
	        	mMmsTarget = STATE_OFF;
	        };
	        
	        

	        
	        
	        imageButton1.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	
	            	TextView StavInternetu = (TextView) findViewById(R.id.StavInternetu);
	            	MediaPlayer mp = MediaPlayer.create(InternetOnOff.this, R.raw.click);
	            	
	            	if ( checkbox1.isChecked() == true ) {
	                	mMmsTarget = STATE_ON;
	                }
	                else {
	                	mMmsTarget = STATE_OFF;
	                };

	            	if (isOnline()) {	
	                    mp.start();
	                    disableAllInDb();
	                    imageButton1.setImageResource(R.drawable.buttonred200);
	                   StavInternetu.setText(R.string.InternetOff);
	              	    StavInternetu.setTextColor(Color.RED);
	                 //   myHandler.postDelayed(mMyRunnableVyp, 5000);
	   
	                } else {
	                	mp.start();
	                	enableAllInDb();
	                	imageButton1.setImageResource(R.drawable.buttongreen200);
	                	StavInternetu.setText(R.string.InternetOn);
	               	    StavInternetu.setTextColor(Color.GREEN);
	                //	myHandler.postDelayed(mMyRunnableZap, 8000);
	           
	                }
	            }
	        });
	        
	       
	        
	        
	        
	         
	        }
	        
	    
	    public void onResume() {
	    	super.onResume();

	    	
	  //  	final ToggleButton toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
	    	final CheckBox checkbox1 = (CheckBox) findViewById(R.id.checkBox1);
	   // 	toggleButton1.setChecked(isOnline());
	    	final ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
	              TextView StavInternetu = (TextView) findViewById(R.id.StavInternetu);
	    	if (isOnline()) {
	       	 imageButton1.setImageResource(R.drawable.buttongreen200);
	         StavInternetu.setText(R.string.InternetOn);
	    	    StavInternetu.setTextColor(Color.GREEN);
	        } 
	        else {
	       	 imageButton1.setImageResource(R.drawable.buttonred200);
	       	 StavInternetu.setText(R.string.InternetOff);
	   	     StavInternetu.setTextColor(Color.RED);
	        };
	    	if ( checkbox1.isChecked() == true ) {
	        	mMmsTarget = STATE_ON;
	        }
	        else {
	        	mMmsTarget = STATE_OFF;
	        };
	    	
	    	
	    }

	        
	         
	   
	    
	    
	    public void printApn(){
	    	
	    	cursor = managedQuery(contentUri, new String[]{ "_id", "apn", "name"}, null, null, null);
	        
	        //    if (cursor != null)
	            while (cursor.moveToNext())
	            {
	            
	            Log.v(TAG, "neni null") ;
	          //  cursor.moveToFirst();
	            String id = cursor.getString(0);
	            Log.v(TAG, "id  " + id);
	            String apn = cursor.getString(1);
	            Log.v(TAG, "apn  " + apn);
	            String name = cursor.getString(2);
	            Log.v(TAG, "name  " + name);
	          
	            }
	    
	    }
	    
	    public void printListApn (List<ApnInfo> apns) {
	    	for (ApnInfo apnInfo : apns) {

	    		Log.v(TAG, "id  " + apnInfo.id );
	    		Log.v(TAG, "apn  " + apnInfo.apn );
	    		Log.v(TAG, "name  " + apnInfo.name );
	    		Log.v(TAG, "type  " + apnInfo.type );
	    	}
	    }
	    
	    public void updateApn(){
	    	ContentValues newValues = new ContentValues();
	    	newValues.put("name", "tobicek" + selectedApn() );
	    	String where = "apn = 'bucicek'"; 
	    	getContentResolver().update(contentUri, newValues, where, null);
	    	
	        
	        }
	    
	        
	        
	        public void disableApn(){
	        	ContentValues newValues = new ContentValues();
	        	newValues.put("apn", PREFIX + selectedApn() );
	        	String where = "apn = 'bucicek'"; 
	        	getContentResolver().update(contentUri, newValues, where, null);
	        	
	            
	            }
	        
	        private boolean disableApnList(List<ApnInfo> apns) {
	            
	            for (ApnInfo apnInfo : apns) {
	                ContentValues values = new ContentValues();
	                String newApnName = addPrefix(apnInfo.apn);
	                values.put(APN, newApnName);
	                String newApnType = addComplexPrefix(apnInfo.type);
	                values.put(TYPE, newApnType);
	                getContentResolver().update(contentUri, values, ID + "=?", new String[] { String.valueOf(apnInfo.id) });
	            }
	            return true;
	        }
	        
	        private boolean disableAllInDb() {
	            List<ApnInfo> apns = getEnabledApnsMap();

	            // when selected apns is empty
	            if (apns.isEmpty()) {
	                return countDisabledApns() > 0;
	            }

	            return disableApnList(apns);
	        }
	        
	        private int countDisabledApns() {
	            return executeCountQuery("apn like ? or type like ?", new String[] { DB_LIKE_PREFIX, DB_LIKE_TYPE_PREFIX });
	        }

	          
	        private boolean enableApnList(List<ApnInfo> apns) {
	            
	            for (ApnInfo apnInfo : apns) {
	                ContentValues values = new ContentValues();
	                String newApnName = removePrefix(apnInfo.apn);
	                values.put(APN, newApnName);
	                String newApnType = removeComplexPrefix(apnInfo.type);
	                if ("".equals(newApnType)) {
	                    values.putNull(TYPE);
	                } else {
	                    values.put(TYPE, newApnType);
	                }
	                getContentResolver().update(contentUri, values, ID + "=?", new String[] { String.valueOf(apnInfo.id) });

	            }
	            return true;// we always return true because in any situation we can
	            // reset all apns to initial state
	        }

	        private boolean enableAllInDb() {
	            List<ApnInfo> apns = getDisabledApnsMap();
	            return enableApnList(apns);
	        }

	        private List<ApnInfo> getDisabledApnsMap() {
	            return selectApnInfo("apn like ? or type like ?", new String[] { DB_LIKE_PREFIX, DB_LIKE_TYPE_PREFIX });
	        }

	        
	        private int executeCountQuery(String whereQuery, String[] whereParams) {
	            Cursor cursor = null;
	            try {
	                cursor = managedQuery(contentUri, new String[] { "count(*)" }, whereQuery, whereParams, null);
	                if (cursor != null && cursor.moveToFirst()) {
	                    return cursor.getInt(0);
	                } else {
	                    return -1;
	                }
	            } finally {
	                if (cursor != null) {
	                    cursor.close();
	                }
	            }
	        }


	        private List<ApnInfo> getEnabledApnsMap() {
	            String query;
	            boolean disableAll = this.mDisableAll;
	            String disableAllQuery = disableAll ? null : "current is not null";
	            if (mMmsTarget == STATE_OFF) {
	                query = disableAllQuery;
	            } else {
	                query = "(not lower(type)='mms' or type is null)";
	                if (!disableAll) {
	                    query += " and " + disableAllQuery;
	                }
	            }
	            return selectApnInfo(query, null);
	        }

	        
	        
	        
	        private List<ApnInfo> createApnList(Cursor mCursor) {
	            List<ApnInfo> result = new ArrayList<ApnInfo>();
	            mCursor.moveToFirst();
	            while (!mCursor.isAfterLast()) {
	                long id = mCursor.getLong(0);
	                String apn = mCursor.getString(1);
	                String name = mCursor.getString(2);
	                String type = mCursor.getString(3);
	                result.add(new ApnInfo(id, apn, name, type));
	                mCursor.moveToNext();
	            }
	            return result;
	        }
	        
	        private List<ApnInfo> selectApnInfo(String whereQuery, String[] whereParams) {
	            Cursor cursor = null;
	            try {
	                cursor = managedQuery(contentUri, new String[] { ID, APN, NAME, TYPE }, whereQuery, whereParams, null);

	                if (cursor == null) return Collections.emptyList();
	                
	                return createApnList(cursor);
	            } finally {
	                if (cursor != null) {
	                    cursor.close();
	                }
	            }
	        }

	        
	        
	            public String selectedApn(){
	            	
	            String vysledek = ""; 	
	        	
	        	cursor = managedQuery(contentUri, new String[]{ "apn"}, null, null, null);
	            
	            
	                
	        	    cursor.moveToFirst();
	                vysledek = cursor.getString(0);
	                Log.v(TAG, "vysledek= " + vysledek) ;
	               
	                
	                return vysledek;
	        
	        }
	        
	        public boolean isOnline() {
	            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	            NetworkInfo netInfo = cm.getActiveNetworkInfo();
	            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	            //	Toast.makeText(APN.this, "zapnuty", Toast.LENGTH_SHORT).show();
	            	return true;
	                
	            }
	        //    Toast.makeText(APN.this, "vypnuty", Toast.LENGTH_SHORT).show();
	            return false;
	        }
	        
	        private static final class ApnInfo {

	            final long id;
	            final String apn;
	            final String name;
	            final String type;

	            public ApnInfo(long id, String apn, String name, String type) {
	                this.id = id;
	                this.apn = apn;
	                this.name = name;
	                this.type = type;
	            }
	        }
	        
	        public static String addSuffix(String currentName) {
	            if (currentName == null) {
	                return SUFFIX;
	            } else {
	                return currentName + SUFFIX;
	            }
	        }
	        
	        public static String addPrefix(String currentName) {
	            if (currentName == null) {
	                return PREFIX;
	            } else {
	                return PREFIX + currentName;
	            }
	        }
	        
	        
	        public String addComplexSuffix(String complexString){
	            if (complexString == null) return SUFFIX;

	            StringBuilder builder = new StringBuilder(complexString.length());
	            StringTokenizer tokenizer = new StringTokenizer(complexString, ",");
	            boolean leaveMmsEnabled = mMmsTarget == STATE_OFF;
	            while (tokenizer.hasMoreTokens()){
	                String str = tokenizer.nextToken().trim();
	                if (leaveMmsEnabled && "mms".equals(str)){
	                    builder.append(str);
	                }else{
	                    builder.append(addSuffix(str));
	                }
	                if (tokenizer.hasMoreTokens()){
	                    builder.append(",");
	                }
	            }
	            return builder.toString();
	        }

	        public String addComplexPrefix(String complexString){
	            if (complexString == null) return PREFIX;

	            StringBuilder builder = new StringBuilder(complexString.length());
	            StringTokenizer tokenizer = new StringTokenizer(complexString, ",");
	            boolean leaveMmsEnabled = mMmsTarget == STATE_OFF;
	            while (tokenizer.hasMoreTokens()){
	                String str = tokenizer.nextToken().trim();
	                if (leaveMmsEnabled && "mms".equals(str)){
	                    builder.append(str);
	                }else{
	                    builder.append(addPrefix(str));
	                }
	                if (tokenizer.hasMoreTokens()){
	                    builder.append(",");
	                }
	            }
	            return builder.toString();
	        }
	        
	        
	        public static String removeSuffix(String currentName) {
	            if (currentName == null) {
	                return "";
	            }
	            if (currentName.endsWith(SUFFIX)) {
	                return currentName.substring(0, currentName.length() - SUFFIX.length());
	            } else {
	                return currentName;
	            }
	        }

	        public static String removePrefix(String currentName) {
	            if (currentName == null) {
	                return "";
	            }
	            if (currentName.startsWith(PREFIX)) {
	               return currentName.substring(PREFIX.length() , currentName.length() );
	             //  return "vasek";
	            } else {
	                return currentName;
	            }
	        } 
	       

	        public static String removeComplexSuffix(String complexString){
	            if (complexString == null) return "";

	            StringBuilder builder = new StringBuilder(complexString.length());
	            StringTokenizer tokenizer = new StringTokenizer(complexString, ",");
	            while (tokenizer.hasMoreTokens()){
	                builder.append(removeSuffix(tokenizer.nextToken().trim()));
	                if (tokenizer.hasMoreTokens()){
	                    builder.append(",");
	                }
	            }
	            return builder.toString();
	        }

	        public static String removeComplexPrefix(String complexString){
	            if (complexString == null) return "";

	            StringBuilder builder = new StringBuilder(complexString.length());
	            StringTokenizer tokenizer = new StringTokenizer(complexString, ",");
	            while (tokenizer.hasMoreTokens()){
	                builder.append(removePrefix(tokenizer.nextToken().trim()));
	                if (tokenizer.hasMoreTokens()){
	                    builder.append(",");
	                }
	            }
	            return builder.toString();
	        } 

	        private Runnable mMyRunnableVyp = new Runnable()
	        {
	            @Override
	            public void run()
	            {
	               //Change state here
	  ///          	final ToggleButton toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
	            	final ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
	            	      TextView StavInternetu = (TextView) findViewById(R.id.StavInternetu);
	            	if (isOnline()) {
	                	Toast.makeText(InternetOnOff.this, "nepodarilo se vypnout", Toast.LENGTH_SHORT).show();
	   ///             	toggleButton1.setChecked(true);
	                	imageButton1.setImageResource(R.drawable.buttongreen200);
	                	StavInternetu.setText(R.string.InternetOn);
	               	 StavInternetu.setTextColor(Color.GREEN);
	                } else {
	                	Toast.makeText(InternetOnOff.this, "vypnuti OK", Toast.LENGTH_SHORT).show();
	                	imageButton1.setImageResource(R.drawable.buttonred200);
	               	StavInternetu.setText(R.string.InternetOff);
	                StavInternetu.setTextColor(Color.RED);
	                }
	            }
	         };

	         
	         
	         private Runnable mMyRunnableZap = new Runnable()
	         {
	             @Override
	             public void run()
	             {
	                //Change state here
	             //	final ToggleButton toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
	             	final ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
	             	      TextView StavInternetu = (TextView) findViewById(R.id.StavInternetu);
	             	if (!isOnline()) {
	                 	Toast.makeText(InternetOnOff.this, "nepodarilo se zapnout", Toast.LENGTH_SHORT).show();
	            //     	toggleButton1.setChecked(false);
	                 	imageButton1.setImageResource(R.drawable.buttonred200);
	                   StavInternetu.setText(R.string.InternetOff);
	               	 StavInternetu.setTextColor(Color.RED);
	                 } else {
	                	 Toast.makeText(InternetOnOff.this, "zapnuti OK", Toast.LENGTH_SHORT).show();
	                	 imageButton1.setImageResource(R.drawable.buttongreen200);
	                	 StavInternetu.setText(R.string.InternetOn);
	                	 StavInternetu.setTextColor(Color.GREEN);
	                 }
	             }
	          };

	    	

	}