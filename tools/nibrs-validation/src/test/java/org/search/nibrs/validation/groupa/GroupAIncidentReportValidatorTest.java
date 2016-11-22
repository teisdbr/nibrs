package org.search.nibrs.validation.groupa;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.validation.RuleViolationExemplarFactory;

public class GroupAIncidentReportValidatorTest {
	
	private static final Logger LOG = LogManager.getLogger(GroupAIncidentReportValidatorTest.class);
		
	private GroupAIncidentReportValidator validator;
	private RuleViolationExemplarFactory exemplarFactory;
	
	@Before
	public void init() {
		validator = new GroupAIncidentReportValidator();
		exemplarFactory = RuleViolationExemplarFactory.getInstance();
	}
	
	@Test
	public void testRule101() {
		testRule(NIBRSErrorCode._101, 101);
	}
		
	@Test
	public void testRule104() {
		testRule(NIBRSErrorCode._104, 104);
	}

	@Test
	public void testRule115() {
		testRule(NIBRSErrorCode._115, 115);
		// note that rule 116 is a duplicate of 115 (essentially) so we implement them both with 115
		testRule(NIBRSErrorCode._115, 116);
	}
		
	@Test
	public void testRule117() {
		testRule(NIBRSErrorCode._117, 117);
	}

	@Test
	public void testRule119() {
		testRule(NIBRSErrorCode._119, 119);
	}

	@Test
	public void testRule152() {
		testRule(NIBRSErrorCode._152, 152);
	}
	
	@Test
	public void testRule153() {
		testRule(NIBRSErrorCode._153, 153);
	}
	
	@Test
	public void testRule155() {
		testRule(NIBRSErrorCode._155, 155);
	}
	
	@Test
	public void testRule156() {
		testRule(NIBRSErrorCode._156, 156);
	}
	
	@Test
	public void testRule170() {
		testRule(NIBRSErrorCode._170, 170);
	}
	
	@Test
	public void testRule171() {
		testRule(NIBRSErrorCode._171, 171);
	}
	
	@Test
	public void testRule172() {
		testRule(NIBRSErrorCode._172, 172);
	}
	
	@Test
	public void testRule201() {
		testRule(NIBRSErrorCode._201, 201);
	}
	
	@Test
	public void testRule204() {
		testRule(NIBRSErrorCode._204, 204);
	}
	
	@Test
	public void testRule206() {
		testRule(NIBRSErrorCode._206, 206);
	}
	
	@Test
	public void testRule207() {
		testRule(NIBRSErrorCode._207, 207);
	}
	
	@Test
	public void testRule219() {
		testRule(NIBRSErrorCode._219, 219);
	}
	
	@Test
	public void testRule220() {
		testRule(NIBRSErrorCode._220, 220);
	}
	
	@Test
	public void testRule221() {
		testRule(NIBRSErrorCode._221, 221);
	}
	
	@Test
	public void testRule251() {
		testRule(NIBRSErrorCode._251, 251);
	}
	
	@Test
	public void testRule252() {
		testRule(NIBRSErrorCode._252, 252);
	}
	
	@Test
	public void testRule253() {
		testRule(NIBRSErrorCode._253, 253);
	}
	
	@Test
	public void testRule254() {
		testRule(NIBRSErrorCode._254, 254);
	}
	
	@Test
	public void testRule255() {
		testRule(NIBRSErrorCode._255, 255);
	}
	
	@Test
	public void testRule256() {
		testRule(NIBRSErrorCode._256, 256);
	}
	
	@Test
	public void testRule257() {
		testRule(NIBRSErrorCode._257, 257);
	}
	
	@Test
	public void testRule258() {
		testRule(NIBRSErrorCode._258, 258);
	}
	
	@Test
	public void testRule264() {
		testRule(NIBRSErrorCode._264, 264);
	}
	
	@Test
	public void testRule265() {
		testRule(NIBRSErrorCode._265, 265);
	}
	
