# mercans_integration

I tried to make this solution as abstract, as it possible
it should work if new fields are specified in config, if order, naming or amount of fields in CSV will change

Program works in two modes:
1) generate data objects. 
To do so, main file should be executed wit "generate" parameter. Programm will regenerate data classes based on config.json

2) parse CSV into json
No need to specify any parameters

There are files : test_input.csv and test_input_usecases.txt which representing probable data inputs and expected results. To use those files, change CSV_PATH constant in
main file.  
