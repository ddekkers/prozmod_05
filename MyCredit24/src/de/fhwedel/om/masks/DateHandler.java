package de.fhwedel.om.masks;

import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;

public class DateHandler {
	private String format;
	
	public DateHandler (String format) {
		this.format = format;
	}
	
	public DateHandler () {
		this.format = "";
	}
	
	public Date parseStringToDate(String dateString) {
		return DateTimeFormat.getFormat(this.format).parse(dateString);
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
	
	public String getDateAsString (Date date) {
		return DateTimeFormat.getFormat(format).format(date);
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getFormat() {
		return format;
	}
}