	@Test
	public void testRule267() {
		testRule(NIBRSErrorCode._267, 267);
	}
	
	@Test
	public void testRule269() {
		testRule(NIBRSErrorCode._269, 269);
	}
	
	@Test
	public void testRule270() {
		testRule(NIBRSErrorCode._270, 270);
	}
	
	@Test
	public void testRule304() {
		testRule(NIBRSErrorCode._304, 304);
	}
	
	@Test
	public void testRule305() {
		testRule(NIBRSErrorCode._305, 305);
	}
	
	@Test
	public void testRule306() {
		testRule(NIBRSErrorCode._306, 306);
	}
	
	@Test
	public void testRule320() {
		testRule(NIBRSErrorCode._320, 320);
	}
	
	@Test
	public void testRule342() {
		testRule(NIBRSErrorCode._342, 342);
	}
	
	@Test
	public void testRule351() {
		testRule(NIBRSErrorCode._351, 351);
	}
	
	@Test
	public void testRule352() {
		testRule(NIBRSErrorCode._352, 352);
	}
	
	@Test
	public void testRule353() {
		testRule(NIBRSErrorCode._353, 353);
	}
	
	@Test
	public void testRule354() {
		testRule(NIBRSErrorCode._354, 354);
	}
	
	@Test
	public void testRule355() {
		testRule(NIBRSErrorCode._355, 355);
	}
	
	@Test
	public void testRule357() {
		testRule(NIBRSErrorCode._357, 357);
	}
	
	@Test
	public void testRule358() {
		testRule(NIBRSErrorCode._358, 358);
	}
	
	@Test
	public void testRule359() {
		testRule(NIBRSErrorCode._359, 359);
	}
	
	@Test
	public void testRule360() {
		testRule(NIBRSErrorCode._360, 360);
	}
	
	@Test
	public void testRule361() {
		testRule(NIBRSErrorCode._361, 361);
	}
	
	@Test
	public void testRule362() {
		testRule(NIBRSErrorCode._362, 362);
	}
	
	@Test
	public void testRule363() {
		testRule(NIBRSErrorCode._363, 363);
	}
	
	@Test
	public void testRule364() {
		testRule(NIBRSErrorCode._364, 364);
	}
	
	@Test
	public void testRule367() {
		testRule(NIBRSErrorCode._367, 367);
	}
	
	@Test
	public void testRule391() {
		testRule(NIBRSErrorCode._391, 391);
	}
	
	@Test
	public void testRule401(){
		testRule(NIBRSErrorCode._401, 401);
	}
	
	@Test
	public void testRule404(){
		testRule(NIBRSErrorCode._404, 404);
	}
	
	@Test
	public void testRule406(){
		testRule(NIBRSErrorCode._406, 406);
	}
	
	@Test
	public void testRule407(){
		testRule(NIBRSErrorCode._407, 407);
	}
	
	@Test
	public void testRule410(){
		testRule(NIBRSErrorCode._410, 410);
	}
	
	@Test
	public void testRule419(){
		testRule(NIBRSErrorCode._419, 419);
	}
	
	@Test
	public void testRule422(){
		testRule(NIBRSErrorCode._422, 422);
	}
	
	@Test
	public void testRule450(){
		testRule(NIBRSErrorCode._450, 450);
	}
	
	@Test
	public void testRule453(){
		testRule(NIBRSErrorCode._453, 453);
	}
	
	@Test
	public void testRule454(){
		testRule(NIBRSErrorCode._454, 454);
	}
	
	@Test
	public void testRule455(){
		testRule(NIBRSErrorCode._455, 455);
	}
	
	@Test
	public void testRule456(){
		testRule(NIBRSErrorCode._456, 456);
	}
	
	@Test
	public void testRule457(){
		testRule(NIBRSErrorCode._457, 457);
	}
	
	@Test
	public void testRule458(){
		testRule(NIBRSErrorCode._458, 458);
	}
	
