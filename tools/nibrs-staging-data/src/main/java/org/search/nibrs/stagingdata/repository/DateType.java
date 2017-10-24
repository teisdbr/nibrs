/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.stagingdata.repository;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class DateType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer dateTypeId; 
	
	private Date calendarDate; 
	private Integer year; 
	private String yearLabel; 
	private Integer calendarQuarter; 
	private Integer month; 
	private String monthName; 
	private String fullMonth; 
	private Integer day; 
	private String dayOfWeek; 
	private Integer dayOfWeekSort; 
	private String dateMMDDYYYY; 
	
	public DateType() {
		super();
	}
	
	public DateType(Integer dateTypeId) {
		super();
		this.dateTypeId = dateTypeId;
	}
	
	public DateType(Integer dateTypeId, Date calendarDate, Integer year, String yearLabel, Integer calendarQuarter,
			Integer month, String monthName, String fullMonth, Integer day, String dayOfWeek, Integer dayOfWeekSort,
			String dateMMDDYYYY) {
		this();
		this.dateTypeId = dateTypeId;
		this.calendarDate = calendarDate;
		this.year = year;
		this.yearLabel = yearLabel;
		this.calendarQuarter = calendarQuarter;
		this.month = month;
		this.monthName = monthName;
		this.fullMonth = fullMonth;
		this.day = day;
		this.dayOfWeek = dayOfWeek;
		this.dayOfWeekSort = dayOfWeekSort;
		this.dateMMDDYYYY = dateMMDDYYYY;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getDateTypeId() {
		return dateTypeId;
	}

	public void setDateTypeID(Integer dateTypeId) {
		this.dateTypeId = dateTypeId;
	}

	public Date getCalendarDate() {
		return calendarDate;
	}

	public void setCalendarDate(Date calendarDate) {
		this.calendarDate = calendarDate;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getYearLabel() {
		return yearLabel;
	}

	public void setYearLabel(String yearLabel) {
		this.yearLabel = yearLabel;
	}

	public Integer getCalendarQuarter() {
		return calendarQuarter;
	}

	public void setCalendarQuarter(Integer calendarQuarter) {
		this.calendarQuarter = calendarQuarter;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public String getFullMonth() {
		return fullMonth;
	}

	public void setFullMonth(String fullMonth) {
		this.fullMonth = fullMonth;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Integer getDayOfWeekSort() {
		return dayOfWeekSort;
	}

	public void setDayOfWeekSort(Integer dayOfWeekSort) {
		this.dayOfWeekSort = dayOfWeekSort;
	}

	public String getDateMMDDYYYY() {
		return dateMMDDYYYY;
	}

	public void setDateMMDDYYYY(String dateMMDDYYYY) {
		this.dateMMDDYYYY = dateMMDDYYYY;
	}

}
