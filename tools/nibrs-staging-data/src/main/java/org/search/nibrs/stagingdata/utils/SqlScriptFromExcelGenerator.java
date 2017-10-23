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
package org.search.nibrs.stagingdata.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
/**
 * A dirty simple program that reads an Excel file.
 *
 */
public class SqlScriptFromExcelGenerator {
     
    public static void main(String[] args) throws IOException {
        generatePolulateCodeTableScript("src/test/resources/db/data.sql", 
        		"src/test/resources/codeSpreadSheets/NIBRSCodeTables.xlsx", false);
    }

	private static void generatePolulateCodeTableScript(String sqlScriptPath, String excelFilePath, boolean isSqlServerInsert) 
				throws FileNotFoundException, IOException {
		Path adamsSqlPath = Paths.get(sqlScriptPath);

        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
         
        Workbook workbook = new XSSFWorkbook(inputStream);
        StringBuilder sb = new StringBuilder(); 
        sb.append("/*\n "
        		+ "* Unless explicitly acquired and licensed from Licensor under another license, the contents of\n "
        		+ "* this file are subject to the Reciprocal Public License (\"RPL\") Version 1.5, or subsequent\n "
        		+ "* versions as allowed by the RPL, and You may not copy or use this file in either source code\n "
        		+ "* or executable form, except in compliance with the terms and conditions of the RPL\n "
        		+ "* \n "
        		+ "* All software distributed under the RPL is provided strictly on an \"AS IS\" basis, WITHOUT\n "
        		+ "* WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH\n "
        		+ "* WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A\n "
        		+ "* PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language\n "
        		+ "* governing rights and limitations under the RPL.\n "
        		+ "*\n "
        		+ "* http://opensource.org/licenses/RPL-1.5\n "
        		+ "*\n "
        		+ "* Copyright 2012-2017 Open Justice Broker Consortium\n "
        		+ "*/\n");
        
        for (int i=0; i<workbook.getNumberOfSheets(); i++){
        	Sheet sheet = workbook.getSheetAt(i); 
        
        	if (isSqlServerInsert){
        		sb.append("SET IDENTITY_INSERT dbo." + sheet.getSheetName() + " ON;\n");
        	}

        	System.out.println("sheetName: " + sheet.getSheetName());
        	String baseString = "insert into " + getTableName(sheet.getSheetName()) + 
        			"  values (";
            for (int j = 1; j<=sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                StringBuilder insertString = new StringBuilder();
                insertString.append(baseString);
                
                Integer pkId = Double.valueOf(row.getCell(0).getNumericCellValue()).intValue();
                insertString.append("'" + pkId + "'" );
                
                for ( int z = 1; z < row.getLastCellNum(); z++){
                	
                	String value = null; 
                	
                	if (row.getCell(z).getCellType() == Cell.CELL_TYPE_NUMERIC){
                		value = String.valueOf((int)row.getCell(z).getNumericCellValue());
                	}
                	else{
                		value = row.getCell(z).getStringCellValue();
                	}
                	
                	insertString.append(", '" + value.replace("'", "''") + "'");
                }
                
                insertString.append( ");\n");
                sb.append(insertString);
            }
            
        	if (isSqlServerInsert){
        		sb.append("SET IDENTITY_INSERT dbo." + sheet.getSheetName() + " OFF;\n");
        	}
        }
         
        workbook.close();
        inputStream.close();
        
    	if (isSqlServerInsert){
    		sb.append("SET IDENTITY_INSERT dbo.Date ON;\n");
    	}
    	
    	LocalDate localDate = LocalDate.of(2010, 1, 1);
    	LocalDate endDate = LocalDate.of(2100, 12, 31);
    	String baseString = "insert into Date " + 
    			"  values (";
    	int i = 1; 
    	while (!localDate.isAfter(endDate)){
            StringBuilder insertString = new StringBuilder();
            insertString.append(baseString);
            
            insertString.append("'"+ i + "'"); 
            i ++; 
            
        	insertString.append(", '" + java.sql.Date.valueOf(localDate) + "' ");
        	insertString.append(", " + localDate.getYear() + " ");
        	insertString.append(", '" + String.valueOf(localDate.getYear()) + "'");
        	insertString.append(", " + localDate.get(IsoFields.QUARTER_OF_YEAR) + " ");
        	insertString.append(", " + localDate.getMonthValue() + " ");
        	insertString.append(", '" + Month.of(localDate.getMonthValue()) + "'");
        	insertString.append(", '" + localDate.toString().substring(0, 7) + "' ");
        	insertString.append(", " +  localDate.getDayOfYear() + " ");
        	insertString.append(", '" + localDate.getDayOfWeek().toString() + "'");
        	insertString.append(", " + getDayOfWeekSort(localDate)  + "");
        	
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        	insertString.append(", '" + localDate.format( formatter) + "'");
        	insertString.append( ");\n");
        	
        	localDate = localDate.plusDays(1);
        	sb.append(insertString);
    	}
    	
    	if (isSqlServerInsert){
    		sb.append("SET IDENTITY_INSERT dbo.Date OFF;\n");
    	}

        
        try (BufferedWriter writer = Files.newBufferedWriter(adamsSqlPath)) {
            writer.write(sb.toString());
        }
        
        System.out.println("Sql script " + sqlScriptPath + " generated. ");
	}

	private static String getTableName(String sheetName) {
		switch (sheetName) {
		case "AggravatedAssaultHomicideCircum":
			return "AggravatedAssaultHomicideCircumstancesType";
		case "AdditionalJustifiableHomicideCi":
			return "AdditionalJustifiableHomicideCircumstancesType";
		case "RelationshipsVictimToOffendersT":
			return "VictimOffenderRelationshipType";
		case "MultipleArresteeSegmentsIndicat":
			return "MultipleArresteeSegmentsIndicatorType";
		case "DispositionOfArresteeUnder18Typ":
			return "DispositionOfArresteeUnder18Type";
		default:
			return sheetName;
		}
	}

	private static int getDayOfWeekSort(LocalDate localDate) {
		localDate.getDayOfWeek().getValue();  
		
		switch (localDate.getDayOfWeek()){
		case SUNDAY: 
			return 1; 
		default: 
			return localDate.getDayOfWeek().getValue() + 1; 
		}
	}

}