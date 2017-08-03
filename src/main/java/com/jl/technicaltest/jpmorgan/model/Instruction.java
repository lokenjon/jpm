package com.jl.technicaltest.jpmorgan.model;

import java.time.LocalDate;
import java.util.Currency;

import com.jl.technicaltest.jpmorgan.enums.BuySell;

public class Instruction {
	private String entity;
	private BuySell buySell;
	private Float agreedFx;
	private Currency currency;
	private LocalDate instuctionDate;
	private LocalDate settlementDate;
	private Long units;
	private Float pricePerUnit;
	
	public Instruction(String entity, BuySell buySell, Float agreedFx, Currency currency, LocalDate instuctionDate,
			LocalDate settlementDate, Long units, Float pricePerUnit) {
		super();
		this.entity = entity;
		this.buySell = buySell;
		this.agreedFx = agreedFx;
		this.currency = currency;
		this.instuctionDate = instuctionDate;
		this.settlementDate = settlementDate;
		this.units = units;
		this.pricePerUnit = pricePerUnit;
	}
	public Instruction(){
		
	};
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public BuySell getBuySell() {
		return buySell;
	}
	public void setBuySell(BuySell buySell) {
		this.buySell = buySell;
	}
	public Float getAgreedFx() {
		return agreedFx;
	}
	public void setAgreedFx(Float agreedFx) {
		this.agreedFx = agreedFx;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public LocalDate getInstuctionDate() {
		return instuctionDate;
	}
	public void setInstuctionDate(LocalDate instuctionDate) {
		this.instuctionDate = instuctionDate;
	}
	public LocalDate getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(LocalDate settlementDate) {
		this.settlementDate = settlementDate;
	}
	public Long getUnits() {
		return units;
	}
	public void setUnits(Long units) {
		this.units = units;
	}
	public Float getPricePerUnit() {
		return pricePerUnit;
	}
	public void setPricePerUnit(Float pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	@Override
	public String toString() {
		return "Instruction [entity=" + entity + ", buySell=" + buySell + ", agreedFx=" + agreedFx + ", currency="
				+ currency + ", instuctionDate=" + instuctionDate + ", settlementDate=" + settlementDate
				+ ", units=" + units + ", pricePerUnit=" + pricePerUnit
				+ "]";
	}
}
