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
package org.search.nibrs.stagingdata.service.summary;

public class ReturnAForm{
	private Columns[] columns = new Columns[ReturnARow.values().length]; 

	public ReturnAForm() {
		super();
		for (int i = 0; i < getColumns().length; i++){
			getColumns()[i] = new Columns(); 
		}
	}


	
	public Columns[] getColumns() {
		return columns;
	}

	public void setColumns(Columns[] columns) {
		this.columns = columns;
	}



	public class Columns{
		private int reportedOffenses; 
		private int unfoundedOffenses; 
		private int actualOffenses; 
		private int clearedOffenses;
		private int clearanceInvolvingJuvenile;
		
		public int getReportedOffenses() {
			return reportedOffenses;
		}
		public void setReportedOffenses(int reportedOffenses) {
			this.reportedOffenses = reportedOffenses;
		}
		public void increaseReportedOffenses(int increment){
			reportedOffenses += increment;
		}
		
		public int getUnfoundedOffenses() {
			return unfoundedOffenses;
		}
		public void setUnfoundedOffenses(int unfoundedOffenses) {
			this.unfoundedOffenses = unfoundedOffenses;
		}
		public void increaseUnfoundedOffenses(int increment){
			unfoundedOffenses += increment;
		}
		
		public int getActualOffenses() {
			return reportedOffenses - unfoundedOffenses;
		}
		
		public int getClearedOffenses() {
			return clearedOffenses;
		}
		public void setClearedOffenses(int clearedOffenses) {
			this.clearedOffenses = clearedOffenses;
		}
		public void increaseClearedOffenses(int increment){
			clearedOffenses += increment;
		}
		public int getClearanceInvolvingJuvenile() {
			return clearanceInvolvingJuvenile;
		}
		public void setClearanceInvolvingJuvenile(int clearanceInvolvingJuvenile) {
			this.clearanceInvolvingJuvenile = clearanceInvolvingJuvenile;
		}
		public void increaseClearanceInvolvingJuvenile(int increment){
			clearanceInvolvingJuvenile += increment;
		}
		@Override
		public String toString() {
			return "Summary [reportedOffenses=" + reportedOffenses + ", unfoundedOffenses=" + unfoundedOffenses
					+ ", actualOffenses=" + actualOffenses + ", clearedOffense=" + clearedOffenses
					+ ", clearanceInvolvingJuvenile=" + clearanceInvolvingJuvenile + "]";
		}
	}
	
	public enum ReturnARow{
		MURDER_NONNEGLIGENT_HOMICIDE,  
		MANSLAUGHTER_BY_NEGLIGENCE, 
		FORCIBLE_RAPE_TOTAL, 
		RAPE_BY_FORCE, 
		ATTEMPTS_TO_COMMIT_FORCIBLE_RAPE, 
		ROBBERY_TOTAL, 
		FIREARM_ROBBERY, 
		KNIFE_CUTTING_INSTRUMENT_ROBBERY, 
		OTHER_DANGEROUS_WEAPON_ROBBERY, 
		STRONG_ARM_ROBBERY, 
		ASSAULT_TOTAL, 
		FIREARM_ASSAULT, 
		KNIFE_CUTTING_INSTRUMENT_ASSAULT, 
		OTHER_DANGEROUS_WEAPON_ASSAULT, 
		HANDS_FISTS_FEET_AGGRAVATED_INJURY_ASSAULT, 
		OTHER_ASSAULT_NOT_AGGRAVATED, 
		BURGLARY_TOTAL, 
		FORCIBLE_ENTRY_BURGLARY,
		UNLAWFUL_ENTRY_NO_FORCE_BURGLARY,
		ATTEMPTED_FORCIBLE_ENTRY_BURGLARY,
		/**
		 * Larceny-Theft Total (Except Motor Vehicle Theft) 
		 */
		LARCENCY_THEFT_TOTAL,
		MOTOR_VEHICLE_THEFT_TOTAL, 
		AUTOS_THEFT, 
		TRUCKS_BUSES_THEFT, 
		OTHER_VEHICLES_THEFT, 
		ARSON, 
		GRAND_TOTAL
	}



}