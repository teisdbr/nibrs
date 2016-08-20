library(dplyr)
library(openxlsx)

rules <- read.xlsx("~/git-repos/SEARCH-NCJIS/nibrs/docs/NIBRSErrors_Raw.xlsx", colNames=FALSE, startRow=5) %>%
  filter(trimws(X1) != '' & !is.na(X1))

rulesClean <- data.frame()

ruleNum <- NA
ruleMessage <- NA
ruleDescription <- NA
ruleMessageNext <- TRUE

for (i in 1:nrow(rules)) {
  
  val <- trimws(rules[i, 'X1'])
  #print(paste0('val=', val, ' ruleMessageNext=', ruleMessageNext))
  if (grepl("^[0-9]+$", val)) {
    outRow <- data.frame(RuleNumber=ruleNum, RuleMessage=ruleMessage, RuleDescription=ruleDescription, stringsAsFactors = FALSE)
    if (i > 1) {
      rulesClean <- bind_rows(rulesClean, outRow)
    }
    ruleNum <- val
    ruleMessage <- ''
    ruleDescription <- ''
    ruleMessageNext <- TRUE
  } else if (ruleMessageNext) {
    ruleMessage <- val
    ruleMessageNext <- FALSE
    #print(paste0('ruleMessage=', ruleMessage))
  } else {
    sep <- '\n'
    if (ruleDescription == '') {
      sep <- ''
    }
    ruleDescription <- paste0(ruleDescription, sep, val)
    #print(paste0('ruleDescription=', ruleDescription))
  }
  
}

write.xlsx(rulesClean, "NIBRSRules.xlsx")
