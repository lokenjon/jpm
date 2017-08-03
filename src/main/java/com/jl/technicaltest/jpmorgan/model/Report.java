package com.jl.technicaltest.jpmorgan.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Create a report that returns
 * <ul>
 * <li> Amount in USD settled incoming everyday</li>
 * <li> Amount in USD settled outgoing everyday</li>
 * <li> Ranking of entities based on incoming and outgoing amount. Eg: If entity foo instructs the highest
 * 		amount for a buy instruction, then foo is rank 1 for outgoing</li>
 * </ul>
 * This class in non-anemic ( it has some business logic)
 */
public class Report {

	private Map<LocalDate, Float> date2IncomingUSD = new HashMap<LocalDate, Float>();
	private Map<LocalDate, Float> date2OutgoingUSD = new HashMap<LocalDate, Float>();
	
	//rank
	private Map<String, Float> entity2IncomingUSD = new HashMap<String, Float>();
	private Map<String, Float> entity2OutgoingUSD = new HashMap<String, Float>();

	private boolean exceptionOccured = false;
	
	public Map<LocalDate, Float> getDate2IncomingUSD() {
		return date2IncomingUSD;
	}
	public void setDate2IncomingUSD(Map<LocalDate, Float> date2IncomingUSD) {
		this.date2IncomingUSD = date2IncomingUSD;
	}
	public Map<LocalDate, Float> getDate2OutgoingUSD() {
		return date2OutgoingUSD;
	}
	public void setDate2OutgoingUSD(Map<LocalDate, Float> date2OutgoingUSD) {
		this.date2OutgoingUSD = date2OutgoingUSD;
	}

	public boolean getExceptionOccured() {
		return exceptionOccured;
	}
	public void setExceptionOccured(boolean exceptionOccured) {
		this.exceptionOccured = exceptionOccured;
	}

	public void setEntity2OutgoingUSD(Map<String, Float> entity2OutgoingUSD) {
		this.entity2OutgoingUSD = entity2OutgoingUSD;
	}
	public void setEntity2IncomingUSD(Map<String, Float> entity2IncomingUSD) {
		this.entity2IncomingUSD = entity2IncomingUSD;
	}
	
	
	public Map<String, Float> getEntity2IncomingUSD() {
		return entity2IncomingUSD;
	}
	public Map<String, Float> getEntity2OutgoingUSD() {
		return entity2OutgoingUSD;
	}
	/**
	 * return sorted by value order by desc
	 * @return
	 */
	public Map<String, Float> getEntity2IncomingUSDRanked() {
		return sortByRankDesc(entity2IncomingUSD);
	}

	/**
	 * return sorted by value order by desc
	 * @return
	 */
	public Map<String, Float> getEntity2OutgoingUSDRanked() {
		return sortByRankDesc(entity2OutgoingUSD);
	}
	public Map<String, Float> sortByRankDesc(Map<String, Float> map) {
		return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
	}
	
	@Override
	public String toString() {
		return "Report [date2IncomingUSD=" + date2IncomingUSD + ", date2OutgoingUSD=" + date2OutgoingUSD
				+ ", entity2IncomingUSD=" + entity2IncomingUSD + ", entity2OutgoingUSD=" + entity2OutgoingUSD
				+ ", exceptionOccured=" + exceptionOccured + "]";
	}
}
