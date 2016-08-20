library(dplyr)
library(tidyr)
library(openxlsx)
library(stringr)

dfs <- list()

for (f in c("~/git-repos/SEARCH-NCJIS/nibrs/docs/ElementEdits1.xlsx",
            "~/git-repos/SEARCH-NCJIS/nibrs/docs/ElementEdits2.xlsx",
            "~/git-repos/SEARCH-NCJIS/nibrs/docs/ElementEdits3.xlsx")) {
  
  for (s in getSheetNames(f)) {
    df <- list(read.xlsx(f, sheet=s, colNames = FALSE))
    names(df) <- s
    dfs <- c(dfs, df)
  }
  
}

df <- bind_rows(dfs)

df <- df %>%
  separate(X3, into=LETTERS[1:8], extra="drop", fill="right")

EditRules <- df %>%
  gather(segmentseq, RuleNumber, -X1, -X2, na.rm=TRUE) %>%
  arrange(X1) %>%
  select(-segmentseq) %>%
  rename(EditRuleText=X2) %>%
  mutate(DataElement=gsub(x=X1, pattern="\\s+", replacement=" "),
    ApplicableSegmentType=str_sub(RuleNumber, 1, 1),
    DataElementNumber=ifelse(grepl(x=tdf$DataElement, pattern="Data Element"),
                             gsub(x=tdf$DataElement, pattern="Data Element\\s+(.+)", replacement="\\1"), NA)) %>%
  select(DataElement, DataElementNumber, RuleNumber, EditRuleText, ApplicableSegmentType)

write.xlsx(EditRules, "~/git-repos/SEARCH-NCJIS/nibrs/docs/EditRules.xlsx")
