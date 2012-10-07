/**********************************************************************************************
 * Copyright 2009 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file 
 * except in compliance with the License. A copy of the License is located at
 *
 *       http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE.txt" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License. 
 *
 * ********************************************************************************************
 *
 *  Amazon Product Advertising API
 *  Signed Requests Sample Code
 *
 *  API Version: 2009-03-31
 *
 */

//package com.amazon.advertising.api.sample;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.sun.xml.internal.fastinfoset.util.StringArray;

//import com.sun.java.util.jar.pack.Package.File;

/*
 * This class shows how to make a simple authenticated ItemLookup call to the
 * Amazon Product Advertising API.
 * 
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */


public class ItemLookupSample {
    /*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
	public static String[][] StrArray = new String[10][2];
	public static ArrayList<String> vManufacturers = new ArrayList<String>();
	public static ArrayList<Integer> vRating = new ArrayList<Integer>();
	
	
    private static final String AWS_ACCESS_KEY_ID = "";

    /*
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
     * Your Account page.
     */
    private static final String AWS_SECRET_KEY = "";

    /*
     * Use one of the following end-points, according to the region you are
     * interested in:
     * 
     *      US: ecs.amazonaws.com 
     *      CA: ecs.amazonaws.ca 
     *      UK: ecs.amazonaws.co.uk 
     *      DE: ecs.amazonaws.de 
     *      FR: ecs.amazonaws.fr 
     *      JP: ecs.amazonaws.jp
     * 
     */
    private static final String ENDPOINT = "ecs.amazonaws.com";

    /*
     * The Item ID to lookup. The value below was selected for the US locale.
     * You can choose a different value if this value does not work in the
     * locale of your choice."0545010225"
     */
    private static final String ITEM_ID = "B0051QVF7A";

    // my changes. It was returning void earlier. And the name was main(String args[])
    public static void main_lookup(String sProduct, String sMinRating, ArrayList<String> ALManufacturers, ArrayList<Integer> ALRating, Integer minRating, MutableInteger countOfRecords) {
        /*
         * Set up the signed requests helper 
         */
        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return ; // my changes. It was returning void earlier
        }
        
        String requestUrl = null;
        String title = null;

        Map<String, String> params = SetParams(sProduct, 1);
           
        
        requestUrl = helper.sign(params);
        System.out.println("Signed Request is \"" + requestUrl + "\"");

        MutableInteger count = new MutableInteger();
        
        fetchTitle(ALManufacturers, ALRating, requestUrl, minRating, count);
        System.out.println("Signed Title is \"" + title + "\"");
        System.out.println();
        countOfRecords.value = count.value;
        return ;
    }
    
    private static Map<String, String> SetParams(String sProduct, int ItemPage){
    	/* The helper can sign requests in two forms - map form and string form */
        
        /*
         * Here is an example in map form, where the request parameters are stored in a map.
         */
    	System.out.println("Map form example:");
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2011-08-01");
        //params.put("Operation", "ItemLookup");
        params.put("Operation", "ItemSearch");
        params.put("Keywords", sProduct);
        params.put("SearchIndex", "All");
        params.put("ItemPage", Integer.toString(ItemPage));
        params.put("AssociateTag", "cellardoor0b-20");
        
        return params;
    }
    
    private static int GetRating(String ASIN, String Manufacturer){
    	int rating = 0;
       	
    	SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return 0; 
        }
        
        String requestUrl = null;
        
        /* The helper can sign requests in two forms - map form and string form */
        
        /*
         * Here is an example in map form, where the request parameters are stored in a map.
         */
        System.out.println("For Rating:");
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2011-08-01");
        params.put("Operation", "ItemLookup");
        //params.put("Operation", "ItemSearch");
        params.put("ASIN", ASIN);
        params.put("Manufacturer", "Manufacturer");
        params.put("ResponseGroup", "Reviews");
        params.put("AssociateTag", "cellardoor0b-20");
        params.put("BrowseNode", "All");
        params.put("IncludeReviewsSummary", "False");
        params.put("TruncateReviewsAt", "256");
        
        requestUrl = helper.sign(params);
        System.out.println("Signed Request is \"" + requestUrl + "\"");
        
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            
            rating = 0;//= Integer.parseInt(doc.getElementsByTagName("Manufacturer").item(0).getTextContent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    	return rating;
    }

    /*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    private static void fetchTitle(ArrayList<String> ALManufacturers, ArrayList<Integer> ALRating, String requestUrl, Integer minRating, MutableInteger count) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            doc.getDocumentElement().normalize();
            
            int nProducts = Integer.parseInt(doc.getElementsByTagName("TotalResults").item(0).getTextContent());
            count.value = (nProducts >= 15) ? 15 : nProducts;
            int nPage = 1;
            int limit = count.value;	
            for(int i = 0; i < limit; i++){
            	String str = "";
            	try{
            		str = doc.getElementsByTagName("Manufacturer").item(i).getTextContent();
            	}catch(Exception e_xml){
            		str = null;
            	}
            	if(str != null){
	            	//if(GetRating(doc.getElementsByTagName("ASIN").item(i).getTextContent(), doc.getElementsByTagName("Manufacturer").item(i).getTextContent()) >= minRating){
	            		ALManufacturers.add(doc.getElementsByTagName("Manufacturer").item(i).getTextContent());
	            		ALRating.add(i);//(GetRating(doc.getElementsByTagName("ASIN").item(i).getTextContent(), doc.getElementsByTagName("Manufacturer").item(i).getTextContent()));
	            	//}
            	}
            	else{
            		ALManufacturers.add("Not Available!");
            		ALRating.add(0);
            	}
            	if(i == 9){
            		// Read new page
            		++nPage;
            		limit -= 10;
            		i -= 10;
            		/*
                     * Set up the signed requests helper 
                     */
                    SignedRequestsHelper helper;
                    try {
                        helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ; // my changes. It was returning void earlier
                    }
                    
                    //String requestUrl = null;
            		Map<String, String> params = SetParams(doc.getElementsByTagName("Keywords").item(0).getTextContent(), nPage);
                    requestUrl = helper.sign(params);
                    System.out.println("Signed Request is \"" + requestUrl + "\"");
                    doc = db.parse(requestUrl);
                    doc.getDocumentElement().normalize();
            	}
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
