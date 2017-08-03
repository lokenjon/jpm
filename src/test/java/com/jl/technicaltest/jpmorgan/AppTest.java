package com.jl.technicaltest.jpmorgan;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jl.technicaltest.jpmorgan.enums.BuySell;
import com.jl.technicaltest.jpmorgan.impl.AppImpl;
import com.jl.technicaltest.jpmorgan.model.Instruction;

import junit.framework.TestCase;
;
public class AppTest extends TestCase{
	private AppImpl appImpl = new AppImpl();
	private App app = new AppImpl();

    public void testSampleData() throws Exception{

        JSONArray array = getJsonArray("/sample-data1.json");
        processInstructions(array);

        assertEquals(1, app.getReport().getDate2IncomingUSD().size());
        assertEquals(200f, app.getReport().getDate2IncomingUSD().get(getDate("04 Jan 2016")));//2 Jan is Sat, 4th is Monday 
        
        assertEquals(1, app.getReport().getDate2OutgoingUSD().size());
        assertEquals(400f, app.getReport().getDate2OutgoingUSD().get(getDate("04 Jan 2016")));//2 Jan is Sat, 4th is Monday
        
        assertFalse(app.getReport().getExceptionOccured());

        app.showReport();
    }

    public void testSampleData2() throws Exception{

        JSONArray array = getJsonArray("/sample-data2.json");
        processInstructions(array);

        assertEquals(1, app.getReport().getDate2IncomingUSD().size());
        assertEquals(200f, app.getReport().getDate2IncomingUSD().get(getDate("04 Jan 2016")));//2 Jan is Sat, 4th is Monday 
        
        assertEquals(1, app.getReport().getDate2OutgoingUSD().size());
        assertEquals(200f, app.getReport().getDate2OutgoingUSD().get(getDate("04 Jan 2016")));//2 Jan is Sat, 4th is Monday
        
        assertFalse(app.getReport().getExceptionOccured());

        app.showReport();
    }
    
    public void testSampleData3() throws Exception{

        JSONArray array = getJsonArray("/sample-data3.json");
        processInstructions(array);

        assertEquals(2, app.getReport().getDate2IncomingUSD().size());
        assertEquals(100f, app.getReport().getDate2IncomingUSD().get(getDate("04 Jan 2016")));//2 Jan is Sat, 4th is Monday 
        assertEquals(100f, app.getReport().getDate2IncomingUSD().get(getDate("05 Jan 2016")));//2 Jan is Sat, 4th is Monday 
        
        assertEquals(2, app.getReport().getDate2OutgoingUSD().size());
        assertEquals(100f, app.getReport().getDate2OutgoingUSD().get(getDate("04 Jan 2016")));//2 Jan is Sat, 4th is Monday
        assertEquals(100f, app.getReport().getDate2OutgoingUSD().get(getDate("05 Jan 2016")));//2 Jan is Sat, 4th is Monday
        
        assertFalse(app.getReport().getExceptionOccured());

        app.showReport();
    }
    public void testSampleDataHalfPrice() throws Exception{

        JSONArray array = getJsonArray("/sample-data4.json");
        processInstructions(array);

        assertEquals(1, app.getReport().getDate2IncomingUSD().size());
        assertEquals(100f, app.getReport().getDate2IncomingUSD().get(getDate("04 Jan 2016")));//2 Jan is Sat, 4th is Monday 
        
        assertEquals(1, app.getReport().getDate2OutgoingUSD().size());
        assertEquals(200f, app.getReport().getDate2OutgoingUSD().get(getDate("04 Jan 2016")));//2 Jan is Sat, 4th is Monday
        
        assertFalse(app.getReport().getExceptionOccured());

        app.showReport();
    }
    public void testSampleDataComplex() throws Exception{

        JSONArray array = getJsonArray("/sample-dataComplex.json");//complex
        try{
        	processInstructions(array);
        }catch (Exception e){
        	e.printStackTrace();
        	fail("did not manage complex case. "+e.getMessage());
        }
        assertFalse(app.getReport().getExceptionOccured());

        app.showReport();
    }

    public void testSampleDataFail2() throws Exception{

        JSONArray array = getJsonArray("/sample-dataFail2.json");//complex
       	processInstructions(array);
        assertTrue(app.getReport().getExceptionOccured());

        app.showReport();
    }
    
    //========= RANK =========
    
