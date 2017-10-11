#include <Rcpp.h>
#include <fstream>
using namespace Rcpp;

// [[Rcpp::plugins(cpp11)]]

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
    std::string outputFilePath = outputDir + "/" + state + "-" + std::to_string(i+1) + ".txt";
    //std::cout << "input path: " + inputFilePath + ", output path: " + outputFilePath + "\n";

    std::ifstream infile(inputFilePath);
    std::ofstream outfile(outputFilePath);

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
