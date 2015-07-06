package de.fhwedel.om.widgets;

import java.time.LocalDate;
import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;

public class DateHandler {
	private DateTimeFormat format;
	public DateHandler (String format) {
		this.format = DateTimeFormat.getFormat(format);
	}
	
	public LocalDate parseStringToDate(String dateString) {
		return LocalDate.parse(dateString);
	}
	
	static public LocalDate today() {
		
		return LocalDate.now();
	}
	
	static public boolean isDateFourWeeksAfterToday(LocalDate date) {
		LocalDate today = LocalDate.now();
		today.plusWeeks(4);
		return !date.isBefore(today);
	}
}
