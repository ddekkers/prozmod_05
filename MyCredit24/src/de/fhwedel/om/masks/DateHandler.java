package de.fhwedel.om.masks;

import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;

public class DateHandler {
	private DateTimeFormat format;
	
	public DateHandler (String format) {
		this.format = DateTimeFormat.getFormat(format);
	}
	
	public DateHandler () {
		this.format = DateTimeFormat.getFormat("");
	}
	
	public Date parseStringToDate(String dateString) {
		return this.format.parse(dateString);
	}
	
	public long daysBetween (Date earlierDate, Date laterDate) {
		long msLater = laterDate.getTime();
		long msEarlier = earlierDate.getTime();
		long milliSecondsBetween = msLater - msEarlier;
		return milliSecondsBetween / 24 / 60 / 60 / 1000;
	}
	
	public boolean isAAfterOrEqualsBAndBeforeC(Date A, Date B, Date C) {
		
		return (daysBetween(B, A) >= 0) && (daysBetween(A,C) > 0);
	}
	
	
}