	@Test
	public void testRule459(){
		testRule(NIBRSErrorCode._459, 459);
	}
	
	@Test
	public void testRule460(){
		testRule(NIBRSErrorCode._460, 460);
	}
	
	@Test
	public void testRule461(){
		testRule(NIBRSErrorCode._461, 461);
	}
	
	@Test
	public void testRule462(){
		testRule(NIBRSErrorCode._462, 462);
	}
	
	@Test
	public void testRule463(){
		testRule(NIBRSErrorCode._463, 463);
	}
	
	@Test
	public void testRule464(){
		testRule(NIBRSErrorCode._464, 464);
	}
	
	@Test
	public void testRule465(){
		testRule(NIBRSErrorCode._465, 465);
	}
	
	@Test
	public void testRule467(){
		testRule(NIBRSErrorCode._467, 467);
	}
	
	@Test
	public void testRule468(){
		testRule(NIBRSErrorCode._468, 468);
	}
	
	@Test
	public void testRule469(){
		testRule(NIBRSErrorCode._469, 469);
	}
	
	@Test
	public void testRule471(){
		testRule(NIBRSErrorCode._471, 471);
	}
	
	@Test
	public void testRule472(){
		testRule(NIBRSErrorCode._472, 472);
	}
	
	@Test
	public void testRule475(){
		testRule(NIBRSErrorCode._475, 475);
	}
	
	@Test
	public void testRule476(){
		testRule(NIBRSErrorCode._476, 476);
	}
	
	@Test
	public void testRule477(){
		testRule(NIBRSErrorCode._477, 477);
	}
	
	@Test
	public void testRule478(){
		testRule(NIBRSErrorCode._478, 478);
	}
	
	@Test
	public void testRule479(){
		testRule(NIBRSErrorCode._479, 479);
	}
	
	@Test
	public void testRule481(){
		testRule(NIBRSErrorCode._481, 481);
	}
	
	@Test
	public void testRule482(){
		testRule(NIBRSErrorCode._482, 482);
	}
	
	@Test
	public void testRule483(){
		testRule(NIBRSErrorCode._483, 483);
	}
	
	@Test
	public void testRule501(){
		testRule(NIBRSErrorCode._501, 501);
	}
	
	@Test
	public void testRule504(){
		testRule(NIBRSErrorCode._504, 504);
	}
	
	@Test
	public void testRule510(){
		testRule(NIBRSErrorCode._510, 510);
	}
	
	@Test
	public void testRule522(){
		testRule(NIBRSErrorCode._522, 522);
	}
	
	@Test
	public void testRule550(){
		testRule(NIBRSErrorCode._550, 550);
	}
	
	@Test
	public void testRule552(){
		testRule(NIBRSErrorCode._552, 552);
	}
	
	@Test
	public void testRule553(){
		testRule(NIBRSErrorCode._553, 553);
	}
	
	@Test
	public void testRule554(){
		testRule(NIBRSErrorCode._554, 554);
	}
	
	@Test
	public void testRule556(){
		testRule(NIBRSErrorCode._556, 556);
	}
	
	@Test
	public void testRule557(){
		testRule(NIBRSErrorCode._557, 557);
	}
	
	@Test
	public void testRule572(){
		testRule(NIBRSErrorCode._572, 572);
	}
	
	private void testRule(NIBRSErrorCode ruleCode, int ruleNumber) {
		List<GroupAIncidentReport> exemplars = exemplarFactory.getGroupAIncidentsThatViolateRule(ruleNumber);
		for (GroupAIncidentReport r : exemplars) {
			List<NIBRSError> errorList = validator.validate(r);
			boolean found = false;
			for (NIBRSError e : errorList) {
				if (ruleCode == e.getNIBRSErrorCode()) {
					found = true;
					break;
				}
			}
			if (!found) {
				LOG.error("Expected rule violdation not found for rule #" + ruleNumber + ".  Incident: " + r);
			}
			assertTrue(found);
		}
	}
		
}

