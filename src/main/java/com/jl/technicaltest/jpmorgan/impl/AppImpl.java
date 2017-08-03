package com.jl.technicaltest.jpmorgan.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Currency;
import java.util.Map;

import com.jl.technicaltest.jpmorgan.App;
import com.jl.technicaltest.jpmorgan.enums.BuySell;
import com.jl.technicaltest.jpmorgan.model.Instruction;
import com.jl.technicaltest.jpmorgan.model.Report;

public class AppImpl implements App{
    private static final String SAR = "SAR";
	private static final String AED = "AED";
	
	private Report report = null;

	@Override
	public void initialize() {
		report = new Report();
	}


	/**
	 * Spec: Create a report that shows
	 * <ul>
	 * <li> Amount in USD settled incoming everyday</li>
	 * <li> Amount in USD settled outgoing everyday</li>
	 * <li> Ranking of entities based on incoming and outgoing amount. Eg: If entity foo instructs the highest
	 * 		amount for a buy instruction, then foo is rank 1 for outgoing</li>
	 * </ul>
	 * Note: 'entity foo instructs...'. This does not make sense to me. It is the CLIENT who 'instructs'. The 'entity',
	 * which I take to mean a company (e.g. IBM), does not 'instruct' anything. 
	 * TODO: Spec needs clarification wrt who instructs. <br>
	 * 
	 * Given more time, this class should be broken down into individual implementions of Interface 'Report'.
	 * 
	 * NOTE: Java is not so good at Statistics. Matlab/c#/C++ are better dealing with arrays. 
	 * Do stats on the fly (rather than keeping all data in memory and then doing stats) 
	 * @param instruction
	 */
	@Override
    public void processInstruction(Instruction instruction){
		try{
			//for testing purposes throw an exception somewhere
			if (new Float(0).equals(instruction.getAgreedFx())){
				throw new Exception("agreedFX cannot be 0");//TODO: create my own Exception type. 
			}
			//The following 2 variables I could have stored in additional fields in Instructions in order to keep data together
			LocalDate validDate = getValidWorkingDate(instruction.getCurrency(), instruction.getSettlementDate());
	    	Float amountUSD = amountOfATradeUSD(instruction);
	
	    	//-------------- incoming/outgoing ---------------------
	    	//increase 
	    	if (BuySell.B.equals(instruction.getBuySell())){
	    		//Incoming - buy
		    	Map<LocalDate, Float> date2Incoming = report.getDate2IncomingUSD();//contains map of 'date' keys
		    	
	    		Float newValue = date2Incoming.get(validDate) == null?
		    				amountUSD: 
		    				amountUSD + date2Incoming.get(validDate);
	    		date2Incoming.put(validDate, newValue);
	    	}
	    	else{
	    		//Outgoing - sell
		    	Map<LocalDate, Float> date2Outgoing = report.getDate2OutgoingUSD();//contains map of 'date' keys
	    		
		    	Float newValue = date2Outgoing.get(validDate) == null?
	    				amountUSD: 
	    				amountUSD + date2Outgoing.get(validDate);
		    	date2Outgoing.put(validDate, newValue);
	    	}
	    	
	    	
	    	//----------------- RANK ---------------------
	    	if (BuySell.B.equals(instruction.getBuySell())){
	    		//Incoming - buy
		    	Map<String, Float> entity2Incoming = report.getEntity2IncomingUSD();//contains map of 'entity' keys
		    	
	    		Float newValue = entity2Incoming.get(instruction.getEntity()) == null?
		    				amountUSD: 
		    				amountUSD + entity2Incoming.get(instruction.getEntity());
	    		entity2Incoming.put(instruction.getEntity(), newValue);
	    	}
	    	else{
	    		//Outgoing - sell
		    	Map<String, Float> entity2Outgoing = report.getEntity2OutgoingUSD();//contains map of 'entity' keys
	    		
		    	Float newValue = entity2Outgoing.get(instruction.getEntity()) == null?
	    				amountUSD: 
	    				amountUSD + entity2Outgoing.get(instruction.getEntity());
		    	entity2Outgoing.put(instruction.getEntity(), newValue);
	    	}

		}catch (Exception e){
			//an exception will invalidate the report, so flag this
			report.setExceptionOccured(true);
		}
    }
	
	@Override
	public Report getReport(){
		return report;
	}
    
	@Override
	public void showReport() {
		System.out.println("Incoming (buy) (USD): "+report.getDate2IncomingUSD());
		System.out.println("Outgoing (sell)(USD): "+report.getDate2OutgoingUSD());

		System.out.println("Result Rank Incoming (buy) (USD): "+report.getEntity2IncomingUSDRanked());
		System.out.println("Result Rank Outgoing (sell)(USD): "+report.getEntity2OutgoingUSDRanked());
		
		System.out.println("Exception occured? "+report.getExceptionOccured());
		System.out.println();
	}
	
    /**
     * USD amount of a trade = Price per unit * Units * Agreed Fx
     */
    public Float amountOfATradeUSD(Instruction instruction){
    	return instruction.getPricePerUnit() *
    			instruction.getUnits() *
    			instruction.getAgreedFx();
    }
    
    /**
     * A work week starts Monday and ends Friday, unless the currency of the trade is 
     * AED or SAR, where the work week starts Sunday and ends Thursday. No other holidays 
     * to be taken into account. 
     *  
     * If an instructed settlement date falls on a weekend, then the settlement 
     * date should be changed to the next working day
     * @return
     */
    public LocalDate getValidWorkingDate(Currency currency, LocalDate date){
    	DayOfWeek dayOfWeek = date.getDayOfWeek();
    	
    	if (currency.getCurrencyCode().equals(AED) || currency.getCurrencyCode().equals(SAR)){
    		
    		if (dayOfWeek.equals(DayOfWeek.FRIDAY) || dayOfWeek.equals(DayOfWeek.SATURDAY)){
    			return date.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
    		}
    		return date;
    	}
    	
    	//non-AED-SAR
    	if (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)){
			return date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
		}
    	return date;
    }
}
