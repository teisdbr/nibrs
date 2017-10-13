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
#include <Rcpp.h>
#include <fstream>
using namespace Rcpp;

//' Filter the (very large) ICPSR data files to include only the records for a single state
//' @export
//' @param filePaths paths to the files to filter
//' @param state two-letter state code for records to select
//' @param outputDir directory into which filtered files are written
//' @return a named character vector mapping input file paths to output (filtered) file paths
// [[Rcpp::export]]
StringVector filterICPSRFiles(StringVector filePaths, std::string state, std::string outputDir) {

  int nFiles = filePaths.size();

  StringVector ret(0);

  // in ICPSR NIBRS files, state appears in column 101 and length 2
  int pos = 101;
  int nchars = 2;

  pos = pos - 1; // zero-based

  for (int i=0;i < nFiles;i++) {

    std::string inputFilePath = Rcpp::as<std::string>(filePaths[i]);
    char istr[10];
    sprintf(istr, "%i", i+1);
    std::string outputFilePath = outputDir + "/" + state + "-" + istr + ".txt";
    //std::cout << "input path: " + inputFilePath + ", output path: " + outputFilePath + "\n";

    std::ifstream infile(inputFilePath.c_str());
    std::ofstream outfile(outputFilePath.c_str());

    std::string line;
    while (getline(infile, line)) {
      if (line.length() >= (pos + nchars) && !line.substr(pos, nchars).compare(state)) {
        outfile << line << "\n";
      }
    }

    infile.close();
    outfile.close();

    ret[inputFilePath] = outputFilePath;

  }

  return ret;

}