    public void testSampleDataRank() throws Exception{

        JSONArray array = getJsonArray("/sample-dataRank.json");
        processInstructions(array);

        app.showReport();

        {
	        Map<String, Float> map = app.getReport().getEntity2IncomingUSDRanked();
			assertEquals(2, map.size());
	        Iterator<String> it = map.keySet().iterator();
	        
			String value = it.next();
			assertEquals("foo50", value);
			assertEquals(50f, map.get(value));
			
			value = it.next();
			assertEquals("foo", value);
			assertEquals(20f, map.get(value));
        }
        {
			Map<String, Float> map = app.getReport().getEntity2OutgoingUSDRanked();
			assertEquals(2, map.size());
	        Iterator<String> it = map.keySet().iterator();
	        
			String value = it.next();
			assertEquals("foo40", value);
			assertEquals(40f, map.get(value));
			
			value = it.next();
			assertEquals("foo30", value);
			assertEquals(30f, map.get(value));
        }
        assertFalse(app.getReport().getExceptionOccured());
    }

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    
    private LocalDate getDate(String dateString)throws DateTimeException {
    	return LocalDate.parse(dateString, formatter);
    }

   
    public void testNextWorkingDate(){
    	Currency currencyUsd = Currency.getInstance("USD");
    	Currency currencyAed = Currency.getInstance("AED");
    	Currency currencySar = Currency.getInstance("SAR");
    	
    	LocalDate thu = getDate("03 Aug 2017");//Thu
    	LocalDate fri = getDate("04 Aug 2017");//Fri
    	LocalDate sat = getDate("05 Aug 2017");//sat
    	LocalDate sun = getDate("06 Aug 2017");//sun
    	LocalDate mon = getDate("07 Aug 2017");//mon

    	//USD
    	assertEquals(thu, appImpl.getValidWorkingDate(currencyUsd, thu));
    	assertEquals(mon, appImpl.getValidWorkingDate(currencyUsd, sat));
    	assertEquals(mon, appImpl.getValidWorkingDate(currencyUsd, sun));
    	
    	//aed
    	assertEquals(thu, appImpl.getValidWorkingDate(currencyAed, thu));
    	assertEquals(sun, appImpl.getValidWorkingDate(currencyAed, fri));
    	assertEquals(sun, appImpl.getValidWorkingDate(currencyAed, sat));
    	assertEquals(sun, appImpl.getValidWorkingDate(currencyAed, sun));
    	
    	//sar
    	assertEquals(thu, appImpl.getValidWorkingDate(currencySar, thu));
    	assertEquals(sun, appImpl.getValidWorkingDate(currencySar, fri));
    	assertEquals(sun, appImpl.getValidWorkingDate(currencySar, sat));
    	assertEquals(sun, appImpl.getValidWorkingDate(currencySar, sun));
    }
    
    public void testAmountOfATradeUSD(){
    	Instruction i = new Instruction();
    	i.setPricePerUnit(10.10f);
    	i.setUnits(20l);
    	i.setAgreedFx(11.11f);
    	
    	assertEquals(2244.22f, appImpl.amountOfATradeUSD(i)); //10.10 * 20 * 11.11 = 2244.22
    }
    
    public void testCurrency(){
    	{
	    	Currency c1 = Currency.getInstance("USD");
	    	assertEquals("US Dollar", c1.getDisplayName());
    	}

		try{
			Currency c1 = Currency.getInstance("usd");
			fail("Should not allow lower case usd");
		}catch (Exception e){
		}

		Currency.getInstance("AED");//exists
		Currency.getInstance("SAR");//exists

		try {
			Currency.getInstance("SGP");
			fail("SGP does not exist - at least not in JDK 1.8");
		} catch (Exception e) {
		}
    }
    

	private JSONArray getJsonArray(String jsonFileName) throws IOException {
		System.out.println(jsonFileName+"\n------------------");
		
		File file = new File(getClass().getResource(jsonFileName).getFile().replaceAll("%20", " "));//Woah - does not properly deal with %20 by default
        String content = new String(Files.readAllBytes(file.toPath()));
        
        JSONObject obj = new JSONObject(content);
        JSONArray array = obj.getJSONArray("instructions");
		return array;
	}
	
	private void processInstructions(JSONArray array) {
		app.initialize();
        
        for (int i = 0; i < array.length(); i++) {
        	JSONObject instructionObject = array.getJSONObject(i);

        	Instruction instruction = new Instruction(
        			instructionObject.getString("Entity"),
        			BuySell.valueOf(instructionObject.getString("BuySell")),
        			new Float(instructionObject.getString("AgreedFx")), 
        			Currency.getInstance(instructionObject.getString("Currency")),
        			getDate(instructionObject.getString("InstructionDate")),
        			getDate(instructionObject.getString("SettlementDate")),
        			new Long(instructionObject.getString("Units")),
        			new Float(instructionObject.getString("PricePerUnit"))
        			);
        	System.out.println(instruction.toString());
        	
        	app.processInstruction(instruction);
		}
	}
}
